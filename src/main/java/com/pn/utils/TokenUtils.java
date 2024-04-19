package com.pn.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pn.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * token的工具类
 */
@Component
public class TokenUtils {

    //注入到redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //注入token的过期时间
    @Value("${warehouse.expire-time}")
    private int expireTime;

    /**
     * 常量
     */
    //token中存放用户id所对应的名字（此处定义该常量的默认值）
    private static final String CLAIM_NAME_USERID = "CLAIM_NAME_USERID";
    //token中存放用户名所对应的名字（此处定义该常量的默认值）
    private static final String CLAIM_NAME_USERCODE = "CLAIM_NAME_USERCODE";
    //token中存放用户真实姓名所对应的名字（此处定义该常量的默认值）
    private static final String CLAIM_NAME_USERNAME = "CLAIM_NAME_USERNAME";

    //生成jwt token
    private String sign(CurrentUser currentUser,String securityKey){
        //生成token
        String token = JWT.create()
                //给jwt token的载体中存放用户的信息
                .withClaim(CLAIM_NAME_USERID,currentUser.getUserId())
                .withClaim(CLAIM_NAME_USERCODE,currentUser.getUserCode())
                .withClaim(CLAIM_NAME_USERNAME,currentUser.getUserName())
                //定义jwt token d 发行时间
                .withIssuedAt(new Date())
                //定义jwt token的有效时间
                .withExpiresAt(new Date(System.currentTimeMillis()+expireTime*1000))
                //通过密钥来指定签名
                .sign(Algorithm.HMAC256(securityKey));
        return token;
    }

    /**
     * 将当前用户信息以及用户密码为密钥来生成token的方法
     * @return
     */
    public String loginSign(CurrentUser currentUser,String password){
        //生成token
        String token = sign(currentUser,password);
        //将获取到的token保存到redis中，并且设置token在redis中的过期时间
        //                                                  过期时间        过期时间单位：秒
        stringRedisTemplate.opsForValue().set(token,token,expireTime*2, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 从客户端返回的token中获取用户信息的方法
     */
    public CurrentUser getCurrentUser(String token){
        //判断token是否存在值
        if(StringUtils.isEmpty(token)){
            throw new BusinessException("令牌为空，请进行登录操作!");
        }
        //对token进行解析，获取解析后的token内容
        DecodedJWT decodedJWT = null;
        try{
            decodedJWT = JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new BusinessException("令牌格式错误，请进行登录操作!");
        }
        //从解码后的token中，获取用户信息并且保存到CurrentUser对象中
        int userId = decodedJWT.getClaim(CLAIM_NAME_USERID).asInt();
        String userCode = decodedJWT.getClaim(CLAIM_NAME_USERCODE).asString();
        String userName = decodedJWT.getClaim(CLAIM_NAME_USERNAME).asString();
        //非空判断
        if(StringUtils.isEmpty(userCode) || StringUtils.isEmpty(userName)){
            throw new BusinessException("令牌缺失用户的信息，请进行登录操作!");
        }
        return new CurrentUser(userId,userCode,userName);
    }

}
