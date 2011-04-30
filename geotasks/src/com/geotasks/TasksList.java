package com.geotasks;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.geotasks.android.R;
import com.geotasks.provider.Tasks;

public class TasksList extends ListActivity {

  private final static String[] PROJECTION = new String[] { Tasks._ID, Tasks.NAME };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    if (intent.getData() == null) {
      intent.setData(Tasks.CONTENT_URI);
    }
    try {
      Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);
      SimpleCursorAdapter adapter = new SimpleCursorAdapter(
          this, R.layout.tasks_list_item, cursor, new String[] { Tasks.NAME },
          new int[] { android.R.id.text1 });
      setListAdapter(adapter);
    } catch (Exception e) {
      Log.e("Tasks List", "Query", e);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.task_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add_task:
        startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
    String action = getIntent().getAction();
    if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
      setResult(RESULT_OK, new Intent().setData(uri));
    } else {
      startActivity(new Intent(Intent.ACTION_EDIT, uri));
    }
  }
}