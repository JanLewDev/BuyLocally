package com.example.firstprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


// this class implements the SQLite database

public class DBHelper extends SQLiteOpenHelper {


    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ID = "ID";

    // constructor of the class
    public DBHelper(@Nullable Context context) {
        super(context, "customers.db", null, 1);
    }


    // this is called the first time database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create the SQL statement of creating a table
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FIRSTNAME + " TEXT, " + COLUMN_SURNAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PASSWORD + " TEXT)";

        // execute the statement
        db.execSQL(createTableStatement);

    }

    // this is called whenever the version number of the database changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE " + CUSTOMER_TABLE);
        onCreate(db);

    }

    // registration
    public boolean addCustomer(CustomerModel customer){

        // get the access to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // insert the data to content values
        cv.put(COLUMN_FIRSTNAME, customer.getFirstName());
        cv.put(COLUMN_SURNAME, customer.getSurname());
        cv.put(COLUMN_EMAIL, customer.getEmail());
        cv.put(COLUMN_PASSWORD, customer.getPassword());


        // add the record to the database and get the result
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        db.close();

        // return the correct boolean result
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

        // get access to the database
        SQLiteDatabase db = this.getReadableDatabase();

        // do a query to search the database
        Cursor cursor = db.rawQuery(selectStatement, null);

        if(cursor.moveToFirst()){
            // loop through the result and create new customers and add them to the list
            do {
                // convert all cursor values to their types
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

    // changing password when forgotten
    public boolean changePassword(String newPassword, int id){

        // get the access to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // insert the data to content values
        cv.put(COLUMN_PASSWORD, newPassword);


        // update the correct record
        long insert = db.update(CUSTOMER_TABLE, cv, COLUMN_ID + "=" + id, null);
        db.close();

        // return the correct boolean result
        if(insert == -1){
            return false;
        }
        return true;

    }


}
