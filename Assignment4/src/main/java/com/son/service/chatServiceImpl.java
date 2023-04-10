package com.son.service;

import com.son.repository.chatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Configuration
@ComponentScan
@Service
public class chatServiceImpl implements chatService {
    @Autowired
    chatDao dao;

    private String nonOverlapString = "";
    private char[] nonOverlapCharArr = null;
    // encodeArr에 알파벳을 넣기위한 ArrayList 생성
    private ArrayList<String> alphabetArr = new ArrayList<>();
    // encodeArr 5x5 2차원 배열
    private String[][] encodeArr = new String[5][5];

    // 생성자에서 encode 배열을 만듬
    public chatServiceImpl(@Value("${encode.key}") String encodeKey) {
        // encodeKey 중복 character 제거와 uppercase 변환
        deduplicationToUpperCase(encodeKey);
        // string to char Array, alphabet Array Change the order
        ChangeTypeAndOrder();
        // 5x5 encode 배열 만들기
        makeEncodeArr();
    }

    @Override
    public String[] getRoomList() {
        String[] arr = dao.getRoomList();

        if (arr != null) {
            return arr;
        }

        return null;
    }

    @Override
    public int addList(String roomName) {
    	// 존재하면 name, 없으면 null
    	String isRoom = dao.isRoom(roomName);
    	if (isRoom == null) {
    		dao.insertNewRoom(roomName);
    		return 1;
    	}
    	return 0;
    }
    
    @Override
	public int deleteRoom(String roomName) {
    	int result = dao.deleteRoom(roomName);
    	if (result != 0) {
    		return result;
    	} else {
    		return 0;
    	}
		
	}

    // 입력받은 msg를 encoding하여 반환
    @Override
    public String encodeMsg(String msg) {
        // 1. msg를 char화 해서 char 하나씩 배열에서 인덱스를 찾아주고 i와 j를 서로 바꿔준다.
        // 2. msg에 i, j가 바뀐 문자를 추가해준다.(char to string해서)
        // string to charArr and toUpperCase
        char[] msgArr = msg.toUpperCase().toCharArray();
        // 암호화된 msg 생성
        String encodedMsg = "";

        for (int i = 0; i < msgArr.length; i++) {
            // msg와 encodedMsg 비교해서 대문자면 소문자로 변경
            if (Character.isUpperCase(msg.charAt(i))) {
                encodedMsg += encoding(msgArr[i]).toLowerCase();
            } else {
                encodedMsg += encoding(msgArr[i]).toUpperCase();
            }
        }
        System.out.println("암호화된 msg : " + encodedMsg);

        return encodedMsg;
    }

    // encodeKey 중복 character 제거와 uppercase 변환
    public void deduplicationToUpperCase(String encodeKey) {
        // 문장이 들어와도 key가 될 수 있도록 하기 위한 replace문 (현재 미사용)
//		encodeKey = encodeKey.replace(" ","");
        // encodeKey를 모두 대문자로 변경
        String upperEncodeKey = encodeKey.toUpperCase();
        // encodeKey의 중복 character 제거
        for (int i = 0; i < upperEncodeKey.length(); i++) {
            if (upperEncodeKey.indexOf(upperEncodeKey.charAt(i)) == i) {
                nonOverlapString += upperEncodeKey.charAt(i);
            }
        }
    }

    // string to char Array, alphabat Array Change the order
    public void ChangeTypeAndOrder() {
        // String to char[]
        nonOverlapCharArr = nonOverlapString.toCharArray();

        // 알파벳 A-Y까지 array에 담기
        char lower = 'A';
        for (int i = 0; i < 25; i++) {
            alphabetArr.add(Character.toString(lower));
            lower++;
        }

        // encodeKey alphaArr에서 제거
        for (int i = 0; i < nonOverlapCharArr.length; i++) {
            String character = Character.toString(nonOverlapCharArr[i]);
            alphabetArr.remove(character);
        }

        // encodeKey 제거 후 alphaArr 출력 테스트
//		for (int i = 0; i < alphaArr.size(); i++) {
//			System.out.print(alphaArr.get(i));
//		}
//		System.out.println();
    }

    // 5x5 encode 배열 만들기
    public void makeEncodeArr() {
        int addNonOverlapCharArr = 0;
        int addAlphaArr = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (addNonOverlapCharArr != (25 - alphabetArr.size())) {
                    encodeArr[i][j] = Character.toString(nonOverlapCharArr[j]);
                    addNonOverlapCharArr++;
//					System.out.print(Character.toString(nonOverlapCharArr[j]));
                } else {
                    encodeArr[i][j] = alphabetArr.get(addAlphaArr);
//					System.out.print(alphaArr.get(addAlphaArr));
                    addAlphaArr++;
                }
            }
        }
        // 배열에 어떻게 들어가 있는지 보기위한 출력
//		System.out.println(Arrays.deepToString(encode));
    }

    /* happy keyword 예시
     * [H, A, P, Y, B]
     * [C, D, E, F, G]
     * [I, J, K, L, M]
     * [N, O, Q, R, S]
     * [T, U, V, W, X]
     */
    public String encoding(char msgChar) {
        // char가 아스키 코드로 되어있어서 String과 equals로 비교할 수 없음..
        String msg = Character.toString(msgChar);
        for (int i = 0; i < encodeArr.length; i++) {
            for (int j = 0; j < encodeArr.length; j++) {
                // msgArr와 encodeArr의 문자를 비교해 index 추출
                switch (msgChar) {
                    case ' ':
                        return " ";
                    case 'Z':
                        return "z";
                    default:
                        if (encodeArr[i][j].equals(msg)) {
                            return encodeArr[j][i];
                        }
                        break;
                } // switch case
            } // for j
        } // for i
		return msg;
    }

	
}