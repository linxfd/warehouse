package com.pn.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码工具kaptcha的配置类
 */
@Configuration
public class CaptchaConfig {

    /**
     * 配置Producer接口的实现类DefaultKaptcha的bean对象，该对象用于生成验证码图片
     * @return
     */
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean(){
        //创建对象
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        //创建属性集对象（对.properties文件的封装）
        Properties properties = new Properties();
        //将与验证码有关的数据保存到properties对象中
        //验证码图片是否有边框，默认值为true,也可以自己设定值
        properties.setProperty("kaptcha.border","yes");
        //验证码边框的颜色，默认值为Color.BLACK
        properties.setProperty("kaptcha.border.color","105,179,90");
        //验证码文本字符的颜色，默认值为Color.BLACK
        properties.setProperty("kaptcha.textproducer.font.color","blue");
        //验证码图片的宽度，默认值是200
        properties.setProperty("kaptcha.image.width","120");
        //验证码图片的高度，默认值是50
        properties.setProperty("kaptcha.image.height","40");
        //验证码文本字符的大小，默认值为40
        properties.setProperty("kaptcha.textproducer.font.size","32");
        //将验证码进行本地缓存  session/cookie
        properties.setProperty("kaptcha.session.key","kaptchaCode");
        //验证码文本字符的间距，默认值为2
        properties.setProperty("kaptcha.textproducer.char.space","4");
        //验证码文本字符的长度，默认值为5
        properties.setProperty("kaptcha.textproducer.char.length","4");
        //验证码文本字符的字体样式
        properties.setProperty("kaptcha.textproducer.font.names","Arial,Courier");
        //验证码噪点颜色
        properties.setProperty("kaptcha.noise.color","gray");

        //创建配置对象
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
