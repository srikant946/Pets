/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity
{

    /** Database helper that will provide us access to the database */
    private PetDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new PetDbHelper(this);

//        // Within the onCreate() method, we would call the displayDatabaseInfo() for returning errors if they get encountered while our SQLite Database is queried.
//        displayDatabaseInfo();
//
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo()
    {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        PetDbHelper mDbHelper = new PetDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        // rawQuery() is a method by which we can fetch the data from the database.
        // But, while using the query() method, we would comment out the rawQuery() method and also define a array of projections now
        // Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);

        // The Columns which are to be read and returned
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_WEIGHT
        };

        // query() method for reading from the database and later displaying it via the Textview referenced from 'activity_catalog.xml' File
        Cursor cursor = db.query(PetEntry.TABLE_NAME, projection, null,null,null,null,null,null);
        try
        {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            // TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            // displayView.setText("Number of rows in pets database table: " + cursor.getCount());

            // Displaying all the data present in the cursor object
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);


            // Create a header in the Text View that looks like this:
            // The pets table contains <number of rows in Cursor> pets.
            // _id - name - breed - gender - weight

            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.

            // Get the Textview by its id and simply set its text
            displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");

            // The way to display '_id - name - breed - gender - weight' is mentioned below:
            displayView.append(PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - " +
                    PetEntry.COLUMN_PET_BREED + " - " +
                    PetEntry.COLUMN_PET_GENDER + " - " +
                    PetEntry.COLUMN_PET_WEIGHT + "\n");

            // Figure out the index of each column via the getColumnIndex() method
            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);               // Returns 0
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);   // returns 1
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            // Fetching the values from the cursor object via iteration and then displaying them.
            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext())
            {
                // Use that index to extract the String or Int value of the word at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append("\n" + currentID + " - " + currentName + " - " + currentBreed + " - " + currentGender + " - " + currentWeight);
            }
        }
        finally
        {
            // Always close the cursor when you're done reading from it. This releases all its resources and makes it invalid.
            cursor.close();
        }
    }

    // Method overridden so that the new database status gets shown as soon as we start the app
    @Override
    protected void onStart()
    {
        super.onStart();
        displayDatabaseInfo();
    }

    // OPTIONS/OVERFLOW MENU SECTION
    /*
    For inflating the menu, the method used is onCreateOptionsMenu() which is a method of the AppCompatActivity Class.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertPet()
    {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the pets table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.
        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);
    }

    /*
    For Defining the behavior of the menu item once its clicked, we would override the method 'onOptionsItemSelected()' method of the AppCompatActivity class.
    */
    @Override
    // The (MenuItem item) as passed in the parameter is for accepting the id of the menu item which would be clicked.
    // A Switch case would decide which id was clicked upon and the action would then be mentioned there.
    // The way of identifying id would be by assigning every list item an id in the XML File when its created.
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId())
        {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();

                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
