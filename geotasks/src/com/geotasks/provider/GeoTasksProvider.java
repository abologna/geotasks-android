package com.geotasks.provider;

import android.content.*;
import android.database.*;
import android.net.*;

import com.geotasks.database.*;

public class GeoTasksProvider extends ContentProvider {
  private static final UriMatcher uriMatcher;
  private static final String AUTHORITY = "com.geotasks.provider.geotasksprovider";
  private static final int PLACES = 1;
  private static final int PLACE_ID = 2;
  private static final int TASKS = 3;
  private static final int TASK_ID = 4;

  private DatabaseService databaseService;

  @Override
  public boolean onCreate() {
    databaseService = new SQLiteDatabaseService(getContext());
    return true;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int count;
    switch (uriMatcher.match(uri)) {
      case TASKS:
        count = databaseService.deleteTask(selection, selectionArgs);
        break;
      case TASK_ID:
        String taskId = uri.getPathSegments().get(1);
        count = databaseService.deleteTaskId(taskId, selection, selectionArgs);
        break;
      case PLACES:
        count = databaseService.deletePlace(selection, selectionArgs);
        break;
      case PLACE_ID:
        String placeId = uri.getPathSegments().get(1);
        count = databaseService.deletePlaceId(placeId, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    // TODO replace null
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case TASKS:
        return Tasks.CONTENT_TYPE;
      case TASK_ID:
        return Tasks.CONTENT_ITEM_TYPE;
      case PLACES:
        return Places.CONTENT_TYPE;
      case PLACE_ID:
        return Places.CONTENT_ITEM_TYPE;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    Uri contentUri;
    long rowId;
    switch (uriMatcher.match(uri)) {
      case TASKS:
        rowId = databaseService.createTask(values);
        contentUri = Tasks.CONTENT_URI;
        break;
      case PLACES:
        rowId = databaseService.createPlace(values);
        contentUri = Places.CONTENT_URI;
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    if (rowId > 0) {
      Uri itemUri = ContentUris.withAppendedId(contentUri, rowId);
      getContext().getContentResolver().notifyChange(itemUri, null);
      return itemUri;
    }
    throw new SQLException("Failed to insert row into " + uri);
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor cursor;
    switch (uriMatcher.match(uri)) {
      case TASK_ID:
        String taskId = uri.getPathSegments().get(1);
        cursor = databaseService.getTaskById(taskId, projection, selection, selectionArgs, sortOrder);
        break;
      case TASKS:
        cursor = databaseService.getAllTasks(projection, selection, selectionArgs, sortOrder);
        break;
      case PLACE_ID:
        String placeId = uri.getPathSegments().get(1);
        cursor = databaseService.getPlaceById(placeId, projection, selection, selectionArgs, sortOrder);
        break;
      case PLACES:
        cursor = databaseService.getAllPlaces(projection, selection, selectionArgs, sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    // Tell the cursor what uri to watch, so it knows when its source data changes.
    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {   
    int count;
    switch (uriMatcher.match(uri)) {
      case TASKS:
        count = databaseService.updateTask(values, selection, selectionArgs);
        break;
      case TASK_ID:
        String taskId = uri.getPathSegments().get(1);
        count = databaseService.updateTaskId(taskId, values, selection, selectionArgs);
        break;
      case PLACES:
        count = databaseService.updatePlace(values, selection, selectionArgs);
        break;
      case PLACE_ID:
        String placeId = uri.getPathSegments().get(1);
        count = databaseService.updatePlaceId(placeId, values, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Unknown URI " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(AUTHORITY, "places", PLACES);
    uriMatcher.addURI(AUTHORITY, "places/#", PLACE_ID);
    uriMatcher.addURI(AUTHORITY, "tasks", TASKS);
    uriMatcher.addURI(AUTHORITY, "tasks/#", TASK_ID);
  }

}
