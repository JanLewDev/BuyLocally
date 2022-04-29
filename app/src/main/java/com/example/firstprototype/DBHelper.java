package com.example.firstprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ID = "ID";

    public DBHelper(@Nullable Context context) {
        super(context, "customers.db", null, 1);
    }


    // this is called the first time database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRSTNAME + " TEXT, " + COLUMN_SURNAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT)";

        db.execSQL(createTableStatement);
        //myDB.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , FIRSTNAME TEXT , SURNAME TEXT , EMAIL TEXT , PASSWORD TEXT) ");
    }

    // this is called whenever the version number of the database changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE " + CUSTOMER_TABLE);
        onCreate(db);

    }

    // registration
    public boolean addCustomer(CustomerModel customer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRSTNAME, customer.getFirstName());
        cv.put(COLUMN_SURNAME, customer.getSurname());
        cv.put(COLUMN_EMAIL, customer.getEmail());
        cv.put(COLUMN_PASSWORD, customer.getPassword());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        db.close();

        if(insert == -1){
            return false;
        }
        return true;
    }


    // get a list of all accounts with given email
    public List<CustomerModel> getAllMatching(String checkedEmail){

        List<CustomerModel> retList = new ArrayList<>();

        // get the data from the database
        String selectStatement = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_EMAIL + " = '" + checkedEmail + "'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()){
            // loop through the result and create new customers and add them to the list
            do {
                int customerID = cursor.getInt(0);
                String customerFirstName = cursor.getString(1);
                String customerSurname = cursor.getString(2);
                String customerEmail = cursor.getString(3);
                String customerPassword = cursor.getString(4);

                // create the customer object and add it to the list

                CustomerModel newCustomer = new CustomerModel(customerID, customerFirstName, customerSurname, customerEmail, customerPassword);
                retList.add(newCustomer);

            } while (cursor.moveToNext());

        } else {
            // the list will just be empty - no matching results
        }

        // close both the database and the cursor
        cursor.close();
        db.close();
        return retList;
    }

//    // registration handler
//    public boolean registerUser(String firstName, String surname, String email, String password){
//
//        SQLiteDatabase myDB = this.getWritableDatabase();
//        ContentValues content = new ContentValues();
//        content.put(COL_2, firstName);
//        content.put(COL_3, surname);
//        content.put(COL_4, email);
//        content.put(COL_5, password);
//
//        long result = myDB.insert(TABLE_NAME, null, content);
//
//        if(result == -1)
//            return false;
//        return true;
//    }
//
//    // login handler
//
//    public Cursor loginUser(String email, String password){
//        SQLiteDatabase myDB = this.getWritableDatabase();
//
//        Cursor result = myDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_EMAIL + "=' " + email + " 'AND " + COLUMN_PASSWORD + "=' " + password + " ' ", null);
//        return result;
//
//    }
}
