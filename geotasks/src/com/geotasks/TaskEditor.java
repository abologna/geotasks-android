package com.geotasks;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.geotasks.android.R;
import com.geotasks.provider.Tasks;

public class TaskEditor extends Activity implements View.OnClickListener {

  private Uri uri;
  private Cursor cursor;
  private EditText editName;
  private final static String[] PROJECTION = new String[] { Tasks._ID, Tasks.NAME };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.task_editor);

    editName = (EditText) findViewById(R.id.name);
    Button add = (Button) this.findViewById(R.id.done);
    add.setOnClickListener(this);

    final Intent intent = getIntent();
    final String action = intent.getAction();

    if (Intent.ACTION_EDIT.equals(action)) {
      uri = intent.getData();
    } else {
      uri = getContentResolver().insert(intent.getData(), null);
    }
    cursor = managedQuery(uri, PROJECTION, null, null, null);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (cursor != null) {
      cursor.moveToFirst();
      editName.setText(cursor.getString(1));
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (cursor != null) {
      String text = editName.getText().toString();
      int length = text.length();

      if (isFinishing() && (length == 0)) {
        setResult(RESULT_CANCELED);
        deleteTask();
      } else {
        ContentValues values = new ContentValues();
        values.put(Tasks.NAME, text);
        getContentResolver().update(uri, values, null, null);
      }
    }
  }

  private void deleteTask() {
    if (cursor != null) {
      cursor.close();
      cursor = null;
      getContentResolver().delete(uri, null, null);
      editName.setText("");
    }
  }

  public void onClick(View v) {
    finish();
  }
}
