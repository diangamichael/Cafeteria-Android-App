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

public class LoginActivity extends Activity{

    protected UserSession s;
    public final static String SessionID = "Dummy Text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        s = new UserSession(this);

        //Log.d("App", "Logged Innnn... " + s.getSession());

       // DatabaseFunctions dbf = new DatabaseFunctions(this);
        //dbf.open();
        //dbf.updateID("A20357165","Yes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public boolean login(View V){
        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        EditText id = (EditText) findViewById(R.id.l_id);
        EditText pwd = (EditText) findViewById(R.id.l_password);

        Log.d("App", "IDs" + id.getText().toString());

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

        if(pwd.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        //dbf.deleteID("A20357165");
        dbf.updateID("A20357165","Yes");

        User u = dbf.getID(id.getText().toString());
        //dbf.deleteMenuItem(0);
        if(u == null) {
            Toast.makeText(this, "You are not a registered user!", Toast.LENGTH_LONG).show();
            //User doesn't exist
            //Log.d("App", "User: " + u);
            return false;
        }
        else{
            Log.d("App", "User: " + u.password+ " "+pwd.getText().toString());
            if(u.password.trim().equals(pwd.getText().toString().trim())) {
                Log.d("App", "u Sentt "+u.sent);
                s.setSession(id.getText().toString());
                s.setSessionPWD(pwd.getText().toString());
                if(u.sent == 0) {
                    Log.d("App", "Passwords match");
                    if (u.admin.equals("Yes")) {
                        Intent admin_intent = new Intent(this, MenuAdminActivity.class);
                        admin_intent.putExtra(SessionID, s.getSession());
                        startActivity(admin_intent);
                        Toast.makeText(this, "Welcome admin!", Toast.LENGTH_LONG).show();
                        //Log.d("App", "Admin User exists");
                    } else {
                        //Intent menu_intent = new Intent(this, .class);
                        //startActivity(menu_intent);
                        Intent student_intent = new Intent(this, MenuActivity.class);
                        student_intent.putExtra(SessionID, s.getSession());
                        startActivity(student_intent);
                        Toast.makeText(this, "Welcome student!", Toast.LENGTH_LONG).show();
                        //Log.d("App", "Non-admin User exists");
                    }
                }else{
                    Intent ic = new Intent(this, ChangePasswordActivity.class);
                    startActivity(ic);
                }
            }else{
                Toast.makeText(this, "Incorrect Login ID/Password", Toast.LENGTH_LONG).show();
                //Log.d("App", "Wrong password");
                return false;
            }
            return true;
        }
    }

    public void signup(View V){
        Intent update_intent = new Intent(this, SignupActivity.class);
        startActivity(update_intent);
    }

    public void callForgotPassword(View v){
        Intent fp = new Intent(this, ForgotPassword.class);
        startActivity(fp);

    }
}
