package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Deepika on 11/8/15.
 */

public class d_DeliveryLocationsActivity extends Activity {
    public static String global_delLoc;
    RadioGroup delLocGrp;
    int ch;
    UserSession s;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s = new UserSession(this);

        final DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();
        final User user = dbf.getID(s.getSession());


            setContentView(R.layout.d_deliverylocations);

            ch = -1;

            delLocGrp = (RadioGroup) findViewById(R.id.delLocations);
            delLocGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(i);
                    global_delLoc = checkedRadioButton.getText().toString();
                    View radioButton = radioGroup.findViewById(i);
                    ch = radioGroup.indexOfChild(radioButton);
                }
            });

            Button confDelLocBtn = (Button) findViewById(R.id.confDelLocBtn);
            confDelLocBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ch == -1)
                        Toast.makeText(d_DeliveryLocationsActivity.this, "Please choose a delivery location first", Toast.LENGTH_LONG).show();
                    else {
                        dbf.setLocation(user.ID, global_delLoc);
                        orderConfirmation(view);
                    }
                }
            });

    }

    public void orderConfirmation(View view){
        Intent intent = new Intent(this, d_OrderConfirmationActivity.class);
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
