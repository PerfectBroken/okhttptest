package com.httpstudy.okhttp;

import okhttp3.*;
import okhttp3.internal.Util;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OkHttpTest {
    public static void main(String[] args) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
//        String url = "http://180.76.156.103:8500/v1/kv/application1/key1/key2";
//        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=13_lKhXZSJS2EuIKrN9YVEB2OdQvJHAhFvT4fMQD2npqQvg4Pxt9kK8mUf-AmeiBEPVCwVkdZhqVifZP0wCSokQewnrnBRCWIK6T7rLvgstOq0kl2L2OURlq332tQV208zbA3b0TNwZX7Su-tqNHHMcAGAPPU&media_id=1HeTWgjvmrgMZna1LxihN-yzDMitF2Y5aBcihpZzg2DWt1VFb_1spuHMmzJIhYB2";
//        String url = "http://180.76.156.103:8080?a=12";
//        httpRequest(url);
        String url = "http://10.1.133.138:8080/";
        runHttp(url);

    }

    static void runHttp(String url) throws Exception {
        int i = 10;
        //建造者模式 自定义连接池。默认连接池即支持长链接
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool pool = new ConnectionPool(10, 30 * 1000L, TimeUnit.SECONDS);
//        builder.connectionPool(pool);
//        builder.connectTimeout(10,TimeUnit.SECONDS);
//        builder.readTimeout(10,TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);

        OkHttpClient okHttpClient = builder.build();
        Request request = new Request.Builder().url(url).build();
        
        Response response = null;
        while (i-- > 0) {
            try {
                System.out.println("**********" + --i + "************");
                response = okHttpClient.newCall(request).execute();
                System.out.println(response.body().string());
                Thread.sleep(120 * 1000L);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    static void runHttpAsyncTest(String url) throws Exception {
        String[] URL=new String[2];
        URL[0]="http://localhost:8080/";
        URL[1]="http://180.76.156.103:8080";
        int i = 10;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(10,50,60,TimeUnit.MINUTES,new LinkedBlockingDeque<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
        builder.dispatcher(new Dispatcher(threadPoolExecutor));
//        ConnectionPool pool = new ConnectionPool(10, 60L, TimeUnit.MINUTES);
        OkHttpClient okHttpClient = builder.build();
        Response response = null;
        ResponseBody responseBody = null;
        String result = "";
        while (i-- > 0) {
            try {
                Request request = new Request.Builder().url(URL[i%2]).build();
//                okHttpClient=new OkHttpClient();
                Callback callback=new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            Thread.sleep(1000);
                        }
                        catch (Exception e){
                            System.out.println(e.toString());
                        }
                        System.out.println(response.body().string());
                    }
                };
                okHttpClient.newCall(request).enqueue(callback);
//                System.out.println(i);
//                Thread.sleep(500);
//                okHttpClient.dispatcher().cancelAll();
//                return;
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    static String httpRequest(String requestUrl) {
        StringBuilder buffer = new StringBuilder(1000);

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            String inputString = "";
            while (true) {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                writeFile(inputStream);

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                FileUtils.

                while ((inputString = bufferedReader.readLine()) != null) {
                    buffer.append(inputStream);
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();

                System.out.println(buffer.toString());
                buffer = new StringBuilder(1000);
                return "";
// Thread.sleep(120000);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return buffer.toString();
    }

    public static void writeFile(InputStream inputStream) {
        OutputStream outputStream = null;
        try {
            File file = new File("C:\\watchsale\\writeTest\\aaa.jpg");
            outputStream = new FileOutputStream(file);
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//            inputStreamReader.read
//
//            int
            int byteOffset = 0;
            int byteLength = 2048;
            byte[] bytes = new byte[1024];
//              IOUtils.copy(inputStream,outputStream);
            while ((inputStream.read(bytes, byteOffset, byteLength)) != -1) {
                outputStream.write(bytes);
                byteOffset += byteLength;
            }
            outputStream.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}