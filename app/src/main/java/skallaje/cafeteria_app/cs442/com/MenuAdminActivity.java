package skallaje.cafeteria_app.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MenuAdminActivity extends Activity {
    UserSession s;
    ListView lv;
    MenuAdapter md;
    List<MenuItems> mlist;
    int sel_id;

    public final static String Val = "Dummy Text";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuitem);
        Intent intent = getIntent();
        String SessionID = intent.getStringExtra(LoginActivity.SessionID);
        s = new UserSession(this);
       //Toast.makeText(this, "A"+s.getSession(), Toast.LENGTH_LONG).show();
        if(s.getSession() != "" && s.getSession() != null && s.getSession().length()==8 && s.getSession().equals(SessionID))
            Toast.makeText(this, "Yay! Menu Screen!", Toast.LENGTH_LONG).show();
            //start a new activity

        lv = (ListView) findViewById(R.id.listView);

        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        mlist = dbf.getAllMenuItems() ;

      /*  mlist.add(new skallaje.cafeteria_app.cs442.com.Menu(1, "Pizza", 250));
        mlist.add(new skallaje.cafeteria_app.cs442.com.Menu(2, "Burger", 250));
        mlist.add(new skallaje.cafeteria_app.cs442.com.Menu(3, "Deviled Eggs", 250));
        mlist.add(new skallaje.cafeteria_app.cs442.com.Menu(4, "Pizza", 250));
        mlist.add(new skallaje.cafeteria_app.cs442.com.Menu(5, "Burger", 250));
        mlist.add(new skallaje.cafeteria_app.cs442.com.Menu(6, "Deviled Eggs", 250)); */

        md = new MenuAdapter(mlist, this);
        lv.setAdapter(md);
        //lv.setItemsCanFocus(true);
        lv.setOnItemClickListener(lvOnClick);
    }

    private AdapterView.OnItemClickListener lvOnClick = new AdapterView.OnItemClickListener()
    {
        public void onItemClick(AdapterView<?> adapter, View v, int position,
                                long arg3)
        {
            MenuItems m = (MenuItems)adapter.getItemAtPosition(position);
            sel_id=m.getID();

            //RadioButton chkboxView = (RadioButton) v.findViewById(R.id.m_chkbox);
            //chkboxView.setTag(true);
            //Log.d("App","Vaaal "+position+" "+m.getID());
            // assuming string and if you want to get the value on click of list item
            // do what you intend to do on click of listview row
        }
    };

    public void callAdd(View v){
        Intent add_intent = new Intent(this, AddItemActivity.class);
        startActivity(add_intent);
    }

    public void callUpdate(View v){
        Intent update_intent = new Intent(this, UpdateItemActivity.class);
        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();
        Log.d("App", "int " + sel_id);

        if(dbf.getAllMenuItems().size() != 0) {
            if (sel_id != 0) {
                MenuItems m = dbf.getFoodItem(sel_id, "");
                update_intent.putExtra(Val, m.getID() + ":" + m.getItem() + ":" + m.getPrice() + ":" + m.getDesc());
                startActivity(update_intent);
            } else {
                Toast.makeText(this, "Please select an item and then click on 'UPDATE' button", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Menu is empty. Add an item!", Toast.LENGTH_LONG).show();
        }
    }

    public void callDelete(View v){
        Intent delete_intent = new Intent(this, MenuAdminActivity.class);
        DatabaseFunctions dbf = new DatabaseFunctions(this);
        dbf.open();

        if(dbf.getAllMenuItems().size() != 0) {
            if (sel_id != 0) {
                dbf.deleteMenuItem(Integer.valueOf(sel_id));
                startActivity(delete_intent);
            } else {
                Toast.makeText(this, "Please select an item and then click on 'DELETE' button", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Menu is empty. Add an item!", Toast.LENGTH_LONG).show();
        }
    }
/*
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = lv.getPositionForView(buttonView);
        if(position != ListView.INVALID_POSITION){
            skallaje.cafeteria_app.cs442.com.Menu m= mlist.get(position);
            m.setChecked(isChecked);

            Toast.makeText(this, "You have selected "+m.getItem(), Toast.LENGTH_LONG).show();

        }

    } */
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