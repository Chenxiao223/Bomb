package com.example.administrator.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/7/4 0004.
 */
public class User extends BmobObject {
    private String user;
    private String sex;
    private String age;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
