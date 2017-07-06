package com.example.administrator.bmob;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

public class MainActivity extends Activity {
    public EditText et_name,et_age,et_sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Bmob.initialize(this, "a7be8bf1bdb8ac0bc3df7fa75e43081c");
        //自动更新
//        BmobUpdateAgent.initAppVersion();//执行过一次之后就屏蔽或删除
        BmobUpdateAgent.setUpdateOnlyWifi(false);//不管是流量还是wifi情况都提醒
        BmobUpdateAgent.update(this);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                // TODO Auto-generated method stub
                //根据updateStatus来判断更新是否成功
                Log.i("状态值：",updateStatus+","+updateStatus);
                if (updateStatus == UpdateStatus.Yes) {//版本有更新
                    Toast.makeText(MainActivity.this, "有新版本", Toast.LENGTH_SHORT).show();
                }else if(updateStatus == UpdateStatus.No){
                    Toast.makeText(MainActivity.this, "版本无更新", Toast.LENGTH_SHORT).show();
                }else if(updateStatus==UpdateStatus.EmptyField){//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                    Toast.makeText(MainActivity.this, "请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。", Toast.LENGTH_SHORT).show();
                }else if(updateStatus==UpdateStatus.IGNORED){
                    Toast.makeText(MainActivity.this, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                }else if(updateStatus==UpdateStatus.ErrorSizeFormat){
                    Toast.makeText(MainActivity.this, "请检查target_size填写的格式，请使用file.length()方法获取apk大小。", Toast.LENGTH_SHORT).show();
                }else if(updateStatus==UpdateStatus.TimeOut){
                    Toast.makeText(MainActivity.this, "查询出错或查询超时", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_name= (EditText) findViewById(R.id.et_name);
        et_age= (EditText) findViewById(R.id.et_age);
        et_sex= (EditText) findViewById(R.id.et_sex);
        Button addData= (Button) findViewById(R.id.addData);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_name.getText().toString())||TextUtils.isEmpty(et_age.getText().toString())||TextUtils.isEmpty(et_sex.getText().toString())){
                    Toast.makeText(MainActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User();
                    user.setUser(et_name.getText().toString());
                    user.setAge(et_age.getText().toString());
                    user.setSex(et_sex.getText().toString());
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(MainActivity.this, "数据添加成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "数据添加失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        Button readData= (Button) findViewById(R.id.readData);
        readData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobQuery<User> b=new BmobQuery<User>();
                b.order("-createdAt");//按时间顺序排序

                b.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "查询成功：共"+object.size()+"条数据。", Toast.LENGTH_SHORT).show();
                            for (User user : object) {
                                Log.i("info", "名字："+user.getUser()+"，年龄："+user.getAge()+"，性别："+user.getSex());
                            }
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });
            }
        });
    }

    public void upData(View view){
        BmobUpdateAgent.forceUpdate(MainActivity.this);
    }
}
