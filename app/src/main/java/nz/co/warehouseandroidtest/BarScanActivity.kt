package nz.co.warehouseandroidtest

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.uuzuche.lib_zxing.activity.CaptureFragment
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.uuzuche.lib_zxing.activity.CodeUtils.AnalyzeCallback
import nz.co.warehouseandroidtest.ProductDetailActivity

class BarScanActivity : AppCompatActivity() {
    private val captureFragment = CaptureFragment()
    private var isOpen = false
    private var ivFlashToggle: ImageView? = null
    public override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_bar_scan)
        ivFlashToggle = findViewById<View>(R.id.iv_flashlight) as ImageView
        ivFlashToggle!!.setImageDrawable(getDrawable(R.mipmap.ic_flash_off_black_24dp))
        ivFlashToggle!!.setOnClickListener { toggleFlashlight() }
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_zxing_scan)
        captureFragment.analyzeCallback = analyzeCallback
        supportFragmentManager.beginTransaction().replace(R.id.fl_zxing_container, captureFragment)
            .commit()
    }

    private val analyzeCallback: AnalyzeCallback = object : AnalyzeCallback {
        override fun onAnalyzeSuccess(mBitmap: Bitmap, result: String) {
            val intent = Intent()
            intent.setClass(this@BarScanActivity, ProductDetailActivity::class.java)
            intent.putExtra(
                ProductDetailActivity.Companion.FLAG_INTENT_SOURCE,
                ProductDetailActivity.Companion.FLAG_INTENT_SOURCE_SCAN
            )
            intent.putExtra(ProductDetailActivity.Companion.FLAG_BAR_CODE, result)
            startActivity(intent)
        }

        override fun onAnalyzeFailed() {
            Toast.makeText(
                this@BarScanActivity,
                "Oops, bar code analysis failed!",
                Toast.LENGTH_SHORT
            )
            finish()
        }
    }

    fun toggleFlashlight() {
        isOpen = if (!isOpen) {
            CodeUtils.isLightEnable(true)
            ivFlashToggle!!.setImageDrawable(getDrawable(R.mipmap.ic_flash_on_black_24dp))
            true
        } else {
            CodeUtils.isLightEnable(false)
            ivFlashToggle!!.setImageDrawable(getDrawable(R.mipmap.ic_flash_off_black_24dp))
            false
        }
    }
}