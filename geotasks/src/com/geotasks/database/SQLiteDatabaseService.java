package com.geotasks.database;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

import static com.geotasks.util.StringUtil.*;
import com.geotasks.provider.*;

public class SQLiteDatabaseService implements DatabaseService
{
  private static final String EMPTY_STRING = "";
  private SQLiteHelper dbHelper;
  
  public SQLiteDatabaseService(Context ctx)
  {
    dbHelper = new SQLiteHelper(ctx);
  }

  public int deleteTask(String selection, String[] args)
  {
    return dbHelper.delete(Tasks.SQL.TABLE_NAME, selection, args);
  }

  public int deleteTaskId(String taskId, String selection, String[] args)
  {
    return dbHelper.delete(Tasks.SQL.TABLE_NAME, Tasks._ID + "=" + taskId + use(selection).orDefault(EMPTY_STRING), args);
  }
  
  public int deletePlace(String selection, String[] args)
  {
    return dbHelper.delete(Places.SQL.TABLE_NAME, selection, args);
  }
  
  public int deletePlaceId(String placeId, String selection, String[] args)
  {
    return dbHelper.delete(Places.SQL.TABLE_NAME, Places._ID + "=" + placeId + use(selection).orDefault(EMPTY_STRING), args);
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
    String orderBy = use(sortOrder).orDefault(Tasks.DEFAULT_SORT_ORDER);
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public Cursor getAllTasks(String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.setTables(Tasks.SQL.TABLE_NAME);
    query.setProjectionMap(Tasks.PROJECTION_MAP);
    String orderBy = use(sortOrder).orDefault(Tasks.DEFAULT_SORT_ORDER);
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public Cursor getPlaceById(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.appendWhere(Places._ID + "=" + id);
    query.setTables(Places.SQL.TABLE_NAME);
    query.setProjectionMap(Places.PROJECTION_MAP);
    String orderBy = use(sortOrder).orDefault(Places.DEFAULT_SORT_ORDER);
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public Cursor getAllPlaces(String[] projection, String selection, String[] selectionArgs, String sortOrder)
  {
    SQLiteQueryBuilder query = new SQLiteQueryBuilder();
    query.setTables(Places.SQL.TABLE_NAME);
    query.setProjectionMap(Places.PROJECTION_MAP);
    String orderBy = use(sortOrder).orDefault(Places.DEFAULT_SORT_ORDER);
    SQLiteDatabase db = dbHelper.getReadableDatabase();
    return query.query(db, projection, selection, selectionArgs, null, null, orderBy);
  }

  public int updateTaskId(String taskId, ContentValues values, String selection, String[] selectionArgs)
  {
    return dbHelper.update(Tasks.SQL.TABLE_NAME, values, Tasks._ID + "=" + taskId + use(selection).orDefault(EMPTY_STRING), selectionArgs); 
  }

  public int updateTask(ContentValues values, String selection, String[] selectionArgs)
  {
    return dbHelper.update(Tasks.SQL.TABLE_NAME, values, selection, selectionArgs);
  }

  public int updatePlaceId(String placeId, ContentValues values, String selection, String[] selectionArgs)
  {
    return dbHelper.update(Places.SQL.TABLE_NAME, values, Places._ID + "=" + placeId + use(selection).orDefault(EMPTY_STRING), selectionArgs);
  }

  public int updatePlace(ContentValues values, String selection, String[] selectionArgs)
  {
    return dbHelper.update(Places.SQL.TABLE_NAME, values, selection, selectionArgs);
  }
}
