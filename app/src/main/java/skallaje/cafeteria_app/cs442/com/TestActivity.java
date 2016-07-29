package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class TestActivity extends Activity {

    ImageView imageview=null;
    byte[] img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

       // img=dbf.getImage();

        imageview = (ImageView) findViewById(R.id.imv);

        Bitmap b = BitmapFactory.decodeByteArray(img, 0, img.length);
        System.out.println("Comment deleted with imgaaa: " + b+" "+b.toString()+" "+b.describeContents()+" "+b.getByteCount());
        imageview.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
        Toast.makeText(this, "Retrived successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
