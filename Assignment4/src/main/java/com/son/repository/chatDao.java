package com.son.repository;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import dto.RoomDTO;
import dto.UserDTO;

@Repository
@Mapper
public interface chatDao {

	public ArrayList<RoomDTO> getRoomList();

	public void insertNewRoom(String roomName, String owner);

	public String isRoom(String roomName);

	public int deleteRoom(String roomName, String owner);

	// mabatis seminar test
	public ArrayList<UserDTO> roomListAll(
			@Param("deptment") String deptment,
			@Param("name") String name,
			@Param("test_score") int test_score);
	
	public void updateYN(UserDTO user);

	public ArrayList<UserDTO> selectUser(String[] users);
}
