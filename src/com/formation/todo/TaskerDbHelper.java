package com.formation.todo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskerDbHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	// database name
	private static final String DATABASE_NAME = "taskerManager";

	// tasks table name
	private static final String TABLE_TASKS = "tasks";

	// tasks columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TASKNAME = "taskName";
	private static final String KEY_STATUS = "status";

	public TaskerDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_TASKNAME + " TEXT, " + KEY_STATUS + " INTEGER)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
		// create table again
		onCreate(db);
	}

	public void addTask(Task task) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getTaskName());// task name
		// status of task can be 0 for not done and 1 for done
		values.put(KEY_STATUS, task.getStatus());

		// insert row
		db.insert(TABLE_TASKS, null, values);
		db.close();// closing database connection
	}

	// now we will read from database and store all values in the Array list
	public List<Task> getAllTasks() {
		List<Task> taskList = new ArrayList<Task>();
		// select all from query
		String selectQuery = "SELECT * FROM " + TABLE_TASKS;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping throught all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Task task = new Task();
				task.setId(cursor.getInt(0));
				task.setTaskName(cursor.getString(1));
				task.setStatus(cursor.getInt(2));
				// adding contact to list
				taskList.add(task);
			} while (cursor.moveToNext());
		}
		// return task list
		return taskList;
	}

	// -----------the update operation----------------
	public void updateTask(Task task) {
		// updating row
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TASKNAME, task.getTaskName());
		values.put(KEY_STATUS, task.getStatus());
		db.update(TABLE_TASKS, values, KEY_ID + " =? ",
				new String[] { String.valueOf(task.getId()) });
	}

}
