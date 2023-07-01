//package com.uin.RateLimiter.config;
//
//import cn.hutool.json.JSONUtil;
//import com.uin.RateLimiter.ResponseDTO;
//import com.uin.RateLimiter.ResponseEnum;
//import java.io.PrintWriter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
///**
// * @author dingchuan
// */
//@Slf4j
//public abstract class AbstrtactInteceptor extends HandlerInterceptorAdapter {
//
//  @Override
//  public boolean preHandle(HttpServletRequest request,
//      HttpServletResponse response, Object handler) throws Exception {
//    ResponseEnum responseEnum;
//
//    try {
//      responseEnum = preFilter(request);
//    } catch (Exception e) {
//      log.error("prefilter catch an Exception:{}", e);
//      responseEnum = ResponseEnum.SYSTEM_ERROR;
//    }
//    if (ResponseEnum.SUCCESS == responseEnum) {
//      return true;
//    }
//    // 失败直接返回结果
//    handlerResponse(responseEnum, response);
//    return false;
//  }
//
//  private void handlerResponse(ResponseEnum responseEnum, HttpServletResponse response) {
//    ResponseDTO dto = new ResponseDTO();
//    dto.setCode(responseEnum.getCode());
//    dto.setMsg(responseEnum.getMessage());
//    dto.setStatus(HttpServletResponse.SC_OK);
//    dto.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//    PrintWriter writer = null;
//    try {
//      writer = response.getWriter();
//      writer.write(JSONUtil.toJsonStr(dto));
//    } catch (Exception e) {
//      log.error("handlerResponse catch an Exception:{}", e);
//    } finally {
//      if (writer != null) {
//        writer.close();
//      }
//    }
//  }
//
//  protected abstract ResponseEnum preFilter(HttpServletRequest request);
//}
