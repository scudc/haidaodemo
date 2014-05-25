package com.justone.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ʹ��java.security.MessageDigest��д��һ��������������ȡMD5��
 * @author Talen
 * @see java.security.MessageDigest
 */
public class MD5Util {
        /**  
        * Ĭ�ϵ������ַ�����ϣ�apacheУ�����ص��ļ�����ȷ���õľ���Ĭ�ϵ�������  
        */  
        protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };   
        protected static MessageDigest messagedigest = null;   
        static{   
           try{   
            messagedigest = MessageDigest.getInstance("MD5");   
           }catch(NoSuchAlgorithmException nsaex){   
            System.err.println(MD5Util.class.getName()+"��ʼ��ʧ�ܣ�MessageDigest��֧��MD5Util��");   
            nsaex.printStackTrace();   
           }   
        }   
          

        /**
             * ��getMD5��������һ������Ҫת����ԭʼ�ַ������������ַ�����MD5��
             * @param code ԭʼ�ַ���
             * @return �����ַ�����MD5��
             */
        public static String getMD5(String code) throws Exception {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] bytes = code.getBytes();
                byte[] results = messageDigest.digest(bytes);
                StringBuilder stringBuilder = new StringBuilder();

                for (byte result : results) {
                        //��byte����ת��Ϊ16�����ַ�����stringbuilder��
                        stringBuilder.append(String.format("%02x", result));
                }

                return stringBuilder.toString();
        }
        /**  
        * ��������G����ļ�  
        * @param file  
        * @return  
        * @throws IOException  
        */  
        public static String getFileMD5String(File file) throws IOException {   
           FileInputStream in = new FileInputStream(file);   
           FileChannel ch = in.getChannel();   
           MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());   
           messagedigest.update(byteBuffer);   
           return bufferToHex(messagedigest.digest());   
        }   
        private static String bufferToHex(byte bytes[]) {   
                   return bufferToHex(bytes, 0, bytes.length);   
                }   
                  
                private static String bufferToHex(byte bytes[], int m, int n) {   
                   StringBuffer stringbuffer = new StringBuffer(2 * n);   
                   int k = m + n;   
                   for (int l = m; l < k; l++) {   
                    appendHexPair(bytes[l], stringbuffer);   
                   }   
                   return stringbuffer.toString();   
                }   
                  
                  
                private static void appendHexPair(byte bt, StringBuffer stringbuffer) {   
                   char c0 = hexDigits[(bt & 0xf0) >> 4];   
                   char c1 = hexDigits[bt & 0xf];   
                   stringbuffer.append(c0);   
                   stringbuffer.append(c1);   
                }   
                  
                public static boolean checkPassword(String password, String md5PwdStr) {   
                   String s = getMD5String(password);   
                   return s.equals(md5PwdStr);   
                }   
                public static String getMD5String(String s) {   
                           return getMD5String(s.getBytes());   
                        }   
                          
                        public static String getMD5String(byte[] bytes) {   
                           messagedigest.update(bytes);   
                           return bufferToHex(messagedigest.digest());   
                        }   
        /** */
        /**
             * main�������ڲ���
             */
        public static void main(String[] args) {
                // TODO Auto-generated method stub
                //�ַ�'a'��MD5����0cc175b9c0f1b6a831c399e269772661,����ǣ���ɹ���
                try {
                        System.out.println(MD5Util.getMD5("a"));
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
}