package nz.co.warehouseandroidtest

import nz.co.warehouseandroidtest.data.Price
import nz.co.warehouseandroidtest.data.SearchResultItem
import nz.co.warehouseandroidtest.data.Product
import nz.co.warehouseandroidtest.data.ProductWithoutPrice
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
import nz.co.warehouseandroidtest.data.ProductDetail
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
import java.util.ArrayList

class SearchResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: MutableList<ProductWithoutPrice?> = ArrayList()
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2
    private var currentLoadState = 2
    val LOADING = 1
    val LOADING_COMPLETE = 2
    val LOADING_END = 3
    fun setData(data: List<ProductWithoutPrice?>?) {
        if (data != null) {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 == itemCount) {
            TYPE_FOOTER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            return SearchResultViewHolder(view)
        } else if (viewType == TYPE_FOOTER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_refresh_footer, parent, false)
            return FooterViewHolder(view)
        }
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchResultViewHolder) {
            holder.bind(data[position])
        } else if (holder is FooterViewHolder) {
            val footerViewHolder = holder
            when (currentLoadState) {
                LOADING -> {
                    footerViewHolder.pbLoading.visibility = View.VISIBLE
                    footerViewHolder.tvLoading.visibility = View.VISIBLE
                    footerViewHolder.llEnd.visibility = View.GONE
                }
                LOADING_COMPLETE -> {
                    footerViewHolder.pbLoading.visibility = View.INVISIBLE
                    footerViewHolder.tvLoading.visibility = View.INVISIBLE
                    footerViewHolder.llEnd.visibility = View.GONE
                }
                LOADING_END -> {
                    footerViewHolder.pbLoading.visibility = View.GONE
                    footerViewHolder.tvLoading.visibility = View.GONE
                    footerViewHolder.llEnd.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    fun setLoadState(loadState: Int) {
        currentLoadState = loadState
        notifyDataSetChanged()
    }
}