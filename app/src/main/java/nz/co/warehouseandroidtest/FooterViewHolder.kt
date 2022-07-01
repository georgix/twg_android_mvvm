package nz.co.warehouseandroidtest

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var pbLoading: ProgressBar
    var tvLoading: TextView
    var llEnd: LinearLayout

    init {
        pbLoading = itemView.findViewById<View>(R.id.pb_loading) as ProgressBar
        tvLoading = itemView.findViewById<View>(R.id.tv_loading) as TextView
        llEnd = itemView.findViewById<View>(R.id.ll_end) as LinearLayout
    }
}