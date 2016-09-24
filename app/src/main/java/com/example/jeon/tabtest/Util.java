package com.example.jeon.tabtest;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 재학 on 2016-06-10.
 */
public class Util {
    public static void setGlobalFont(Context context,View view){
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) view;
                int len = vg.getChildCount();
                for (int i = 0; i < len; i++) {
                    View v = vg.getChildAt(i);
                    if (v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "2.ttf"));
                    }
                    setGlobalFont(context, v);
                }
            }
        } else {
        }

    }
}
