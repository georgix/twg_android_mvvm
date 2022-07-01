package nz.co.warehouseandroidtest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.View
import nz.co.warehouseandroidtest.SearchResultActivity

class SearchActivity : AppCompatActivity() {
    private var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView = findViewById<View>(R.id.search_view) as SearchView
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    val intent = Intent()
                    intent.setClass(this@SearchActivity, SearchResultActivity::class.java)
                    intent.putExtra(SearchResultActivity.Companion.FLAG_KEY_WORD, query)
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }
}