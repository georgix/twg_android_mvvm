package nz.co.warehouseandroidtest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import nz.co.warehouseandroidtest.PermissionActivity

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
        builder.setNeutralButton("Quit") { _, _ ->
            setResult(PERMISSION_DENIED)
            finish()
        }
        builder.setPositiveButton("Settings") { _, _ -> startAppSettings() }
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

    val permissions
        get() = intent.getStringArrayExtra(PERMISSION_EXTRA_FLAG)

    companion object {
        var PERMISSION_GRANTED = 0
        var PERMISSION_DENIED = 1
        const val PERMISSION_EXTRA_FLAG = "nz.co.warehouseandroidtest.permission.extra_permission"
        fun startActivityForResult(
            activity: Activity,
            requestCode: Int,
            permissions: Array<String>?
        ) {
            val intent = Intent()
            intent.setClass(activity, PermissionActivity::class.java)
            intent.putExtra(PERMISSION_EXTRA_FLAG, permissions)
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
        }
    }
}