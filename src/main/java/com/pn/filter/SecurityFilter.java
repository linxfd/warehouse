package com.pn.filter;

import com.alibaba.fastjson.JSON;
import com.pn.entity.Result;
import com.pn.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录限制的servlet过滤器类
 */
public class SecurityFilter implements Filter {

    private StringRedisTemplate stringRedisTemplate;

    //生成redis模块的set方法
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 过滤器拦截到请求执行的方法
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        //请求
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        //响应
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        //获取请求的url接口
        String path = request.getServletPath();
        /**
         * 白名单请求，都直接放行
         */
        List<String> urlList = new ArrayList<>();
        //验证码需要设置成白名单请求
        urlList.add("/captcha/captchaImage");
        //登录需要设置成白名单请求
        urlList.add("/login");
        //退出需要设置成白名单
        urlList.add("/logout");
        //如果包含，就说明是白名单请求
        if(urlList.contains(path)){
            //递归
            filterChain.doFilter(request,response);
            //中止函数
            return;
        }
        /**
         * 其他请求都需要校验是否携带了token，以及判断redis中是否存在token的键
         */
        //从前端获取到返回的token
        String clientToken = request.getHeader(WarehouseConstants.HEADER_TOKEN_NAME);
        //对token进行校验，如果通过则请求放行
        if(StringUtils.hasText(clientToken) && stringRedisTemplate.hasKey(clientToken)){
            //递归
            filterChain.doFilter(request,response);
            //中止函数
            return;
        }
        //校验失败（未登录或者token已经过期），向前端响应失败的结果
        Result result = Result.err(Result.CODE_ERR_UNLOGINED,"请进行登录!");
        //将对象转换成json字符串
        String jsonStr = JSON.toJSONString(result);
        //设置响应的正文
        response.setContentType("application/json;charset=UTF-8");
        //使用流输出
        PrintWriter out = response.getWriter();
        out.print(jsonStr);
        out.flush();
        out.close();
    }

}
