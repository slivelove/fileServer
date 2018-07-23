package com.server.fileServer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import utils.ThreeDESUtil;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServerApplicationTests {

    @Test
    public void contextLoads() {
        File file = new File("D:/upload/","工作安排.txt");
        byte[] bytes = ThreeDESUtil.fileToByte(file);
        //byte[] s = ThreeDESUtil.encryptMode(bytes);  //加密
        byte[] s = ThreeDESUtil.decryptMode(bytes); //解密
        OutputStream os = null;
        try {
            os = new FileOutputStream("D:/upload/工作安排.txt");
            for (int i = 0; i < s.length; i++) {
                os.write((int) s[i]);
                os.flush();
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ok");

    }

}
