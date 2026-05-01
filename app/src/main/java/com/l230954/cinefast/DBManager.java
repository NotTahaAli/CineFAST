package com.l230954.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBManager {

    public static final String DATABASE_NAME = "SnacksDB";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "snacks";
    public static final String COLUMN_ID = "snack_id";
    public static final String COLUMN_NAME = "snack_name";
    public static final String COLUMN_PRICE = "snack_price";
    public static final String COLUMN_IMAGE = "snack_image";
    public static final String COLUMN_DESCRIPTION = "snack_description";
    Context context;
    DBHelper helper;
    public DBManager(Context context)
    {
        this.context = context;
    }

    private long addSnack(Snack snack, SQLiteDatabase writeDb) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, snack.name);
        cv.put(COLUMN_PRICE, snack.price);
        cv.put(COLUMN_DESCRIPTION, snack.description);
        cv.put(COLUMN_IMAGE, snack.imageId);
        return writeDb.insert(TABLE_NAME, null, cv);
    }

    public long addSnack(Snack snack)
    {
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        long count = addSnack(snack, writeDb);
        writeDb.close();
        return count;
    }

    public ArrayList<Snack> getSnacks()
    {
        SQLiteDatabase readDb = helper.getReadableDatabase();
        Cursor cursor = readDb.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Snack> snacks = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            int index_name = cursor.getColumnIndex(COLUMN_NAME);
            int index_price = cursor.getColumnIndex(COLUMN_PRICE);
            int index_image = cursor.getColumnIndex(COLUMN_IMAGE);
            int index_description = cursor.getColumnIndex(COLUMN_DESCRIPTION);

            do {
                String name = cursor.getString(index_name);
                String description = cursor.getString(index_description);
                float price = cursor.getFloat(index_price);
                Integer resourceId = cursor.getInt(index_image);
                snacks.add(new Snack(name, price, description, resourceId));
            }while (cursor.moveToNext());
        }
        cursor.close();
        readDb.close();
        return snacks;
    }

    public void open()
    {
        helper = new DBHelper(context);
    }

    public void close()
    {
        helper.close();
    }

    private class DBHelper extends SQLiteOpenHelper
    {
        public DBHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // schema create   (you have changed the schema here)
            String query = "CREATE TABLE "+TABLE_NAME+"("
                    +COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    +COLUMN_NAME+" TEXT, "
                    +COLUMN_DESCRIPTION+" TEXT, "
                    +COLUMN_IMAGE+" INT, "
                    +COLUMN_PRICE+" REAL)";
            sqLiteDatabase.execSQL(query);

            ArrayList<Snack> snacks = new ArrayList<>();
            snacks.add(new Snack("Popcorn", 5.0f, "Large/Buttered", R.drawable.snacks1));
            snacks.add(new Snack("Nachos", 10.0f, "With Cheese Dip", R.drawable.snacks2));
            snacks.add(new Snack("Soft Drink", 15.0f, "Large/Any Flavor", R.drawable.snacks3));
            snacks.add(new Snack("Fries", 2.5f, "Large", R.drawable.snacks4));

            for (Snack snack : snacks) {
                addSnack(snack, sqLiteDatabase);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // upgrade
            // backup
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }



}