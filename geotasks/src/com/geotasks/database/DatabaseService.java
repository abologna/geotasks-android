package com.geotasks.database;

import android.content.*;
import android.database.*;

public interface DatabaseService {
  int deleteTask(String selection, String[] args);
  int deleteTaskId(String taskId, String selection, String[] args);
  int deletePlace(String selection, String[] args);
  int deletePlaceId(String placeId, String selection, String[] args);
  long createTask(ContentValues values);
  long createPlace(ContentValues values);
  Cursor getTaskById(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder);
  Cursor getAllTasks(String[] projection, String selection, String[] selectionArgs, String sortOrder);
  Cursor getPlaceById(String id, String[] projection, String selection, String[] selectionArgs, String sortOrder);
  Cursor getAllPlaces(String[] projection, String string, String[] selectionArgs, String sortOrder);
  int updateTask(ContentValues value, String selection, String[] selectionArgs);
  int updateTaskId(String taskId, ContentValues values, String selection, String[] selectionArgs);
  int updatePlace(ContentValues values, String selection, String[] selectionArgs);
  int updatePlaceId(String placeId, ContentValues values, String selection, String[] selectionArgs);
}
