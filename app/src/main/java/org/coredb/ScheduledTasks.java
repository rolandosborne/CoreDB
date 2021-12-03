package org.coredb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.coredb.NameRegistry;

import org.coredb.service.ConfigService;
import org.coredb.service.AgentService;

@Component
public class ScheduledTasks {

  private long intervalCount = 0;
  private long configInterval = 60;
  private long agentInterval = 1440;

  @Value("${app.enable.scheduling:true}")
  private boolean enable;
  
  @Autowired
  private ConfigService configService;
  
  @Autowired
  private AgentService agentService;

  @Scheduled(fixedRate = 60000)
  public void reportCurrentTime() {

    if(enable) {
      if(intervalCount % configInterval == 0) {
        configInterval = configService.refresh();
      }
      if(intervalCount % agentInterval == 0) {
        agentInterval = agentService.clearExpired();
      } 
      intervalCount++;
    }
  }
}

