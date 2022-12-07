package com.example.hospitalreview2.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    //userName 꺼내기
    public static String getUserName(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userName", String.class);
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static boolean isExpired(String token, String secretKey) {
        //return expire timestamp
//        Date expiredDate = extractClaims(token, secretKey).getExpiration();
//        return expiredDate.before(new Date());
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date()); //만료일이 현재 날짜 이전이면 true(만료됨) 리턴
    }
    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims(); //Claims는 일종의 map이다. claim에 userName을 넣어준다.
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) //토큰 발급일
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) //토큰 만료일
                .signWith(SignatureAlgorithm.HS256, key) //HS256 알고리즘으로 키를 암호화한다.
                .compact()
                ;
    }
}