package com.tudomart.store.adapters.orders.orderDetails;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.tudomart.store.helpers.CircularImageProgress;
import com.tudomart.store.helpers.network.ApiUrl;

import com.tudomart.store.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.tudomart.store.helpers.CircularImageProgress;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.model.order.orderDetails.ModelOrderItemsPacking;

public class AdapterOrderDetailsItemsPacking extends RecyclerView.Adapter<AdapterOrderDetailsItemsPacking.ItemsVH> {

    ArrayList<ModelOrderItemsPacking> data;
    Context context;
    AdapterEvents callback;
    public int readyDispatch = 0;

    private boolean isUpdating = false;

    public void setUpdating(boolean updating) {
        isUpdating = updating;
        notifyDataSetChanged();
    }

    public ArrayList<ModelOrderItemsPacking> getList() {
        return data;
    }

    public AdapterOrderDetailsItemsPacking(ArrayList<ModelOrderItemsPacking> data, Context context, AdapterEvents callback) {
        this.data = data;
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ItemsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_order_packing_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsVH holder, final int position) {
        final ModelOrderItemsPacking dataItem = data.get(position);
        if (dataItem.getBlnSubstitute()) {
            holder.btnSubstitute.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
            holder.btnSubstitute.setText("ITEM SUBSTITUTED");
        } else {
            holder.btnSubstitute.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
            holder.btnSubstitute.setText("SUBSTITUTE ITEM");
        }
        holder.btnSubstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onSubtituteClick(dataItem);
            }
        });
        holder.mTxtProductName.setText(dataItem.getItemName());
        holder.mTxtQuantity.setText(dataItem.getItemUnit());
        Glide.with(context).load(dataItem.getImageUrl()).placeholder(new CircularImageProgress().getProgress(context))
                .error(new CircularImageProgress().getProgress(context)).into(holder.mImgProduct);
        if (dataItem.getBlnCheck().equalsIgnoreCase("true")) {
            dispatchedUi(holder);
//            holder.mImgCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));
        } else {
            readyDispatchUi(holder);
//            holder.mImgCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error));
            readyDispatch++;
        }
        if (dataItem.getFrozenFoode()) {
            holder.mTxtFrozenFood.setVisibility(View.VISIBLE);
        } else {
            holder.mTxtFrozenFood.setVisibility(View.GONE);
        }
        holder.mTxtPrice.setText(String.format("%.2f", Double.parseDouble(dataItem.getItemPrice())));
        holder.quantity_multiple.setText("x " + dataItem.getItemQuantity());
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
        holder.deleteSubstituteBtn.setOnClickListener(view -> {
            callback.deleteSubstiture(dataItem, position);
        });

        holder.orderDispatchBtn.setOnClickListener(v -> {
            callDispatchedApi(holder, dataItem, position, dataItem.getBlnCheck().equalsIgnoreCase("true"));
        });

        holder.mImgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ssdfsd", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void dispatchedUi(ItemsVH holder) {
        holder.orderDispatchBtn.setVisibility(VISIBLE);
        holder.orderDispatchBtn.setText("PACKED");
        holder.mImgCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check));
    }

    void readyDispatchUi(ItemsVH holder) {
        holder.orderDispatchBtn.setVisibility(View.VISIBLE);
        holder.orderDispatchBtn.setText("MARK AS PACKED");
        holder.mImgCheck.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_error));
    }

    void loading(boolean value, ItemsVH holder) {
        //      int progressVisible = value ? VISIBLE : GONE;
        int orderDispatchVisible = value ? GONE : VISIBLE;
        //  holder.loading.setVisibility(progressVisible);
        holder.orderDispatchBtn.setVisibility(orderDispatchVisible);
    }

    void callDispatchedApi(ItemsVH holder, ModelOrderItemsPacking dataItem, int position, boolean isChecked) {
        loading(true, holder);

        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("strOrderID", dataItem.getOrderId());
            requestBody.put("strLoginUserID", new UserSessionManager(context).getUserDetails().get("userid"));
            requestBody.put("strProducId", dataItem.getProductId());
            requestBody.put("strCheck", !isChecked);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.UPDATE_CHECK_PRODUCT, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading(false, holder);
                try {
                    if (response.getBoolean("success")) {
                        data.get(position).setBlnCheck(String.valueOf((!isChecked)));
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading(false, holder);
                Toast.makeText(context, context.getString(R.string.dialog_title_error_loading_data), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", new UserSessionManager(context).getUserDetails().get("token"));
                return params;
            }
        };
        RequestController.getInstance().addToRequestQueue(clearNotifRequest);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemsVH extends RecyclerView.ViewHolder {
        private ImageView mImgCheck;
        private ImageView mImgProduct;
        private TextView mTxtProductName;
        private TextView mTxtFrozenFood;
        private TextView mTxtQuantity;
        private TextView mTxtPrice;
        private TextView quantity_multiple;
        private TextView barcode;
        private TextView stock;
        private TextView btnSubstitute;
        private Button orderDispatchBtn;
        private ImageView deleteSubstituteBtn;
        //  private ProgressBar loading;


        public ItemsVH(@NonNull View itemView) {
            super(itemView);
            deleteSubstituteBtn = itemView.findViewById(R.id.deleteSubstitute);
            //   loading = itemView.findViewById(R.id.loadingProgress);
            mImgCheck = itemView.findViewById(R.id.imgCheck);
            orderDispatchBtn = itemView.findViewById(R.id.mainButton);
            mImgProduct = itemView.findViewById(R.id.img_product);
            mTxtProductName = itemView.findViewById(R.id.txt_product_name);
            mTxtFrozenFood = itemView.findViewById(R.id.txtFrozenFood);
            mTxtQuantity = itemView.findViewById(R.id.txt_quantity);
            mTxtPrice = itemView.findViewById(R.id.txt_price);
            quantity_multiple = itemView.findViewById(R.id.quantity_multiple);
            barcode = itemView.findViewById(R.id.txt_barcode);
            stock = itemView.findViewById(R.id.txt_product_stock);
            btnSubstitute = itemView.findViewById(R.id.subtitute);
        }
    }

    public interface AdapterEvents {
        void onItemClick(ModelOrderItemsPacking dataItem, ArrayList<ModelOrderItemsPacking> data);

        void onSubtituteClick(ModelOrderItemsPacking dataItem);

        void deleteSubstiture(ModelOrderItemsPacking delteItem, int position);
    }
}
