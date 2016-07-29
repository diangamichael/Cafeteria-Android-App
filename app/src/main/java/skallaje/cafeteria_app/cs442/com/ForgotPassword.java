package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

public class ForgotPassword extends Activity {
    EditText eid;
   // Session jms;
//    ProgressDialog pd;
    String s1;
    String txtmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        eid = (EditText) findViewById(R.id.emailid);
        eid.setText("@hawk.iit.edu");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
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

    public boolean sendSMS(View v){

        Intent i;
        if(eid.getText().toString().trim().length() == 0 || eid == null || eid.getText().toString().trim().equals("@hawk.iit.edu") || !eid.getText().toString().trim().contains("@")) {
            Toast.makeText(this, "Please enter a valid E-mail ID", Toast.LENGTH_LONG).show();
            return false;

        }

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        User u = dbf.checkIfRegistered(eid.getText().toString().trim());

        if(u == null){
            Toast.makeText(this, "You are not a registered user", Toast.LENGTH_LONG).show();
            i= new Intent(this, LoginActivity.class);
            startActivity(i);


        }else{
            s1 = eid.getText().toString();
            txtmsg = "Your Login ID: "+u.ID + ".\nYour password : "+u.password;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(String.valueOf(u.phone), null, txtmsg, null, null);
            dbf.smsSentStatus(u.ID,1);

            /*  Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.socketFactory.port", "465");
            p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.port", "465");

            jms = Session.getDefaultInstance(p, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return super.getPasswordAuthentication();
                }
            });

            //pd = ProgressDialog.show(this, "", "Sending mail...", true);

            SendEmail se = new SendEmail();
            se.doInBackground();
            */

            Toast.makeText(this, "An SMS with the password has been sent to you. Relogin and change password for security.", Toast.LENGTH_LONG).show();
            i= new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        return true;


    }

    /*
    class SendEmail extends AsyncTask<String, String, Void>
    {

        @Override
        protected Void doInBackground(String... params) {

            Message msg = new MimeMessage(jms);
            try {
                s1 = "skallaje@hawk.iit.edu";
                msg.setFrom(new InternetAddress("skallaje@hawk.iit.edu"));
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s1));
                msg.setContent(txtmsg, "text/html; charset=utf-8");

                Transport.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    } */
}

