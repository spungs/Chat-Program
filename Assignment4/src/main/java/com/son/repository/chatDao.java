package com.son.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface chatDao {
	
	public String[] getRoomList();

	public void insertNewRoom(String roomName);
	
	public String isRoom(String roomName);

	public int deleteRoom(String roomName);
}
