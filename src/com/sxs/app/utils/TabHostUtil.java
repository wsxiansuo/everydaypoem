package com.sxs.app.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.sxs.app.everydaypoem.R;
/**
 * 创建生成Tab标签
 * @author xssong
 *
 */
public final class TabHostUtil {
    private TabHostUtil(){
        
    }
    public static List<View> createTabs(Context context, FragmentTabHost tabHost, String[] tabTextArray, int[] tabIconArray, Class<?>[] fragmentArray, View.OnClickListener onItemClickListener){
        int count = fragmentArray.length;  
        List<View> tabs = new ArrayList<View>();
        for (int i = 0; i < count; i++){    
            //为每一个Tab按钮设置图标、文字和内容  
            tabs.add(getTabItemView(context, tabTextArray, tabIconArray, i));
            TabSpec tabSpec = tabHost.newTabSpec(tabTextArray[i]).setIndicator(tabs.get(i));  
            //将Tab按钮添加进Tab选项卡中  
            tabHost.addTab(tabSpec, fragmentArray[i], null);  
            //设置Tab按钮的背景  
            tabHost.getTabWidget().getChildAt(i).setOnClickListener(onItemClickListener);
        }  
        return tabs;
    }
    /**
     * 生成tab的标签
     * @param context
     * @param tabTextArray
     * @param tabIconArray
     * @param index
     * @return
     */
    public static View getTabItemView(Context context, String[] tabTextArray, int[] tabIconArray, int index){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tab_item_view, null); 
        if (tabIconArray != null && tabTextArray.length == tabIconArray.length){
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab_item_icon);
            imageView.setImageResource(tabIconArray[index]);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_tab_item_title);
        textView.setText(tabTextArray[index]);
        return view;   
    }  
}
