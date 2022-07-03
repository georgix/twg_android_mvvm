package nz.co.warehouseandroidtest.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import nz.co.warehouseandroidtest.Constants
import nz.co.warehouseandroidtest.EndlessRecyclerOnScrollListener
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.SearchResultAdapter
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductWithoutPrice
import nz.co.warehouseandroidtest.databinding.FragmentHomeBinding
import nz.co.warehouseandroidtest.databinding.FragmentListBinding
import nz.co.warehouseandroidtest.ui.viewmodel.ListViewModel

@AndroidEntryPoint
class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ListViewModel
    private val data: MutableList<ProductWithoutPrice?> = ArrayList()
    private lateinit var listAdapter: SearchResultAdapter
    private var user: String? = null
    private var totalItemNum = 0
    private var startIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listAdapter = SearchResultAdapter()
        binding.lists.adapter = listAdapter
        binding.lists.addOnScrollListener(object: EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                listAdapter.setLoadState(listAdapter.LOADING)
                if (data.size < totalItemNum) {
                    loadData(startIndex, 20)
                } else {
                    listAdapter.setLoadState(listAdapter.LOADING_END)
                }
            }
        })
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> showLoading(false)
                is Result.Success -> {
                    showLoading(false)
                    user = it.data?.UserID
                }
            }
        })
        viewModel.getUserId()
        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> showLoading(true)
                is Result.Error -> showLoading(false)
                is Result.Success -> {
                    showLoading(false)
                    val searchResult = it.data
                    val ifFound = searchResult!!.Found
                    if (ifFound == "Y") {
                        totalItemNum = searchResult.HitCount.toInt()
                        startIndex += searchResult.Results.size
                        for (i in searchResult.Results!!.indices) {
                            val item = searchResult.Results!![i]
                            val product = item!!.Products!![0]!!
                            if (product.Description == null) {
                                product.Description = item.Description
                            }
                            data.add(product)
                        }
                        listAdapter!!.setData(data)
                        listAdapter!!.setLoadState(listAdapter!!.LOADING_COMPLETE)
                    }
                }
            }
        }
        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if( query.isNotEmpty()) {
                    loadData(0, 20)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        binding.swipeRefresh.setOnRefreshListener {
            data.clear()
            startIndex = 0
            totalItemNum = 0
            loadData(0, 20)
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.swipeRefresh.isRefreshing = loading
        binding.networkStatus.visibility = if(loading) View.VISIBLE else View.INVISIBLE
    }

    private fun loadData(startIndex: Int, itemsPerPage: Int) {
        val paramsMap = mutableMapOf<String, String>()
        paramsMap["Search"] = binding.search.query.toString()
        paramsMap["MachineID"] = Constants.MACHINE_ID
        paramsMap["UserID"] = user ?: ""
        paramsMap["Branch"] = Constants.BRANCH_ID
        paramsMap["Start"] = startIndex.toString()
        paramsMap["Limit"] = itemsPerPage.toString()
        viewModel.getSearchProducts(paramsMap)
    }

    private fun loadMore() {
        if (binding.search.query.toString().isNotEmpty() && startIndex < totalItemNum) {
            loadData(startIndex, 20)
        } else {
            showLoading(false)
        }
    }

}