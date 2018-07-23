package utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

public class FileTools {

    /**
     * 清理过期文件
     * @param dirPath
     * @param days
     */
    public static void deleteFiles(String dirPath, int days){
        Date pointDate = new Date();
        long timeInterval = pointDate.getTime() - convertDaysToMilliseconds(days);
        pointDate.setTime(timeInterval);

        // 设置文件过滤条件
        IOFileFilter timeFileFilter = FileFilterUtils.ageFileFilter(pointDate, true);
        IOFileFilter fileFiles = FileFilterUtils.andFileFilter(FileFileFilter.FILE, timeFileFilter);

        // 删除符合条件的文件
        File deleteRootFolder = new File(dirPath);
        Iterator itFile = FileUtils.iterateFiles(deleteRootFolder, fileFiles, TrueFileFilter.INSTANCE);
        while (itFile.hasNext())
        {
            File file = (File)itFile.next();
            file.delete();
        }


    }

    private static long convertDaysToMilliseconds(int days)
    {
        return days * 24L * 3600 * 1000;
    }


    /**
     * 读取外部properties文件
     * @return
     */
    public static Properties getProperties(){
        Properties p = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream("conf/base.properties");
            p.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

}
