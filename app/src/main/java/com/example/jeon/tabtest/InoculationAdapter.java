package com.example.jeon.tabtest;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-06-06.
 */
public class InoculationAdapter extends BaseAdapter {
    private LayoutInflater mInflater; // layout을 전달 받기 위한 inflater 변수 선언
    private ArrayList<String> mRowList; // 데이터를 받기 위한 arrayLait 변수 선언
    private int resourceLayoutId; //adapter 구현을 위한 변수
    String data;
    int age , day;

    public InoculationAdapter(Context context, int textViewResourceId, ArrayList<String> rowList, int age, int day) {
        // adapter 사용을 위한 기본 설정
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resourceLayoutId = textViewResourceId;
        mRowList = rowList;
        Log.i("aaaa", mRowList.get(0));
        this.age = age;
        this.day = day;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder hold;
        if (convertView == null) {
            convertView = mInflater.inflate(resourceLayoutId, parent, false);

            hold = new holder(); // 재사용 layout 정의
            // layout 연결

            hold.inoculationName = (TextView) convertView.findViewById(R.id.inoculationName);
            hold.inoculationDday = (TextView) convertView.findViewById(R.id.inoculationDday);

            convertView.setTag(hold);
        } else {
            hold = (holder) convertView.getTag();
        }


        hold.position = position; // 리스트뷰의 포지션값을 재사용 layout 포지션값으로 설정
        data = mRowList.get(position); // mRowList에서 해당 리스트뷰 포지션값에 해당하는 데이터를 datast에 대입

        Log.i("asdqwe", data);

        if (hold.position == position) { // 재사용뷰의 포지션값과 getView의 포지션을 같을시 실행

            hold.inoculationName.setText(data);
            hold.inoculationDday.setText(day - age + " 일"+"남았습니다.");

        }


        return convertView;
    }



    private static class holder {


        public TextView inoculationName , inoculationDday;
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