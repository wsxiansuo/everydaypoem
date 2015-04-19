package com.sxs.app.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sxs.app.everydaypoem.R;

/**
 * 修改显示
 * @author xssong
 *
 */
public class IconTextView extends RelativeLayout {
    
    private TextView titleTV;
    private ImageView iconIV;
    
    public IconTextView(Context context){
        super(context);
    }
    
    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.icon_text_view_attrs);
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_icon_textview, this);
        // 标题
        titleTV = (TextView)findViewById(R.id.tv_icon_title_view);
        String title = a.getString(R.styleable.icon_text_view_attrs_icon_title);
        titleTV.setText(title);
        iconIV = (ImageView) findViewById(R.id.iv_icon_view);
        iconIV.setImageResource(a.getResourceId(R.styleable.icon_text_view_attrs_icon_bg, R.drawable.ic_bs_cstd_nor));
        a.recycle();
    }
    public void setText(String text){
        if (titleTV != null){
            titleTV.setText(text);
        }
    }
    public void setIcon(int resourceId){
        if (iconIV != null){
            iconIV.setBackgroundResource(resourceId);
        }
    }

}
