package com.geotasks;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.geotasks.android.R;
import com.geotasks.provider.Tasks;

public class TaskCursorAdapter extends SimpleCursorAdapter {

  public TaskCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
    super(context, layout, c, from, to);
  }

  @Override
  public void bindView(View view, final Context context, Cursor cursor) {
    TextView name = (TextView) view.findViewById(R.id.taskName);
    CheckBox complete = (CheckBox) view.findViewById(R.id.taskComplete);

    String nameText = cursor.getString(cursor.getColumnIndex(Tasks.NAME));
    name.setText(nameText);
    int completed = cursor.getInt(cursor.getColumnIndex(Tasks.COMPLETED));
    complete.setChecked(completed == 0 ? false : true);
    final long taskId = cursor.getLong(cursor.getColumnIndex(Tasks._ID));

    view.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        Uri uri = ContentUris.withAppendedId(Tasks.CONTENT_URI, taskId);
        context.startActivity(new Intent(Intent.ACTION_EDIT, uri));
      }
    });
  }
}