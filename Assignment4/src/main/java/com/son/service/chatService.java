package com.son.service;

public interface chatService {
	
	public String[] getRoomList();

	public int addList(String roomName, String owner);
	
	public int deleteRoom(String roomName, String owner);

	public String encodeMsg(String msg);

	public String encode(String html, String method);

	// test for mabatis seminar
//	 public void ChooseWhen();

}
