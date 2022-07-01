package nz.co.warehouseandroidtest

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import nz.co.warehouseandroidtest.ProductDetailActivity
import nz.co.warehouseandroidtest.data.ProductWithoutPrice

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