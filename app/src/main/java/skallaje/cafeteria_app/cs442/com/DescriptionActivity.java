package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Smruti on 10/23/2015.
 */
public class DescriptionActivity extends Activity{

   // DB db;
    TextView etName,etPrice,etDesc;
    ImageView im;
    String id = null;
    byte [] img;
    UserSession s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_update);
        id = getIntent().getExtras().getString("ID");
        s=new UserSession(this);
        //db = new DB(this);
        //Cursor res = db.getUpdateData(Integer.parseInt(id));

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        MenuItems m = dbf.getFoodItem(Integer.valueOf(id),"");
        img=dbf.getImage(m.getID());

        etName = (TextView)findViewById(R.id.textView3);
        im = (ImageView)findViewById(R.id.imageView);

       // etPrice = (EditText)findViewById(R.id.ePrice);
       // etDesc = ( EditText)findViewById(R.id.eDesc);
      /*  if (res.getCount() == 0) {
            Toast.makeText(this, "No Data to show! ", Toast.LENGTH_LONG).show();
        } */

                String name, price, details;
                //name = res.getString(1);
                //price = res.getString(2);
                details = m.getDesc();
                etName.setText(details);
               // int idd = getResources().getIdentifier("spate149_a6.cs442.com:mipmap/d" + String.valueOf(id), null, null);
                Bitmap b = BitmapFactory.decodeByteArray(img, 0, img.length);
                im.setImageBitmap(b);
                //etPrice.setText(price);
               // etDesc.setText(details);

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

   /* public void updateItem(){
        db = new DB(this);
        String name = etName.getText().toString();
        String price = etPrice.getText().toString();
        String details = etDesc.getText().toString();
        int res = db.updateItem(id,name,price,details);
        Toast.makeText(this, "Updated Item", Toast.LENGTH_LONG).show();
    }*/

