package ae.tudomart.store.ui.activities.ordersubstitute

import ae.tudomart.store.utils.AppUtils
import ae.tudomart.store.utils.loadImage
import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ae.matajar.store.R
import ae.matajar.store.databinding.ProductListItemLargeBinding


class SearchResultProductAdapter(
    val activity: Activity,
    val addItem: (item: ModelSubCategoryProductsResponse.Data, Int) -> Unit,
    val onClickRemoveItem: (index: Int, item: ModelSubCategoryProductsResponse.Data) -> Unit,
) : RecyclerView.Adapter<SearchResultProductAdapter.ProductVH>() {

    var data: ArrayList<ModelSubCategoryProductsResponse.Data> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val itemBinding =
            ProductListItemLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductVH(itemBinding)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        val product = data[position]
        holder.bindingView.apply {
            ivRecommendedItem.loadImage(product.arrayThumbnail?.get(0)?.imageUrl ?: "")
            tvProductName.text = product.strProductName ?: ""
            tvProductLabel.text = product.strDescription
//          tvItemCount.text = product.quantity.toString()
//          tvOfferPercent.text = Utility.getFormattedDiscount(product.intDiscount ?: 0.0)
            tvProductPrice.text = AppUtils.getFormattedPrice(product.intMRP ?: 0.0)
            tvOfferPrice.text =
                AppUtils.getFormattedPrice(product.intSellingPrice ?: 0.0 * product.quantity)
//            tvIncrease.setOnClickListener {
//                var currentQuantity = data[position].quantity
//                if (currentQuantity == 0) {
//                    addItem(product, position)
//                } else {
//                    updateItem(++currentQuantity, product, position)
//                }
//            }
//            tvDecrease.setOnClickListener {
//                var currentQuantity = data[position].quantity
//                if (currentQuantity == 0) return@setOnClickListener
//                if (currentQuantity > 1) {
//                    updateItem(--currentQuantity, product, position)
//                } else {
//                    onClickRemoveItem(position, product)
//                }
//            }
//            imgDisc.isVisible = !product.strDisclaimer.isNullOrBlank()
            if (!product.strDisclaimer.isNullOrBlank()) {
//                imgDisc.setOnClickListener {
//                    Dialogs.showDisclaimerAlert(product.strDisclaimer!!, activity)
//                }
            }
            if (product.isSelected) {
                btnAdd.text = "Remove"
            } else {
                btnAdd.text = "Add"
            }

            cvAdd.setOnClickListener {
                val isSelected = product.isSelected
                data[position].isSelected = !isSelected
                if (isSelected) {
                    btnAdd.text = "Add"
                    onClickRemoveItem(position, product)
                } else {
                    btnAdd.text = "Remove"
                    addItem(product, position)
                }
            }
            /*cvAdd.visibility = if (product.quantity == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }

            cvUpdateCountLayout.visibility = if (product.quantity == 0) {
                View.GONE
            } else {
                View.VISIBLE
            }*/
            if ((product.intDiscount ?: 0.0) > 0) {
//                tvProductPrice.show()
//                tvOfferPercent.show()
            } else {
//                tvProductPrice.hide()
//                tvOfferPercent.hide()
            }
            tvProductPrice.paintFlags = tvProductPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            if (product.blnStockAvailability == true) {
//                vOutOfStock.hide()
                cvAdd.isEnabled = true
//                tvDecrease.isEnabled = true
//                tvIncrease.isEnabled = true
                cvAdd.setCardBackgroundColor(
                    ContextCompat.getColor(
                        cvAdd.context,
                        R.color.colorPrimary
                    )
                )
            } else {
//                vOutOfStock.show()
                cvAdd.setCardBackgroundColor(Color.LTGRAY)
                cvAdd.isEnabled = false
//                tvDecrease.isEnabled = false
//                tvIncrease.isEnabled = false
            }
//            imgDisc.isVisible = !product.strDisclaimer.isNullOrBlank()
            if (!product.strDisclaimer.isNullOrBlank()) {
//                imgDisc.setOnClickListener {
//                    Dialogs.showDisclaimerAlert(product.strDisclaimer!!, activity)
//                }
            }
            btnExpress.isVisible = product.blnExpress ?: false
        }

    }

    fun updateData(data: List<ModelSubCategoryProductsResponse.Data?>?) {
        data?.let { list ->
            for (item in list) {
                item?.let {
                    this.data.add(it)
                    notifyItemInserted(this.data.size)
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun itemRemoved(index: Int) {
        data[index].quantity = 0
        notifyItemChanged(index)
    }

    fun quantityUpdated(position: Int, quantity: Int) {
        data[position].quantity = quantity
        notifyItemChanged(position)
    }

    fun clear() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun removeItem(item: ModelSubCategoryProductsResponse.Data) {
        if (!data.contains(item)) return
        val index = data.indexOf(item)
        data.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index,data.size)
    }

    inner class ProductVH(val bindingView: ProductListItemLargeBinding) :
        RecyclerView.ViewHolder(bindingView.root)
}