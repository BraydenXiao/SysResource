package org.aurona.lib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
/**
 * ʵ�ֶԱ�Ĵ��������¡������������
 * 
 * ��Android ������������ݵĲ�����SQLite����ʵ����ع��ܹ���
 * �����Ǳ���̳�SQLiteOpenHelper��ʵ����صĹ��ܡ�
 *  
 * 
 * @author longgangbai
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String TB_NAME = "ResRecord";

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	
	/**
	 * �����±�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " +
				TB_NAME + "(" +
				ResRecordBean.ID + " varchar primary key," +
				ResRecordBean.PACKAGENAME + " varchar," + 
				ResRecordBean.MANAGERNAME + " varchar," + 
				ResRecordBean.CLICKITEMNAME + " varchar"+
				")");
	}
	
	/**
	 * �������ǰһ�δ������ݿ�汾��һ��ʱ����ɾ�����ٴ����±�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}
	
	/**
	 * �������
	 * @param db
	 * @param oldColumn
	 * @param newColumn
	 * @param typeColumn
	 */
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					TB_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
