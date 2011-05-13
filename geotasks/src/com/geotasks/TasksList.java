package com.geotasks;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.geotasks.android.R;
import com.geotasks.provider.Tasks;

public class TasksList extends ListActivity {

  private final static String[] PROJECTION = new String[] { Tasks._ID, Tasks.NAME, Tasks.COMPLETED };

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_task_list);
    ((TextView) findViewById(R.id.title_text)).setText(getTitle());

    Intent intent = getIntent();
    if (intent.getData() == null) {
      intent.setData(Tasks.CONTENT_URI);
    }
    try {
      Cursor cursor = managedQuery(getIntent().getData(), PROJECTION, null, null, null);
      TaskCursorAdapter adapter = new TaskCursorAdapter(
          this, R.layout.tasks_list_item, cursor, new String[] { Tasks.NAME, Tasks.COMPLETED },
          new int[] { R.id.taskName, R.id.taskComplete });
      setListAdapter(adapter);

    } catch (Exception e) {
      Log.e("Tasks List", "Query", e);
    }
  }

  public void onAddClick(View v) {
    startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
  }

  public void onSearchClick(View v) {
    Toast.makeText(this, "Search Not Implemented", Toast.LENGTH_SHORT).show();
  }
}