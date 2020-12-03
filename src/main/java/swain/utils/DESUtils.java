package swain.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

public class DESUtils {
    private static final String DES_PRIVATE_KEY= "bOwYXyf8";

    /**
     * @param args
     */
    public static void main(String[] args) {
        String content = "13126157654";
        System.out.println("加密前：" + content);
        String encryResult = encrypt(content);
        System.out.println("加密后：" + encryResult);
    }

    /**
     * DES加密
     * @param content
     * @param passwd
     * @return
     */
    public static String encrypt(String content, String passwd) {
        try {
            DESKeySpec keySpec = new DESKeySpec(passwd.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(keySpec.getKey()));
            byte[] result = cipher.doFinal(content.getBytes());
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        try {

            byte[] contentByte = Base64.getDecoder().decode(content);

            DESKeySpec keySpec = new DESKeySpec(DES_PRIVATE_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(DES_PRIVATE_KEY.getBytes()));
            return new String(cipher.doFinal(contentByte), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 加密
     * @param content
     * @return
     */
    public static String encrypt(String content){
        return encrypt(content,DES_PRIVATE_KEY);
    }
}
