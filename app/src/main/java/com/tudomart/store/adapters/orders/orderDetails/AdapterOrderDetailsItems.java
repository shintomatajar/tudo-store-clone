package com.tudomart.store.adapters.orders.orderDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tudomart.store.helpers.CircularImageProgress;

import com.tudomart.store.R;

import com.tudomart.store.helpers.CircularImageProgress;
import com.tudomart.store.model.order.orderDetails.ModelOrderItems;

import java.util.ArrayList;

import static android.view.View.GONE;

public class AdapterOrderDetailsItems extends RecyclerView.Adapter<AdapterOrderDetailsItems.ItemsVH> {

    ArrayList<ModelOrderItems> data;
    Context context;
    AdapterEvents callback;

    public AdapterOrderDetailsItems(ArrayList<ModelOrderItems> data, Context context, AdapterEvents callback) {
        this.data = data;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ItemsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_order_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsVH holder, int position) {
        final ModelOrderItems dataItem = data.get(position);
//        holder.mImgProduct.setText();
        holder.mTxtProductName.setText(dataItem.getItemName());
        // holder.mTxtOrderId.setText("#MAB123AETH09");
        holder.mTxtQuantity.setText(dataItem.getItemUnit());
        holder.mTxtPrice.setText(String.format("%.2f", Double.parseDouble(dataItem.getItemPrice())));
        holder.quantity_multiple.setText("x " + dataItem.getItemQuantity());
        //holder.mTxtStatus.setText("Yet to deliver");
        // holder.mTxtStatus.setTextColor(Color.GREEN);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(dataItem);
            }
        });
        if (dataItem.getBlnCheck().equalsIgnoreCase("true")) {
            holder.imgCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));
        } else {
            holder.imgCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error));
        }
        Glide.with(context).load(dataItem.getImageUrl()).placeholder(new CircularImageProgress().getProgress(context))
                .error(new CircularImageProgress().getProgress(context)).into(holder.mImgProduct);

        if (dataItem.getFrozenFoode()) {
            holder.mTxtFrozenFood.setVisibility(View.VISIBLE);
        }

        holder.barcode.setText(dataItem.getBarcode());

        if (dataItem.getStock().equalsIgnoreCase("A")) {
            holder.stock.setVisibility(GONE);
        } else if (dataItem.getStock().equalsIgnoreCase("N")) {
            holder.stock.setVisibility(View.VISIBLE);
            holder.stock.setText("Out of Stock");
        } else /*if (dataItem.getStock().equalsIgnoreCase("C"))*/ {
            holder.stock.setVisibility(View.VISIBLE);
            holder.stock.setText(dataItem.getStock());
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemsVH extends RecyclerView.ViewHolder {
        private ImageView mImgProduct;
        private ImageView imgCheck;
        private TextView mTxtProductName;
        private TextView mTxtQuantity;
        private TextView mTxtPrice;
        private TextView mTxtStatus;
        private TextView mTxtOrderId;
        private TextView mTxtFrozenFood;
        private TextView quantity_multiple;
        private TextView barcode;
        private TextView stock;


        public ItemsVH(@NonNull View itemView) {
            super(itemView);
            mImgProduct = itemView.findViewById(R.id.img_product);
            mTxtProductName = itemView.findViewById(R.id.txt_product_name);
            mTxtQuantity = itemView.findViewById(R.id.txt_quantity);
            mTxtPrice = itemView.findViewById(R.id.txt_price);
            mTxtStatus = itemView.findViewById(R.id.text_status);
            mTxtOrderId = itemView.findViewById(R.id.text_order_id);
            mTxtFrozenFood = itemView.findViewById(R.id.txtFrozenFood);
            quantity_multiple = itemView.findViewById(R.id.quantity_multiple);
            imgCheck = itemView.findViewById(R.id.imgCheck);
            barcode = itemView.findViewById(R.id.txt_barcode);
            stock = itemView.findViewById(R.id.txt_product_stock);
        }
    }

    public interface AdapterEvents {
        void onItemClick(ModelOrderItems data);
    }
}
