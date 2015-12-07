/**
 * Created by chaoya on 11/4/15.
 * How to use:
 *  DatabaseHelper db = new DatabaseHelper(this, null, null, 1);
 *  Then db.method()
 */

package cmu.banana.classdiscuz.ws.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.entities.User;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "ClassDiscuz";
    private static final String TABLE_USER = "user";
    private static final String TABLE_COURSE = "course";

    private static final String KEY_ID = "ID";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_PASSWORD = "PASSWORD";
    private static final String KEY_NAME = "NAME";
    private static final String KEY_AVATAR = "AVATAR";
    private static final String KEY_COLLEGE = "COLLEGE";
    private static final String KEY_MAJOR = "MAJOR";
    private static final String KEY_FOCUS = "FOCUS";
    private static final String KEY_CHATID = "CHATID";

    private static final String KEY_C_ID = "cID";
    private static final String KEY_U_ID = "uID";
    private static final String KEY_NUM = "NUM";
    private static final String KEY_INSTRUCTOR = "INSTRUCTOR";
    private static final String KEY_C_NAME = "cNAME";
    private static final String KEY_TIME = "TIME";
    private static final String KEY_LOCATION = "LOCATION";
    private static final String KEY_DIALOGID = "DIALOGID";

    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    } // end DatabaseOpenHelper constructor

    // creates the contacts table when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // query to create a new table named contacts
        String createQuery = "CREATE TABLE " + TABLE_USER + "(" +
                KEY_ID + " INTEGER primary key," +
                KEY_EMAIL + " TEXT," +
                KEY_PASSWORD + " TEXT," +
                KEY_NAME + " TEXT," +
                KEY_AVATAR + " BLOB," +
                KEY_COLLEGE + " TEXT," +
                KEY_MAJOR + " TEXT," +
                KEY_FOCUS + " INTEGER," +
                KEY_CHATID + " INTEGER);";

        db.execSQL(createQuery); // execute the query

        createQuery = "CREATE TABLE " + TABLE_COURSE + "(" +
                KEY_C_ID + " INTEGER primary key," +
                KEY_U_ID + " INTEGER," +
                KEY_NUM + " TEXT," +
                KEY_INSTRUCTOR + " TEXT," +
                KEY_C_NAME + " TEXT," +
                KEY_TIME + " TEXT," +
                KEY_LOCATION + " TEXT, " +
                KEY_DIALOGID + " INTEGER, " +
                "FOREIGN KEY(" + KEY_U_ID + ") REFERENCES " + TABLE_USER + "("+ KEY_ID +"));";

        db.execSQL(createQuery); // execute the query

    } // end method onCreate

    // Upgrading database

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
        // Create tables again
        onCreate(db);
    }

    public void insertUser(User newUser, int chatID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues result = new ContentValues();

        result.put(KEY_ID, newUser.getId());
        result.put(KEY_EMAIL, newUser.getEmail());
        result.put(KEY_PASSWORD, newUser.getPassword());
        result.put(KEY_NAME, newUser.getName());
        result.put(KEY_AVATAR, newUser.getAvatar());
        result.put(KEY_COLLEGE, newUser.getCollege());
        result.put(KEY_MAJOR, newUser.getMajor());
        result.put(KEY_FOCUS, newUser.getFocus());
        result.put(KEY_CHATID, chatID);

        db.insert(TABLE_USER, null, result);
        db.close(); // Closing database connection
    } // end method insertContact

    public void addCourse(Course course, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues result = new ContentValues();

        result.put(KEY_C_ID, course.getId());
        result.put(KEY_U_ID, userID);
        result.put(KEY_NUM, course.getNum());
        result.put(KEY_INSTRUCTOR, course.getInstructor());
        result.put(KEY_C_NAME, course.getName());
        result.put(KEY_TIME, course.getName());
        result.put(KEY_LOCATION, course.getLocation());
        result.put(KEY_LOCATION, course.getDialogId());

        db.insert(TABLE_COURSE, null, result);
        db.close(); // Closing database connection
    }

    public void dropCourse(String course_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COURSE + " WHERE " + KEY_NUM + "= '" + course_id + "'");
        db.close();

    }

    public int updateProfile(int id, User newUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues result = new ContentValues();

        result.put(KEY_ID, newUser.getId());
        result.put(KEY_EMAIL, newUser.getEmail());
        result.put(KEY_PASSWORD, newUser.getPassword());
        result.put(KEY_NAME, newUser.getName());
        result.put(KEY_AVATAR, newUser.getAvatar());
        result.put(KEY_COLLEGE, newUser.getCollege());
        result.put(KEY_MAJOR, newUser.getMajor());
        result.put(KEY_FOCUS, newUser.getFocus());
        result.put(KEY_CHATID, newUser.getChatId());
        db.update(TABLE_USER, result, "ID " + "=" + id, null);
        return 0;
    }

    public int updateFocus(int studentId, int focus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues result = new ContentValues();

        result.put(KEY_FOCUS, focus);

        db.update(TABLE_USER, result, "ID " + "=" + studentId, null);

        return 0;
    }

    public User getMemberByID(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                KEY_ID,
                KEY_EMAIL,
                KEY_PASSWORD,
                KEY_NAME,
                KEY_AVATAR,
                KEY_COLLEGE,
                KEY_MAJOR,
                KEY_FOCUS,
                KEY_CHATID
        };

        Cursor cursor = db.query(
                TABLE_USER,                                 // The table to query
                projection,                                 // The columns to return
                KEY_ID + "=?",                              // The columns for the WHERE clause
                new String[]{String.valueOf(userID)},    // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                        // The sort order
        );
        cursor.moveToFirst();
//        String q1 = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
//        Log.i("chaoya", q1);

        User user = new User(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                cursor.getBlob(cursor.getColumnIndexOrThrow(KEY_AVATAR)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_COLLEGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_MAJOR)),
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_FOCUS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CHATID))
                );
        db.close();
        return user;
    }



    // Getting All Contacts
    public ArrayList<Course> getAllResults() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_COURSE;
        ArrayList<Course> result = new ArrayList<Course>();
        Course course;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                course = new Course(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_C_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_NUM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_INSTRUCTOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_C_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DIALOGID)));
                result.add(course);
                Log.i("chaoya", cursor.getString(cursor.getColumnIndexOrThrow(KEY_C_NAME)));
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return result;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COURSE);
        db.execSQL("DELETE FROM " + TABLE_USER);
        db.close();
    }
}