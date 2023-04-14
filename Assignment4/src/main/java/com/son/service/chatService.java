package com.son.service;

public interface chatService {
	
	public String[] getRoomList();

	public int addList(String roomName);
	
	public String encodeMsg(String msg);

	public int deleteRoom(String roomName);
	
	// test 
	 public void ChooseWhen();

}
