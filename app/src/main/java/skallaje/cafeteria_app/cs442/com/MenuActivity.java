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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class MenuActivity extends Activity {
    public ArrayList<String> itemName; //Added by Deepika
    public ArrayList<String> itemPrice; //Added by Deepika
    public ArrayList<String> itemQty; //Added by Deepika
    ListView listView;
    UserSession s;
    MenuAdapter md;
    List<MenuItems> mlist;
    ListDataAdapter listDataAdapter;
    public static ListView menuListView; //Added by Deepika
    d_cartDB db; //Added by Deepika


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        s=new UserSession(this);
        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        User u = dbf.getID(s.getSession());

        if(u != null && !u.ID.equals("A20357165") && s.getSessionPWD().equals(u.password))
            setContentView(R.layout.activity_main);
        else {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            return;
        }
        db = new d_cartDB(this); //Added by Deepika
        menuListView = (ListView) findViewById(R.id.listViews);  //Added by Deepika


        if(dbf.getAllMenuItems().size()==0){
            dbf.createMenuItem("Pasta", "1.99", "Mediterranean Pasta with Mayonnaise and Jalapenos",convertToBytes(R.drawable.pasta));
            dbf.createMenuItem("Burger", "2.99", "Cheese Burger with tomatoes and onions",convertToBytes(R.drawable.burger));
            dbf.createMenuItem("Pizza", "3.99", "Thin crust pizza with Jalapenos, lemon, tomatoes, cottage cheese",convertToBytes(R.drawable.pizza));
        }

        mlist = dbf.getAllMenuItems() ;

        //s=new UserSession(this);

       // md = new MenuAdapter(mlist, this);
       // listView.setAdapter(md);

        listView = (ListView) findViewById(R.id.listViews);
        listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.display_row);
        listView.setAdapter(listDataAdapter);

            for(int i = 0;i<mlist.size();i++) {

                MenuItems m = new MenuItems(mlist.get(i).getID(),
                        mlist.get(i).getItem(),
                        mlist.get(i).getPrice(),
                        mlist.get(i).getDesc());
                listDataAdapter.add(m);
            }

        listDataAdapter.notifyDataSetChanged();

        Button addToCartBtn = (Button) findViewById(R.id.btnUpdate);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartBtnClk(view);
            }
        });
    }

    public byte [] convertToBytes(int img_id){
        Bitmap bmp = BitmapFactory.decodeResource(getResources(),img_id);
        ByteArrayOutputStream imagebyte = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, imagebyte);
        byte [] image = imagebyte.toByteArray();
        return image;
    }

    public boolean validateQuantity(View v){
        if(mlist.size() > 0) {
           // EditText q = (EditText) findViewById(R.id.t_qty);
			 RelativeLayout rl = (RelativeLayout) findViewById(R.id.rellay);

            for (int i = 0; i < mlist.size(); i++) {
                if (rl.getChildAt(i) instanceof EditText) {
                    //myEditTextList.add((EditText) myLayout.getChildAt(i));
                    EditText q = (EditText) rl.getChildAt(i);

                    System.out.println("Comment deleted with img: " + mlist.size());
                    System.out.println("Comment deleted with getCC: " + rl.getChildCount());

            if (q.getText().toString().trim().equals("")) {
                Toast.makeText(this, "QUANTITY cannot be blank!", Toast.LENGTH_LONG).show();
                return false;
            }

            if (!q.getText().toString().trim().matches(("[0-9]+"))) {
                Toast.makeText(this, "QUANTITY can only be a number!", Toast.LENGTH_LONG).show();
                return false;
            }

            if (Integer.valueOf(q.getText().toString().trim()) >= 100) {
                Toast.makeText(this, "Quantity per item must be less than 100!", Toast.LENGTH_LONG).show();
                return false;
            }

            Intent cart_intent = new Intent(this, LoginActivity.class);
            startActivity(cart_intent);
            return true;
        }
            }
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Added by Deepika
    private void addToCartBtnClk(View view) {
        getMenuList();
        emptyCart(view);
        addToCart();
        Intent intent = new Intent(this, d_MenuCartActivity.class);
        startActivity(intent);
    }

    // Added by Deepika
    public void getMenuList() {
        itemName = new ArrayList<String>();
        itemPrice = new ArrayList<String>();
        itemQty = new ArrayList<String>();
//        itemName = null;
//        itemPrice = null;
//        itemQty = null;
        View parentView = null;
        Random r = new Random();
        for(int i=0; i < MenuActivity.menuListView.getCount(); i++) {
            parentView = getViewByPostion(i, MenuActivity.menuListView);
            String qty = ((TextView) parentView.findViewById(R.id.t_qty)).getText().toString();
            try {
                if (Integer.parseInt(qty) > 0) {
                    itemName.add(((TextView) parentView.findViewById(R.id.t_name)).getText().toString());
                    itemPrice.add(((TextView) parentView.findViewById(R.id.t_price)).getText().toString());
                    itemQty.add(((TextView) parentView.findViewById(R.id.t_qty)).getText().toString());
//            int tmpQty = r.nextInt(9); //Temporay hardcoding of Quantity values.
//            itemQty.add(Integer.toString(tmpQty));
                }
            }catch(NumberFormatException e){}
        }
    }

    // Added by Deepika
    public View getViewByPostion(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        }
        else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    //Added by Deepika
    public void addToCart() {
        Iterator itr1 = itemName.iterator();
        Iterator itr2 = itemPrice.iterator();
        Iterator itr3 = itemQty.iterator();
        while(itr1.hasNext() && itr2.hasNext() && itr3.hasNext()) {
            String itemNameStr = itr1.next().toString();
            String itemPriceStr1 = itr2.next().toString();
            String itemPriceStr = itemPriceStr1.substring(1);
            String itemQtyStr = itr3.next().toString();
            double itemPriceNum = Double.parseDouble(itemPriceStr);
            int itemQtyNum = Integer.parseInt(itemQtyStr);
            double itemTotalPrice = itemPriceNum * itemQtyNum;
            db.insertData(itemNameStr, itemPriceStr, itemQtyStr, Double.toString(itemTotalPrice));
        }
    }

    //Added by Deepika
    public void emptyCart(View view) {
        db.truncateDB();
        d_MenuCartActivity.global_finalTotal = 0;
        d_MenuCartActivity.global_qty = 0;
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