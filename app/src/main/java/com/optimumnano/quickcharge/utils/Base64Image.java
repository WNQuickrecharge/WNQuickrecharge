package com.optimumnano.quickcharge.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64Image {

    //图片转化成base64字符串  
    public static String image2Base64Str(String imgPath) {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组  
        try {
            in = new FileInputStream(imgPath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码  
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串  
    }

    //base64字符串转化成图片  
    public static boolean Base642Image(String imgStr,String outPath) {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空  
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码  
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(outPath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //base64字符串转化成Bitmap
    public static Bitmap Base64ToBitmap(String imgStr) {   //对字节数组字符串进行Base64解码并生成Bitmap
        if (imgStr == null) //图像数据为空
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            return Bytes2Bimap(b);
        } catch (Exception e) {
            return null;
        }
    }

    private static Bitmap Bytes2Bimap(byte[] b){
        if(b.length!=0){
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        else {
            return null;
        }
    }
}  