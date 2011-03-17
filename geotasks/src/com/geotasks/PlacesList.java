package com.geotasks;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.geotasks.android.R;
import com.geotasks.provider.Places;

public class PlacesList extends ListActivity {

  private final static String[] PROJECTION = new String[] { Places._ID, Places.NAME };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /*
    ContentResolver resolver = getContentResolver();
    Uri url = Uri.parse("content://com.geotasks.provider.geotasksprovider/places");

    // Insert
    try {
      ContentValues values = new ContentValues();
      
      values.put(Places.NAME, "Work");
      resolver.insert(url, values);
      
      values.put(Places.NAME, "Home");
      resolver.insert(url, values);
      
      values.put(Places.NAME, "Market");
      resolver.insert(url, values);
      
    } catch (Exception e) {
      Log.e("PLACES LIST", "Inserting", e);
    }
    */
    
    Intent intent = getIntent();
    if (intent.getData() == null) {
      intent.setData(Places.CONTENT_URI);
    }
    
    try {
      Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);

      SimpleCursorAdapter adapter = new SimpleCursorAdapter(
              this, R.layout.places_list_item, cursor, new String[] { Places.NAME },
              new int[] { android.R.id.text1 });
      setListAdapter(adapter);
      
    } catch (Exception e) {
      Log.e("PLACES LIST", "Query", e);
    }

  }
}