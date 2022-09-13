package com.tudomart.store.adapters.orders.orderList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tudomart.store.model.order.ModelOrderList;

import com.tudomart.store.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.tudomart.store.model.order.ModelOrderList;

;

public class AdapterStorePaginatedSpittedListing extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = AdapterStorePaginatedSpittedListing.class.getSimpleName();

    private Context context;
    AdapterCallback callback;
    //private AdapterCallback onItemClickListener;
    //private ArrayList<FinalDashboardModel> data = new ArrayList<>();
    private int SLOT_ITEM = 1122;
    private int DATE_ITEM = 2244;

    ArrayList<ModelOrderList> data = new ArrayList<>();


    public AdapterStorePaginatedSpittedListing(Context context, AdapterCallback onItemClickListener) {
        this.context = context;
        this.callback = onItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_my_orders_item, parent, false);
            return new ItemsVH(view);
       /* if (viewType == SLOT_ITEM)
        {
        } else {
            view = LayoutInflater.from(parent.requireActivity()).inflate(R.layout.recycler_dashboard_title, parent, false);
            return new TitleVH(view);
        }*/
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if (data.get(position)!=null) {

            ItemsVH byItemsholder = (ItemsVH) holder;


            final ModelOrderList dataItem = data.get(position);

            byItemsholder.mMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClickItem(dataItem.getOrderId(), dataItem.getStatus(), dataItem.getTimeStamp());
                }
            });
            if (!dataItem.getStrPlace().equalsIgnoreCase("null")){
                byItemsholder.strEmirate.setText(dataItem.getStrEmirate() + " | "+dataItem.getStrPlace());
            } else {
                byItemsholder.strEmirate.setText(dataItem.getStrEmirate() );
            }

            if (dataItem.getStatus() == 0) {
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_green));
                byItemsholder.mTextView2.setText("Store Delivered");
                byItemsholder.mTextStatus.setText("Store Delivered");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_green_button));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
