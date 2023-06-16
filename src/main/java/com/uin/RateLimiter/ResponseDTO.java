package com.uin.RateLimiter;

import lombok.Data;

/**
 * @author dingchuan
 */
@Data
public class ResponseDTO {

  private Integer code;
  private String msg;
  private Integer status;
  private String contentType;
}
