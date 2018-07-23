package com.server.fileServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import utils.FileTools;
import utils.ThreeDESUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

@Controller
@RequestMapping("/fileService")
public class FileHandleController {

    private static final Logger logger = LoggerFactory.getLogger(FileHandleController.class);

    @RequestMapping("file")
    public String file(){
        return "/file";
    }

    /**
     * 文件上传
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public String uploadFile(HttpServletRequest request) {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
        Iterator<String> iter = multiRequest.getFileNames();
        MultipartFile file = multiRequest.getFile(iter.next());
        if(file.isEmpty()){
            return "文件为空";
        }
        String fileFullName = file.getOriginalFilename();  //获取文件名
        String fileName = fileFullName.substring(0,fileFullName.indexOf("."));
        String suffix = fileFullName.substring(fileFullName.indexOf(".")); //获取文件后缀
        String timeMillis = String.valueOf(System.currentTimeMillis());
        Random random = new Random();  //设置六位随机数
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        String modifyFileName = fileName+timeMillis+result+suffix;  //存入文件服务器的文件名
        logger.info("上传的文件名为：" + modifyFileName);
        Properties p = FileTools.getProperties();
        String path = p.getProperty("path") ;
        String realmName = p.getProperty("realmName");
        File dest = new File(path + "/" + modifyFileName);
        //判断文件是否已经存在
        if (dest.exists()) {
            return "文件已存在";
        }
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //文件上传
            byte[] bytes = ThreeDESUtil.fileToByte(dest);
            byte[] s = ThreeDESUtil.encryptMode(bytes);
            OutputStream os = new FileOutputStream(path + "/" + modifyFileName);
            for (int i = 0; i < s.length; i++) {
                os.write(s[i]);
                os.flush();
            }
            os.close();
            byte[] modifyFileNameBytes = ThreeDESUtil.encryptMode(modifyFileName.getBytes("UTF8"));
            modifyFileName= ThreeDESUtil.parseByte2HexStr(modifyFileNameBytes);
            String url = realmName+"fileService/download/"+modifyFileName;
            return url;
        }  catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
    }

    /**
     * 文件下载
     * @param fileName
     * @param response
     * @return
     */
    @RequestMapping(value = "/download/{fileName}")
    @ResponseBody
    public String downloadFile(@PathVariable String fileName,HttpServletResponse response) {
        try {
            byte[] fileNameBytes = ThreeDESUtil.decryptMode(ThreeDESUtil.parseHexStr2Byte(fileName));
            fileName = new String(fileNameBytes,"UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (fileName != null) {
            Properties p = FileTools.getProperties();
            String path = p.getProperty("path") ;
            File file = new File(path , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                byte[] bytes = ThreeDESUtil.fileToByte(file);
                byte[] buffer = ThreeDESUtil.decryptMode(bytes);
                InputStream is = null;
                BufferedInputStream bis = null;
                try {
                    is = new ByteArrayInputStream(buffer);
                    bis = new BufferedInputStream(is);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    logger.info("文件下载成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
