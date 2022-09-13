package com.tudomart.store.ui.activities.ordersubstitute

import com.tudomart.store.utils.Utility
import com.tudomart.store.utils.loadImage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tudomart.store.databinding.RecyclerAllItemsListBinding

class AllItemsListAdapter(
    val data: List<ModelSubCategoryProductsResponse.Data?>?,
) : RecyclerView.Adapter<AllItemsListAdapter.ItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val itemBinding =
            RecyclerAllItemsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemVH(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bind(data?.get(position))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ItemVH(private val itemViewBinding: RecyclerAllItemsListBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {
        fun bind(item: ModelSubCategoryProductsResponse.Data?) {
            item?.let {
                itemViewBinding.apply {
                    productImage.loadImage(item.arrayThumbnail?.get(0)?.imageUrl ?: "")
                    tvProductName.text = item.strProductName
                    tvPrice.text = Utility.getFormattedPrice(item.intSellingPrice ?: 0.0)
                }

            }
        }

        private fun removeItem(
            item: ModelSubCategoryProductsResponse.Data,
            absoluteAdapterPosition: Int
        ) {

        }
    }
}