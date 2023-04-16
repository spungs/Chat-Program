package com.son.service;

public interface chatService {
	
	public String[] getRoomList();

	public int addList(String roomName, String owner);
	
	public int deleteRoom(String roomName, String owner);

	public String encodeMsg(String msg);
	
	// test 
	 public void ChooseWhen();

}
