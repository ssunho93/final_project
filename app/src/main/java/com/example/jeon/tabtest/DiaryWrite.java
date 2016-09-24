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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016-04-14.
 */
public class DiaryWrite extends AppCompatActivity {

    ImageView img;
    Uri imgurl0;
    String imagePath, date;
    EditText writeHeight, writeWeight, writeHead, writeMemo;
    String edit0S, edit1S, edit2S, edit3S;
    DBManager db;

    int requestCode;
    int resultCode;
    private static final String TAG = "Main";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Util.setGlobalFont(this, getWindow().getDecorView());

        img = (ImageView) findViewById(R.id.writeImg);
        writeHeight = (EditText) findViewById(R.id.writeHeight);
        writeWeight = (EditText) findViewById(R.id.writeWeight);
        writeHead = (EditText) findViewById(R.id.writeHead);
        writeMemo = (EditText) findViewById(R.id.writeMemo);


        db = new DBManager(this);

        img.setImageResource(R.drawable.no_image);


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 결과값이 돌아오고, 성공했을 경우만 내부로 진입
        if (resultCode == RESULT_OK && intent != null) {
            imgurl0 = intent.getData();
            final String[] filePathColumn = {MediaStore.Images.Media.DATA};
            final Cursor imageCursor = this.getContentResolver().query(imgurl0, filePathColumn, null, null, null);
            imageCursor.moveToFirst();
            final int columnIndex = imageCursor.getColumnIndex(filePathColumn[0]);
            imagePath = imageCursor.getString(columnIndex);
            imageCursor.close();
            BitmapFactory.Options resize = new BitmapFactory.Options();
            resize.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, resize);

            img.setImageBitmap(bitmap);


        }
    }

    public void writeImageCapture(View v) {
        Intent picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(picture, 0);
        onActivityResult(requestCode, resultCode, picture);
    }

    public void writeGallery(View v) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 0);
        onActivityResult(requestCode, resultCode, gallery);
    }

    public void writePictureDelete(View v) {
        img.setImageBitmap(null);
    }

    public String getDate() {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return SDF.format(date);
    }

    public void writeCancel(View v) {
        finish();
    }

    public void write(View v) {
        date = getDate();

        edit0S = writeHeight.getText().toString().trim();
        edit1S = writeWeight.getText().toString().trim();
        edit2S = writeHead.getText().toString().trim();
        edit3S = writeMemo.getText().toString().trim();

        db.insertDiary(imagePath, edit0S, edit1S, edit2S, edit3S, date);


        finish();
    }

}