package ae.tudomart.store.ui.activities.ordersubstitute

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ae.matajar.store.R

class SearchProductsActivity : AppCompatActivity() {

    var products: String? = null
    var strSubCategoryId: String? = null
    var order_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_products)
        strSubCategoryId = intent.getStringExtra("strSubCategoryId")
        products = intent.getStringExtra("products")
        order_id = intent.getStringExtra("order_id")
        initToolbar();
    }

    private fun initToolbar() {
        findViewById<TextView>(R.id.title_toolbar).text = "Search Products"
        findViewById<ImageView>(R.id.back_icon).setOnClickListener {
            finish()
        }
    }
}