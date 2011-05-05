package com.geotasks.database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.text.*;
import android.util.*;

import com.geotasks.provider.*;

public class SQLiteDatabaseService implements DatabaseService
{
  private DatabaseHelper dbHelper;
  
  public SQLiteDatabaseService(Context ctx)
  {
    dbHelper = new DatabaseHelper(ctx);
  }

  public int deleteTask(String selection, String[] args)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.delete(Tasks.SQL.TABLE_NAME, selection, args);
  }

  public int deleteTaskId(String taskId, String selection, String[] args)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.delete(Tasks.SQL.TABLE_NAME, Tasks._ID + "=" + taskId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), args);
  }
  
  public int deletePlace(String selection, String[] args)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.delete(Places.SQL.TABLE_NAME, selection, args);
  }
  
  public int deletePlaceId(String placeId, String selection, String[] args)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.delete(Places.SQL.TABLE_NAME, Places._ID + "=" + placeId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), args);
  }

  public long createTask(ContentValues values)
  {
    values = normalize(values);
    String tableName;
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
    tableName = Tasks.SQL.TABLE_NAME;
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.insert(tableName, null, values);
  }
  
  public long createPlace(ContentValues values)
  {
    values = normalize(values);
    String tableName = Places.SQL.TABLE_NAME;
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.insert(tableName, null, values);
  }
  
  private ContentValues normalize(ContentValues initialValues)
  {
    return (initialValues != null) ? new ContentValues(initialValues) : new ContentValues();
  }

  public Cursor getTaskById(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.appendWhere(Tasks._ID + "=" + id);
    query.setTables(Tasks.SQL.TABLE_NAME);
    query.setProjectionMap(Tasks.PROJECTION_MAP);
    String orderBy = TextUtils.isEmpty(sortOrder) ? Tasks.DEFAULT_SORT_ORDER : sortOrder;
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public Cursor getAllTasks(String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.setTables(Tasks.SQL.TABLE_NAME);
    query.setProjectionMap(Tasks.PROJECTION_MAP);
    String orderBy = TextUtils.isEmpty(sortOrder) ? Tasks.DEFAULT_SORT_ORDER : sortOrder;
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public Cursor getPlaceById(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.appendWhere(Places._ID + "=" + id);
    query.setTables(Places.SQL.TABLE_NAME);
    query.setProjectionMap(Places.PROJECTION_MAP);
    String orderBy = TextUtils.isEmpty(sortOrder) ? Places.DEFAULT_SORT_ORDER : sortOrder;
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public Cursor getAllPlaces(String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.setTables(Places.SQL.TABLE_NAME);
    query.setProjectionMap(Places.PROJECTION_MAP);
    String orderBy = TextUtils.isEmpty(sortOrder) ? Places.DEFAULT_SORT_ORDER : sortOrder;
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public int updateTaskId(String taskId, ContentValues values, String selection, String[] selectionArgs)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.update(Tasks.SQL.TABLE_NAME, values, Tasks._ID + "=" + taskId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs); 
  }

  public int updateTask(ContentValues values, String selection, String[] selectionArgs)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase(); 
    return db.update(Tasks.SQL.TABLE_NAME, values, selection, selectionArgs);
  }

  public int updatePlaceId(String placeId, ContentValues values, String selection, String[] selectionArgs)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.update(Places.SQL.TABLE_NAME, values, Places._ID + "=" + placeId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
  }

  public int updatePlace(ContentValues values, String selection, String[] selectionArgs)
  {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    return db.update(Places.SQL.TABLE_NAME, values, selection, selectionArgs);
  }
  
  // TODO Create appendIfPresent(String) for AND clauses
  // TODO Create sortOrderOrDefault

  private static class DatabaseHelper extends SQLiteOpenHelper
  {
    DatabaseHelper(Context context)
    {
      super(context, Database.NAME, Database.DEFAULT_CURSOR_FACTORY, Database.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
      db.execSQL(Tasks.SQL.CREATE_TABLE);
      db.execSQL(Places.SQL.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
      Log.w("GeoTasksProvider", "Upgrading database from version " + oldVersion + " to " + newVersion);
      db.execSQL(Tasks.SQL.DROP_TABLE);
      db.execSQL(Places.SQL.DROP_TABLE);
      onCreate(db);
    }
  }
}
