package tw.myapp.ballsiteapp.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleAPIWorker  implements  Runnable{
    // 接收 Request 並在執行完(下載後) 將資料回傳給 Activity(Handler)
    Request request;
    android.os.Handler handler;
    OkHttpClient client ;

    public SimpleAPIWorker(Request request, Handler handler) {
        this.request = request;
        this.handler = handler;
        client = new OkHttpClient();
    }

    @Override
    public void run() {
        String jsonString = null;
        Message m = handler.obtainMessage();
        Bundle bundle = new Bundle();

        try {
            Response response = client.newCall(request).execute();
            // 安全作法應該要判斷是否成功下載 才能取出回傳內容
            if( response.isSuccessful() ) {
                jsonString = response.body().string();
                bundle.putInt("status", 200);
                bundle.putString("data" , jsonString);
            } else {
                bundle.putInt("status", 404);
                bundle.putString("data" , "{'error':404, 'mesg':'下載失敗'}");
            }
            // 準備回傳給呼叫端 ( 某個 Activity )
            m.setData(bundle);
            handler.sendMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}