package nz.co.warehouseandroidtest.Utils

import android.content.Context
import nz.co.warehouseandroidtest.Constants

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