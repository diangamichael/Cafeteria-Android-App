package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Deepika on 11/22/15.
 */
public class d_OrderSuccessActivity extends Activity {

    UserSession s;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_ordersuccess);
        s = new UserSession(this);

        TextView orderNo = (TextView) findViewById(R.id.txt2);
        orderNo.setText("Order#: " + d_OrderConfirmationActivity.orderNo);

        TextView paymentID = (TextView) findViewById(R.id.txt3);
        paymentID.setText("Payment#: " + d_OrderConfirmationActivity.global_paymentID);

        TextView paymentTot = (TextView) findViewById(R.id.txt4);
        paymentTot.setText("Transaction Amount: $" + String.format("%.2f", d_MenuCartActivity.global_finalTotal));

        Button newOrderBtn = (Button) findViewById(R.id.newOrderBtn);
        newOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMenuActivity(view);
            }
        });

        Button signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut(view);
            }
        });

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        Log.d("App ","Log Pay "+s.getSession());
        User u = dbf.getID(s.getSession());
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(String.valueOf(u.phone), null, "Your payment is successful."+" \nYour order has been placed. Your Order ID is "+d_OrderConfirmationActivity.orderNo, null, null);
    }

    public void startMenuActivity(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        if (s != null) {
            s.deleteSession();
            super.onDestroy();
            finish();
            Intent logout = new Intent(this, LoginActivity.class);
            logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logout);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_logout:
                if (s != null) {
                    s.deleteSession();
                    super.onDestroy();
                    finish();
                    Intent logout = new Intent(this, LoginActivity.class);
                    logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logout);
                }
                break;

            case R.id.menu_change_password:
                if (s != null) {
                    Intent change_password = new Intent(this, ChangePasswordActivity.class);
                    startActivity(change_password);
                }
                break;
        }

        return true;
    }
}