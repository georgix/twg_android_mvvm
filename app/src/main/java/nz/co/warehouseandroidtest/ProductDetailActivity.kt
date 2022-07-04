package nz.co.warehouseandroidtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.common.Result
import nz.co.warehouseandroidtest.data.model.ProductDetail
import nz.co.warehouseandroidtest.databinding.ActivityProductDetailBinding
import nz.co.warehouseandroidtest.ui.viewmodel.DetailViewModel
import nz.co.warehouseandroidtest.ui.viewmodel.ListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {
    private var _binding: ActivityProductDetailBinding? = null
    private val binding get()=_binding!!
    private lateinit var viewModel: DetailViewModel
    private var barCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barCode = intent.extras?.getString(FLAG_BAR_CODE)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val paramMap = mutableMapOf<String, String>()
        paramMap["BarCode"] = barCode ?: ""
        paramMap["MachineID"] = Constants.MACHINE_ID
        paramMap["UserID"] = PreferenceUtil.getUserId(this) ?: ""

        viewModel.productDetail.observe(this) {
            when (it) {
                is Result.Loading -> {}
                is Result.Error -> showError()
                is Result.Success -> {
                    val productDetail = it.data
                    if (productDetail == null) {
                        showError()
                        return@observe
                    }
                    Glide.with(this@ProductDetailActivity)
                        .load(productDetail.Product?.ImageURL).into(
                            binding.ivProduct
                        )
                    binding.tvProduct.text = productDetail.Product.ItemDescription
                    binding.tvPrice.text = "$" + productDetail.Product.Price.Price
                    binding.tvBarcode.text = productDetail.Product.Barcode
                    binding.ivClearance.visibility = if (productDetail.Product.Price.Type == "CLR")
                        View.VISIBLE else View.GONE
                }
            }
        }
        viewModel.getSearchProducts(paramMap)
    }

    private fun showError() {
        Toast.makeText(
            this@ProductDetailActivity,
            "Get product detail failed!",
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val FLAG_INTENT_SOURCE_SCAN = 10001
        const val FLAG_INTENT_SOURCE_SEARCH = 10002
        const val FLAG_BAR_CODE = "barCode"
        const val FLAG_INTENT_SOURCE = "intentSource"
    }
}