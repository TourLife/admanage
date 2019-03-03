package com.example.admanage.controller;

import com.example.admanage.constants.Constants;
import com.example.admanage.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @program: admanage
 * @description: 基础controller
 * @author: BaoXu2
 * @create: 2018-10-08 18:45
 **/
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取session中的用户信息
     *
     * @return
     */
    protected User getLoginUserInfo() {
        HttpSession session = getSession();
        User userInfo = (User) session.getAttribute(Constants.SESSION_USER_KEY);

        return userInfo;
    }

    /**
     * 不使用参数方式而直接获取session
     *
     * @return
     */
    protected HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception e) {
            logger.error("通过getSession方法获取session失败！");
        }
        return session;
    }

    /**
     * 不使用参数方式而直接获取request
     *
     * @return
     */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    /**
     * 文件上传具体实现方法;
     *
     * @param file
     * @return
     */
    public String handleFileUpload(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String imgType = file.getContentType().split("/")[1];
                String filename = System.currentTimeMillis() + "."+imgType;
                String rootPath = BaseController.class.getClassLoader().getResource("").getFile();
                Path path = Paths.get(new File(rootPath).getAbsolutePath()+File.separator + Constants.UPLOADED_FOLDER + filename);
                logger.info("保存图片路径为---"+path);
                Files.write(path, bytes);
                return filename;
            } catch (IOException e) {
                e.printStackTrace();
                return "fail";
            }
        } else {
            return "fail";
        }
    }
    public static double div(double num1,double num2,int scale){
        if (scale<0) {
            throw new IllegalArgumentException("参数异常。。。");
        }
        BigDecimal v1=BigDecimal.valueOf(num1);
        BigDecimal v2=BigDecimal.valueOf(num2);
        return v1.divide(v2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double multiply(double num1,double num2,int scale){
        if (scale<0) {
            throw new IllegalArgumentException("参数异常。。。");
        }
        BigDecimal v1 = BigDecimal.valueOf(num1);
        BigDecimal v2 = BigDecimal.valueOf(num2);
        return v1.multiply(v2).setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double add(double num1,double num2){
        BigDecimal v1=BigDecimal.valueOf(num1);
        BigDecimal v2=BigDecimal.valueOf(num2);
        return v1.add(v2).doubleValue();
    }

    public static double sub(double num1,double num2){
        BigDecimal v1=BigDecimal.valueOf(num1);
        BigDecimal v2=BigDecimal.valueOf(num2);
        return v1.subtract(v2).doubleValue();
    }
    public static double changeDecimal(double d,int scale){
        BigDecimal bg = new BigDecimal(d);
        double value = bg.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    /*
    获取精确到秒的时间戳
    @param date
    @return
    */
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime()/1000);
        return Integer.valueOf(timestamp);
    }

    public static void main(String[] args){
        System.out.println(getSecondTimestamp(new Date()));
    }
}
