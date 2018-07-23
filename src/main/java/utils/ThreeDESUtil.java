package utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ThreeDESUtil {

    public static String Algorithm = "DESede";
    public static String PASSWORD_CRYPT_KEY = "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4";

    /**
     * 加密
     * @param file
     * @return
     */
    public static byte[] encryptMode(byte[] file){
        try {
            SecretKey desKey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            return cipher.doFinal(file);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param file
     * @return
     */
    public static byte[] decryptMode(byte[] file) {
        try {
            SecretKey desKey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            return cipher.doFinal(file);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把文件转成byte数组
     * @param file
     * @return
     */
    public static byte[] fileToByte(File file){
        try {
            FileInputStream fis = new FileInputStream(file);
            if (file.length() > Integer.MAX_VALUE) {
                return null;
            }
            int Length = (int) file.length();
            byte[] bytes = new byte[Length];
            fis.read(bytes);
            fis.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("utf-8");
        /*
         * 执行数组拷贝
         * System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    /**
     * byte转换成十六进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < buf.length; i++){
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 十六进制转换成把byte
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr){
        if(hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
