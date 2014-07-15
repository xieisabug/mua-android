package com.xjy.mua.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.xjy.mua.model.Friend;

import java.util.ArrayList;

/**
 * �������ݿ����
 */
public class FriendDB extends DbHelper {
    private static final String TAG = FriendDB.class.getSimpleName();

    public FriendDB(Context context) {
        super(context);
    }

    /**
     * ��ȡ���еĺ���
     * @return �������к���
     */
    public ArrayList<Friend> getAllFriends() {
        ArrayList<Friend> list;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Friend item;
        String sql = "select * from " + FRIEND_TABLE_NAME;

        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            }
            list = new ArrayList<Friend>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    item = new Friend();
                    item.setFriendId(cursor.getInt(cursor.getColumnIndex("friendId")));
                    item.setName(cursor.getString(cursor.getColumnIndex("name")));
                    item.setLevel(cursor.getInt(cursor.getColumnIndex("level")));
                    item.setSpecialName(cursor.getString(cursor.getColumnIndex("specialName")));
                    item.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                    list.add(item);
                }
            }
            return list;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public void batchInsert(ArrayList<Friend> qList) {
        SQLiteDatabase db = null;
        ContentValues cv;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.beginTransaction();// �ֶ����ÿ�ʼ����
            }
            // ���ݲ������ѭ��
            for (Friend qEntity : qList) {
                cv = new ContentValues();
                cv.put("friendId", qEntity.getFriendId());
                cv.put("level", qEntity.getLevel());
                cv.put("name", qEntity.getName());
                cv.put("specialName", qEntity.getSpecialName());
                cv.put("status", qEntity.getStatus());
                if (db != null) {
                    db.insert(FRIEND_TABLE_NAME, null, cv);
                }
            }
            if (db != null) {
                db.setTransactionSuccessful();// ����������ɹ��������û��Զ��ع����ύ
                db.endTransaction();
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void updateQuestionContent(String friendId, Friend friend) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            String where = "friendId=?";
            String[] whereValue = {friendId};
            ContentValues cv = new ContentValues();
            cv.put("friendId", friend.getFriendId());
            cv.put("level", friend.getLevel());
            cv.put("name", friend.getName());
            cv.put("specialName", friend.getSpecialName());
            cv.put("status", friend.getStatus());
            if (db != null) {
                db.update(FRIEND_TABLE_NAME, cv, where, whereValue);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.delete(FRIEND_TABLE_NAME, null, null);
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

}
