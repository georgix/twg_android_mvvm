package nz.co.warehouseandroidtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.data.ProductDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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