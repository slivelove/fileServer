package com.server.fileServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utils.FileTools;
import java.util.Properties;

@Component
public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    /**
     * 清理过期文件的定时任务
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void deleteOFDFile(){
        logger.info("定时任务，现在时间："+System.currentTimeMillis());
        Properties p = FileTools.getProperties();
        String dirPath = p.getProperty("path");
        int days = Integer.parseInt(p.getProperty("days"));
        FileTools.deleteFiles(dirPath,days);
    }

}
