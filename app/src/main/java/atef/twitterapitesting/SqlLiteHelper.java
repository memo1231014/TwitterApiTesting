package atef.twitterapitesting;
/************************************************************************\
			This code written by Tefa Alhwa
			
								 \|||||/
								 /-----\
								 |0   0|
								 | { } |
								 \-----/
								    |
								   /|\
								  / | \
								 /  |  \
								    |
								    |
								   / \
								  /   \
								 /     \
								/       \
			
			
			please don't give shits if you couldn't understand my codes, 
			Really I'm trying to do my best as fast as possible so if you
			couldn't understand it tell me and if you got any idea better
			than mine tell me please thank you and don't small us :)
			
						I Think I Imagine I Code 

\************************************************************************/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlLiteHelper {
	SQLiteDatabase database;

	public SqlLiteHelper(Context context) {
		database = context.openOrCreateDatabase("twitterOfflineDB", Context.MODE_PRIVATE,null);
		database.execSQL("create table if not exists followers(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,user_name varchar(255),handle varchar(255),bio varchar(255),profile_image varchar(255),background_image varchar(255))");
	}

	public long insert(String tableName, ContentValues cv) {
		return database.insert(tableName, null, cv);
	}

	public Cursor select(String query) {
		return database.rawQuery(query, null);
	}

	public Boolean deleteOrUpdate(String query) {
		return database.rawQuery(query, null).moveToFirst();
	}

	public void closeConnection(){
		database.close();
	}
}
