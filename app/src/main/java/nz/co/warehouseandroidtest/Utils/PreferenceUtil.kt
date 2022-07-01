package nz.co.warehouseandroidtest.Utils

import nz.co.warehouseandroidtest.data.Price
import nz.co.warehouseandroidtest.data.SearchResultItem
import nz.co.warehouseandroidtest.data.Product
import nz.co.warehouseandroidtest.data.ProductWithoutPrice
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import android.content.Intent
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import android.widget.Toast
import android.app.Activity
import android.content.Context
import com.uuzuche.lib_zxing.activity.CaptureFragment
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.uuzuche.lib_zxing.activity.CodeUtils.AnalyzeCallback
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import retrofit2.http.GET
import nz.co.warehouseandroidtest.data.ProductDetail
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
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import android.text.TextUtils
import nz.co.warehouseandroidtest.*

object PreferenceUtil {
    fun getUserId(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences(Constants.PREF_USER_ID, Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.PREF_USER_ID, null)
    }

    fun putUserId(context: Context, userId: String?) {
        val sharedPreferences =
            context.getSharedPreferences(Constants.PREF_USER_ID, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.PREF_USER_ID, userId)
        editor.commit()
    }
}