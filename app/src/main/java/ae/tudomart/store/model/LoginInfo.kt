package ae.tudomart.store.model


import com.google.gson.annotations.SerializedName

data class LoginInfo(
    @SerializedName("pkUserId")
    var pkUserId: String?, // 5e99b39fac422b426f2fb375
    @SerializedName("strEmail")
    var strEmail: String?, // hadhi.aeth@gmail.com
    @SerializedName("strPhone")
    var strPhone: String?, // 7034674705
    @SerializedName("strUserName")
    var strUserName: String?, // Hadhi
    @SerializedName("token")
    var token: String? // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InN0ckVtYWlsIjoiaGFkaGkuYWV0aEBnbWFpbC5jb20iLCJpbnRVc2VySWQiOiI1ZTk5YjM5ZmFjNDIyYjQyNmYyZmIzNzUifSwiaWF0IjoxNTg3NDYwMzAyfQ.Wp1vFEFdWJqvJ2DdzX9LC9WDoxKxPreEIzpPanJGNOw
)