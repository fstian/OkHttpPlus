package com.socks.sample.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.socks.okhttp.plus.callback.OkHttpCallback;
import com.socks.okhttp.plus.request.OkHttpRequest;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tv_response;
    private TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_response = (TextView) findViewById(R.id.tv_response);
        tv_header = (TextView) findViewById(R.id.tv_header);
    }

    public void getUser(View view) {
        String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/user.gson";
        new OkHttpRequest.Builder().url(url).get(new OkHttpResultCallback<User>() {
            @Override
            public void onError(Request request, Exception e) {
                KLog.e(e.getMessage());
            }

            @Override
            public void onResponse(Response response, User data) {
                tv_response.setText(data.toString());
                KLog.d("code = " + response.code());
            }
        });
    }

    public void getUsers(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "zhy");
        String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/users.gson";
        new OkHttpRequest.Builder().url(url).params(params).post(new OkHttpResultCallback<List<User>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("TAG", "onError , e = " + e.getMessage());
            }

            @Override
            public void onResponse(Response response, List<User> data) {
                Log.e("TAG", "onResponse , users = " + data);
                tv_response.setText(data.get(0).toString());
            }
        });
    }

    public void getSimpleString(View view) {
        String url = "https://raw.githubusercontent.com/hongyangAndroid/okhttp-utils/master/user.gson";
        new OkHttpRequest.Builder().url(url)
                .get(new OkHttpResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Log.e("TAG", "onError , e = " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response, String data) {
                        tv_response.setText(data);
                    }
                });
    }

    public void getHtml(View view) {
        String url = "http://www.csdn.net/";
        new OkHttpRequest.Builder().url(url).get(new OkHttpResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("TAG", "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Response response, String data) {
                tv_response.setText(data);
            }
        });
    }

    public void getHttpsHtml(View view) {
        String url = "https://kyfw.12306.cn/otn/";
        new OkHttpRequest.Builder().url(url).get(new OkHttpResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("TAG", "onError" + e.getMessage());
            }

            @Override
            public void onResponse(Response response, String data) {
                tv_response.setText(data);
            }
        });
    }

    public void uploadFile(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");

        String url = "http://192.168.56.1:8080/okHttpServer/fileUpload";
        new OkHttpRequest.Builder()
                .url(url)
                .params(params)
                .headers(headers)
                .files(new Pair<>("mFile", file))//
                .upload(stringResultCallback);
    }


    public void multiFileUpload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test1.txt");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");

        String url = "http://192.168.1.103:8080/okHttpServer/mulFileUpload";
        new OkHttpRequest.Builder()//
                .url(url)//
                .params(params)
                .files(new Pair<>("mFile", file), new Pair<>("mFile", file2))
                .upload(stringResultCallback);
    }


    public void downloadFile(View view) {
        String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/gson-2.2.1.jar?raw=true";
        new OkHttpRequest.Builder()
                .url(url)
                .destFileDir(Environment.getExternalStorageDirectory().getAbsolutePath())
                .destFileName("gson-2.2.1.jar")
                .download(stringResultCallback);
    }

    public abstract class OkHttpResultCallback<T> extends OkHttpCallback<T> {

        @Override
        public void onBefore(Request request) {
            super.onBefore(request);
            setTitle("loading...");
        }

        @Override
        public void onAfter() {
            super.onAfter();
            setTitle("OkHttpPlus");
        }
    }

    private OkHttpCallback<String> stringResultCallback = new OkHttpResultCallback<String>() {
        @Override
        public void onError(Request request, Exception e) {
            Log.e("TAG", "onError , e = " + e.getMessage());
        }

        @Override
        public void onResponse(Response response, String data) {
            Log.e("TAG", "onResponse , response = " + data);
            tv_response.setText("operate success");
        }

        @Override
        public void inProgress(float progress) {
        }
    };
}