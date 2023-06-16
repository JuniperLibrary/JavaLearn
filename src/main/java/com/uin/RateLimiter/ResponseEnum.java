package com.uin.RateLimiter;

import lombok.Getter;

/**
 * @author dingchuan
 */
@Getter
public enum ResponseEnum {
  SUCCESS(0, "OK"),
  PARAMETER_ERROR(1, "参数异常"),
  SYSTEM_ERROR(500, "服务器异常，请联系管理员"),
  RATE_LIMIT(304,"限流中.." );

  ResponseEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  private final Integer code;
  private final String message;
}
