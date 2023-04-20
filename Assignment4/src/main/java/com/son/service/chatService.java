package com.son.service;

import java.util.ArrayList;

import dto.RoomDTO;

public interface chatService {
	
	public ArrayList<RoomDTO> getRoomList();

	public int addList(String roomName, String owner);
	
	public int deleteRoom(String roomName, String owner);

	public String encodeMsg(String msg);

	public String encode(String html, String method);

	public String isRoom(String roomName);

	public String isSame(String encodeKey, String inputKey);

	// test for mabatis seminar
//	 public void ChooseWhen();

}
