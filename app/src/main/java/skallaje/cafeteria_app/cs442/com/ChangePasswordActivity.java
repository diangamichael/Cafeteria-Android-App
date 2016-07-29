package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity {

    UserSession s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        s = new UserSession(this);
        //Log.d("App", "Logged Innnn CPPPP... " + s.getSession());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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

        }

        return true;
    }

    public boolean changePassword(View v){

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        EditText pwd1 = (EditText) findViewById(R.id.c_pass);
        EditText pwd2 = (EditText) findViewById(R.id.cc_pass);

        Log.d("S id in CP ", ""+s.getSession());
        User u = dbf.getID(s.getSession());
        Log.d("Ch ID", ""+u.ID);


        if(pwd1.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "PASSWORD cannot be blank!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(pwd2.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "PASSWORD cannot be blank!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!pwd2.getText().toString().trim().equals(pwd1.getText().toString().trim()) ) {
            Toast.makeText(this, "PASSWORDS don't match!", Toast.LENGTH_LONG).show();
            return false;
        }

        Log.d("App", "ints " +s.getSession()+" : "+ u.password +" : "+pwd1.getText().toString().trim()+" : "+pwd2.getText().toString().trim());

        if(u.password.trim().equals(pwd1.getText().toString().trim()) || u.password.trim().equals(pwd2.getText().toString().trim()) ) {
            Toast.makeText(this, "New password cannot be the same as old password!", Toast.LENGTH_LONG).show();
            return false;
        }

        int count = dbf.updatePassword(s.getSession(), pwd1.getText().toString());
        Intent original_intent;
        if(count == 1){
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show();

        if(u.sent == 1)
                dbf.smsSentStatus(u.ID,0);

        if(u.admin.equals("Yes"))
            original_intent = new Intent(this,MenuAdminActivity.class);
        else
            original_intent = new Intent(this,MenuActivity.class);

            //Intent original_intent = new Intent(this,TestActivity.class);
            startActivity(original_intent);
            return true;
        }

        Toast.makeText(this, "Error occurred while updating password. Please try again later", Toast.LENGTH_LONG).show();
        original_intent = new Intent(this,LoginActivity.class);
        //Intent original_intent = new Intent(this,TestActivity.class);
        startActivity(original_intent);

    return false;
    }
}
