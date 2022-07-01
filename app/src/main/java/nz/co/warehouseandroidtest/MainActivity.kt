package nz.co.warehouseandroidtest

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.data.User
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