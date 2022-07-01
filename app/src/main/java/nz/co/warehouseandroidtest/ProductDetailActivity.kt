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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ProductDetailActivity : AppCompatActivity() {
    private var ivProduct: ImageView? = null
    private var tvProduct: TextView? = null
    private var ivClearance: ImageView? = null
    private var tvPrice: TextView? = null
    private var tvBarcode: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        ivProduct = findViewById<View>(R.id.iv_product) as ImageView
        tvProduct = findViewById<View>(R.id.tv_product) as TextView
        ivClearance = findViewById<View>(R.id.iv_clearance) as ImageView
        tvPrice = findViewById<View>(R.id.tv_price) as TextView
        tvBarcode = findViewById<View>(R.id.tv_barcode) as TextView
        val barCode = intent.extras.getString(FLAG_BAR_CODE)
        val paramMap = mutableMapOf<String, String?>()
        paramMap["BarCode"] = barCode
        paramMap["MachineID"] = Constants.MACHINE_ID
        paramMap["UserID"] = PreferenceUtil.getUserId(this)
        paramMap["Branch"] = Constants.BRANCH_ID
        (applicationContext as WarehouseTestApp).warehouseService?.getProductDetail(paramMap)
            ?.enqueue(object : Callback<ProductDetail?> {
                override fun onResponse(call: Call<ProductDetail?>, response: Response<ProductDetail?>) {
                    if (response.isSuccessful) {
                        val productDetail = response.body() as ProductDetail?
                        Glide.with(this@ProductDetailActivity)
                            .load(productDetail!!.Product!!.ImageURL).into(
                            ivProduct!!
                        )
                        tvProduct!!.text = productDetail.Product!!.ItemDescription
                        tvPrice!!.text = "$" + productDetail.Product!!.Price!!.Price
                        tvBarcode!!.text = productDetail.Product!!.Barcode
                        if (productDetail.Product!!.Price!!.Type == "CLR") {
                            ivClearance!!.visibility = View.VISIBLE
                        } else {
                            ivClearance!!.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(
                            this@ProductDetailActivity,
                            "Get product detail failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ProductDetail?>, t: Throwable) {
                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Get product detail failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    companion object {
        const val FLAG_INTENT_SOURCE_SCAN = 10001
        const val FLAG_INTENT_SOURCE_SEARCH = 10002
        const val FLAG_BAR_CODE = "barCode"
        const val FLAG_INTENT_SOURCE = "intentSource"
    }
}