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

    // for the customers
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
    public static final String COLUMN_SURNAME = "SURNAME";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_ID = "ID";

    // for the producers
    public static final String PRODUCER_TABLE = "PRODUCER_TABLE";
    public static final String COLUMN_COMPANY_NAME = "COMPANY";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_LOCATION = "LOCATION";

    // for the link table
    public static final String LINK_TABLE = "LINK_TABLE";
    public static final String COLUMN_CUSTOMER_ID = "CUSTOMERID";
    public static final String COLUMN_PRODUCER_ID = "PRODUCERID";

    // constructor of the class
    public DBHelper(@Nullable Context context) {
        super(context, "customers.db", null, 1);
    }


    // this is called the first time database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create the SQL statement of creating a table
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRSTNAME + " TEXT, "
                + COLUMN_SURNAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";

        // execute the statement
        db.execSQL(createTableStatement);

        String createProducerTableStatement = "CREATE TABLE " + PRODUCER_TABLE +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_COMPANY_NAME + " TEXT, "
                + COLUMN_FIRSTNAME + " TEXT, "
                + COLUMN_SURNAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_LOCATION + " TEXT)";

        db.execSQL(createProducerTableStatement);

        String createLinkTableStatement = "CREATE TABLE LINK_TABLE(" +
                "CUSTOMERID INTEGER, " +
                "PRODUCERID INTEGER, " +
                "FOREIGN KEY(CUSTOMERID) REFERENCES CUSTOMER_TABLE(ID), " +
                "FOREIGN KEY(PRODUCERID) REFERENCES PRODUCER_TABLE(ID), " +
                "PRIMARY KEY(CUSTOMERID, PRODUCERID))";

        db.execSQL(createLinkTableStatement);

    }

    // this is called whenever the version number of the database changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE " + CUSTOMER_TABLE);
        db.execSQL("DROP TABLE " + PRODUCER_TABLE);
        db.execSQL("DROP TABLE " + LINK_TABLE);
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

    //---------------- for producers --------------------//

    // creating the company record
    public boolean addProducer(ProducerModel producer){

        // get the access to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // insert the data to content values
        cv.put(COLUMN_COMPANY_NAME, producer.getCompanyName());
        cv.put(COLUMN_FIRSTNAME, producer.getFirstName());
        cv.put(COLUMN_SURNAME, producer.getSurname());
        cv.put(COLUMN_DESCRIPTION, producer.getDescription());
        cv.put(COLUMN_TYPE, producer.getType());
        cv.put(COLUMN_LOCATION, producer.getLocation());

        // add the record to the database and get the result
        long insert = db.insert(PRODUCER_TABLE, null, cv);
        db.close();

        // return the correct boolean result
        if(insert == -1){
            return false;
        }
        return true;
    }

    // get a list of all producers
    public List<ProducerModel> getAllProducers(){
        List<ProducerModel> retList = new ArrayList<>();
        // get the data from the database
        String selectStatement = "SELECT * FROM " + PRODUCER_TABLE;

        // get access to the database
        SQLiteDatabase db = this.getReadableDatabase();
        // do a query to search the database
        Cursor cursor = db.rawQuery(selectStatement, null);
        if(cursor.moveToFirst()){
            // loop through the result and create new objects and add them to the list
            do {
                // convert all cursor values to their types
                int id = cursor.getInt(0);
                String companyName = cursor.getString(1);
                String firstName = cursor.getString(2);
                String surname = cursor.getString(3);
                String description = cursor.getString(4);
                String type = cursor.getString(5);
                String location = cursor.getString(6);

                ProducerModel newProducer = new ProducerModel(id, companyName, firstName, surname, description, type, location);
                retList.add(newProducer);

            } while (cursor.moveToNext());
        } else {
            // the list will just be empty - no matching results
        }
        // close both the database and the cursor
        cursor.close();
        db.close();
        return retList;
    }

    // get a list of all producers with given name or ID
    public List<ProducerModel> getProducerByNameOrID(String checkedName, int ID){
        List<ProducerModel> retList = new ArrayList<>();
        String selectStatement = "";
        // decide which statement to use
        if(ID == -1) {
            // get the data from the database
            selectStatement = "SELECT * FROM " + PRODUCER_TABLE + " WHERE " + COLUMN_COMPANY_NAME + " = '" + checkedName + "'";
        } else {
            selectStatement = "SELECT * FROM " + PRODUCER_TABLE + " WHERE " + COLUMN_ID + " = '" + ID + "'";
        }
        // get access to the database
        SQLiteDatabase db = this.getReadableDatabase();

        // do a query to search the database
        Cursor cursor = db.rawQuery(selectStatement, null);
        if(cursor.moveToFirst()){
            // loop through the result and create new customers and add them to the list
            do {
                int id = cursor.getInt(0);
                String companyName = cursor.getString(1);
                String firstName = cursor.getString(2);
                String surname = cursor.getString(3);
                String description = cursor.getString(4);
                String type = cursor.getString(5);
                String location = cursor.getString(6);

                ProducerModel newProducer = new ProducerModel(id, companyName, firstName, surname, description, type, location);
                retList.add(newProducer);
            } while (cursor.moveToNext());
        } else {
            // the list will just be empty - no matching results
        }
        // close both the database and the cursor
        cursor.close();
        db.close();
        return retList;
    }

    //------------------- for the link table -----------------------//

    public boolean addToFavourites(CustomerModel customer, ProducerModel producer){

        // get the access to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        // insert the data to content values
        cv.put(COLUMN_CUSTOMER_ID, customer.getId());
        cv.put(COLUMN_PRODUCER_ID, producer.getId());

        // add the record to the database and get the result
        long insert = db.insert(LINK_TABLE, null, cv);
        db.close();

        // return the correct boolean result
        if(insert == -1){
            return false;
        }
        return true;
    }

    // get all producers which this user added to their favourites
    public List<Integer> getAllFavourites(CustomerModel customer){
        List<Integer> retList = new ArrayList<>();

        // get the data from the database
        String selectStatement = "SELECT * FROM " + LINK_TABLE + " WHERE " + COLUMN_CUSTOMER_ID + " = '" + customer.getId() + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectStatement, null);
        if(cursor.moveToFirst()){
            // loop through the result and create new customers and add them to the list
            do {
                int customerID = cursor.getInt(0);
                int producerID = cursor.getInt(1);
                retList.add(producerID);
            } while (cursor.moveToNext());
        } else {
            // the list will just be empty - no matching results
        }
        // close both the database and the cursor
        cursor.close();
        db.close();
        return retList;
    }

    public boolean removeFromFavourites(CustomerModel customer, ProducerModel producer){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = db.delete(LINK_TABLE, COLUMN_CUSTOMER_ID + "=" + customer.getId()
                + " AND " + COLUMN_PRODUCER_ID + "=" + producer.getId(), null) > 0;
        db.close();
        return result;
    }

}
