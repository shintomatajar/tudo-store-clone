package ae.tudomart.store.helpers.network;

public class ApiUrl {


    //    public static final String SOCKET_BASE_URL = "http://157.175.82.202:5000/";  //test
//    public static final String SOCKET_BASE_URL = "http://15.184.223.59:5000/";  //test http://15.185.42.68:4000/api/
//    public static final String SOCKET_BASE_URL = "http://15.185.42.68:4000/"; // test  https://store.tudomart.com/http://15.185.42.68:8000
//        public static final String SOCKET_BASE_URL = "http://15.185.42.68:5000/"; // test
    public static final String SOCKET_BASE_URL = "https://store.matajar.ae/";
    //   public static final String SOCKET_BASE_URL = "https://store.tudomart.com/";

    private static final String NOTIFICATION_PUSH_V2 = SOCKET_BASE_URL + "v2/api/";

    private static final String BASE_URL = SOCKET_BASE_URL + "api/";
    public static String NEW_ORDERS_LISTING_URL = BASE_URL + "orders/NewOrdersListing";

    public static final String CUSTOMER_PUSH_CALL = NOTIFICATION_PUSH_V2 + "order/nottification";

    // public static String NEW_ORDERS_DETAILS_URL = BASE_URL + "orders/GetOrderDetails";
    public static String MY_ORDERS_ACCEPT_URL = BASE_URL + "StatusChange/change_coordinator_accepted";
    public static String NUMBERS_DASHBOARD_URL = BASE_URL + "orders/diffStatusOrderCount";
    public static String OVERVIEW_DASHBOARD_URL = BASE_URL + "orders/OrderCount";

    public static String PACKED_ORDERS_DETAILS_URL = BASE_URL + "orders/PackedOrderListing";
    public static String READY_DISPATCH_URL = BASE_URL + "orders/DispatchReadyOrders";
    public static String DISPATCHED_ORDERS_URL = BASE_URL + "orders/DispatchedOrders";
    public static String ALL_ORDERS_URL = BASE_URL + "orders/GetAllOrders";
    public static String MY_ORDERS_DISPATCH_URL = BASE_URL + "StatusChange/ready_to_dispatch";
    public static String STORE_LOGIN_URL = BASE_URL + "orders/StaffLogin";
    public static String STORE_PROFILE_URL = BASE_URL + "orders/StaffProfile";
    public static String SAVE_INVOICE_URL = BASE_URL + "orders/SaveInvoiceNumber";

    public static String PICKUP_FROM_STORE = BASE_URL + "orders/GetAllStoreOrders";

    public static String PICKUP_STORE_STATUS = BASE_URL + "StatusChange/store_order_deliver";
    public static String ALL_ORDERS_DETAILS_URL = BASE_URL + "orders/OrderDetails";
    public static String EDIT_ORDER_AMOUNT = BASE_URL + "orders/updateOrderData";
    public static String UPDATE_CHECK_PRODUCT = BASE_URL + "product/UpdateCheckProduct";
    public static String GET_SLOTS_WITH_DATE = BASE_URL + "orders/get_slots";
    public static String SEARCH_PRODUCT_URL = SOCKET_BASE_URL + "v2/api/search/search_results_products";
    public static String SUBSTITUTE_PRODUCTS_URL = SOCKET_BASE_URL + "v2/api/search/order_substitute";
    public static String SUBSTITUTED_PRODUCTS_LIST_URL = SOCKET_BASE_URL + "v2/api/search/listed_Products";
    public static String CLEAR_SUBSTITUTION = SOCKET_BASE_URL + "v2/api/search/remove_outOfStock";
    public static String DELETE_SUBSTITUTE_PRODUCT = NOTIFICATION_PUSH_V2 + "order/deleteoutOfstockproduct";

}