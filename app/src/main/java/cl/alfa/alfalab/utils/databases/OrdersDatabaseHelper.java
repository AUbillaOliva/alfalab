package cl.alfa.alfalab.utils.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import cl.alfa.alfalab.MainActivity;
import cl.alfa.alfalab.models.Orders;

public class OrdersDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "download.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteOpenHelper dbhandler = null;
    private SQLiteDatabase db;

    public OrdersDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() { db = dbhandler.getWritableDatabase(); }

    public void close() { dbhandler.close(); }

    public boolean exists(String key){

        final String[] projection = {
                OrdersContract.OrdersEntry._ID,
                OrdersContract.OrdersEntry.COLUMN_ORDER_ID,
                OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER,
                OrdersContract.OrdersEntry.COLUMN_ORDER_COMMENTARY,
                OrdersContract.OrdersEntry.COLUMN_ORDER_RESPONSIBLE,
                OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKIN,
                OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKOUT,
                OrdersContract.OrdersEntry.COLUMN_ORDER_TYPE,
                OrdersContract.OrdersEntry.COLUMN_ORDER_STATUS,
                OrdersContract.OrdersEntry.COLUMN_ORDER_PRICE,
                OrdersContract.OrdersEntry.COLUMN_ORDER_CLIENT_ID
        };
        final String selection = OrdersContract.OrdersEntry.COLUMN_ORDER_ID + " =?";
        final String[] selectionArgs = { key };
        final String limit = "1";

        db = this.getWritableDatabase();

        final Cursor cursor = db.query(OrdersContract.OrdersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null, limit);
        final boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        final String SQL_CREATE_DOWNLOAD_TABLE = "CREATE TABLE " + OrdersContract.OrdersEntry.TABLE_NAME + " (" +
                OrdersContract.OrdersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                OrdersContract.OrdersEntry.COLUMN_ORDER_ID + " TEXT NOT NULL, " +
                OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER + " INTEGER NOT NULL, " +
                OrdersContract.OrdersEntry.COLUMN_ORDER_COMMENTARY + " TEXT, " +
                OrdersContract.OrdersEntry.COLUMN_ORDER_RESPONSIBLE + " TEXT NOT NULL, " +
                OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKIN + " TEXT NOT NULL, " +
                OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKOUT + " TEXT, " +
                OrdersContract.OrdersEntry.COLUMN_ORDER_TYPE + " TEXT," +
                OrdersContract.OrdersEntry.COLUMN_ORDER_STATUS + " INTEGER," +
                OrdersContract.OrdersEntry.COLUMN_ORDER_PRICE + " INTEGER," +
                OrdersContract.OrdersEntry.COLUMN_ORDER_CLIENT_ID + " TEXT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_DOWNLOAD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int il){

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OrdersContract.OrdersEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void addOrder(Orders order){

        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues values = new ContentValues();

        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER, order.getNumber()); // NOT NULL
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_COMMENTARY, order.getCommentaries());
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_RESPONSIBLE, order.getResponsible()); // NOT NULL
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKIN, order.getCheckin()); // NOT NULL
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKOUT, order.getCheckin());
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_TYPE, order.getType());
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_STATUS, order.getStatus());
        values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_PRICE, order.getPrice());

        db.insert(OrdersContract.OrdersEntry.TABLE_NAME, null, values);
        db.close();

    }

    public void addOrders(ArrayList<Orders> orders){

        final SQLiteDatabase db = this.getWritableDatabase();
        final ContentValues values = new ContentValues();

        for(Orders order : orders){
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER, order.getNumber()); // NOT NULL
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_COMMENTARY, order.getCommentaries());
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_RESPONSIBLE, order.getResponsible()); // NOT NULL
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKIN, order.getCheckin()); // NOT NULL
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKOUT, order.getCheckin());
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_TYPE, order.getType());
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_STATUS, order.getStatus());
            values.put(OrdersContract.OrdersEntry.COLUMN_ORDER_PRICE, order.getPrice());
            db.insert(OrdersContract.OrdersEntry.TABLE_NAME, null, values);
        }
        db.close();

    }

    public void deleteOrder(String number){

        final SQLiteDatabase db = this.getWritableDatabase();
        db.delete(OrdersContract.OrdersEntry.TABLE_NAME, OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER + "='" + number + "'", null);

    }

    public ArrayList<Orders> getAllOrders(){

        final String[] columns = {
                OrdersContract.OrdersEntry._ID,
                OrdersContract.OrdersEntry.COLUMN_ORDER_ID,
                OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER,
                OrdersContract.OrdersEntry.COLUMN_ORDER_COMMENTARY,
                OrdersContract.OrdersEntry.COLUMN_ORDER_RESPONSIBLE,
                OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKIN,
                OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKOUT,
                OrdersContract.OrdersEntry.COLUMN_ORDER_TYPE,
                OrdersContract.OrdersEntry.COLUMN_ORDER_STATUS,
                OrdersContract.OrdersEntry.COLUMN_ORDER_PRICE,
                OrdersContract.OrdersEntry.COLUMN_ORDER_CLIENT_ID
        };

        final String sortOrder =
                OrdersContract.OrdersEntry._ID + " ASC";
        final ArrayList<Orders> ordersList = new ArrayList<>();

        final SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor = db.query(OrdersContract.OrdersEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                final Orders order = new Orders();
                order.setNumber(cursor.getInt(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_NUMBER)));
                order.setCommentaries(cursor.getString(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_COMMENTARY)));
                order.setResponsible(cursor.getString(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_RESPONSIBLE)));
                order.setCheckin(cursor.getString(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKIN)));
                order.setCheckout(cursor.getString(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_CHECKOUT)));
                order.setType(cursor.getString(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_TYPE)));
                order.setStatus(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_STATUS))));
                order.setPrice(cursor.getInt(cursor.getColumnIndex(OrdersContract.OrdersEntry.COLUMN_ORDER_PRICE)));

                ordersList.add(order);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return ordersList;

    }

}
