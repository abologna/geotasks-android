package com.geotasks.provider;

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
  
  public static final String DESCRIPTION = "description";
  
  public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.geotasks.task";
  
  public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.geotasks.task";

  public static final String DEFAULT_SORT_ORDER = "modified_date DESC";
}
