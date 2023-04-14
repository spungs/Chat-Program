package com.son.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.son.repository.chatDao;

import dto.UserDTO;

@Configuration
@ComponentScan
@Service
public class aRnuTest {
	
    String encodeKey = "happy";
    private String nonOverlapChar = "";
    private char[] nonOverlapCharArr = null;
    private static ArrayList<String> alphabetArr = new ArrayList<>();
    private String[][] encodeArr = new String[5][5];

    public static void main(String[] args) {
        aRnuTest a = new aRnuTest();

        // encodeKey 중복 character 제거와 uppercase 변환
        a.deduplicationToUpperCase();
        // encodeArr에 알파벳을 넣기위한 ArrayList 생성
        // string to char Array, alphabat Array Change the order
        a.ChangeTypeAndOrder(alphabetArr);
        // 5x5 encode 배열 만들기
        a.makeEncodeArr(alphabetArr);
        // encode msg
        a.encodeMsg("Hi My name is Zoo");
    }
    
    

    // 입력받은 msg를 encoding하여 반환
    public void encodeMsg(String msg) {
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

        // 2차원 배열의 인덱스 추출 테스트 출력
//        int x = 0;
//        int y = 0;
//
//        for (int i = 0; i < encodedMsg.length(); i++) {
//
//        }

//		System.out.println(encode[x][y]);
    }

    // encodeKey 중복 character 제거와 uppercase 변환
    public void deduplicationToUpperCase() {
        // 문장이 들어와도 key가 될 수 있도록 하기 위한 replace문 (현재 미사용)
//			encodeKey = encodeKey.replace(" ","");
        // encodeKey를 모두 대문자로 변경
        encodeKey = encodeKey.toUpperCase();
        // encodeKey의 중복 character 제거
        for (int i = 0; i < encodeKey.length(); i++) {
            if (encodeKey.indexOf(encodeKey.charAt(i)) == i) {
                nonOverlapChar += encodeKey.charAt(i);
            }
        }
    }

    // string to char Array, alphabat Array Change the order
    public void ChangeTypeAndOrder(ArrayList<String> alphaArr) {
        // String to char[]
        nonOverlapCharArr = nonOverlapChar.toCharArray();

        // 알파벳 a-y까지 array에 담기
        char upperChar = 'A';
        for (int i = 0; i < 25; i++) {
            alphaArr.add(Character.toString(upperChar));
            upperChar++;
        }

        // encodeKey alphaArr에서 제거
        for (int i = 0; i < nonOverlapCharArr.length; i++) {
            String character = Character.toString(nonOverlapCharArr[i]);
            alphaArr.remove(character);
        }

    }

    // 5x5 encode 배열 만들기
    public void makeEncodeArr(ArrayList<String> alphaArr) {
        int addNonOverlapCharArr = 0;
        int addAlphaArr = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (addNonOverlapCharArr != (25 - alphaArr.size())) {
                    encodeArr[i][j] = Character.toString(nonOverlapCharArr[j]);
                    addNonOverlapCharArr++;
//										System.out.print(Character.toString(nonOverlapCharArr[j]));
                } else {
                    encodeArr[i][j] = alphaArr.get(addAlphaArr);
//										System.out.print(alphaArr.get(addAlphaArr));
                    addAlphaArr++;
                }
            }
        }
        // 배열에 어떻게 들어가 있는지 보기위한 출력
        System.out.println(Arrays.deepToString(encodeArr));
    }
    /*
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
                }
            }
        }
        return encodeKey;

    }
}
