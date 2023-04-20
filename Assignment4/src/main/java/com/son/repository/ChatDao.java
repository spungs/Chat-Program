package com.son.repository;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import dto.RoomDTO;

@Repository
@Mapper
public interface ChatDao {

	public ArrayList<RoomDTO> getRoomList();

	public void insertNewRoom(String roomName, String owner);

	public String isRoom(String roomName);

	public int deleteRoom(String roomName, String owner);
}
