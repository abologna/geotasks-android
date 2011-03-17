package com.geotasks.android.test;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.geotasks.provider.GeoTasksProvider;
import com.geotasks.provider.Places;
import com.geotasks.provider.Tasks;

public class GeoTasksProviderTest extends ProviderTestCase2<GeoTasksProvider> {

	public GeoTasksProviderTest() {
		super(GeoTasksProvider.class, "com.geotasks.provider.geotasksprovider");
	}

	public void testInsertThenQueryTasks() {
		ContentResolver resolver = getMockContentResolver();
		Uri tasksUrl = Uri
				.parse("content://com.geotasks.provider.geotasksprovider/tasks");

		// Insert
		ContentValues values = new ContentValues();
		values.put(Tasks.COMPLETED, false);
		values.put(Tasks.NAME, "test-task");
		values.put(Tasks.DESCRIPTION, "some description");
		values.put(Tasks.PLACE_ID, 1);
		Uri result = resolver.insert(tasksUrl, values);
		assertEquals(
				"content://com.geotasks.provider.geotasksprovider/tasks/1",
				result.toString());

		// Query
		Cursor cursor = resolver.query(tasksUrl, null, null, null, null);
		int nameIndex = cursor.getColumnIndex(Tasks.NAME);
		int descriptionIndex = cursor.getColumnIndex(Tasks.DESCRIPTION);
		int placeidIndex = cursor.getColumnIndex(Tasks.PLACE_ID);
		int completedIndex = cursor.getColumnIndex(Tasks.COMPLETED);
		cursor.moveToFirst();
		assertEquals("test-task", cursor.getString(nameIndex));
		assertEquals("some description", cursor.getString(descriptionIndex));
		assertEquals(1, cursor.getInt(placeidIndex));
		assertEquals(0, cursor.getInt(completedIndex));
	}

	public void testInsertThenQueryPlaces() {
		ContentResolver resolver = getMockContentResolver();
		Uri placesUrl = Uri
				.parse("content://com.geotasks.provider.geotasksprovider/places");

		// Insert
		ContentValues values = new ContentValues();
		values.put(Places.NAME, "work");
		values.put(Places.LATITUDE, 1);
		values.put(Places.LONGITUDE, -1);
		Uri result = resolver.insert(placesUrl, values);
		assertEquals(
				"content://com.geotasks.provider.geotasksprovider/places/1",
				result.toString());

		// Query
		Cursor cursor = resolver.query(placesUrl, null, null, null, null);
		int nameIndex = cursor.getColumnIndex(Places.NAME);
		int latitudeIndex = cursor.getColumnIndex(Places.LATITUDE);
		int longitudeIndex = cursor.getColumnIndex(Places.LONGITUDE);
		cursor.moveToFirst();
		assertEquals("work", cursor.getString(nameIndex));
		assertEquals(1.0, cursor.getDouble(latitudeIndex));
		assertEquals(-1.0, cursor.getDouble(longitudeIndex));
	}

	public void testProjectionAndSelection() {
		ContentResolver resolver = getMockContentResolver();
		Uri url = Uri
				.parse("content://com.geotasks.provider.geotasksprovider/tasks");

		ContentValues values = new ContentValues();
		values.put(Tasks.NAME, "t1");
		values.put(Tasks.PLACE_ID, 1);
		resolver.insert(url, values);

		values = new ContentValues();
		values.put(Tasks.NAME, "t2");
		values.put(Tasks.PLACE_ID, 2);
		resolver.insert(url, values);

		values = new ContentValues();
		values.put(Tasks.NAME, "t3");
		values.put(Tasks.PLACE_ID, 2);
		resolver.insert(url, values);

		Uri uriQuery = Uri
				.parse("content://com.geotasks.provider.geotasksprovider/tasks");
		Cursor cursor = resolver.query(uriQuery, new String[] { Tasks.NAME },
				Tasks.PLACE_ID + "=?", new String[] { "2" }, "name ASC");
		cursor.moveToFirst();
		assertEquals("t2", cursor.getString(cursor.getColumnIndex(Tasks.NAME)));
		cursor.moveToNext();
		assertEquals("t3", cursor.getString(cursor.getColumnIndex(Tasks.NAME)));
		assertTrue(cursor.isLast());
	}

}
