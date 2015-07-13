package org.aurona.lib.resource.view;

public interface AsyncTextHttpTaskListener {
	public void onRequestDidFailedStatus(Exception e);
	public void onRequestDidFinishLoad(String result);
}
