package com.xjy.mua.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**   
* @Title: DbHelper.java 
* @Package com.sealion.serviceassistant.db 
* @Description: ���ݿ���������ĳ����ࡣ������ݿ��ĳ�ʼ����
* @author jack.lee titans.lee@gmail.com   
* @date 2013-2-17 ����9:22:03 
* @version V1.0
* Copyright: Copyright (c)2012
* Company: �����ж�ͨ�ż������޹�˾
*/
public abstract class DbHelper extends SQLiteOpenHelper
{
	private static final String TAG = "DbHelper";
	protected final static int PAGE_SIZE = 10;
	protected static final int db_version = 1;
	private static final String DB_NAME = "MUA";
	
	protected final static String FRIEND_TABLE_NAME = "friend"; //�����

	//�����
	private String friend_sql = "Create table IF NOT EXISTS " + FRIEND_TABLE_NAME + "(friendId integer primary key, " +
			" name text, " +
			" level int," +
			" status int," +
			" specialName text);";
	
	public DbHelper(Context context)
	{		
		super(context, DB_NAME, null, db_version);
		Log.d(TAG, "��ʼ�����ݿ�.........");
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(friend_sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
//		db.execSQL("DROP TABLE IF EXISTS " + ORDER_LIST_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + NOTICE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + COMMON_ORDER_COMPLISH_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + FILE_MANAGE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + ORDER_CHANGE_TABLE_NAME);
//		db.execSQL("DROP TABLE IF EXISTS " + PHONE_COMPLISH_TABLE_NAME);
		//onCreate(db);
	}
	
}
