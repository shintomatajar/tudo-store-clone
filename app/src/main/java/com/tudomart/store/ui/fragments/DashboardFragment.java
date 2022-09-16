package com.tudomart.store.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.R;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.ui.activities.AllOrdersActivity;
import com.tudomart.store.ui.activities.orders.DispatchedListActivity;
import com.tudomart.store.ui.activities.orders.NewOrdersActivity;
import com.tudomart.store.ui.activities.orders.PackingOrdersActivity;
import com.tudomart.store.ui.activities.orders.ReadyDispatchListActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private LinearLayout newOrdersLayout;
    private LinearLayout packingLayout;
    private LinearLayout readyToDispatchLayout;
    private LinearLayout allOrdersLayout;
    private LinearLayout DispatchLayout;
    private TextView mNewOrdersCount;
    private TextView mPackingOrdersCount;
    private TextView mRtdOrdersCount;
    private TextView mDisOrdersCount;
    private TextView mAllOrdersCount;
    private TextView mTxtAvailableCount;
    private TextView mTxtReceivedCount;
    private TextView mTxtMaxCount;
    private LinearLayout mDashboardLayout;
    private RelativeLayout mLoadingLayout;
    private int NewOrder;

    private TextView mAllStoreCount;

    private LinearLayout storeOrderLayout;

    private SwipeRefreshLayout swipeRefreshLayout;

    boolean isSwipeRefresh = false;

    public DashboardFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews(view);
        initViewsClick();
        initSwipeRefresh();
        // addData();
        //overView();


       /* mNewOrdersCount.setStrokeWidth(1);
        mNewOrdersCount.setStrokeColor("#FF0000");
        mNewOrdersCount.setSolidColor("#FF0000");*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        addData();
        overView();

    }

    private void initSwipeRefresh() {

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.green),
                getResources().getColor(R.color.red));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isSwipeRefresh = true;
                //getActivity().finish();
                //startActivity( getActivity().getIntent());
                addData();
                overView();

            }
        });
    }

    private void overView() {

        mDashboardLayout.setVisibility(View.GONE);
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        JSONObject requestBody = new JSONObject();

        try {
            //     requestBody.put("strShopId", new UserSessionManager(getActivity()).getUserDetails().get("userid")/* new UserSessionManager(requireActivity()).getUserId()*/);
            //       requestBody.put("arrayStore", Utils.getStoreJson(new UserSessionManager(requireActivity()).getShopIdArray()/* new UserSessionManager(requireActivity()).getUserId()*/));
            requestBody.put("strShopId", new UserSessionManager(requireActivity()).getUserDetails().get("userid")/*/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("strStoreId", new UserSessionManager(requireActivity()).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.OVERVIEW_DASHBOARD_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                swipeRefreshLayout.setRefreshing(false);
                isSwipeRefresh = false;
                mLoadingLayout.setVisibility(View.GONE);
                mDashboardLayout.setVisibility(View.VISIBLE);
                try {
                    if (response.getBoolean("success")) {

                        // list.clear();
                        //mOrderItemsRecycler.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");


                        String MaxOrderCount = jsonArray.getJSONObject(0).optString("MaxOrderCount");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String TodayOrdersCount = jsonArray.getJSONObject(0).optString("intSubTotalOrders");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String AvailabilityCount = jsonArray.getJSONObject(0).optString("AvailabilityCount");


                        mTxtMaxCount.setText(MaxOrderCount);

                        mTxtAvailableCount.setText(AvailabilityCount);


                    } else {
                        mLoadingLayout.setVisibility(View.GONE);
                        //mOrderItemsRecycler.setVisibility(View.GONE);
                        if (response.getString("message") == " No Data Found") { // user has made no orders

                            // drawerLayout.setVisibility(View.VISIBLE);

                        } else {

                            MyDialogSheet dialogSheet = new MyDialogSheet(getActivity());
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.getString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    overView();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
//                            dialogSheet.show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mLoadingLayout.setVisibility(View.GONE);
                //mOrderItemsRecycler.setVisibility(View.GONE);
                Utils.checkVolleyError(requireActivity(), error);
                MyDialogSheet dialogSheet = new MyDialogSheet(getActivity());
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(getActivity()).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        overView();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white))
                        .setButtonsColorRes(R.color.colorAccent)
                        .setNegativeButton(getString(R.string.dialog_button_cancel),
                                null);
                dialogSheet.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", new UserSessionManager(getActivity()).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);

    }


    private void addData() {
       /* for (int i = 0; i < 1; i++) {
            data.add(new ModelOrderList(
                    "#M123ABCD098", "Card on Delivery", "100.15", "20", 0, "1Hr 30Min", "14 Apr 20, 10:10"
            ));
        }*/

        mDashboardLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        JSONObject requestBody = new JSONObject();

        try {

            requestBody.put("arrayStore", Utils.getStoreJson(new UserSessionManager(requireContext()).getShopIdArray()));
            //   requestBody.put("strShopId", new UserSessionManager(requireContext()).getUserDetails().get("userid"));//* new UserSessionManager(requireActivity()).getUserId()*//*);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.NUMBERS_DASHBOARD_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                swipeRefreshLayout.setRefreshing(false);
                isSwipeRefresh = false;
                mDashboardLayout.setVisibility(View.VISIBLE);


                mLoadingLayout.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success")) {

                        // list.clear();
                        //  mOrderItemsRecycler.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data");

                        String NewOrder = jsonArray.getJSONObject(0).getString("NewOrder");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String packCount = jsonArray.getJSONObject(1).getString("packCount");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String readyCount = jsonArray.getJSONObject(2).getString("readyCount");
                        String DispatchCount = jsonArray.getJSONObject(3).getString("DispatchCount");
                        //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                        String allOrders = jsonArray.getJSONObject(4).getString("allOrders");
                        String storeCount = jsonArray.getJSONObject(5).getString("storeAllOrders");

                        mNewOrdersCount.setText(NewOrder);
                        mPackingOrdersCount.setText(packCount);
                        mRtdOrdersCount.setText(readyCount);
                        mDisOrdersCount.setText(DispatchCount);
                        mAllOrdersCount.setText(allOrders);
                        mAllStoreCount.setText(storeCount);

                        //  mTxtReceivedCount.setText(NewOrder);

                        //        mAllOrdersCount.setText(allOrders);

                        //     mAllStoreCount.setText(allStore);

                        //  mTxtReceivedCount.setText(NewOrder);


                    } else {
                        mLoadingLayout.setVisibility(View.GONE);
                        //mOrderItemsRecycler.setVisibility(View.GONE);
                        if (response.getString("message") == " No Data Found") { // user has made no orders

                            // drawerLayout.setVisibility(View.VISIBLE);

                        } else {

                            MyDialogSheet dialogSheet = new MyDialogSheet(getActivity());
                            dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                                    .setMessage(response.getString("message"))
                                    .setPositiveButton(getString(R.string.dialog_button_retry),
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    addData();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
                            dialogSheet.show();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mLoadingLayout.setVisibility(View.GONE);
                //mOrderItemsRecycler.setVisibility(View.GONE);
                Utils.checkVolleyError(requireActivity(), error);
                MyDialogSheet dialogSheet = new MyDialogSheet(getActivity());
                dialogSheet.setTitle(getString(R.string.dialog_title_error_loading_data))
                        .setMessage(new VolleyErrorHandler(getActivity()).getVolleyError(error))
                        .setPositiveButton(getString(R.string.dialog_button_retry),
                                new DialogSheet.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        addData();
                                    }
                                })
                        .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white))
                        .setButtonsColorRes(R.color.colorAccent)
                        .setNegativeButton(getString(R.string.dialog_button_cancel),
                                null);
                dialogSheet.show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", new UserSessionManager(getActivity()).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(clearNotifRequest);


    }


    private void initViewsClick() {
        newOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewOrdersActivity.class)
                );
            }
        });
        packingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PackingOrdersActivity.class)
                );
            }
        });
        readyToDispatchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReadyDispatchListActivity.class)
                );
            }
        });
        allOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AllOrdersActivity.class));
            }
        });
        DispatchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), DispatchedListActivity.class));
            }
        });
        storeOrderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), PickupFromStore.class));
            }
        });
    }

    private void initViews(View view) {
        storeOrderLayout = view.findViewById(R.id.store_order_layout);

        swipeRefreshLayout = view.findViewById(R.id.srlDashSwipeRefresh);
        newOrdersLayout = view.findViewById(R.id.newOrdersLayout);
        packingLayout = view.findViewById(R.id.packingLayout);
        readyToDispatchLayout = view.findViewById(R.id.readyToDispatchLayout);
        allOrdersLayout = view.findViewById(R.id.allOrdersLayout);
        DispatchLayout = view.findViewById(R.id.DispatchLayout);
        mNewOrdersCount = view.findViewById(R.id.new_orders_count);
        mPackingOrdersCount = view.findViewById(R.id.packing_orders_count);
        mRtdOrdersCount = view.findViewById(R.id.rtd_orders_count);
        mDisOrdersCount = view.findViewById(R.id.dis_orders_count);
        mAllOrdersCount = view.findViewById(R.id.all_orders_count);
        mAllStoreCount = view.findViewById(R.id.all_store_count);

        mTxtAvailableCount = view.findViewById(R.id.txt_available_count);
        mTxtReceivedCount = view.findViewById(R.id.txt_received_count);
        mTxtMaxCount = view.findViewById(R.id.txt_max_count);
        mDashboardLayout = view.findViewById(R.id.dashboard_layout);
        mLoadingLayout = view.findViewById(R.id.loadingLayout);

    }
}
