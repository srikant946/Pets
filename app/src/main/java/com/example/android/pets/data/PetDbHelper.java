package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.pets.data.PetContract.PetEntry;

// This class provides connection to database and returns the Datatbase Object.
// It also creates a DB if it doesnt exist.
/**
 * Database helper for Pets app. Manages database creation and version management.
 * Own class created and the onCreate() and onUpgrade() is implemented because SQLiteOpenHelper is an ABSTRACT Class
 */
public class PetDbHelper extends SQLiteOpenHelper
{
    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "shelter.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link PetDbHelper} by constructor creation
     *
     * @param context of the app
     */
    public PetDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     * It takes in the Database object as the input parameter. That Database Object represents our entire Database
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // It wont accept the SQL Statement as it is, we would have to convert it to a String for implementation in this method.
        // Create a String that contains the SQL statement to create the pets table
        // Instead of using normal column names like '_id', 'breed' etc, we pass in Constants here that were defined in PetContract.java File
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement via the execSQL() method.
        // This method simply takes in the Constant where in we have defined the statement and hence we DONT USE it with SELECT Statement, because they are intended to return some data whereas execSQL() is NOT Intended to do so.
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     * This method would simply DROP the database and RECREATE in once the execSQL() is again passed within there
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}