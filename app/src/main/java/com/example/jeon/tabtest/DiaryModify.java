package com.example.jeon.tabtest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016-04-23.
 */
public class DiaryModify extends AppCompatActivity {
    // 변수 선언
    LinearLayout linearLayout;
    ImageView imgm;
    Uri imgurl0;
    String imagePath, modifyTxtDate;
    EditText modifyHeight, modifyWeight, modifyHead, modifyMemo;
    String edit0mS, edit1mS, edit2mS, edit3mS, edit4mS;
    DBManager db;

    int requestCode;
    int resultCode;
    private static final String TAG = "Main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Util.setGlobalFont(this, getWindow().getDecorView());


        // xml 과 연결
        linearLayout = (LinearLayout) findViewById(R.id.tv_st_reason);
        imgm = (ImageView) findViewById(R.id.modifyImg);
        modifyHeight = (EditText) findViewById(R.id.modifyHeight);
        modifyWeight = (EditText) findViewById(R.id.modifyWeight);
        modifyHead = (EditText) findViewById(R.id.modifyHead);
        modifyMemo = (EditText) findViewById(R.id.modifyMemo);
        //   modifyTxtDate = (TextView)findViewById(R.id.modifyTxtDate);

        // intent 를 통해 넘어온 값을 각각의 layout에 셋팅
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("img");
        imgm.setImageBitmap(urlToBitmap(intent.getStringExtra("img")));
        modifyHeight.setText(intent.getStringExtra("height"));
        modifyWeight.setText(intent.getStringExtra("weight"));
        modifyHead.setText(intent.getStringExtra("head"));
        modifyMemo.setText(intent.getStringExtra("memo"));
        modifyTxtDate = intent.getStringExtra("date");

        db = new DBManager(this); // db 사용을 위해 선언

    }

    // 이미지 resize 메소드
    public Bitmap urlToBitmap(String String) {
        BitmapFactory.Options resize = new BitmapFactory.Options();
        resize.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(String, resize);

        return bitmap; // resize를 한 이미지를 bitmap 으로 반환
    }


    // 사진 불러오기
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {


        // 결과값이 돌아오고, 성공했을 경우만 내부로 진입
        if (resultCode == RESULT_OK && intent != null) {
            imgurl0 = intent.getData(); // intent 값
            final String[] filePathColumn = {MediaStore.Images.Media.DATA}; // 파일의 간접 경로
            final Cursor imageCursor = this.getContentResolver().query(imgurl0, filePathColumn, null, null, null);
            imageCursor.moveToFirst();
            final int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
            imagePath = imageCursor.getString(columnIndex);
            imageCursor.close();

            Bitmap bitmap = urlToBitmap(imagePath);
            imgm.setImageBitmap(bitmap);


        }
    }

    public void modifyImageCapture(View v) {
        Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(picture, 0);
        onActivityResult(requestCode, resultCode, picture);
    }

    public void modifyGallery(View v) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 0);
        onActivityResult(requestCode, resultCode, gallery);
    }

    public void modifyPictureDelete(View v) {
        imgm.setImageBitmap(null);
    }


    public void modifyCancel(View v) {
        finish();
    }

    public void modify(View v) {


        edit0mS = modifyHeight.getText().toString().trim();
        edit1mS = modifyWeight.getText().toString().trim();
        edit2mS = modifyHead.getText().toString().trim();
        edit3mS = modifyMemo.getText().toString().trim();
        edit4mS = modifyTxtDate;

        db.modify(imagePath, edit0mS, edit1mS, edit2mS, edit3mS, edit4mS);

        finish();
    }
}