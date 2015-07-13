package org.aurona.lib.database;
/**
 * ��ͨJavaBean
 * @author longgangbai
 *
 */
public class ResRecordBean {
	public static final String ID = "_id";
	public static final String PACKAGENAME = "packagename";
	public static final String MANAGERNAME = "managername";
	public static final String CLICKITEMNAME = "clickitemname";
	
	private String id;
	private String packageName;
	private String managerName;
	private String clickItemnName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packagename) {
		this.packageName = packagename;
	}
	public String getManagerName() {
		return managerName;
	}
	public void setManagerName(String code) {
		this.managerName = code;
	}

	public String getClickItemnName() {
		return clickItemnName;
	}
	public void setClickItemnName(String code) {
		this.clickItemnName = code;
	}
	
}
