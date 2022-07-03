package nz.co.warehouseandroidtest

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import nz.co.warehouseandroidtest.data.model.ProductWithoutPrice

class SearchResultAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: MutableList<ProductWithoutPrice?> = ArrayList()
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2
    private var currentLoadState = 2
    val LOADING = 1
    val LOADING_COMPLETE = 2
    val LOADING_END = 3
    fun setData(data: List<ProductWithoutPrice?>?) {
        if (data != null) {
            this.data.clear()
            this.data.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 == itemCount) {
            TYPE_FOOTER
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
            return SearchResultViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_refresh_footer, parent, false)
            return FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchResultViewHolder) {
            holder.bind(data[position])
        } else if (holder is FooterViewHolder) {
            val footerViewHolder = holder
            when (currentLoadState) {
                LOADING -> {
                    footerViewHolder.pbLoading.visibility = View.VISIBLE
                    footerViewHolder.tvLoading.visibility = View.VISIBLE
                    footerViewHolder.llEnd.visibility = View.GONE
                }
                LOADING_COMPLETE -> {
                    footerViewHolder.pbLoading.visibility = View.INVISIBLE
                    footerViewHolder.tvLoading.visibility = View.INVISIBLE
                    footerViewHolder.llEnd.visibility = View.GONE
                }
                LOADING_END -> {
                    footerViewHolder.pbLoading.visibility = View.GONE
                    footerViewHolder.tvLoading.visibility = View.GONE
                    footerViewHolder.llEnd.visibility = View.VISIBLE
                }
                else -> {}
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    fun setLoadState(loadState: Int) {
        currentLoadState = loadState
        notifyDataSetChanged()
    }
}