package nz.co.warehouseandroidtest

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import nz.co.warehouseandroidtest.R
import android.content.Intent
import nz.co.warehouseandroidtest.BarScanActivity
import nz.co.warehouseandroidtest.SearchActivity
import nz.co.warehouseandroidtest.PermissionActivity
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.WarehouseTestApp
import android.widget.Toast
import android.app.Activity
import nz.co.warehouseandroidtest.SearchResultActivity
import com.uuzuche.lib_zxing.activity.CaptureFragment
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.uuzuche.lib_zxing.activity.CodeUtils.AnalyzeCallback
import android.graphics.Bitmap
import nz.co.warehouseandroidtest.ProductDetailActivity
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import retrofit2.http.GET
import nz.co.warehouseandroidtest.WarehouseService
import okhttp3.OkHttpClient
import kotlin.Throws
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import android.support.v4.content.ContextCompat
import android.content.pm.PackageManager
import android.content.DialogInterface
import android.graphics.Rect
import android.support.v4.app.ActivityCompat
import android.view.ViewGroup
import android.view.LayoutInflater
import nz.co.warehouseandroidtest.SearchResultViewHolder
import nz.co.warehouseandroidtest.FooterViewHolder
import android.support.v4.widget.SwipeRefreshLayout
import nz.co.warehouseandroidtest.SearchResultAdapter
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DividerItemDecoration
import nz.co.warehouseandroidtest.EndlessRecyclerOnScrollListener
import com.bumptech.glide.Glide
import android.text.TextUtils
import android.view.View
import nz.co.warehouseandroidtest.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.HashMap

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
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        mKeyWord = intent.extras.getString(FLAG_KEY_WORD)
        loadData(startIndex, 20)
    }

    private fun loadData(startIndex: Int, itemsPerPage: Int) {
        val paramsMap: MutableMap<*, *> = HashMap<String, String>()
        paramsMap["Search"] = mKeyWord
        paramsMap["MachineID"] = Constants.MACHINE_ID
        paramsMap["UserID"] = PreferenceUtil.getUserId(this)
        paramsMap["Branch"] = Constants.BRANCH_ID
        paramsMap["Start"] = startIndex.toString()
        paramsMap["Limit"] = itemsPerPage.toString()
        (applicationContext as WarehouseTestApp).warehouseService.getSearchResult(paramsMap)
            .enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<*>?, response: Response<*>) {
                    if (response.isSuccessful) {
                        val searchResult = response.body() as SearchResult?
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

                override fun onFailure(call: Call<*>?, t: Throwable) {
                    Toast.makeText(this@SearchResultActivity, "Search failed!", Toast.LENGTH_LONG)
                        .show()
                }
            })
    }

    companion object {
        var FLAG_KEY_WORD = "keyWord"
    }
}