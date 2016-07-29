package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Deepika on 11/8/15.
 */

public class d_DeliveryOptionActivity extends Activity{
    public static int global_delOpt;
    public static String global_del_loc;
    RadioGroup delOptGrp;
    int ch;
//    boolean def = false;
    UserSession s;
    final DatabaseFunctions dbf = new DatabaseFunctions(this);

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_deliveryoptions);
        s=new UserSession(this);
        ch = -1;

//        global_del_loc = user.location;

//        RadioButton def = (RadioButton) findViewById(R.id.def_del_loc);
//        if(global_del_loc != null) {
//            def.setText("Previous delivery location preference : '" + global_del_loc +"'");
//        }
//        if(global_del_loc == null) {
//            def.setVisibility(View.GONE);
//        }

        delOptGrp = (RadioGroup)findViewById(R.id.delChoice);
        delOptGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                ch = radioGroup.indexOfChild(radioButton);
            }
        });



        Button proceedBtn = (Button) findViewById(R.id.proceedBtn);
        //CheckBox defchkbutton = (CheckBox)findViewById(R.id.def_del_loc);
      /*  defchkbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if(isChecked)
                                                            def = true;
                                                    }
                                                }
        );
        */


                proceedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        global_delOpt = ch;
                            if (ch == 0) {
                                global_del_loc = "Pickup-MTCC";
                                orderConfirmation(view);
                            }
                            else if (ch == 1)
                                deliveryLocations(view);
//                            else if (ch == 2)
//                                orderConfirmation(view);
                            else
                                Toast.makeText(d_DeliveryOptionActivity.this, "Please choose a delivery option first", Toast.LENGTH_LONG).show();
                        }
                });

    }

    public void orderConfirmation(View view){
        dbf.open();
        final User user = dbf.getID(s.getSession());
        dbf.setLocation(user.ID, global_del_loc);
        Intent intent = new Intent(this, d_OrderConfirmationActivity.class);
        startActivity(intent);
    }

    public void deliveryLocations(View view){
        Intent intent = new Intent(this, d_DeliveryLocationsActivity.class);
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
