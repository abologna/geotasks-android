package com.geotasks.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class GeoTasksProvider extends ContentProvider {
  private static final String TAG = "GeoTasksProvider";

  private static final String DATABASE_NAME = "geotasks.db";
  private static final int DATABASE_VERSION = 1;
  private static final String TASKS_TABLE_NAME = "tasks";
  private static final String PLACES_TABLE_NAME = "places";

  private static final UriMatcher uriMatcher;
  private static final String AUTHORITY = "com.geotasks.provider.geotasksprovider";
  private static final int PLACES = 1;
  private static final int PLACE_ID = 2;
  private static final int TASKS = 3;
  private static final int TASK_ID = 4;

  private static HashMap<String, String> tasksProjectionMap;
  private static HashMap<String, String> placesProjectionMap;

  private static class DatabaseHelper extends SQLiteOpenHelper {

    DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("CREATE TABLE " + TASKS_TABLE_NAME + " (" + Tasks._ID
              + " INTEGER PRIMARY KEY," + Tasks.NAME + " TEXT," + Tasks.COMPLETED + " BOOLEAN,"
              + Tasks.PLACE_ID + " INTEGER," + Tasks.DESCRIPTION + " TEXT,"
              + Tasks.CREATED_DATE + " INTEGER," + Tasks.MODIFIED_DATE + " INTEGER);");

      db.execSQL("CREATE TABLE " + PLACES_TABLE_NAME + " (" + Places._ID
              + " INTEGER PRIMARY KEY," + Places.NAME + " TEXT,"
              + Places.LATITUDE + " DECIMAL," + Places.LONGITUDE + " DECIMAL);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
              + newVersion + ", which will destroy all old data");
      db.execSQL("DROP TABLE IF EXISTS tasks");
      db.execSQL("DROP TABLE IF EXISTS places");
      onCreate(db);
    }
  }

  private DatabaseHelper databaseHelper;

  @Override
  public boolean onCreate() {
    databaseHelper = new DatabaseHelper(getContext());
    return true;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = databaseHelper.getWritableDatabase();  
    int count;
    switch (uriMatcher.match(uri)) {
      case TASKS:
        count = db.delete(TASKS_TABLE_NAME, selection, selectionArgs);
        break;
      case TASK_ID:
        String taskId = uri.getPathSegments().get(1);
        count = db.delete(TASKS_TABLE_NAME, Tasks._ID + "=" + taskId
            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;
      case PLACES:
        count = db.delete(PLACES_TABLE_NAME, selection, selectionArgs);
        break;
      case PLACE_ID:
        String placeId = uri.getPathSegments().get(1);
        count = db.delete(PLACES_TABLE_NAME, Places._ID + "=" + placeId
            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case TASKS:
        return Tasks.CONTENT_TYPE;
      case TASK_ID:
        return Tasks.CONTENT_ITEM_TYPE;
      case PLACES:
        return Places.CONTENT_TYPE;
      case PLACE_ID:
        return Places.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues initialValues) {

    ContentValues values;
    if (initialValues != null) {
      values = new ContentValues(initialValues);
    } else {
      values = new ContentValues();
    }
    String tableName;
    Uri contentUri;
    switch (uriMatcher.match(uri)) {
      case TASKS:
        Long now = Long.valueOf(System.currentTimeMillis());
        if (values.containsKey(Tasks.CREATED_DATE) == false) {
          values.put(Tasks.CREATED_DATE, now);
        }
        if (values.containsKey(Tasks.MODIFIED_DATE) == false) {
          values.put(Tasks.MODIFIED_DATE, now);
        }
        if (values.containsKey(Tasks.COMPLETED) == false) {
          values.put(Tasks.COMPLETED, "false");
        }
        tableName = TASKS_TABLE_NAME;
        contentUri = Tasks.CONTENT_URI;
        break;
      case PLACES:
        tableName = PLACES_TABLE_NAME;
        contentUri = Places.CONTENT_URI;
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    SQLiteDatabase db = databaseHelper.getWritableDatabase();
    long rowId = db.insert(tableName, null, values);
    if (rowId > 0) {
      Uri itemUri = ContentUris.withAppendedId(contentUri, rowId);
      getContext().getContentResolver().notifyChange(itemUri, null);
      return itemUri;
    }
    throw new SQLException("Failed to insert row into " + uri);
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
          String[] selectionArgs, String sortOrder) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
    String orderBy;

    switch (uriMatcher.match(uri)) {
      case TASK_ID:
        qb.appendWhere(Tasks._ID + "=" + uri.getPathSegments().get(1));
        // intentionally missing break
      case TASKS:
        qb.setTables(TASKS_TABLE_NAME);
        qb.setProjectionMap(tasksProjectionMap);
        orderBy = TextUtils.isEmpty(sortOrder) ? Tasks.DEFAULT_SORT_ORDER : sortOrder;
        break;
      case PLACE_ID:
        qb.appendWhere(Places._ID + "=" + uri.getPathSegments().get(1));
        // intentionally missing break
      case PLACES:
        qb.setTables(PLACES_TABLE_NAME);
        qb.setProjectionMap(placesProjectionMap);
        orderBy = TextUtils.isEmpty(sortOrder) ? Places.DEFAULT_SORT_ORDER : sortOrder;
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);

    // Tell the cursor what uri to watch, so it knows when its source data changes.
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;

  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
          String[] selectionArgs) { 
    SQLiteDatabase db = databaseHelper.getWritableDatabase();  
    int count;
    switch (uriMatcher.match(uri)) {
      case TASKS:
        count = db.update(TASKS_TABLE_NAME, values, selection, selectionArgs);
        break;
      case TASK_ID:
        String taskId = uri.getPathSegments().get(1);
        count = db.update(TASKS_TABLE_NAME, values, Tasks._ID + "=" + taskId
            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;
      case PLACES:
        count = db.update(PLACES_TABLE_NAME, values, selection, selectionArgs);
        break;
      case PLACE_ID:
        String placeId = uri.getPathSegments().get(1);
        count = db.update(PLACES_TABLE_NAME, values, Places._ID + "=" + placeId
            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(AUTHORITY, "places", PLACES);
    uriMatcher.addURI(AUTHORITY, "places/#", PLACE_ID);
    uriMatcher.addURI(AUTHORITY, "tasks", TASKS);
    uriMatcher.addURI(AUTHORITY, "tasks/#", TASK_ID);

    tasksProjectionMap = new HashMap<String, String>();
    tasksProjectionMap.put(Tasks._ID, Tasks._ID);
    tasksProjectionMap.put(Tasks.NAME, Tasks.NAME);
    tasksProjectionMap.put(Tasks.PLACE_ID, Tasks.PLACE_ID);
    tasksProjectionMap.put(Tasks.CREATED_DATE, Tasks.CREATED_DATE);
    tasksProjectionMap.put(Tasks.MODIFIED_DATE, Tasks.MODIFIED_DATE);
    tasksProjectionMap.put(Tasks.COMPLETED, Tasks.COMPLETED);
    tasksProjectionMap.put(Tasks.DESCRIPTION, Tasks.DESCRIPTION);

    placesProjectionMap = new HashMap<String, String>();
    placesProjectionMap.put(Places._ID, Places._ID);
    placesProjectionMap.put(Places.NAME, Places.NAME);
    placesProjectionMap.put(Places.LONGITUDE, Places.LONGITUDE);
    placesProjectionMap.put(Places.LATITUDE, Places.LATITUDE);
  }

}
