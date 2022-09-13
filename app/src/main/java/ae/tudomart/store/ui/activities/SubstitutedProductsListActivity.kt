package ae.tudomart.store.ui.activities

import ae.tudomart.store.api.ProductsAPI
import ae.tudomart.store.ui.activities.ordersubstitute.AllItemsListAdapter
import ae.tudomart.store.ui.activities.ordersubstitute.ModelSubCategoryProductsResponse
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ae.matajar.store.R
import ae.matajar.store.databinding.ActivitySubstitutedProductsListBinding

class SubstitutedProductsListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubstitutedProductsListBinding
    private var orderId: String? = null
    private var productId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubstitutedProductsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        orderId = intent.getStringExtra(ORDER_ID)
        productId = intent.getStringExtra(PRODUCT_ID)
        initToolbar();
        initiUI()
    }

    private fun initiUI() {
        binding.btnRemove.setOnClickListener {
            val loginProgress = ProgressDialog(this@SubstitutedProductsListActivity)
            loginProgress.setMessage("Please Wait...")
            loginProgress.setCancelable(false)
            loginProgress.show()

            ProductsAPI(this).clearSubstitution(orderId ?: "", productId ?: "", {
                loginProgress.dismiss()
                finish()
                Toast.makeText(
                    this@SubstitutedProductsListActivity,
                    "Data cleared",
                    Toast.LENGTH_SHORT
                ).show()
            }, {
                loginProgress.dismiss()
                Toast.makeText(this@SubstitutedProductsListActivity, it, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun initToolbar() {
        findViewById<TextView>(R.id.title_toolbar).text = "Products"
        findViewById<ImageView>(R.id.back_icon).setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        getProducts()
    }

    private fun getProducts() {
        ProductsAPI(this).getSubstitutedProducts(orderId ?: "", productId ?: "", {
            initRecyclerView(it)
        }, {

        })
    }

    private fun initRecyclerView(list: List<ModelSubCategoryProductsResponse.Data?>) {
        binding.recyclerView.adapter = AllItemsListAdapter(list)
    }

    companion object {

        const val ORDER_ID = "ORDER_ID"
        const val PRODUCT_ID = "PRODUCT_ID"

        fun start(context: Context, orderId: String, productId: String) {
            context.startActivity(
                Intent(
                    context,
                    SubstitutedProductsListActivity::class.java
                ).apply {
                    putExtra(ORDER_ID, orderId)
                    putExtra(PRODUCT_ID, productId)
                })
        }
    }
}