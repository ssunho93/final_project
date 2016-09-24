package com.example.jeon.tabtest;

/**
 * Created by jeon on 2016-07-31.
 */

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;

public class LoginAcitivity extends AppCompatActivity

{
    private DBManager dbManager; //DB에 접속하기 위한 변수
    SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FirebaseMessaging.getInstance().subscribeToTopic("test");
//        FirebaseInstanceId.getInstance().getToken();
        Log.d("KakaoTalkLoginActivity", "onCreate ");
        getAppKeyHash();// 호출

        dbManager = new DBManager(getApplicationContext()); //profile 데이터베이스에 액세스

         dbManager.createTable("CREATE TABLE IF NOT EXISTS PROFILE" +    //PROFILE 테이블이 존재하지 않는다면 테이블 생성
         "(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, day INTEGER, sex INTEGER, image_uri TEXT);");

         /**
         * 카카오톡 로그아웃 요청
         * 한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출
         * 따라서 테스트 용이를 위해 매번 로그아웃
         * */

        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        /**
         * 세션 연결 성공했을때
         */
        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) { //예외처리?? 들어올일 없음 ㅅㄱ
                    String message = "failed to get user info. msg=" + errorResult;
                    Toast.makeText(LoginAcitivity.this, "message", Toast.LENGTH_SHORT).show();
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity(); - Session관리 부분을 따로 클래스로 나눴을때 액티비티 호출을 위한 메소드인데 난 그렇게 안함
                    }
                }


                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(LoginAcitivity.this, "onSessionClosed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNotSignedUp() {
                    Toast.makeText(LoginAcitivity.this, "onNotSignedUP", Toast.LENGTH_SHORT).show();
                }


                /**
                 * 로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴
                 * 사용자 ID는 보안상의 문제로 제공하지 않고 일련번호로 제공
                 * */
                @Override
                public void onSuccess(UserProfile userProfile) {


                    Log.e("UserProfile", userProfile.toString());

//                    String kakaoID = String.valueOf(userProfile.getId()); // 회원 정보들을 이런식으로 가져옴

                    //테스트용
                    String name = userProfile.getNickname();
                    String date = "20160703";
                    int sex = 2;
                    Toast.makeText(LoginAcitivity.this, "onSuccess", Toast.LENGTH_SHORT).show();

                    dbManager.insert("insert into PROFILE values(null,'" + name + "','" + date + "','" + sex + "','no_image');");
                    Intent intent = new Intent(LoginAcitivity.this, MainActivity.class);

//                    intent.putExtra("kakaoID", kakaoID); // 디비에 넘길거면 이부분은 필요없을수도

                    startActivity(intent);
                    finish();
                }
            });

        }

        /**
         * 세션 연결이 실패했을때
         */
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Toast.makeText(LoginAcitivity.this, "onFailed", Toast.LENGTH_SHORT).show();
            Log.i("실패", exception+ "");
        }
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}
