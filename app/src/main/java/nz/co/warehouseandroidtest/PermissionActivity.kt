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
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.view.ViewGroup
import android.view.LayoutInflater
import nz.co.warehouseandroidtest.SearchResultViewHolder
import nz.co.warehouseandroidtest.FooterViewHolder
import android.support.v4.widget.SwipeRefreshLayout
import nz.co.warehouseandroidtest.SearchResultAdapter
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DividerItemDecoration
import nz.co.warehouseandroidtest.EndlessRecyclerOnScrollListener
import com.bumptech.glide.Glide
import android.text.TextUtils
import java.lang.RuntimeException

class PermissionActivity : AppCompatActivity() {
    val PACKAGE_URL_SCHEME = "package:"
    val PERMISSION_REQUEST_CODE = 0
    var mIsRequiredPermissionCheck = false
    private val checker = PermissionChecker(this)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || !intent.hasExtra(PERMISSION_EXTRA_FLAG)) {
            throw RuntimeException("PermissionsActivity needs to be started by static method startActivityForResult!")
        }
        setContentView(R.layout.activity_permission)
        mIsRequiredPermissionCheck = true
    }

    public override fun onResume() {
        super.onResume()
        if (mIsRequiredPermissionCheck) {
            if (checker.ifLackPermissions(permissions)) {
                requestPermissions(permissions)
            } else {
                allPermissionsGranted()
            }
        } else {
            mIsRequiredPermissionCheck = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (PERMISSION_REQUEST_CODE == requestCode && hasAllPermissionsGranted(grantResults)) {
            mIsRequiredPermissionCheck = true
            allPermissionsGranted()
        } else {
            mIsRequiredPermissionCheck = false
            showMissingPermissionDialog()
        }
    }

    private fun showMissingPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Help")
        builder.setMessage("Current app lacks necessary permissions. \n\nPlease click \"Settings\" - \"Permission\" - to grant permissions. \n\nThen click back button twice to return.")
        builder.setNeutralButton("Quit") { dialog, which ->
            setResult(PERMISSION_DENIED)
            finish()
        }
        builder.setPositiveButton("Settings") { dialog, which -> startAppSettings() }
        builder.show()
    }

    fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }

    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    private fun allPermissionsGranted() {
        setResult(PERMISSION_GRANTED)
        finish()
    }

    fun requestPermissions(permissions: Array<String>?) {
        ActivityCompat.requestPermissions(this, permissions!!, PERMISSION_REQUEST_CODE)
    }

    val permissions: Array<String>
        get() = intent.getStringArrayExtra(PERMISSION_EXTRA_FLAG)

    companion object {
        var PERMISSION_GRANTED = 0
        var PERMISSION_DENIED = 1
        const val PERMISSION_EXTRA_FLAG = "nz.co.warehouseandroidtest.permission.extra_permission"
        fun startActivityForResult(
            activity: Activity?,
            requestCode: Int,
            permissions: Array<String>?
        ) {
            val intent = Intent()
            intent.setClass(activity, PermissionActivity::class.java)
            intent.putExtra(PERMISSION_EXTRA_FLAG, permissions)
            ActivityCompat.startActivityForResult(activity!!, intent, requestCode, null)
        }
    }
}