/**
 * Logback: the generic, reliable, fast and flexible logging framework.
 * 
 * Copyright (C) 1999-2006, QOS.ch
 * 
 * This library is free software, you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation.
 */
package ch.qos.logback.classic;


import java.net.InetAddress;

import junit.framework.TestCase;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.turbo.NOPTurboFilter;
import ch.qos.logback.core.appender.NOPAppender;

public class LoggerPerfTest extends TestCase {

  final static String KAL = "kal";
  String localhostName = null;
  static long RUN_LENGTH = 1000*1000*10;
  
  public void setUp() throws Exception {
    localhostName = InetAddress.getLocalHost().getCanonicalHostName();
  }
  public void testBasic() {
    loopBasic(RUN_LENGTH);
    double avg = loopBasic(RUN_LENGTH); 
    
    System.out.println("Running on "+localhostName);
    // check for performance on KAL only
    if(KAL.equals(localhostName)) {
      assertTrue(30 > avg);
    }
    System.out.println("Average log time for disabled statements: "+avg+" nanos.");
  }
  
  public void testParameterized() {

    loopBasic(RUN_LENGTH);
    double avg = loopParameterized(RUN_LENGTH); 
    
    System.out.println("Running on "+localhostName);
    // check for performance on KAL only
    if(KAL.equals(localhostName)) {
      assertTrue(30 > avg);
    }
    System.out.println("Average log time for disabled (parameterized) statements: "+avg+" nanos.");
  }
  
  
  public void testNOPFilter() {
    loopNopFilter(RUN_LENGTH);
    double avg = loopNopFilter(RUN_LENGTH);
    
    System.out.println("Running on "+localhostName);
    System.out.println("Average log time for disabled (NOPFilter) statements: "+avg+" nanos.");
    // check for performance on KAL only
    if(KAL.equals(localhostName)) {
      assertTrue(80 > avg);
    }

  }
  
  double loopBasic(long len) {
    LoggerContext lc = new LoggerContext();
    NOPAppender<LoggingEvent> mopAppender = new NOPAppender<LoggingEvent>();
    mopAppender.start();
    Logger logger = lc.getLogger(this.getClass());
    logger.setLevel(Level.OFF);
    for(long i = 0; i < len; i++) {
      logger.debug("Toto");
    }
    long start = System.nanoTime();
    for(long i = 0; i < len; i++) {
      logger.debug("Toto");
    }
    long end = System.nanoTime();
    return (end-start)/len;
  }

  double loopParameterized(long len) {
    LoggerContext lc = new LoggerContext();
    NOPAppender<LoggingEvent> mopAppender = new NOPAppender<LoggingEvent>();
    mopAppender.start();
    Logger logger = lc.getLogger(this.getClass());
    logger.setLevel(Level.OFF);
    for(long i = 0; i < len; i++) {
      logger.debug("Toto {}", i);
    }
    long start = System.nanoTime();
    for(long i = 0; i < len; i++) {
      logger.debug("Toto {}", i);
    }
    long end = System.nanoTime();
    return (end-start)/len;
  }
  
  double loopNopFilter(long len) {
    LoggerContext lc = new LoggerContext();
    NOPAppender<LoggingEvent> mopAppender = new NOPAppender<LoggingEvent>();
    NOPTurboFilter nopFilter = new NOPTurboFilter();
    nopFilter.setName("nop");
    mopAppender.start();
    lc.addTurboFilter(nopFilter);
    Logger logger = lc.getLogger(this.getClass());
    logger.setLevel(Level.OFF);
    for(long i = 0; i < len; i++) {
      logger.debug("Toto");
    }
    long start = System.nanoTime();
    for(long i = 0; i < len; i++) {
      logger.debug("Toto");
    }
    long end = System.nanoTime();
    return (end-start)/len;
  }
  
}
