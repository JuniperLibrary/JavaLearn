package com.uin.spi;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {

  /**
   * 默认扩展类全路径
   *
   * @return 默认不填是 default
   */
  String value() default URLKeyConst.DEFAULT;
}
