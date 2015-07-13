package org.aurona.lib.resource.view;

import org.aurona.lib.resource.WBRes;

public interface WBOnResourceChangedListener {
	 void resourceChanged(WBRes res, String type,int count,int pos);
}
