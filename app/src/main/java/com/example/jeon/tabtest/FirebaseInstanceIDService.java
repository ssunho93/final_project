package com.example.jeon.tabtest;

/**
 * Created by g on 2016-07-26.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    DBManager dbManager;
    String[] data;
    int by, bM, bd, dDay;

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        dbManager = new DBManager(getApplicationContext());
        data = dbManager.getProfileData(); //Data를 받아서 변수 data에 저장
//        long nowTime, birthTime; //나이 계산을 위한 변수
//
//        by = Integer.parseInt(data[1].substring(0, 4));
//        bM = Integer.parseInt(data[1].substring(4, 6)) - 1;
//        bd = Integer.parseInt(data[1].substring(6, data[1].length()));
//
//        Calendar nowDate = Calendar.getInstance();
//        Calendar birthDate = Calendar.getInstance();
//
//        birthDate.set(by, bM, bd);
//
//        nowTime = nowDate.getTimeInMillis() / 86400000;
//        birthTime = birthDate.getTimeInMillis() / 86400000;
//        dDay = (int) (nowTime - birthTime) + 1; // 나이계산

        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token  + "date"+ data[1]);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://wast4545.dothome.co.kr/fcm/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
