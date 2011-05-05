package com.geotasks.provider;

import java.util.*;

import android.net.*;
import android.provider.*;

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
  
  public static final Map<String, String> PROJECTION_MAP = getProjectionMap();
  
  private static final Map<String, String> getProjectionMap()
  {
    Map<String,String> map = new HashMap<String, String>();
    map.put(Places._ID, Places._ID);
    map.put(Places.NAME, Places.NAME);
    map.put(Places.LONGITUDE, Places.LONGITUDE);
    map.put(Places.LATITUDE, Places.LATITUDE);
    return Collections.unmodifiableMap(map);
  }
}