//                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_blue));
//                byItemsholder.mTextStatus.setText("New Order");
//                byItemsholder.mTextView2.setText("Accept");
//                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_blue_rectangle));
//                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            } else if (dataItem.getStatus() == 1) {
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_orange));
                byItemsholder.mTextStatus.setText("Packing");
                byItemsholder.mTextView2.setText("Packing");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_orange_rectangle));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.dark_yellow));
            } else if (dataItem.getStatus() == 2) {
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_packed));
                byItemsholder.mTextStatus.setText("Ready to Dispatch");
                byItemsholder.mTextView2.setText("Packed");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_packed_button));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.packed));
            }

            else if (dataItem.getStatus() == 3) {
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_blue));
                byItemsholder.mTextStatus.setText("Yet To Deliver");
                byItemsholder.mTextView2.setText("Dispatched");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_cyan_button));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.skyBlue));
            }
            else if (dataItem.getStatus() == 5) {
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_red));
                byItemsholder.mTextStatus.setText("Returned");
                byItemsholder.mTextView2.setText("Returned");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_red_rectangle));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
            }

            else if (dataItem.getStatus() == 4){
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_green));
                byItemsholder.mTextView2.setText("Delivered");
                byItemsholder.mTextStatus.setText("Delivered");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_green_button));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
            else if (dataItem.getStatus() == 6){
                byItemsholder.mImgIndicator.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_circle_cancelled));
                byItemsholder.mTextView2.setText("Cancelled");
                byItemsholder.mTextStatus.setText("Cancelled");
                byItemsholder.mTextView2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_canceled_button));
                byItemsholder.mTextStatus.setTextColor(ContextCompat.getColor(context, R.color.cancelled));
            }

            byItemsholder.mTextCustomerName.setText(dataItem.getOrderId());
            byItemsholder.mTextPlace.setText(dataItem.getPaymentType());
            byItemsholder.mTextCash.setText(String.format("%.2f", Double.parseDouble(dataItem.getAmount())));
            byItemsholder.mTextItems.setText(dataItem.getItemsCount());
            byItemsholder.mTextView.setText(dataItem.getDuration());
            byItemsholder.mTextView3.setText(dataItem.getTimeStamp());
            byItemsholder.qty_items.setText(dataItem.getItemsCount());
            if (dataItem.getTimeSlot().length()>0)
            {
                byItemsholder.mTxtSlot.setVisibility(View.VISIBLE);
                byItemsholder.mTxtSlot.setText(dataItem.getTimeSlot());
            }
            else
            {
                byItemsholder.mTxtSlot.setVisibility(View.GONE);
            }

            if (dataItem.getDeliveryType().length()>0)
            {
                byItemsholder.mDeliveryType.setVisibility(View.VISIBLE);
                if (data.get(position).getDeliveryType().equalsIgnoreCase("EXPRESS")) {
                    byItemsholder.mDeliveryType.setText("Express");
                    byItemsholder.mDeliveryType.setBackgroundResource(R.drawable.background_express_button);
                }

                else
                if (data.get(position).getDeliveryType().equalsIgnoreCase("NORMAL"))
                {
                    byItemsholder.mDeliveryType.setText("Normal");
                    byItemsholder.mDeliveryType.setBackgroundResource(R.drawable.background_normal_button);

                }

            }
            else
            {
                byItemsholder.mDeliveryType.setVisibility(View.GONE);
            }


        }


    }

    private String getTitleText(String inputDate) {
        String ouputString = inputDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        try {
            Date input = sdf.parse(inputDate);
            Date currentDate = sdf.parse(sdf.format(today));
            Date tomorrowDate = sdf.parse(sdf.format(tomorrow));
            if (input != null) {
                if (input.compareTo(currentDate) == 0) {
                    ouputString = "Today";
                } else if (tomorrowDate != null) {
                    if (tomorrowDate.compareTo(input) == 0) {
                        ouputString = "Tomorrow";
                    } else {
                        SimpleDateFormat newFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
                        ouputString = newFormat.format(sdf.parse(inputDate));

                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ouputString;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

   /* public interface OnItemClickListener {
        void onItemClick(FinalDashboardModel dataItem);
    }*/

    public void updateData(ArrayList<ModelOrderList> data) {
        for (ModelOrderList datum : data) {
            this.data.add(datum);
            notifyItemInserted(this.data.indexOf(datum));
        }
    }

   /* @Override
    public int getItemViewType(int position) {
        if (data.get(position).getTitle()) {
            return DATE_ITEM;
        } else {
            return SLOT_ITEM;
        }
    }*/

    class ItemsVH extends RecyclerView.ViewHolder {
        private ImageView mOrderLogo;
        private TextView mTextCustomerName;
        private TextView mTextPlace;
        private TextView mTextCashText;
        private TextView mTextCash;
        private TextView mTextItems;
        private TextView mTextItemsItems;
        private View mImgIndicator;
        private TextView mTextStatus;
        private TextView strEmirate;
        private TextView mTextView;
        private TextView mTextView2;
        private TextView qty_items;
        private TextView mTextView3;
        private CardView mMainLayout;
        private TextView mTxtSlot;
        private TextView mDeliveryType;
        
        public ItemsVH(@NonNull View itemView) {
            super(itemView);
            mOrderLogo = itemView.findViewById(R.id.order_logo);
            mTextCustomerName = itemView.findViewById(R.id.text_order_id);
            mTextPlace = itemView.findViewById(R.id.text_place);
            mTextCashText = itemView.findViewById(R.id.text_cash_text);
            mTextCash = itemView.findViewById(R.id.text_cash);
            mTextItems = itemView.findViewById(R.id.text_items);
            mTextItemsItems = itemView.findViewById(R.id.text_items_items);
            mImgIndicator = itemView.findViewById(R.id.img_indicator);
            mTextStatus = itemView.findViewById(R.id.text_status);
            strEmirate = itemView.findViewById(R.id.text_orderid);
            mTextView = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView3);
            mMainLayout = itemView.findViewById(R.id.main_layout);
            qty_items = itemView.findViewById(R.id.qty_items);
            mTxtSlot = itemView.findViewById(R.id.txt_slot);
            mDeliveryType = itemView.findViewById(R.id.delivery_status);
            
        }
    }

    public interface AdapterCallback {
        void onClickItem(String orderId, int orderStatus, String timeStamp);
    }


    public ModelOrderList removeItem(int position) {

        ModelOrderList item = null;
        if (data.get(position)!=null)
        {

            try {
                item = data.get(position);
                data.remove(position);
                notifyItemRemoved(position);
                // notifyItemRangeChanged(position,data.size());
            } catch(Exception e) {
                Log.e("TAG", e.getMessage());
            }

        }

        return item;
    }



    public ModelOrderList getItem(int position) {
        ModelOrderList item = null;
        try {
            item = data.get(position);

        } catch(Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return item;
    }

    public void addItem(ModelOrderList item, int position) {
        try {
            data.add(position, item);
            notifyItemInserted(position);
        } catch(Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    public void clearData(){
        if (data!=null) {
            data.clear();
            notifyDataSetChanged();
        }
    }

}
