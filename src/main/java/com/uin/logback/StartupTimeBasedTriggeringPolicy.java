package com.uin.logback;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import cn.hutool.core.util.ReflectUtil;
import java.util.Date;

public class StartupTimeBasedTriggeringPolicy<E> extends RollingFileAppender<E> {

  @Override
  public void start() {
    SizeAndTimeBasedRollingPolicy rollingPolicy = (SizeAndTimeBasedRollingPolicy)this.getRollingPolicy();
    SizeAndTimeBasedFNATP timeBasedFileNamingAndTriggeringPolicy = (SizeAndTimeBasedFNATP)rollingPolicy.getTimeBasedFileNamingAndTriggeringPolicy();
    try {
      TimeBasedRollingPolicy<E> tbrp=(TimeBasedRollingPolicy) ReflectUtil.getFieldValue(timeBasedFileNamingAndTriggeringPolicy,ReflectUtil.getField(SizeAndTimeBasedFNATP.class,"tbrp"));
      FileNamePattern fileNamePatternWithoutCompSuffix=(FileNamePattern)ReflectUtil.getFieldValue(tbrp,ReflectUtil.getField(TimeBasedRollingPolicy.class,"fileNamePatternWithoutCompSuffix"));
      Date dateInCurrentPeriod =(Date)ReflectUtil.getFieldValue(timeBasedFileNamingAndTriggeringPolicy,ReflectUtil.getField(SizeAndTimeBasedFNATP.class,"dateInCurrentPeriod"));
      int currentPeriodsCounter=(int)ReflectUtil.getFieldValue(timeBasedFileNamingAndTriggeringPolicy,ReflectUtil.getField(SizeAndTimeBasedFNATP.class,"currentPeriodsCounter"));
      String fileName = fileNamePatternWithoutCompSuffix.convertMultipleArguments(dateInCurrentPeriod, currentPeriodsCounter);
      ReflectUtil.setFieldValue(timeBasedFileNamingAndTriggeringPolicy,ReflectUtil.getField(SizeAndTimeBasedFNATP.class,"elapsedPeriodsFileName"),fileName);
      ReflectUtil.setFieldValue(timeBasedFileNamingAndTriggeringPolicy,ReflectUtil.getField(SizeAndTimeBasedFNATP.class,"currentPeriodsCounter"),++currentPeriodsCounter);
      this.rollover();
    }catch (Exception e){
    }
  }
}
