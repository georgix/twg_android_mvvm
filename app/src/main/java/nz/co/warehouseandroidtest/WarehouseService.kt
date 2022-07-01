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
import nz.co.warehouseandroidtest.data.*
import retrofit2.Call
import retrofit2.http.QueryMap

interface WarehouseService {
    @get:GET("bolt/newuser.json")
    val newUserId: Call<User?>

    @GET("bolt/price.json")
    fun getProductDetail(@QueryMap paramMap: Map<String?, String?>?): Call<ProductDetail?>

    @GET("bolt/search.json")
    fun getSearchResult(@QueryMap paramMap: Map<String?, String?>?): Call<SearchResult?>
}