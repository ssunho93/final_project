package com.example.jeon.tabtest;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.Calendar;

public class DiaryAdapter extends BaseAdapter {
    DBManager dbManager;

    private LayoutInflater mInflater; // layout을 전달 받기 위한 inflater 변수 선언
    private ArrayList<DataST> mRowList; // 데이터를 받기 위한 arrayLait 변수 선언
    private int resourceLayoutId; //adapter 구현을 위한 변수
    DataST datast; // 정해진 데이터 형식 선언
    DisplayImageOptions options; // 라이브러리 옵션 변수
    String profile[];
    String fileBornDay;
    int dDay;
    Context mContext;

    public DiaryAdapter(Context context, int textViewResourceId, ArrayList<DataST> rowList) {
        // adapter 사용을 위한 기본 설정
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceLayoutId = textViewResourceId;
        mRowList = rowList;

        dbManager = new DBManager(context.getApplicationContext()); //PROFILE DB 호출
        profile = dbManager.getProfileData();
        fileBornDay = profile[1];
        Log.i("asdf", fileBornDay);

        // 라이브러리 옵션 설정
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.gray)
                        // .showImageForEmptyUri(R.drawable.empty)
                .showImageOnFail(null)
                .resetViewBeforeLoading(false)
                .cacheInMemory(false)
                .cacheOnDisc(true)
                .considerExifParams(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
    }
    // 리스트뷰에 layout 을 넣기 위한 메소드
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder hold;
        if (convertView == null) {
            convertView = mInflater.inflate(resourceLayoutId, parent, false);

            hold = new holder(); // 재사용 layout 정의
            // layout 연결
            hold.mainImg = (ImageView) convertView.findViewById(R.id.mainImg);

            hold.bornDay = (TextView) convertView.findViewById(R.id.bornDay);

            hold.txtHeight = (TextView) convertView.findViewById(R.id.txtHeight);
            hold.txtWeight = (TextView) convertView.findViewById(R.id.txtWeight);
            hold.txtHead = (TextView) convertView.findViewById(R.id.txtHead);
            hold.mainMemo = (TextView) convertView.findViewById(R.id.mainMemo);

            hold.mainHeight = (TextView) convertView.findViewById(R.id.mainHeight);
            hold.mainWeight = (TextView) convertView.findViewById(R.id.mainWeight);
            hold.mainHead = (TextView) convertView.findViewById(R.id.mainHead);
            hold.mainMemo = (TextView) convertView.findViewById(R.id.mainMemo);
            hold.txtDate = (TextView) convertView.findViewById(R.id.txtDate);

            hold.mainUnitHeight = (TextView)convertView.findViewById(R.id.mainUnitHeight);
            hold.mainUnitWeight = (TextView)convertView.findViewById(R.id.mainUnitWeight);
            hold.mainUnitHead = (TextView)convertView.findViewById(R.id.mainUnitHead);
            Util.setGlobalFont(mContext, convertView);
            convertView.setTag(hold);

        } else {
            hold = (holder) convertView.getTag();
        }

        hold.position = position; // 리스트뷰의 포지션값을 재사용 layout 포지션값으로 설정
        datast = mRowList.get(position); // mRowList에서 해당 리스트뷰 포지션값에 해당하는 데이터를 datast에 대입

        int by, bM, bd , by1, bM1, bd1  ; //DB에서 받은 태어난일을 int 형으로 변환하기 위한 변수
        long nowTime, birthTime; //나이 계산을 위한 변수

        by = Integer.parseInt(fileBornDay.substring(0, 4));
        bM = Integer.parseInt(fileBornDay.substring(4, 6)) - 1;
        bd = Integer.parseInt(fileBornDay.substring(6, fileBornDay.length()));

        by1 = Integer.parseInt(datast.date.substring(0, 4));
        bM1 = Integer.parseInt(datast.date.substring(5, 7)) - 1;
        bd1 = Integer.parseInt(datast.date.substring(8, 10));

        Calendar nowDate = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        nowDate.set(by1,bM1,bd1);
        birthDate.set(by, bM, bd);

        nowTime = nowDate.getTimeInMillis() / 86400000;
        birthTime = birthDate.getTimeInMillis() / 86400000;
        dDay = (int) (nowTime - birthTime) + 1; // 나이계산





        if (hold.position == position) { // 재사용뷰의 포지션값과 getView의 포지션을 같을시 실행

            if(datast.imgurl.equals("null")) {
                hold.mainImg.setVisibility(View.GONE);
            }
            ImageLoader.getInstance().displayImage("file://" + datast.imgurl, hold.mainImg, options); // 라이브러리를 이용하여 이미지뷰 셋팅
            // 데이터값 셋팅
            hold.bornDay.setText(dDay+"일");
            hold.mainHeight.setText(datast.height);
            hold.mainWeight.setText(datast.weight);
            hold.mainHead.setText(datast.head);
            hold.mainMemo.setText(datast.memo);
            hold.txtDate.setText(datast.date);

        }


        return convertView;
    }

    // layout 재사용을 위한 클래스 정의
    private static class holder {

        public ImageView mainImg;
        public TextView bornDay, txtHeight, mainHeight, txtWeight, mainWeight, txtHead, mainHead, mainMemo, txtDate , mainUnitHeight , mainUnitWeight , mainUnitHead;
        public int position;

    }

    @Override
    public int getCount() {
        return mRowList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}