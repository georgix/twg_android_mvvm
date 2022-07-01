package nz.co.warehouseandroidtest

import android.Manifest
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
import android.view.View
import nz.co.warehouseandroidtest.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var permissions = arrayOf(Manifest.permission.CAMERA)
    var checker = PermissionChecker(this)
    val REQUEST_PERMISSION_CODE = 0
    private var tvScan: TextView? = null
    private var tvSearch: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvScan = findViewById<View>(R.id.tv_scan_barcode) as TextView
        tvSearch = findViewById<View>(R.id.tv_search) as TextView
        tvScan!!.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, BarScanActivity::class.java)
            startActivity(intent)
        }
        tvSearch!!.setOnClickListener {
            val intent = Intent()
            intent.setClass(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        if (checker.ifLackPermissions(permissions)) {
            PermissionActivity.Companion.startActivityForResult(
                this,
                REQUEST_PERMISSION_CODE,
                permissions
            )
        }
        if (PreferenceUtil.getUserId(this) == null) {
            (applicationContext as WarehouseTestApp).warehouseService?.newUserId?.enqueue(object :
                Callback<User?> {
                override fun onResponse(call: Call<User?>, response: Response<User?>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (null != user && null != user.UserID) {
                            PreferenceUtil.putUserId(this@MainActivity, user.UserID)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Get User failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<User?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Get User failed!", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (RESULT_OK != resultCode) {
            return
        }
        if (requestCode == REQUEST_PERMISSION_CODE && resultCode == PermissionActivity.Companion.PERMISSION_DENIED) {
            finish()
        }
    }
}