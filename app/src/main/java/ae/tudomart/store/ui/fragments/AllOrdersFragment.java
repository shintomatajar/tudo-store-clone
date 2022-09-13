package ae.tudomart.store.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
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
import ae.matajar.store.R;

import ae.tudomart.store.adapters.orders.orderList.AdapterPaginatedListing;
import ae.tudomart.store.helpers.network.ApiUrl;
import ae.tudomart.store.helpers.network.RequestController;
import ae.tudomart.store.helpers.network.VolleyErrorHandler;
import ae.tudomart.store.helpers.sharedPref.UserSessionManager;
import ae.tudomart.store.interfaces.dashboard.InterFragmentCommunicator;
import ae.tudomart.store.model.order.ModelOrderList;
import ae.tudomart.store.ui.activities.DispatchedActivity;
import ae.tudomart.store.ui.activities.OrderAcceptActivity;
import ae.tudomart.store.ui.activities.PackingActivity;
import ae.tudomart.store.ui.activities.ReadyToDispatchActivity;
import ae.tudomart.store.ui.customViews.MyDialogSheet;
import ae.tudomart.store.utils.Utils;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class AllOrdersFragment extends Fragment {

    private RecyclerView mRecyclerOrders;
    private Button mBtnRetry;
    private TextView mTextDate;
    private ImageView mImgPreviousDate;
    private ImageView mImgNextDate;
    private RelativeLayout mDate;
    private LinearLayout mLayoutNoItems;
    private TextView mTextPending;
    private TextView mTextPacking;
    private TextView mTextReady;
    private TextView mTextDispatched;
    private TextView orders_count_all;
    private TextView txtDate;
    int status = 0;
    long hours = 0, minutes = 0, days = 0;
    private int year;
    private int month;
    private int day;
    StringBuilder date;

    private int year1;
    private int month1;
    private int day1;

    int itemcount = 0;
    int initial_skip_count = 0;
    int page_limit = 10;

    private int allOrders;


    private InterFragmentCommunicator callback;
    private RelativeLayout mErrorLayout;
    private LinearLayout mErrorParentLayout;
    private RelativeLayout mLoadingLayout;
    private TextView mOrdersCountAll;
    private TextView mTxtErrorMessage;
    private NestedScrollView mNestedScroll;

    private ProgressBar mLoadmoreProgress;

    private int SKIP_COUNT = 0;
    private int PAGE_LIMIT = 10;

    private SwipeRefreshLayout swipeRefreshLayout;

    boolean isSwipeRefresh = false;

    private AdapterPaginatedListing adapter;
    // private boolean isSwipeRefresh = false;
    private boolean isLoading = false;
    private boolean isFullyLoaded = false;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    String changedDate = "";


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (InterFragmentCommunicator) context;
    }

    public AllOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_orders, container, false);
        initViews(view);
        //StringBuilder date = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        date = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day);
        // addData(date.toString());

        //ViewCompat.setNestedScrollingEnabled(mRecyclerOrders, false);

        initRecyclerView();
        initSwipeRefresh();
        initLayoutClicks();

        if (month < 10 && day < 10) {
            //month = "0" + monthOfYear;
            txtDate.setText(new StringBuilder().append(0).append(day).append("-").append(0).append(month + 1).append("-").append(year));

        } else if (day < 10) {
            //  day = "0" + day;
            txtDate.setText(new StringBuilder().append(0).append(day).append("-").append(month + 1).append("-").append(year));
        } else if (month < 10) {
            //  day = "0" + day;
            txtDate.setText(new StringBuilder().append(day).append("-").append(0).append(month + 1).append("-").append(year));
        } else {
            txtDate.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year));

        }


        return view;
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

                    SKIP_COUNT = 0;
                    addData(date.toString());
                }

            }
        });
    }

    private void loadNextPage() {
        SKIP_COUNT += PAGE_LIMIT;
        addData(date.toString());
    }

    private void initRecyclerView() {
        final LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerOrders.getLayoutManager();
        mRecyclerOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mRecyclerOrders.getLayoutManager().getChildCount();
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
        adapter = new AdapterPaginatedListing(getActivity(), new AdapterPaginatedListing.AdapterCallback() {
            @Override
            public void onClickItem(ModelOrderList modelOrderList) {
                callback.onClickOrderItem(modelOrderList.getOrderId(), AllOrdersFragment.this.status, modelOrderList.getTimeStamp());
                if (modelOrderList.getStatus() == 0) {
                    startActivity(new Intent(requireActivity(), OrderAcceptActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId()));
                } else if (modelOrderList.getStatus() == 1) {
                    startActivity(new Intent(requireActivity(), PackingActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId()));
                } else if (modelOrderList.getStatus() == 2) {
                    startActivity(new Intent(requireActivity(), ReadyToDispatchActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId()));
                } else if (modelOrderList.getStatus() == 3) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId())
                            .putExtra("DATE_KEY", modelOrderList.getTimeStamp()).putExtra("STATUS", modelOrderList.getStatus()));
                } else if (modelOrderList.getStatus() == 4) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId())
                            .putExtra("DATE_KEY", modelOrderList.getTimeStamp()).putExtra("STATUS", modelOrderList.getStatus()));
                } else if (modelOrderList.getStatus() == 5) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId())
                            .putExtra("DATE_KEY", modelOrderList.getTimeStamp()).putExtra("STATUS", modelOrderList.getStatus()));
                } else if (modelOrderList.getStatus() == 6) {
                    startActivity(new Intent(requireActivity(), DispatchedActivity.class).putExtra("ORDER_ID", modelOrderList.getOrderId())
                            .putExtra("DATE_KEY", modelOrderList.getTimeStamp()).putExtra("STATUS", modelOrderList.getStatus()));
                }

            }
        });
        mRecyclerOrders.setAdapter(adapter);

    }

    private void initLayoutClicks() {
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                itemcount = 0;

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                monthOfYear++;
                                String day = "" + dayOfMonth;
                                String month = "" + monthOfYear;

                                /*  date = null;*/
                                date = new StringBuilder().append(year + "-" + month + "-" + day);


                                if (adapter != null) {
                                    adapter.clearData();

                                    SKIP_COUNT = 0;
                                    addData(year + "-" + month + "-" + day);

                                }


                                if (monthOfYear < 10) {
                                    month = "0" + monthOfYear;
                                }
                                if (dayOfMonth < 10) {
                                    day = "0" + day;
                                }

                                txtDate.setText(day + "-" + month + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();


            }
        });
    }


    private void addData(final String s) {
        mLayoutNoItems.setVisibility(View.GONE);
        mErrorLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mRecyclerOrders.setVisibility(View.VISIBLE);


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
//          requestBody.put("arr_shops", new UserSessionManager(getActivity()).getUserDetails().get("shop")/* new UserSessionManager(requireActivity()).getUserId()*/);
            requestBody.put("arr_shops", jsonArray);
            requestBody.put("strDate", s /*"2020-4-25"*//*year+"-"+"-"+month+"-"+day*/);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest clearNotifRequest = new JsonObjectRequest(Request.Method.POST, ApiUrl.ALL_ORDERS_URL, requestBody, new Response.Listener<JSONObject>() {
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

                mRecyclerOrders.setVisibility(View.VISIBLE);

                try {
                    if (response.getBoolean("success")) {


                        mRecyclerOrders.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("data").getJSONArray(0);
                        itemcount = response.getJSONArray("data").getJSONObject(1).getInt("totalOrderCount");

                        ArrayList<ModelOrderList> data = new ArrayList<>();
                        if (jsonArray == null || jsonArray.length() == 0) {
                            isFullyLoaded = true;
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                String orderId = jsonArray.getJSONObject(i).optString("strOrderID");

                                String paymentType = jsonArray.getJSONObject(i).optString("strPaymentMode");
                                if (paymentType.equals("Cash On Delivery") && paymentType.equals("Card on Delivery"))
                                    mRecyclerOrders.setVisibility(View.GONE);

                                String amount = jsonArray.getJSONObject(i).optString("intGrandTotal");
                                String itemsCount = jsonArray.getJSONObject(i).optString("intTotalItemQuantity");
                                String order_status = jsonArray.getJSONObject(i).optString("strStoreStatus");
                                if (order_status.equalsIgnoreCase("PACKING")) {
                                    status = 1;
                                } else if (order_status.equalsIgnoreCase("READY TO DISPATCH")) {
                                    status = 2;
                                } else if (order_status.equalsIgnoreCase("ACCEPT")) {
                                    status = 0;
                                } else if (order_status.equalsIgnoreCase("DISPATCHED")) {
                                    status = 3;
                                } else if (order_status.equalsIgnoreCase("DELIVERED")) {
                                    status = 4;
                                } else if (order_status.equalsIgnoreCase("RETURNED")) {
                                    status = 5;
                                } else if (order_status.equalsIgnoreCase("CANCELLED")) {
                                    status = 6;
                                }

                                String timeStamp = getNeDate(jsonArray.getJSONObject(i).optString("dateCreateDateAndTime"));
                                //SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, hh:mm ");

                                String duration = getFormattedDate(jsonArray.getJSONObject(i).optString("dateCreateDateAndTime"));


                                String timeSlot = "", deliveryType = "";
                                if (jsonArray.getJSONObject(i).has("arrayTimeSlot")) {
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

                            mOrdersCountAll.setText(String.valueOf(itemcount) + " Orders");
                        }

                        //Log.d("Response", response.toString());

                        adapter.updateData(data);
                        callback.updateToolbar(itemcount);


                    } else {

                        if (response.has("count")) {
                            mOrdersCountAll.setText(response.optString("count") + " Orders");
                        }


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

                                        SKIP_COUNT = 0;
                                        addData(s);
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

                mOrdersCountAll.setText(String.valueOf(0) + " Orders");
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
                            addData(s);
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
                                            addData(s);
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

          /*  System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/

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

    private void initViews(View view) {

        mBtnRetry = view.findViewById(R.id.btn_retry);
        swipeRefreshLayout = view.findViewById(R.id.srlDashSwipeRefresh);
        mRecyclerOrders = view.findViewById(R.id.recyclerOrders);
        mLayoutNoItems = view.findViewById(R.id.layout_no_items);
        /*mTextPending = view.findViewById(R.id.text_pending);
        mTextPacking = view.findViewById(R.id.text_packing);
        mTextReady = view.findViewById(R.id.text_ready);
        mTextDispatched = view.findViewById(R.id.text_dispatched);*/
        txtDate = view.findViewById(R.id.txtDate);

        mErrorLayout = view.findViewById(R.id.error_layout);
        mErrorParentLayout = view.findViewById(R.id.error_parent_layout);
        mLoadingLayout = view.findViewById(R.id.loadingLayout);
        mOrdersCountAll = view.findViewById(R.id.orders_count_all);
        mTxtErrorMessage = view.findViewById(R.id.txt_error_message);

        mLoadmoreProgress = view.findViewById(R.id.loadmore_progress);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        // set current date into textview


        // set current date into datepicker

    }


    @Override
    public void onResume() {
        super.onResume();
        /*final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        date = new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day);*/

        if (adapter != null) {
            adapter.clearData();


            SKIP_COUNT = 0;
            addData(date.toString());

        }


        /* if (month < 10 && day < 10) {
            //month = "0" + monthOfYear;
            txtDate.setText(new StringBuilder().append(0).append(day).append("-").append(0).append(month + 1).append("-").append(year));

        } else if (day < 10) {
            //  day = "0" + day;
            txtDate.setText(new StringBuilder().append(0).append(day).append("-").append(month + 1).append("-").append(year));
        } else if (month < 10) {
            //  day = "0" + day;
            txtDate.setText(new StringBuilder().append(day).append("-").append(0).append(month + 1).append("-").append(year));
        } else {
            txtDate.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year));

        }*/

        initLayoutClicks();

        //initRecycler();

    }

}
