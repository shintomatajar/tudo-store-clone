package com.tudomart.store.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marcoscg.dialogsheet.DialogSheet;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;

import com.tudomart.store.adapters.orders.orderList.AdapterPaginatedSpittedListing;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import com.tudomart.store.model.order.ModelOrderList;
import com.tudomart.store.ui.activities.DispatchedActivity;
import com.tudomart.store.ui.activities.OrderAcceptActivity;
import com.tudomart.store.ui.activities.PackingActivity;
import com.tudomart.store.ui.activities.ReadyToDispatchActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PackingFragment extends Fragment {
    private RecyclerView packingOrdersRecycler;
    private ArrayList<ModelOrderList> data = new ArrayList<>();

    private InterFragmentCommunicator callback;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;
    int status = 0;
    long hours = 0, minutes = 0, days = 0;
    String orderId = "";
    int itemcount = 0;

    private int SKIP_COUNT = 0;
    private int PAGE_LIMIT = 10;

    private SwipeRefreshLayout swipeRefreshLayout;

    boolean isSwipeRefresh = false;

    private AdapterPaginatedSpittedListing adapter;
    // private boolean isSwipeRefresh = false;
    private boolean isLoading = false;
    private boolean isFullyLoaded = false;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (InterFragmentCommunicator) context;

    }

    public PackingFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_packing, container, false);
        initViews(view);
        initSwipeRefresh();
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.clearData();

            data.clear();
            SKIP_COUNT = 0;
            addData();

        }

    }

    private void loadNextPage() {
        SKIP_COUNT += PAGE_LIMIT;
        addData();
    }

    private void initRecyclerView() {


        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) packingOrdersRecycler.getLayoutManager();
        packingOrdersRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = packingOrdersRecycler.getLayoutManager().getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isFullyLoaded) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            isLoading = true;
                            loadNextPage();
                        }
                    }
                }
            }
        });
        adapter = new AdapterPaginatedSpittedListing(getActivity(), new AdapterPaginatedSpittedListing.AdapterCallback() {
            @Override
            public void onClickItem(String orderId, int orderStatus, String timeStamp) {
                if (orderStatus == 0) {
                    startActivity(new Intent(requireActivity(), OrderAcceptActivity.class).putExtra("ORDER_ID", orderId));
                } else if (orderStatus == 1) {
                    startActivity(new Intent(requireActivity(), PackingActivity.class).putExtra("ORDER_ID", orderId));
                } else if (orderStatus == 2) {
                    startActivity(new Intent(requireActivity(), ReadyToDispatchActivity.class).putExtra("ORDER_ID", orderId));
                } else if (orderStatus == 3) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", orderId)
                            .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
                } else if (orderStatus == 4) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", orderId)
                            .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
                } else if (orderStatus == 5) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", orderId)
                            .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
                } else if (orderStatus == 6) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", orderId)
                            .putExtra("DATE_KEY", timeStamp).putExtra("STATUS", orderStatus));
                }
            }
        });
        packingOrdersRecycler.setAdapter(adapter);

    }

    private void addData() {
        mErrorLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        packingOrdersRecycler.setVisibility(View.VISIBLE);

        if (SKIP_COUNT == 0) {
            //Loading data for 1st time
            mLoadingLayout.setVisibility(View.VISIBLE);
            isSwipeRefresh = true;
            swipeRefreshLayout.setRefreshing(true);

        } else {
            //Loading paged item
            isSwipeRefresh = true;
            swipeRefreshLayout.setRefreshing(true);

        }
        RequestQueue rQueue = Volley.newRequestQueue(requireActivity());
        JSONObject requestBody = new JSONObject();
        JSONArray jsonArray = Utils.getStoreJson(new UserSessionManager(getActivity()).getShopIdArray());

        try {
            requestBody.put("strPageLimit", PAGE_LIMIT);
            requestBody.put("strSkipCount", SKIP_COUNT);
            requestBody.put("strLoginUserId", new UserSessionManager(getActivity()).getUserDetails().get("userid")/*new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("arr_shops", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.PACKED_ORDERS_DETAILS_URL, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                isLoading = false;
                if (SKIP_COUNT == 0) {
                    //Loading data for 1st time
                    mLoadingLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    isSwipeRefresh = false;

                } else {
                    //Loading paged item
                    swipeRefreshLayout.setRefreshing(false);
                    isSwipeRefresh = false;
                    //mSrlDashSwipeRefresh.setRefreshing(false);

                }
                try {
                    if (response.getBoolean("success")) {

                        data.clear();
                        packingOrdersRecycler.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                        itemcount = response.getJSONArray("data").getJSONObject(1).getInt("totalOrdersCount");

                        //Log.d("Response",response.toString());

                        if (jsonArray == null || jsonArray.length() == 0) {
                            isFullyLoaded = true;
                        } else {


                            for (int i = 0; i < jsonArray.length(); i++) {

                                orderId = jsonArray.getJSONObject(i).optString("strOrderID");
                                String paymentType = jsonArray.getJSONObject(i).optString("strPaymentMode");
                                String amount = jsonArray.getJSONObject(i).optString("intGrandTotal");
                                String itemsCount = jsonArray.getJSONObject(i).optString("intTotalItemQuantity");
                                String order_status = jsonArray.getJSONObject(i).optString("strStoreStatus");
                                if (order_status.equalsIgnoreCase("PACKING")) {
                                    status = 1;
                                }
                                status = 1;
                                String timeStamp = getNeDate(jsonArray.getJSONObject(i).optString("dateCreateDateAndTime"));
                                //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                                String duration = getFormattedDate(jsonArray.getJSONObject(i).optString("dateCreateDateAndTime"));


                                String timeSlot = "", deliveryType = "";
                                if (jsonArray.getJSONObject(i).has("arrayTimeSlot") && jsonArray.getJSONObject(i).optDouble("dateSlot") > 0) {
                                    timeSlot = getSlotDate(jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("dateSlot")) + " " + jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDisplayName");
                                    if (jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).has("strDeliveryType")) {
                                        deliveryType = jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDeliveryType");
                                    }
                                } else {
                                    timeSlot = "";
                                    deliveryType = "";
                                }

                                String strEmirate = jsonArray.getJSONObject(i).getJSONArray("arrayAddress").getJSONObject(0).optString("strEmirate");
                                String strPlace = jsonArray.getJSONObject(i).getJSONArray("arrayAddress").getJSONObject(0).optString("strLocation");


                                data.add(new ModelOrderList(orderId, paymentType, amount, itemsCount, status, duration, timeStamp, timeSlot, deliveryType, strPlace, strEmirate));
                                //TODO : Add strPlace
                            }
                        }

                        adapter.updateData(data);
                        callback.updateToolbar(itemcount);


                    } else {

                        if (SKIP_COUNT == 0) {
                            //Loading data for 1st time
                            mLoadingLayout.setVisibility(View.GONE);
                            mTxtErrorMessage.setText(response.optString("message"));
                            mErrorLayout.setVisibility(View.VISIBLE);
                            mBtnRetry.setVisibility(View.VISIBLE);
                            mBtnRetry.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (adapter != null) {
                                        adapter.clearData();
                                        data.clear();
                                        SKIP_COUNT = 0;
                                        addData();
                                    }
                                }
                            });

                        } else {
                            //Loading paged item
                            /*final MyDialogSheet dialogSheet = new MyDialogSheet(activity);
                            dialogSheet.setTitle("Error!")
                                    .setMessage(response.optString("message"))
                                    .setPositiveButton("Ok",
                                            new DialogSheet.OnPositiveClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //addData();
                                                    dialogSheet.dismiss();
                                                }
                                            })
                                    .setBackgroundColor(ContextCompat.getColor(activity, R.color.white))
                                    .setButtonsColorRes(R.color.colorAccent)
                                    .setNegativeButton(getString(R.string.dialog_button_cancel),
                                            null);
                            dialogSheet.show();*/
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                isLoading = false;
                Log.d("ERROR", error.toString());
                Utils.checkVolleyError(requireActivity(), error);
                if (SKIP_COUNT == 0) {
                    //Loading data for 1st time
                    mLoadingLayout.setVisibility(View.GONE);
                    mTxtErrorMessage.setText(new VolleyErrorHandler(requireActivity()).getVolleyError(error));
                    mErrorLayout.setVisibility(View.VISIBLE);
                    mBtnRetry.setVisibility(View.VISIBLE);
                    mBtnRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            addData();
                        }
                    });

                } else {
                    //Loading paged item
                    MyDialogSheet dialogSheet = new MyDialogSheet(getActivity());
                    dialogSheet.setTitle("Error!")
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


    public String getFormattedDate(String notificationDate) {
        if (notificationDate.isEmpty()) {
            return "";
        }
        long notifDate = 0;

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));  //Timezone is IST!!!


            String splitDate[] = notificationDate.split("T");
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0, 8));

            Calendar notifRecTime = Calendar.getInstance();
            notifRecTime.setTimeInMillis(notifDate);

            Calendar now = Calendar.getInstance();
            Date currentTime = Calendar.getInstance().getTime();
            Date date1 = date;// sdf.parse(String.valueOf(date))
            Date date2 = sdf.parse("2020-05-26 20:35:55");

            long different = currentTime.getTime() - date1.getTime();

            System.out.println("startDate : " + date1);
            System.out.println("endDate : " + currentTime);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

            hours = elapsedHours;
            minutes = elapsedMinutes;
            days = elapsedDays;

            if (!(date == null)) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


