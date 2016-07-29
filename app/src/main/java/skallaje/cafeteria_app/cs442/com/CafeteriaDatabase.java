package skallaje.cafeteria_app.cs442.com;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by shara on 10/18/2015.
 */
public class CafeteriaDatabase extends SQLiteOpenHelper {


    public static final String LOGIN_TB = "LOGIN_TB";
    public static final String login_id = "login_id";
    public static final String password = "password";
    public static final String admin = "admin";
    public static final String email = "email";
    public static final String phone = "phone";
    public static final String default_location = "default_location";
    public static final String sms_sent = "sms_sent";

    public static final String MENU_TB = "MENU_TB";
    public static final String item_id = "item_id";
    public static final String item_name = "name";
    public static final String item_desc = "desc";
    public static final String item_price = "price";
    public static final String item_image = "image";

    private static final String DATABASE_NAME = "Cafeteria.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String LOGIN_TABLE_CREATE = "create table "
            + LOGIN_TB + "(" + login_id
            + " text not null primary key, " + password
            + " text not null, "+email + " text not null, "+sms_sent + " integer default 0, " +phone + " text not null, "+default_location + " text, "+ admin + " text not null )";

    private static final String MENU_TABLE_CREATE = "create table "
            + MENU_TB + "(" + item_id
            + " integer primary key autoincrement, " +item_desc+ " text not null, " + item_name
            + " text not null, "+item_image+" blob not null, "+ item_price + " real not null);";

    public CafeteriaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(LOGIN_TABLE_CREATE);
        database.execSQL(MENU_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(CafeteriaDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TB);
        db.execSQL("DROP TABLE IF EXISTS " + MENU_TB);
        onCreate(db);
    }

}


