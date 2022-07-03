package nz.co.warehouseandroidtest

import android.graphics.Rect
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Toast
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.data.model.ProductWithoutPrice
import nz.co.warehouseandroidtest.data.model.SearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultActivity : AppCompatActivity() {
    private var mKeyWord: String? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var searchResultAdapter: SearchResultAdapter? = null
    private val data: MutableList<ProductWithoutPrice?> = ArrayList()
    private var totalItemNum: String? = null
    private var startIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        swipeRefreshLayout = findViewById<View>(R.id.refresh_layout) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener {
            data.clear()
            loadData(0, 20)
            startIndex = 0
            swipeRefreshLayout!!.postDelayed({
                if (swipeRefreshLayout != null && swipeRefreshLayout!!.isRefreshing) {
                    swipeRefreshLayout!!.isRefreshing = false
                }
            }, 1000)
        }
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        recyclerView!!.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        recyclerView!!.addItemDecoration(object : DividerItemDecoration(this, VERTICAL) {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top =
                    this@SearchResultActivity.resources.getDimensionPixelOffset(R.dimen.recyclerview_out_rec_top)
            }
        })
        searchResultAdapter = SearchResultAdapter()
        searchResultAdapter!!.setData(data)
        recyclerView!!.adapter = searchResultAdapter
        recyclerView!!.addOnScrollListener(object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                searchResultAdapter!!.setLoadState(searchResultAdapter!!.LOADING)
                if (data.size < totalItemNum!!.toInt()) {
                    loadData(startIndex, 20)
                } else {
                    searchResultAdapter!!.setLoadState(searchResultAdapter!!.LOADING_END)
                }
            }
        })
        mKeyWord = intent.extras?.getString(FLAG_KEY_WORD)
        loadData(startIndex, 20)
    }

    private fun loadData(startIndex: Int, itemsPerPage: Int) {
        val paramsMap = mutableMapOf<String, String?>()
        paramsMap["Search"] = mKeyWord
        paramsMap["MachineID"] = Constants.MACHINE_ID
        paramsMap["UserID"] = PreferenceUtil.getUserId(this)
        paramsMap["Branch"] = Constants.BRANCH_ID
        paramsMap["Start"] = startIndex.toString()
        paramsMap["Limit"] = itemsPerPage.toString()
        (applicationContext as WarehouseTestApp).warehouseService?.getSearchResult(paramsMap)
            ?.enqueue(object : Callback<SearchResult?> {
                override fun onResponse(call: Call<SearchResult?>, response: Response<SearchResult?>) {
                    if (response.isSuccessful) {
                        val searchResult = response.body()
                        val ifFound = searchResult!!.Found
                        if (ifFound == "Y") {
                            totalItemNum = searchResult.HitCount
                            this@SearchResultActivity.startIndex += 20
                            for (i in searchResult.Results!!.indices) {
                                val item = searchResult.Results!![i]
                                data.add(item!!.Products!![0])
                            }
                            searchResultAdapter!!.setData(data)
                            searchResultAdapter!!.setLoadState(searchResultAdapter!!.LOADING_COMPLETE)
                        }
                    } else {
                        Toast.makeText(
                            this@SearchResultActivity,
                            "Search failed!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<SearchResult?>, t: Throwable) {
                    Toast.makeText(this@SearchResultActivity, "Search failed!", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    companion object {
        var FLAG_KEY_WORD = "keyWord"
    }
}