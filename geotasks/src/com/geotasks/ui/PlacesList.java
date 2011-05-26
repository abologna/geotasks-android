package com.geotasks.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.geotasks.android.R;
import com.geotasks.provider.Places;

public class PlacesList extends ListActivity {

  private final static String[] PROJECTION = new String[] { Places._ID, Places.NAME };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_places_list);
    ((TextView) findViewById(R.id.title_text)).setText(getTitle());

    Intent intent = getIntent();
    if (intent.getData() == null) {
      intent.setData(Places.CONTENT_URI);
    }
    try {
      Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);
      SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.places_list_item, cursor,
          new String[] { Places.NAME }, new int[] { R.id.placeName });
      setListAdapter(adapter);
    } catch (Exception e) {
      Log.e("Places List", "Query", e);
    }
  }

  public void onAddClick(View v) {
    startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
  }

  public void onSearchClick(View v) {
    Toast.makeText(this, "Search Not Implemented", Toast.LENGTH_SHORT).show();
  }
}