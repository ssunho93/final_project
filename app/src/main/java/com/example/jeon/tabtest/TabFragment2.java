package com.example.jeon.tabtest;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class TabFragment2 extends Fragment {
    View rootView;
    DBManager db;
    Button btn_diary_delete;
    FloatingActionButton btn_diary_write;
    ImageView mainImg;
    private DiaryAdapter mAdapter; //
    private ListView mListView;
    private ArrayList<DataST> mRowList;
    SQLiteDatabase diary;
    private boolean mLockListView;
    private LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.tab_fragment_2, container, false);

        db = new DBManager(getActivity().getApplicationContext());
        // 멤버 변수 초기화
        initImageLoader(getActivity().getApplicationContext());

        mRowList = new ArrayList<DataST>();
        mLockListView = true;
        mRowList = db.selectDB();
        // 어댑터와 리스트뷰 초기화

        mainImg = (ImageView) rootView.findViewById(R.id.mainImg);

        btn_diary_write = (FloatingActionButton) rootView.findViewById(R.id.btn_diary_write);
        btn_diary_delete = (Button) rootView.findViewById(R.id.btn_diary_delete);

        btn_diary_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiaryWrite.class);
                startActivity(intent);
                Log.i("sdf", "sgdg");
            }
        });

        btn_diary_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diary = db.getWritableDatabase();
                db.onUpgrade(diary, 1, 2);
                diary.close();
            }
        });


        mAdapter = new DiaryAdapter(getActivity().getApplicationContext(), R.layout.diary_item, mRowList);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        //  Log.i(TAG , mRowList.toString());
        // 푸터를 등록합니다. setAdapter 이전에 해야 합니다.
        mInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //    mListView.addFooterView(mInflater.inflate(R.layout.footer, null));

        // 스크롤 리스너를 등록합니다. onScroll에 추가구현을 해줍니다.
        //  mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        registerForContextMenu(mListView);

        // 데미데이터를 추가하기 위해 임의로 만든 메서드 호출
        // addItems(15);
        Util.setGlobalFont(getActivity().getApplicationContext(), rootView);


        return rootView;
    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void onCreateContextMenu(ContextMenu menu, View w, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, w, menuInfo);
        menu.setHeaderTitle("육아일기 편집");
        menu.add(0, 0, 0, "수정");
        menu.add(0, 1, 0, "삭제");
        menu.add(0, 2, 0, "공유하기");

    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo infoo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = infoo.position;


        switch (item.getItemId()) {
            case 0: // 수정


                Intent intent = new Intent(getActivity().getApplicationContext(), DiaryModify.class);

                intent.putExtra("img", mRowList.get(index).imgurl);
                intent.putExtra("height", mRowList.get(index).height);
                intent.putExtra("weight", mRowList.get(index).weight);
                intent.putExtra("head", mRowList.get(index).head);
                intent.putExtra("memo", mRowList.get(index).memo);
                intent.putExtra("date", mRowList.get(index).date);


                startActivity(intent);


                return true;

            case 1: // 삭제

                db.delete(mRowList.get(index).date);

                mRowList.remove(index);

                mAdapter.notifyDataSetChanged();

                return true;


            case 2: // 공유하기

                //  UploadImageToServer up = new UploadImageToServer();
                // up.getImgpath(mRowList.get(index).imgurl);
                //up.execute("http://vldmlzkf.dothome.co.kr/fileUpload.php");

                return true;



            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();

        db = new DBManager(getActivity());
        // 멤버 변수 초기화

        mRowList = new ArrayList<DataST>();
        mLockListView = true;
        mRowList = db.selectDB();
        // 어댑터와 리스트뷰 초기화


        mAdapter = new DiaryAdapter(getActivity().getApplicationContext(), R.layout.diary_item, mRowList);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        //  Log.i(TAG , mRowList.toString());
        // 푸터를 등록합니다. setAdapter 이전에 해야 합니다.
        mInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mListView.addFooterView(mInflater.inflate(R.layout.footer, null));

        // 스크롤 리스너를 등록합니다. onScroll에 추가구현을 해줍니다.
        //   mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);

        Log.i("start", "onResume");
    }








    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}