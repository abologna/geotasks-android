package com.geotasks.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geotasks.android.R;
import com.geotasks.provider.Places;

public class PlaceEditor extends Activity {

  private Uri uri;
  private Cursor cursor;
  private EditText editName;
  private final static String[] PROJECTION = new String[] { Places._ID, Places.NAME };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_place_editor);
    ((TextView) findViewById(R.id.title_text)).setText(getTitle());
    editName = (EditText) findViewById(R.id.name);

    Button save = (Button) this.findViewById(R.id.place_editor_save);
    save.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (cursor != null) {
          String text = editName.getText().toString();
          if (text.length() == 0) {
            setResult(RESULT_CANCELED);
            deleteTask();
          } else {
            ContentValues values = new ContentValues();
            values.put(Places.NAME, text);
            getContentResolver().update(uri, values, null, null);
            setResult(RESULT_OK);
          }
        } else {
          // TODO: handle null cursor case.
          setResult(RESULT_CANCELED);
        }
        finish();
      }
    });

    final Intent intent = getIntent();
    final String action = intent.getAction();

    Button cancel = (Button) this.findViewById(R.id.place_editor_cancel);
    cancel.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (Intent.ACTION_INSERT.equals(action)) {
          deleteTask();
        }
        setResult(RESULT_CANCELED);
        finish();
      }
    });

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

  private void deleteTask() {
    if (cursor != null) {
      cursor.close();
      cursor = null;
      getContentResolver().delete(uri, null, null);
      editName.setText("");
    }
  }
}
