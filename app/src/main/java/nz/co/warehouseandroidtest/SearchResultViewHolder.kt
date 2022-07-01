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
import android.widget.ImageView

class SearchResultViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(
    mItemView
) {
    private val mIvProduct: ImageView
    private val mTvProductName: TextView
    fun bind(product: ProductWithoutPrice?) {
        if (product == null) return
        if (!TextUtils.isEmpty(product.Description)) {
            mTvProductName.text = product.Description
        }
        if (!TextUtils.isEmpty(product.ImageURL)) {
            Glide.with(mIvProduct.context).load(product.ImageURL).into(mIvProduct)
        }
        mItemView.setOnClickListener {
            val intent = Intent()
            intent.setClass(mItemView.context, ProductDetailActivity::class.java)
            intent.putExtra(ProductDetailActivity.Companion.FLAG_BAR_CODE, product.Barcode)
            mItemView.context.startActivity(intent)
        }
    }

    init {
        mIvProduct = mItemView.findViewById(R.id.iv_product)
        mTvProductName = mItemView.findViewById(R.id.tv_product_name)
    }
}