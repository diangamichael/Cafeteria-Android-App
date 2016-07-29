package skallaje.cafeteria_app.cs442.com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Deepika on 11/8/15.
 */

public class d_cartDB extends SQLiteOpenHelper {
    public static final String DB_NAME = "Menu.d_cartDB";
    public static final String TABLE ="cartTable";
    public static final String COL_1 ="ITEM";
    public static final String COL_2 ="PRICE";
    public static final String COL_3 ="QTY";
    public static final String COL_4 ="TOTAL";

    public d_cartDB(Context context)
    {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TABLE + "(" + COL_1 + " TEXT PRIMARY KEY, " + COL_2 + " TEXT, " + COL_3 + " TEXT, " + COL_4 + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
        onCreate(db);
    }

    public void insertData(String item, String price, String qty, String total)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL_1,item);
        cv.put(COL_2,price);
        cv.put(COL_3,qty);
        cv.put(COL_4, total);
        long result = db.insert(TABLE, null, cv);
    }

    public Cursor getData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE, null);
        return res;
    }

    public Integer deleteItem(String item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, COL_1 + "=" +item, null);
    }

    public void truncateDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, "1=1", null);
    }

    public Cursor getFilteredData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE + " where "+ COL_1 +"=" +item, null);
        return res;
    }

    public void dropDB()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}