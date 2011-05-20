package com.geotasks.provider;

import java.util.*;

import android.net.Uri;
import android.provider.BaseColumns;

public class Tasks implements BaseColumns {
  public static final Uri CONTENT_URI
      = Uri.parse("content://com.geotasks.provider.geotasksprovider/tasks");
  
  public static final String NAME = "name";
  
  public static final String PLACE_ID = "place_id";
  
  public static final String COMPLETED = "completed";

  public static final String CREATED_DATE = "created_date";

  public static String MODIFIED_DATE = "modified_date";
  
  public static String DUE_DATE = "due_date";
  
  public static final String DESCRIPTION = "description";
  
  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.geotasks.task";
  
  public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.geotasks.task";

  public static final String DEFAULT_SORT_ORDER = "modified_date DESC";
  
  // SQL
  
  public static class SQL {
	  public static final String TABLE_NAME = "tasks";
	  public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" (" + _ID + " INTEGER PRIMARY KEY," 
 																				 + NAME + " TEXT," 
 																				 + COMPLETED + " BOOLEAN," 
 																				 + PLACE_ID + " INTEGER," 
 																				 + DESCRIPTION + " TEXT,"
 																				 + DUE_DATE + " LONG,"
 																				 + CREATED_DATE + " INTEGER," 
 																				 + MODIFIED_DATE + " INTEGER);";
	  public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME; 
	  
  }
  
  public static final Map<String, String> PROJECTION_MAP = createProjectionMap();
  private static final Map<String, String> createProjectionMap()
  {
    Map<String, String> map = new HashMap<String, String>();
    map.put(Tasks._ID, Tasks._ID);
    map.put(Tasks.NAME, Tasks.NAME);
    map.put(Tasks.PLACE_ID, Tasks.PLACE_ID);
    map.put(Tasks.CREATED_DATE, Tasks.CREATED_DATE);
    map.put(Tasks.MODIFIED_DATE, Tasks.MODIFIED_DATE);
    map.put(Tasks.DUE_DATE, Tasks.DUE_DATE);
    map.put(Tasks.COMPLETED, Tasks.COMPLETED);
    map.put(Tasks.DESCRIPTION, Tasks.DESCRIPTION);
    return Collections.unmodifiableMap(map);
  }
}