//1 minute = 60 seconds
//1 hour = 60 x 60 = 3600
//1 day = 3600 x 24 = 86400

        if (days > 0) {
            return days + "Day " + hours + "Hr " + minutes + "Min";
        } else {
            return hours + "Hr " + minutes + "Min";
        }


    }


    public String getNeDate(String notificationDate) {
        if (notificationDate.isEmpty()) {
            return "";
        }
        long notifDate = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));  //Timezone is IST!!!

            String splitDate[] = notificationDate.split("T");
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0, 8));
            if (!(date == null)) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar notifRecTime = Calendar.getInstance();
        notifRecTime.setTimeInMillis(notifDate);

        Calendar now = Calendar.getInstance();

        final String dateTimeFormatString = "EEEE, MMMM d";
        final String timeFormatString = "h:mm aa";


        return DateFormat.format("dd MMM yy,", notifDate).toString()
                + " "
                + DateFormat.format(timeFormatString, notifDate);

    }

    public String getSlotDate(String notificationDate) {
        if (notificationDate.isEmpty()) {
            return "";
        }
        long notifDate = 0;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+04:00"));  //Timezone is IST!!!

            String splitDate[] = notificationDate.split("T");
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0, 8));
            if (!(date == null)) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar notifRecTime = Calendar.getInstance();
        notifRecTime.setTimeInMillis(notifDate);

        Calendar now = Calendar.getInstance();

        final String dateTimeFormatString = "EEEE, MMMM d";
        final String timeFormatString = "h:mm aa";


        return DateFormat.format("EEEE,dd MMM yyyy,", notifDate).toString();
                /*+ " "
                + DateFormat.format(timeFormatString, notifDate)*/

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
//                addData();
                /* initRecycler();*/
                if (adapter != null) {
                    adapter.clearData();
                    data.clear();
                    SKIP_COUNT = 0;
                    addData();
                }

            }
        });
    }

    private void initViews(View view) {

        packingOrdersRecycler = view.findViewById(R.id.packingOrdersRecycler);
        swipeRefreshLayout = view.findViewById(R.id.srlDashSwipeRefresh);
        mImgError = view.findViewById(R.id.img_error);
        mTxtErrorTitle = view.findViewById(R.id.txt_error_title);
        mTxtErrorMessage = view.findViewById(R.id.txt_error_message);
        mBtnRetry = view.findViewById(R.id.btn_retry);
        mErrorLayout = view.findViewById(R.id.error_layout);
        mErrorParentLayout = view.findViewById(R.id.error_parent_layout);
        mLoadingProgress = view.findViewById(R.id.loadingProgress);
        mLoadingLayout = view.findViewById(R.id.loadingLayout);

    }
}
