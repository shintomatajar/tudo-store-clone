package ae.tudomart.store.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ae.matajar.store.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


object Utility {

    private var loaderDialog: AlertDialog? = null
    private val handler = Handler()


    fun getContext(): Context {
        return MatajarApp.get().applicationContext
    }


    fun isNetworkAvailable(): Boolean {
        val cm = getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                val ni = cm.activeNetworkInfo
                if (ni != null) {
                    return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE || ni.type == ConnectivityManager.TYPE_BLUETOOTH)
                }
            } else {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    if (nc != null) {
                        return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                nc.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                    }
                }
            }
        }
        return false
    }

    fun showLoaderDialog(context: Context?, message: String = "Please Wait..") {
        if (context == null) {
            return
        }
        if (context is AppCompatActivity) {
            hideKeyboard(context)
        }
        val dialogBuilder = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.loader_dialog, null, false)
        dialogBuilder.setView(dialogView)
        val messageView = dialogView.findViewById<TextView>(R.id.message)
        messageView.text = message
        val dialog = dialogBuilder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!(context as AppCompatActivity).isFinishing) {
            dialog.show()
            loaderDialog = dialog
        }
    }

    fun hideLoaderDialog() {
        if (loaderDialog != null) {
            if (loaderDialog!!.isShowing) {
                try {
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        loaderDialog!!.dismiss()
                    }, 100)
                } catch (e: Exception) {

                }
            }
        }
    }

    fun hideKeyboard(context: Activity) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = context.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(getContext())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getFormattedPrice(price: Double): String {
        val symbols = DecimalFormatSymbols(Locale.US)
        return "AED " + DecimalFormat("##0.00", symbols).format(price)
    }

    fun getFormattedDiscount(discount: Double): String {
        if (discount == 0.0) {
            return ""
        }
        val symbols = DecimalFormatSymbols(Locale.US)
        return DecimalFormat("##0.00", symbols).format(discount) + "% Off"
    }

    fun getFormattedDate(isoDate: String): String {
        if (isoDate.isBlank()) {
            return ""
        }
        val temp: String = isoDate.replace("Z", " UTC")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(temp)
        } catch (e: Exception) {
            e.printStackTrace()
            return isoDate
        }
        val newDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return newDate.format(date)
    }

    fun getFormattedDateWithoutYear(isoDate: String): String {
        if (isoDate.isBlank()) {
            return ""
        }
        val temp: String = isoDate.replace("Z", " UTC")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(temp)
        } catch (e: Exception) {
            e.printStackTrace()
            return isoDate
        }
        val newDate = SimpleDateFormat("MMM dd", Locale.getDefault())
        return newDate.format(date)
    }

    fun getFormattedTime(isoDate: String): String {
        if (isoDate.isBlank()) {
            return ""
        }
        val temp: String = isoDate.replace("Z", " UTC")
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z", Locale.getDefault())
        var date: Date? = null
        try {
            date = sdf.parse(temp)
        } catch (e: Exception) {
            e.printStackTrace()
            return isoDate
        }
        val newDate = SimpleDateFormat("H:m")
        return newDate.format(date)
    }


    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

}