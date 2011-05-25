package com.geotasks.ui;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.geotasks.android.R;
import com.geotasks.provider.Tasks;

public class TaskEditor extends Activity {

  private Uri uri;
  private Cursor cursor;
  private EditText editName;
  private Button setDate;
  private final static String[] PROJECTION = new String[] { Tasks._ID, Tasks.NAME, Tasks.DUE_DATE };
  private final static int DATE_DIALOG_ID = 0;
  private Calendar calendar = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_editor);
    ((TextView) findViewById(R.id.title_text)).setText(getTitle());
    editName = (EditText) findViewById(R.id.name);

    setDate = (Button) this.findViewById(R.id.task_editor_setdate);
    setDate.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        showDialog(DATE_DIALOG_ID);
      }
    });

    Button save = (Button) this.findViewById(R.id.task_editor_save);
    save.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        if (cursor != null) {
          String text = editName.getText().toString();
          if (text.length() == 0) {
            setResult(RESULT_CANCELED);
            deleteTask();
          } else {
            ContentValues values = new ContentValues();
            values.put(Tasks.NAME, text);
            values.put(Tasks.DUE_DATE, calendar == null ? 0 : calendar.getTime().getTime());
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

    Button cancel = (Button) this.findViewById(R.id.task_editor_cancel);
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

      Date date = new Date();
      long time = cursor.getLong(2);
      if (time > 0) {
        date.setTime(time);
        if (calendar == null) {
          calendar = Calendar.getInstance();
        }
        calendar.setTime(date);
        setDate.setText(String.format("%1$tm-%1$te-%1$tY", calendar));
      } else {
        calendar = null;
        setDate.setText("Set Due Date");
      }
    }
  }

  private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
    public void onDateSet(DatePicker view, int year, int month, int day) {
      if (calendar == null) {
        calendar = Calendar.getInstance();
      }
      calendar.set(year, month, day);
      setDate.setText(String.format("%1$tm-%1$te-%1$tY", calendar));
    }
  };

  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case DATE_DIALOG_ID:
        Calendar dialogCalendar = calendar != null ? calendar : Calendar.getInstance();
        return new DatePickerDialog(this, dateSetListener, dialogCalendar.get(Calendar.YEAR),
            dialogCalendar.get(Calendar.MONTH), dialogCalendar.get(Calendar.DAY_OF_MONTH));
    }
    return null;
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
