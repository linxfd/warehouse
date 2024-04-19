package com.pn.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 生成验证码
 */
@RequestMapping("/captcha")
@RestController  //相当于@Controller+@RequestBody这两个注解的综合使用
public class VerificationCodeController {

    //注入CaptchaConfig配置类中的验证码规则
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成验证码图片的url接口  /captchaImage
     */
    @GetMapping("/captchaImage")
    public void getKaptchaImage(HttpServletResponse response){
        //输出流
        ServletOutputStream out = null;
        try{
            //禁止浏览器缓存验证码图片的响应头信息
            response.setDateHeader("Expires",0);
            //设置请求头
            response.setHeader("Cache-Control","no-store,no-cache,must-revalidate");
            //添加响应头信息
            response.addHeader("Cache-Control","post-check=0,pre-check=0");
            //禁止缓存
            response.setHeader("Pragma","no-cache");
            //响应正文为jpg图片（生成验证码图片的格式）
            response.setContentType("image/jpeg");
            //生成验证码图片的文本
            String code = captchaProducer.createText();

            //在redis中保存验证码文本
            stringRedisTemplate.opsForValue().set(code,code);

            //在内存中保存验证码图片
            BufferedImage bi = captchaProducer.createImage(code);
            //将验证码的图片响应给前端来显示
            out = response.getOutputStream();
            //bi是内存中所保存的图片
            ImageIO.write(bi,"jpg",out);
            //刷新
            out.flush();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(out != null){
                    //当流使用完毕之后，需要关闭流
                    out.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
