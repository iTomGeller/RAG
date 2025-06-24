package com.cyberlanting.qwen_rag.interceptor;

import com.cyberlanting.qwen_rag.common.context.BaseContext;
import com.cyberlanting.qwen_rag.common.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        // 获取Authorization头
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 设置响应的字符编码和内容类型
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 返回401
            response.getWriter().write("缺少有效的认证凭证");
            return false;
        }

        // 去掉 "Bearer " 前缀，获取实际的Token
        String token = authHeader.substring(7);

        try {
//            log.info("正在解析token：{}", token);
            // 去掉 "Bearer " 前缀，获取实际的Token
            Claims claims = JwtUtils.parseJWT(SECRET_KEY, token);  // 解析Token
//            // 将解析后的用户信息放到request的属性中，以便后续使用
//            request.setAttribute("userClaims", claims);
            Long userId = Long.valueOf(claims.get("userId").toString());
//            log.info("解析到的用户ID：{}", userId);
            BaseContext.setCurrentId(userId);
        } catch (Exception e) {
            // 设置响应的字符编码和内容类型
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 返回401
            response.getWriter().write("Token无效或过期");
            return false;
        }
        return true;  // 继续执行下一个拦截器或控制器
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后清理 ThreadLocal，防止内存泄漏或用户污染
        BaseContext.removeCurrentId();
    }

}