package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddItemActivity extends Activity {

    private static int RESULT_LOAD_IMG = 1;
    String imgStr;
    byte [] image;
    UserSession s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

    }

    public void loadImagefromAndroidGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent picIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(picIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && data != null) {
                // Get the Image from data

                Uri pickedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(pickedImage,
                        filePath, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePath[0]);
                imgStr = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bmp = BitmapFactory.decodeFile(imgStr);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
                image = bos.toByteArray();

            } else {
                if (image == null){
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
            }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Image cannot be uploaded at the moment due to an internal issue. Please try later.", Toast.LENGTH_LONG)
                    .show();
            Intent original_intent = new Intent(this,MenuAdminActivity.class);
            //Intent original_intent = new Intent(this,TestActivity.class);
            startActivity(original_intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_items, menu);
        return true;
    }


    public boolean addNew(View v){

       // byte [] image;
        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();
        EditText item = (EditText) findViewById(R.id.item);
        EditText price = (EditText) findViewById(R.id.price);
        EditText desc = (EditText) findViewById(R.id.desc);

        if(item.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "ITEM cannot be blank!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(price.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "PRICE cannot be blank!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(isFloat(price.getText().toString().trim())== false){
            Toast.makeText(this, "PRICE must be a number!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(desc.getText().toString().trim().length() == 0 ) {
            Toast.makeText(this, "DESCRIPTION cannot be blank!", Toast.LENGTH_LONG).show();
            return false;
        }

        if(image == null){
            Toast.makeText(this, "Please upload an image!", Toast.LENGTH_LONG).show();
            return false;

        }

        MenuItems m = dbf.getFoodItem(-1, item.getText().toString());
        if(m==null) {

            dbf.createMenuItem(item.getText().toString(), price.getText().toString(), desc.getText().toString(),image);
        }
        else{
            Toast.makeText(this, "The item already exists. Do you wish to update? ", Toast.LENGTH_LONG).show();
            return false;
        }

        Intent original_intent = new Intent(this,MenuAdminActivity.class);
        //Intent original_intent = new Intent(this,TestActivity.class);
        startActivity(original_intent);
        return true;


    }

    public  boolean isFloat(String prc){
        boolean flag = true;
        try {
            Float.parseFloat(prc);
        }
        catch(NumberFormatException e){
            flag = false;
        }
        return flag;
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
