package org.aurona.lib.resource.manager;

import org.aurona.lib.resource.WBRes;

public  interface WBManager {

	public int getCount();

	public WBRes getRes(int pos);
	
	public WBRes getRes(String name);
	
	public boolean isRes(String name);

	
}
