package com.xjy.mua.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.xjy.mua.model.Friend;

import java.util.ArrayList;

/**
 * 好友数据库访问
 */
public class FriendDB extends DbHelper {
    private static final String TAG = FriendDB.class.getSimpleName();

    public FriendDB(Context context) {
        super(context);
    }

    /**
     * 获取所有的好友
     * @return 返回所有好友
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
                db.beginTransaction();// 手动设置开始事务
            }
            // 数据插入操作循环
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
                db.setTransactionSuccessful();// 设置事务处理成功，不设置会自动回滚不提交
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
