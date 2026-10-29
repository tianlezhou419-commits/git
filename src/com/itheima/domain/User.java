package com.itheima.domain;

import java.util.Random;
import java.util.UUID;

//domain装所有的javaBean类
public class User {
    //属性：id、用户名、密码、状态
    private String id;
    private String name;
    private String password;
    private boolean status;//禁用，可用

    public User() {
        //调用createID方法生成id值
        id = createID();

        status = true;
    }

    public User( String name, String password) {
        //id和status在javaBean中直接赋值
        id = createID();
        this.name = name;
        this.password = password;
        status = true;
    }

    // 用户无法设置，是自动生成的，格式为：heima+5位数字的随机数

    public String createID(){
        StringBuilder sb = new StringBuilder("heima");

        Random r = new Random();
        for(int i=0;i<5;i++){
            int num = r.nextInt(10);
            sb.append(num);
        }
        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
