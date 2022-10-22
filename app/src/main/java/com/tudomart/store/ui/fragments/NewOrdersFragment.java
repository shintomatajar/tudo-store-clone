package com.tudomart.store.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.tudomart.store.model.ModelTimeSlotResponse;
import com.tudomart.store.utils.Utils;

import com.tudomart.store.R;
import com.tudomart.store.databinding.FragmentNewOrdersFragmentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.tudomart.store.adapters.orders.orderList.AdapterPaginatedSpittedListing;
import com.tudomart.store.helpers.network.ApiUrl;
import com.tudomart.store.helpers.network.RequestController;
import com.tudomart.store.helpers.network.VolleyErrorHandler;
import com.tudomart.store.helpers.sharedPref.UserSessionManager;
import com.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import com.tudomart.store.model.ModelTimeSlotResponse;
import com.tudomart.store.model.order.ModelOrderList;
import com.tudomart.store.ui.activities.DispatchedActivity;
import com.tudomart.store.ui.activities.OrderAcceptActivity;
import com.tudomart.store.ui.activities.PackingActivity;
import com.tudomart.store.ui.activities.ReadyToDispatchActivity;
import com.tudomart.store.ui.customViews.MyDialogSheet;
import com.tudomart.store.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrdersFragment extends Fragment {


    private InterFragmentCommunicator callback;
    private RecyclerView mNewOrdersRecycler;
    private ProgressBar mLoadingProgress;
    private RelativeLayout mLoadingLayout;
    private ImageView mImgError;
    private TextView mTxtErrorTitle;
    private TextView mTxtErrorMessage;
    private Button mBtnRetry;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    int status = 0;
    long hours = 0, minutes = 0, days = 0;
    long diff = 0;
    private ArrayList<ModelOrderList> data = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;
    private ModelTimeSlotResponse.Data selectedSlot;
    boolean isSwipeRefresh = false;
    public int itemcount = 0;

    private int SKIP_COUNT = 0;
    private int PAGE_LIMIT = 10;

    private AdapterPaginatedSpittedListing adapter;
    // private boolean isSwipeRefresh = false;
    private boolean isLoading = false;
    private boolean isFullyLoaded = false;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private FragmentNewOrdersFragmentBinding binding;
    SlotSelectionFragment dialog;
    String filterDate = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewOrdersFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initViews(view);
        //  addData();
        initSwipeRefresh();
        initRecyclerView();
        initFilter();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (InterFragmentCommunicator) context;
    }

    public NewOrdersFragment() {
        // Required empty public constructor

    }

    private void initFilter() {
        dialog = new SlotSelectionFragment(new SlotSelectionFragment.SlotSelectionCallback() {
            @Override
            public void onSlotSelected(String slotID) {
                filterDate = slotID;
                if (adapter != null) {
                    adapter.clearData();

                    data.clear();
                    SKIP_COUNT = 0;
                    addData();
                }
            }

            @Override
            public void onSlotFetched(List<ModelTimeSlotResponse.Data> slots) {
                dialog.dismiss();
                showSlotsDialog(slots);
            }
        });
        binding.layFilterOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(requireActivity().getSupportFragmentManager(), "TAG");
            }
        });
    }

    private void showSlotsDialog(List<ModelTimeSlotResponse.Data> slots) {
        AdapterMultiDaysSlots adapter = new AdapterMultiDaysSlots(slots, selectedSlot, data -> {
            if (selectedSlot == data) {
                selectedSlot = null;
                return null;
            }
            selectedSlot = data;
            return null;
        });
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_slots_layout);

        ListView rvSlots = (ListView) dialog.findViewById(R.id.recyclerView);
        rvSlots.setAdapter(adapter);

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button proceed = dialog.findViewById(R.id.btn_proceed);
        proceed.setOnClickListener(view -> {
            if (selectedSlot != null) {
                dialog.dismiss();
                addData();
            }
        });
        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();

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


    private void loadNextPage() {
        SKIP_COUNT += PAGE_LIMIT;
        addData();
    }

    private void initRecyclerView() {

        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) mNewOrdersRecycler.getLayoutManager();
        mNewOrdersRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mNewOrdersRecycler.getLayoutManager().getChildCount();
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
        mNewOrdersRecycler.setAdapter(adapter);

    }


    private void addData() {

        mErrorLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mNewOrdersRecycler.setVisibility(View.VISIBLE);

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
//            requestBody.put("strShopId",new UserSessionManager(getActivity()).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("arr_shops", jsonArray);
            requestBody.put("strDate", filterDate);

            if (selectedSlot != null) {
                requestBody.put("strTimeSlotId", selectedSlot.getPkTimeSlotId());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.NEW_ORDERS_LISTING_URL, requestBody, new Response.Listener<JSONObject>() {
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

                mNewOrdersRecycler.setVisibility(View.VISIBLE);
                try {
                    if (response.getBoolean("success")) {
                        //Log.d("NewOrders",response.toString());
                        data.clear();
                        mNewOrdersRecycler.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                        itemcount = response.getJSONArray("data").getJSONObject(1).getInt("totalOrderCount");

                        if (jsonArray == null || jsonArray.length() == 0) {
                            isFullyLoaded = true;
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                String orderId = jsonArray.getJSONObject(i).getString("strOrderID");
                                String paymentType = jsonArray.getJSONObject(i).getString("strPaymentMode");
                                String amount = jsonArray.getJSONObject(i).getString("intGrandTotal");
                                String itemsCount = jsonArray.getJSONObject(i).getString("intTotalItemQuantity");
                                String order_status = jsonArray.getJSONObject(i).getString("strStoreStatus");
                                if (order_status.equalsIgnoreCase("ACCEPT")) {
                                    status = 0;
                                }
                                String timeStamp = getNewDate(jsonArray.getJSONObject(i).optString("dateCreateDateAndTime"));
                                //    String timeStamp = jsonArray.getJSONObject(i).optString("dateCreateDateAndTime");
                                //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                                String duration = getDuration(jsonArray.getJSONObject(i).getString("dateCreateDateAndTime"));
                                //        String duration = jsonArray.getJSONObject(i).getString("dateCreateDateAndTime");


                                String timeSlot = "", deliveryType = "";
                                if (jsonArray.getJSONObject(i).has("arrayTimeSlot")) {
                                    timeSlot = getSlotDate(jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("dateSlot")) + " " + jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDisplayName");
                                    if (jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).has("strDeliveryType")) {
                                        deliveryType = jsonArray.getJSONObject(i).getJSONArray("arrayTimeSlot").getJSONObject(0).optString("strDeliveryType");
                                    }
                                }


                                else{
                                timeSlot = "";
                                deliveryType = "";
                            }
                            String strPlace = jsonArray.getJSONObject(i).getJSONArray("arrayAddress").getJSONObject(0).optString("strLocation");

                            data.add(new ModelOrderList(orderId, paymentType, amount, itemsCount, status, duration, timeStamp, timeSlot, deliveryType, strPlace, ""));
                            //TODO : Add strPlace
                        }
                    }

                    adapter.updateData(data);
                    callback.updateToolbar(itemcount);


                }  else{


                    if (SKIP_COUNT == 0) {
                        //Loading data for 1st time
                        mLoadingLayout.setVisibility(View.GONE);
                        mTxtErrorMessage.setText(response.getString("message"));
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
                                    .setMessage(response.getString("message"))
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
            } catch(
            JSONException e)

            {
                e.printStackTrace();


            }
        }
    },new Response.ErrorListener()

    {
        @Override
        public void onErrorResponse (VolleyError error){

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
    })

    {
        @Override
        public Map<String, String> getHeaders () {
        Map<String, String> params = new HashMap<>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", new UserSessionManager(getActivity()).getUserDetails().get("token") /*new UserSessionManager(requireActivity()).getToken()*/);
        return params;
    }
    }

    ;

       RequestController.getInstance().

    addToRequestQueue(clearNotifRequest);


}


    public String getDuration(String notificationDate) {

        /* DateTimeUtils obj = new DateTimeUtils();*/
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


    public String getNewDate(String notificationDate) {
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
            Log.d("Date : ", "" + notificationDate);
            Date date = sdf.parse(splitDate[0] + " " + splitDate[1].substring(0, 8));
            if (!(date == null)) {
                notifDate = date.getTime();
            }

        } catch (ParseException e) {
            Log.d("Date : ", "" + notificationDate);
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

    private void initViews(View view) {

        mNewOrdersRecycler = view.findViewById(R.id.newOrdersRecycler);
        mLoadingProgress = view.findViewById(R.id.loadingProgress);
        mLoadingLayout = view.findViewById(R.id.loadingLayout);
        mImgError = view.findViewById(R.id.img_error);
        mTxtErrorTitle = view.findViewById(R.id.txt_error_title);
        mTxtErrorMessage = view.findViewById(R.id.txt_error_message);
        mBtnRetry = view.findViewById(R.id.btn_retry);
        mErrorLayout = view.findViewById(R.id.error_layout);
        mErrorParentLayout = view.findViewById(R.id.error_parent_layout);
        swipeRefreshLayout = view.findViewById(R.id.srlDashSwipeRefresh);

    }
}
