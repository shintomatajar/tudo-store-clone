package ae.tudomart.store.ui.activities.barcodeGenerator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import ae.matajar.store.R;

import java.util.Hashtable;

import ae.tudomart.store.ui.activities.BaseActivity;
import ae.tudomart.store.utils.Utils;

public class BarcodeGeneratorActivity extends BaseActivity {

    private ImageView backIcon;
    private TextView titleToolbar;

    private TextView mButtonGenerate;

    private EditText mEditTextProductId;
    private ImageView mImageViewResult;
    private EditText mEditTextPriceId;
    private ImageView mImageViewResultPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_generator);
        initViews();
        initToolbar();
    }


    private void initViews() {
        backIcon = findViewById(R.id.back_icon);
        titleToolbar = findViewById(R.id.title_toolbar);
        titleToolbar.setText("Generate Barcode");

        mEditTextProductId = findViewById(R.id.editTextProductId);
        mButtonGenerate = findViewById(R.id.buttonGenerate);
        mEditTextPriceId = findViewById(R.id.editTextPriceId);
        mImageViewResultPrice = findViewById(R.id.imageViewResultPrice);
        mImageViewResult = findViewById(R.id.imageViewResult_order_id);

    }

    private void initToolbar() {
        backIcon.setOnClickListener(v -> onBackPressed());
    }

    public void generateBarcode_onclick(View view) {
        String productId,priceId;
        try {
            if (mEditTextProductId.getText().toString().length() > 0) {
                productId = mEditTextProductId.getText().toString();
                Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                Writer codeWriter;
                codeWriter = new Code128Writer();
                BitMatrix byteMatrix = null;
                try {
                    byteMatrix = codeWriter.encode(productId, BarcodeFormat.CODE_128, 400, 200, hintMap);
                } catch (WriterException ex) {
                    ex.printStackTrace();
                }
                int width = byteMatrix.getWidth();
                int height = byteMatrix.getHeight();
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                mImageViewResult.setImageBitmap(bitmap);
            } else {
                mEditTextProductId.setFocusable(true);
                Toast.makeText(getApplicationContext(), "Enter Data to Generate Barcode..!", Toast.LENGTH_LONG).show();
            }

                if (mEditTextPriceId.getText().toString().length() > 0) {
                    priceId = mEditTextPriceId.getText().toString();
                    Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
                    hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
                    Writer codeWriter;
                    codeWriter = new Code128Writer();
                    BitMatrix byteMatrix = null;
                    try {
                        byteMatrix = codeWriter.encode(priceId, BarcodeFormat.CODE_128, 400, 200, hintMap);
                    } catch (WriterException ex) {
                        ex.printStackTrace();
                    }
                    int width = byteMatrix.getWidth();
                    int height = byteMatrix.getHeight();
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    mImageViewResultPrice.setImageBitmap(bitmap);
                } else {
                    mEditTextPriceId.setFocusable(true);
                    Toast.makeText(getApplicationContext(), "Enter Data to Generate Barcode..!", Toast.LENGTH_LONG).show();
                }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    } @Override
    protected void showAlert(String orderId) {
        Utils.showNewOrderAlert(this,orderId);
    }
}
