package com.flower.spirit.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flower.spirit.service.BiliConfigService;
import com.flower.spirit.service.CookiesConfigService;
import com.flower.spirit.service.FfmpegQueueService;

@Configuration
@Component
public class TaskService {
	
	@Autowired
	private FfmpegQueueService ffmpegQueueService;
	
	@Autowired 
	private BiliConfigService biliConfigService;
	
	@Autowired
	private CookiesConfigService cookiesConfigService;
	
	
	@Scheduled(fixedDelay = 1000*5)
	public void taskCheckStatus() {
		ffmpegQueueService.taskCheckStatus();
	}
	
	@Scheduled(fixedDelay = 1000*5)
	public void taskMergeTasks() {
		ffmpegQueueService.taskMergeTasks();
	}
	
	@Scheduled(cron = "0 0 9 * * ?")
	public void isNeedRefreshAndUpdate() {
		biliConfigService.isNeedRefreshAndUpdate();
		cookiesConfigService.checkCookieStatus();
	}

}
