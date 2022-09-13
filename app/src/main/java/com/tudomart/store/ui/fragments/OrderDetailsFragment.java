package com.tudomart.store.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.R;
import com.tudomart.store.adapters.orders.orderDetails.AdapterOrderDetailsItems;
import com.tudomart.store.model.order.orderDetails.ModelOrderItems;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.ui.customViews.P07FancyAlert;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderDetailsFragment extends Fragment {

    private TextView mTxtDayOfDate;
    private TextView mTxtMonthOfDate;
    private TextView mTxtOrderIdTop;
    private TextView mTxtPriceTop;
    private LinearLayout mHeader;
    private ImageView mIcOrderStar;
    private TextView mTxtOrdered;
    private ImageView mIcPackedStar;
    private TextView mTxtPackedStar;
    private ImageView mIcDispatchedStar;
    private TextView mTxtDispatched;
    private RecyclerView mOrderItemsRecycler;
    private TextView mTxtDeliveryAddress;
    private CardView mLayoutAddress;
    private ImageView mImgPaymentType;
    private TextView mTxtPaymentType;
    private CardView mLayoutPaymentType;
    private TextView mTxtSubTotal;
    private TextView mTxtDeliveryCharge;
    private TextView mTxtDiscount;
    private TextView mTxtTotalPrice;
    private CardView mLayoutPaymentInfo;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
   private LinearLayout mErrorParentLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;
    private Button mMainButton;
    private TextView mSecondButton;
    private LinearLayout mBottomButtons;
    private LinearLayout orderedStar;
    private NestedScrollView layoutData;
    private TextView mUserInfo;

    private ArrayList<ModelOrderItems> list = new ArrayList<>();

    public OrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_order_details, container, false);
        initViews(myView);
        mMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogSheet dialog = new MyDialogSheet(getActivity());
                dialog.setTitle("Confirm Order");
                dialog.setMessage("Are you sure to confirm this order");
                dialog.setPositiveButton("Yes", new DialogSheet.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        final P07FancyAlert alert = new P07FancyAlert(getActivity());
                        alert.setMessage("Order has been accepted");
                        alert.setButton("Continue", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                getActivity().finish();
                            }
                        });
                        alert.setGif(R.raw.animation_success);
                        alert.show();

                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.show();
            }
        });

        orderedStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.getVisibility() == VISIBLE) {
                    mUserInfo.setVisibility(GONE);
                }else{
                    mUserInfo.setVisibility(VISIBLE);
                }
            }
        });

        return myView;
    }


    public void fetchOrderDetails(String orderId) {
        getData();
        mErrorLayout.setVisibility(GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mBottomButtons.setVisibility(GONE);
        mSecondButton.setVisibility(GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mOrderItemsRecycler.setAdapter(new AdapterOrderDetailsItems(list, requireActivity(), new AdapterOrderDetailsItems.AdapterEvents() {
                    @Override
                    public void onItemClick(ModelOrderItems data) {


                    }
                }));
                mErrorLayout.setVisibility(GONE);
                mLoadingLayout.setVisibility(View.GONE);
                mBottomButtons.setVisibility(View.VISIBLE);
                mSecondButton.setVisibility(View.VISIBLE);

            }
        }, 3000);
    }

    private void getData() {
        /*for (int i = 0; i < 2; i++) {
            list.add(new ModelOrderItems(DummyData.drinks[0], "3", "30", "Product Name",false,"500 ML"));
            list.add(new ModelOrderItems(DummyData.drinks[0], "3", "30", "Product Name",true,"500 ML"));
        }*/
    }

    private void initViews(View myView) {
        mUserInfo = myView.findViewById(R.id.userInfo);
        mTxtDayOfDate = myView.findViewById(R.id.txt_day_of_date);
        mTxtMonthOfDate = myView.findViewById(R.id.txt_month_of_date);
        mTxtOrderIdTop = myView.findViewById(R.id.txt_order_id_top);
        mTxtPriceTop = myView.findViewById(R.id.txt_price_top);
        mHeader = myView.findViewById(R.id.header);
        mIcOrderStar = myView.findViewById(R.id.ic_order_star);
        mTxtOrdered = myView.findViewById(R.id.txt_ordered);
        mOrderItemsRecycler = myView.findViewById(R.id.orderItemsRecycler);
        mTxtDeliveryAddress = myView.findViewById(R.id.txt_delivery_address);
        mLayoutAddress = myView.findViewById(R.id.layout_address);
        mImgPaymentType = myView.findViewById(R.id.img_payment_type);
        mTxtPaymentType = myView.findViewById(R.id.txt_payment_type);
        mLayoutPaymentType = myView.findViewById(R.id.layout_payment_type);
        mTxtSubTotal = myView.findViewById(R.id.txt_sub_total);
        mTxtDeliveryCharge = myView.findViewById(R.id.txt_delivery_charge);
        mTxtDiscount = myView.findViewById(R.id.txtDiscount);
        mTxtTotalPrice = myView.findViewById(R.id.txt_total_price);
        mLayoutPaymentInfo = myView.findViewById(R.id.layout_payment_info);
        mImgError = myView.findViewById(R.id.img_error);
        mTxtErrorTitle = myView.findViewById(R.id.txt_error_title);
        mTxtErrorMessage = myView.findViewById(R.id.txt_error_message);
        mBtnRetry = myView.findViewById(R.id.btn_retry);
        mErrorLayout = myView.findViewById(R.id.error_layout);
        mErrorParentLayout = myView.findViewById(R.id.error_parent_layout);
        mLoadingProgress = myView.findViewById(R.id.loadingProgress);
        mLoadingLayout = myView.findViewById(R.id.loadingLayout);
        mMainButton = myView.findViewById(R.id.mainButton);
        mSecondButton = myView.findViewById(R.id.secondButton);
        mBottomButtons = myView.findViewById(R.id.bottomButtons);
        layoutData = myView.findViewById(R.id.layoutData);
        orderedStar = myView.findViewById(R.id.orderedStar);


    }
}
