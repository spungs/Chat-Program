package com.son.service;

import java.util.ArrayList;

import dto.RoomDTO;

public interface ChatService {
	
	public ArrayList<RoomDTO> getRoomList();

	public int addList(String roomName, String owner);
	
	public int deleteRoom(String roomName, String owner);

	public String encryptMsg(String msg);

	public String encryptOrDecryptMsgs(String html, String method);

	public String isRoom(String roomName);

	public String isSame(String encodeKey, String inputKey);
}
