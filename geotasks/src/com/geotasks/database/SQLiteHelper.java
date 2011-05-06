package com.geotasks.database;

import android.content.*;
import android.database.sqlite.*;
import android.util.*;

import com.geotasks.provider.*;

class SQLiteHelper extends SQLiteOpenHelper
{
  SQLiteHelper(Context context)
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

  public int delete(String table, String selection, String[] args)
  {
    return getWritableDatabase().delete(table, selection, args);
  }

  public int update(String table, ContentValues values, String selection, String[] args)
  {
    return getWritableDatabase().update(table, values, selection, args);
  }
}