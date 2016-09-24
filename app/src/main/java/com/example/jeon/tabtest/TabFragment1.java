package com.example.jeon.tabtest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class TabFragment1 extends Fragment {

    DBManager dbManager;
    String[] data;  //DB에서 불러온 data를 저장할 스트릴 배열 선언
    Intent intent;
    ImageView image; //프로필 사진이 보여질 ImageView
    String filePath = Environment.getExternalStorageDirectory() //프로필 사진이 저장되는 폴더
            + "/dodam/temp.jpg";
    TextView txtInoculation;
    ArrayList<String> inoculationList;
    private InoculationAdapter inoculationAdapter; //
    private ListView inoculationListView;
    private LayoutInflater inoculationInflater;
    int by, bM, bd, dDay;
    Dialog dialog;
    View rootView, inoculationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbManager = new DBManager(getActivity().getApplicationContext()); //PROFILE DB 호출
        rootView = inflater.inflate(R.layout.tab_fragment_1, container, false); //home_fragment 레이아웃과 연동
        Button btn_image_select = (Button) rootView.findViewById(R.id.btn_image_select); //프로필 사진을 선택하기위해 갤러리 호출 버튼
        TextView tv_profile = (TextView) rootView.findViewById(R.id.tv_profile); // //프로필을 보여줄 TextView
        image = (ImageView) rootView.findViewById(R.id.imageview);
        txtInoculation = (TextView) rootView.findViewById(R.id.txtInoculation);


        data = dbManager.getProfileData(); //Data를 받아서 변수 data에 저장
        File file = new File(filePath); //프로필 사진이 있는 경로

        if (file.exists() == true) {    //프로필 사진 존재여부 확인

            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            image.setImageBitmap(selectedImage);

        } else {
            image.setImageResource(R.drawable.no_image); //프로필 사진이 없으면 지정된 이미지 보여주기
        }

        //DB에서 받은 태어난일을 int 형으로 변환하기 위한 변수
        long nowTime, birthTime; //나이 계산을 위한 변수

        by = Integer.parseInt(data[1].substring(0, 4));
        bM = Integer.parseInt(data[1].substring(4, 6)) - 1;
        bd = Integer.parseInt(data[1].substring(6, data[1].length()));

        Calendar nowDate = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        birthDate.set(by, bM, bd);

        nowTime = nowDate.getTimeInMillis() / 86400000;
        birthTime = birthDate.getTimeInMillis() / 86400000;
        dDay = (int) (nowTime - birthTime) + 1; // 나이계산


        if (data[2].equals("1")) {
            data[2] = "남";
        } else {
            data[2] = "여";
        }

        tv_profile.setText(data[0] + "  " + data[2] + "\n" + dDay + "일 째"); //프로필 보여주기

        btn_image_select.setOnClickListener(new View.OnClickListener() { //프로필 사진 선택 버튼
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_PICK);    //갤러리 호출
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                pictureCrop(intent); //선택한 사진 잘라내기 기능 활성

                startActivityForResult(intent, 1); //프로필 사진 설정
            }
        });


        txtInoculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inoculation(dDay);
            }
        });


        Util.setGlobalFont(getActivity().getApplicationContext(), rootView);
        return rootView;
    }

    private void inoculationAdap(ArrayList<String> inoculationList,  int age , int day) {


        inoculationAdapter = new InoculationAdapter(getActivity().getApplicationContext(), R.layout.inoculation_list_row, inoculationList , age , day);
        inoculationListView = (ListView) rootView.findViewById(R.id.inoculationList);
        inoculationInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inoculationListView.setAdapter(inoculationAdapter);

        Log.i("asdf", inoculationList.get(1));

       /* dialog = new Dialog(getActivity()) ;
        dialog.setContentView(R.layout.inoculation_main);
        dialog.setTitle("커스텀 다이얼로그");
        dialog.show();*/

    }


    private void inoculation(int age) {

        inoculationList = new ArrayList<>();
        if (age < 30) {

            inoculationList.add("결핵");
            inoculationList.add("B형간염");
            inoculationAdap(inoculationList , age , 30);

        } else if (age < 60) {

            inoculationList.add("B형간염");
            inoculationAdap(inoculationList , age , 60);

        } else if (age < 90) {

            inoculationList.add("뇌수막염");
            inoculationList.add("소아마비");
            inoculationList.add("폐렴구균");
            inoculationList.add("DTaP-IPV");
            inoculationAdap(inoculationList , age , 90);


        } else if (age < 120) {
            inoculationList.add("폐렴구균");
            inoculationList.add("DTaP-IPV");
            inoculationAdap(inoculationList , age , 120);

        } else if (age < 150) {
            inoculationList.add("뇌수막염");
            inoculationAdap(inoculationList , age , 150);

        } else if (age < 180) {

            inoculationList.add("수두");
            inoculationList.add("MMR");
            inoculationAdap(inoculationList , age , 180);
        }


    }

    private void pictureCrop(Intent intent) {

        intent.putExtra("crop", "true");        // Crop기능 활성화
        intent.putExtra("aspectX", image.getWidth());
        intent.putExtra("aspectY", image.getHeight());  //ImageView의 비율에 맞게 잘라내기
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile()));     // 임시파일 생성
        intent.putExtra("outputFormat",         // 포맷방식
                Bitmap.CompressFormat.JPEG.toString());

    }

    private File getTempFile() {

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File f, dir;

        dir = new File(sdPath, "dodam");

        dir.mkdir();    //dodam 폴더 생성

        f = new File(dir, "temp.jpg");

        try {
            f.createNewFile();  //dodam폴더에 temp.jpg 을 생성
        } catch (IOException e) {
            Log.d("asf", "" + e);
        }
        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            Bitmap selectedImage = BitmapFactory.decodeFile(filePath); //dodam폴더의 temp.jpg를 비트맵 형식으로 저장
            image.setImageBitmap(selectedImage); //프로필 사진 설정


        }
    }
}