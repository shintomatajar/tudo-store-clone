package ae.tudomart.store.ui.activities.ordersubstitute

import ae.tudomart.store.utils.loadCircleImage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ae.matajar.store.databinding.SelectedRecommendableProductBinding

class SubstitutionSelectedItemsAdapter(
    val onClickRemoveItem: (index: Int, item: ModelSubCategoryProductsResponse.Data) -> Unit,
) :
    RecyclerView.Adapter<SubstitutionSelectedItemsAdapter.ProductVH>() {

    val data: ArrayList<ModelSubCategoryProductsResponse.Data> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        return ProductVH(
            SelectedRecommendableProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        val product = data[position]
        holder.bindingView.apply {
            imageView.loadCircleImage(product.arrayThumbnail?.first()?.imageUrl ?: "")
            clear.setOnClickListener {
                onClickRemoveItem(position, product)
                data.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, data.size)
            }
        }
    }

    override fun getItemCount() = data.size
    fun addItem(item: ModelSubCategoryProductsResponse.Data) {
        if (data.contains(item)) return
        this.data.add(item)
        notifyItemInserted(data.size)
    }

    fun removeItem(item: ModelSubCategoryProductsResponse.Data) {
        if (!data.contains(item)) return
        val itemIndex = data.indexOf(item)
        this.data.remove(item)
        notifyItemRemoved(itemIndex)
        notifyItemRangeChanged(itemIndex, data.size)
    }

    class ProductVH(val bindingView: SelectedRecommendableProductBinding) :
        RecyclerView.ViewHolder(bindingView.root) {

    }
}