package com.geotasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.geotasks.android.R;
import com.geotasks.provider.Tasks;

public class TaskCursorAdapter extends SimpleCursorAdapter {

  public TaskCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
    super(context, layout, c, from, to);
  }

  @Override
  public void onContentChanged() {
    // Overriden to avoid the default implementation which forces auto-requery on cursor update.
    // Newer versions of the API allow setting a flag on the constructor for auto-requery.
  }
  
  @Override
  public void bindView(final View view, final Context context, Cursor cursor) {
    final TextView name = (TextView) view.findViewById(R.id.taskName);
    CheckBox complete = (CheckBox) view.findViewById(R.id.taskComplete);   
    
    int completed = cursor.getInt(cursor.getColumnIndex(Tasks.COMPLETED));
    complete.setChecked(completed == 0 ? false : true);
    
    // If task completed apply strikethrough style to the text.
    final String nameText = cursor.getString(cursor.getColumnIndex(Tasks.NAME));
    if (completed != 0) {
      SpannableString content = new SpannableString(nameText);
      content.setSpan(new StrikethroughSpan(), 0, content.length(), 0);
      name.setText(content);
    } else {
      name.setText(nameText);
    }
    
    final long taskId = cursor.getLong(cursor.getColumnIndex(Tasks._ID));

    view.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        Uri uri = ContentUris.withAppendedId(Tasks.CONTENT_URI, taskId);
        context.startActivity(new Intent(Intent.ACTION_EDIT, uri));
      }
    });
    
    complete.setOnCheckedChangeListener(new OnCheckedChangeListener() {    
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ContentValues values = new ContentValues();
        values.put(Tasks.COMPLETED, isChecked);
        Uri uri = ContentUris.withAppendedId(Tasks.CONTENT_URI, taskId);
        context.getContentResolver().update(uri, values, null, null);
        
        // Apply or remove strikethrough style depending on isChecked value.
        SpannableString content = new SpannableString(nameText);
        if (isChecked) {
          content.setSpan(new StrikethroughSpan(), 0, content.length(), 0);
        } else {
          content.removeSpan(new StrikethroughSpan());
        }
        name.setText(content);
        
      }
    });
  }
}