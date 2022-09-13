package ae.tudomart.store.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marcoscg.dialogsheet.DialogSheet;
import ae.matajar.store.R;
import ae.tudomart.store.model.order.orderDetails.ModelOrderPriceDetails;
import ae.tudomart.store.ui.customViews.MyDialogSheet;
import ae.tudomart.store.utils.Utils;


public class EditPriceAlert {

    private TextView txt_sub_total;
    private TextView txt_delivery_charge;
    private TextView txtPromoName;
    private TextView txtDiscount;
    private TextView txt_total_price;
    private TextView txtVariation;
    private LinearLayout layPromoDiscount;
    private LinearLayout layVariation;
    private EditText edtPrice;
    private ModelOrderPriceDetails priceDetails;
    private TextView superCoin;

    private String variesAmount;
    private String variedGrandTotal;

    Activity activity;

    public EditPriceAlert(Activity activity) {
        this.activity = activity;
    }

    public void showEditOrderAlert(ModelOrderPriceDetails priceDetails, final onPriceEdited onPriceEdited) {
        this.priceDetails = priceDetails;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_edit_price_pop_up, null);
        initViews(view);
        setUpViews();
        builder.setView(view);
        builder.setPositiveButton("APPLY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDialogSheet dialogSheet = new MyDialogSheet(activity);
                dialogSheet.setTitle("Edit Price?");
                dialogSheet.setMessage("Once you have edited the price then it can't be rolled back!");
                dialogSheet.setPositiveButton("CONTINUE", new DialogSheet.OnPositiveClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPriceEdited.onPriceEdited(variesAmount,variedGrandTotal);
                    }
                });
                dialogSheet.setNegativeButton("CANCEL", null);
                dialogSheet.show();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    private void setUpViews() {
        edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (charSequence.toString().equals("") || charSequence.toString().equals("-")){
                        charSequence = "0";
                    }
                    Float enteredPrice = Float.parseFloat(String.valueOf(charSequence));
                    processPriceEdit(enteredPrice);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(edtPrice.getContext(), "Could not process data, please try again..!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void processPriceEdit(Float enteredPrice) {
        if (enteredPrice > priceDetails.getData().get(0).getIntGrandTotal()) {
            Toast.makeText(activity, "Variation amount can't be greater than sub total", Toast.LENGTH_SHORT).show();
            edtPrice.setText(""+priceDetails.getData().get(0).getIntGrandTotal());
            return;
        }
        double newGrandTotal = priceDetails.getData().get(0).getIntGrandTotal() + enteredPrice;
        txt_total_price.setText("AED "+ Utils.toFixed(newGrandTotal));
        txtVariation.setText("AED "+Utils.toFixed(enteredPrice) );
        variesAmount = enteredPrice.toString();
        variedGrandTotal = Utils.toFixed(newGrandTotal);
        if (enteredPrice != 0){
            layVariation.setVisibility(View.VISIBLE);
        } else {
            layVariation.setVisibility(View.GONE);
        }
    }

    private void initViews(View view) {
        txt_sub_total = view.findViewById(R.id.txt_sub_total);
        txt_delivery_charge = view.findViewById(R.id.txt_delivery_charge);
        txtPromoName = view.findViewById(R.id.txtPromoName);
        txtDiscount = view.findViewById(R.id.txtDiscount);
        txt_total_price = view.findViewById(R.id.txt_total_price);
        txtVariation = view.findViewById(R.id.txtVariation);
        edtPrice = view.findViewById(R.id.edtPrice);
        layPromoDiscount = view.findViewById(R.id.layPromoDiscount);
        layVariation = view.findViewById(R.id.layVariation);
        superCoin = view.findViewById(R.id.txt_reward_discount);

        txt_sub_total.setText("AED "+priceDetails.getData().get(0).getIntSubTotal());
        txt_total_price.setText("AED "+priceDetails.getData().get(0).getIntGrandTotal());
        txt_delivery_charge.setText("AED "+priceDetails.getData().get(0).getIntDeliveryCharge());
        superCoin.setText("AED "+priceDetails.getData().get(0).getIntDeliveryCharge());
        txtDiscount.setText("AED "+priceDetails.getData().get(0).getIntDiscount());

        if (priceDetails.getData().get(0).getIntDiscount() == 0.0) {
            layPromoDiscount.setVisibility(View.GONE);
        }
        if (priceDetails.getData().get(0).getIntDeliveryCharge() == 0.0) {
            txt_delivery_charge.setText("FREE");
        }

    }

    public interface onPriceEdited{
        void onPriceEdited(String amount, String grandTotal);
    }
}
