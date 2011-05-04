package com.geotasks.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Places implements BaseColumns {
  public static final Uri CONTENT_URI
      = Uri.parse("content://com.geotasks.provider.geotasksprovider/places");
 
  public static final String NAME = "name";

  public static final String LATITUDE = "latitude";

  public static final String LONGITUDE = "longitude";
  
  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.geotasks.place";
  
  public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.geotasks.place";

  public static final String DEFAULT_SORT_ORDER = "name ASC";
  
  public static class SQL{
	  public static final String TABLE_NAME = "places";
	  public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" (" + _ID + " INTEGER PRIMARY KEY," 
	  																			+ NAME + " TEXT,"
	  																			+ LATITUDE + " DECIMAL," 
	  																			+ LONGITUDE + " DECIMAL);";
	  public static final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
  }
}
