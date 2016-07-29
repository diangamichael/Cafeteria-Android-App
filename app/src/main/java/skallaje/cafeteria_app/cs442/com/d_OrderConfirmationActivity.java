package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by Deepika on 11/8/15.
 */
public class d_OrderConfirmationActivity extends Activity {
    public static int orderNo;
    private static final String CONFIG_CLIENT_ID = "ASvoTl_qj-DgpTqubv_edi7vGWOIDkO4ssd_cs00Ozj6qOiZdwF7r9s6Nd0-ZPHtU91NMEL7Iv1fzC10";
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
    public static String global_paymentID = "";
    String delLoc;
    String finalTotal;
    int cartItemsQty;
    UserSession s;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_orderconfirm);

        s=new UserSession(this);

        TextView a = (TextView) findViewById(R.id.a);
        TextView b = (TextView) findViewById(R.id.b);
        TextView c = (TextView) findViewById(R.id.c);
        TextView d = (TextView) findViewById(R.id.d);
        TextView e = (TextView) findViewById(R.id.e);

        Random r = new Random();
        orderNo = r.nextInt(999999);

        /* Get Delivery Option (Pickup/Delivery) */
        getDeliveryOption();
        getFinalCartValue();
        getCartItemsQty();

        d.setText("Order#: " + orderNo);
        a.setText("Total Number of Items in Cart: " + cartItemsQty);
        b.setText("Total Amount Payable: $" + finalTotal);
        c.setText("Delivery Method: " +delLoc);
        e.setTextColor(Color.BLUE);
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDelOpt();
            }
        });

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Button addToCartBtn = (Button) findViewById(R.id.goToCheckoutBtn);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paypalPayment(view);
            }
        });
    }

    private void getDeliveryOption() {
        int delOpt = d_DeliveryOptionActivity.global_delOpt;
        //String deldef = d_DeliveryLocationsActivity.global_delLoc;
            if (delOpt == 0)
                delLoc = d_DeliveryOptionActivity.global_del_loc;

            if (delOpt == 1)
                getDeliveryLocation();

            if(delOpt == 2)
                delLoc = d_DeliveryOptionActivity.global_del_loc;
    }


    private void  getDeliveryLocation() {
        delLoc = d_DeliveryLocationsActivity.global_delLoc;
    }

    private void getFinalCartValue() {
        finalTotal = String.format("%.2f", d_MenuCartActivity.global_finalTotal);
    }

    private void getCartItemsQty() {
        cartItemsQty = d_MenuCartActivity.global_qty;
    }

    public void paypalPayment(View view) {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(d_MenuCartActivity.global_finalTotal),"USD", "IIT Cafeteria Services LLC", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        this.startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println("Responseeee" + confirm);
                        Log.i("paymentExample", confirm.toJSONObject().toString());


                        JSONObject jsonObj=new JSONObject(confirm.toJSONObject().toString());

                        global_paymentID = jsonObj.getJSONObject("response").getString("id");
                        System.out.println("payment id:-==" + global_paymentID);
                        Toast.makeText(getApplicationContext(), "Order Placed Successfully! Payment Confirmation ID: "+global_paymentID, Toast.LENGTH_LONG).show();
                        showOrderSuccess();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment was submitted. Please see the docs.");
            }
        }
        //showOrderSuccess();
    }

    public void showOrderSuccess() {
        Intent intent = new Intent(this, d_OrderSuccessActivity.class);
        startActivity(intent);
    }

    public void goToDelOpt() {
        Intent intent = new Intent(this, d_DeliveryOptionActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.menu_logout:
                if(s != null) {
                    s.deleteSession();
                    super.onDestroy();
                    finish();
                    Intent logout = new Intent(this, LoginActivity.class);
                    logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logout);
                }
                break;

            case R.id.menu_change_password:
                if(s != null) {
                    Intent change_password = new Intent(this, ChangePasswordActivity.class);
                    startActivity(change_password);
                }
                break;
        }

        return true;
    }
}
