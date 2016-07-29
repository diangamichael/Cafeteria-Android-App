package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean signup(View v){

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        EditText id = (EditText) findViewById(R.id.s_id);
        EditText pwd = (EditText) findViewById(R.id.s_password);
        EditText email = (EditText) findViewById(R.id.s_username);
        EditText ph = (EditText) findViewById(R.id.s_phone);
        Log.d("App", "pwd: " + pwd.getText().toString());

        if(id.getText().toString().trim().length() != 9 ) {
            Toast.makeText(this, "LOGIN ID must be an 9 characters Student ID!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(id.getText().toString().trim().charAt(0) != 'A' ) {
            Toast.makeText(this, "LOGIN ID must begin with 'A' followed by 8 digit number!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!id.getText().toString().trim().matches("[A-Za-z0-9]+")){
            Toast.makeText(this, "ID can contain Alphabets/Numbers", Toast.LENGTH_LONG).show();
            return false;
        }

        User u = dbf.getID(id.getText().toString());

        if(pwd.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(email.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "USERNAME cannot be empty. It must match the ID of your email address.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!email.getText().toString().trim().matches("[A-Za-z0-9]+")){
            Toast.makeText(this, "USERNAME can contain Alphabets/Numbers", Toast.LENGTH_LONG).show();
            return false;
        }

        if(dbf.checkIfRegistered(email.getText().toString().trim()+"@hawk.iit.edu")!=null){
            Toast.makeText(this, "USERNAME entered already exists in our records. Try with a different username.", Toast.LENGTH_LONG).show();
            return false;
        }

        if(ph.getText().toString().trim().length() != 10 || !ph.getText().toString().trim().matches("[0-9]+")) {
            Toast.makeText(this, "PHONE NUMBER is not valid. Please enter a valid phone number!", Toast.LENGTH_LONG).show();
            return false;
        }

        Long phnum = dbf.checkIfPhoneRegistered(ph.getText().toString().trim());

        Intent original_intent = new Intent(this, LoginActivity.class);
        if(u == null && phnum == null) {
            //User doesn't exist
            User user = dbf.createID(id.getText().toString(), pwd.getText().toString(), "No", email.getText().toString(),0, ph.getText().toString());
            if (user != null) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(String.valueOf(ph.getText().toString().trim()), null, "You have been registered with IIT's Cafeteria App. Your login ID is "+ id.getText().toString()+". Your username is "+email.getText().toString(), null, null);
                Toast.makeText(this, "You have been registered with IIT's Cafeteria App!", Toast.LENGTH_LONG).show();
                startActivity(original_intent);
                return true;
            }
        }

        Toast.makeText(this, "A user already exists with those credentials.", Toast.LENGTH_LONG).show();
        startActivity(original_intent);
        return false;

    }
}
