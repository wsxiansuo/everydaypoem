package com.sxs.app.everydaypoem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.data.PoemTypeVO;

public class MainActivity extends Activity {

	private int[] colors = {R.drawable.main_corner_0,R.drawable.main_corner_1,R.drawable.main_corner_2,R.drawable.main_corner_3,R.drawable.main_corner_4,R.drawable.main_corner_5};
	private String[] menus = {"中华好诗词","古典藏经阁","诗词名人录","诗词大闯关","诗词大搜索","妙手偶得集"};
	private List<PoemTypeVO> menuList;
	
	
	
	@ViewInject(R.id.lv_main_list) 		private ListView listView; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		
		initConment();
	}
	
	public void initConment() {  
		menuList = getMainMenuList();
		ArrayAdapter<PoemTypeVO> adapter = new ArrayAdapter<PoemTypeVO>(this,R.layout.main_color_button
        		,menuList){
        	@Override   
        	public View getView(int position, View convertView, ViewGroup parent) {   
        		
        		 if(convertView==null){  
                     convertView = getLayoutInflater().inflate(R.layout.main_color_button, parent, false);  
                 }  
		  		 RelativeLayout  layout= (RelativeLayout) convertView.findViewById(R.id.main_button_color);  
		  		 PoemTypeVO item = (PoemTypeVO)getItem(position);
		  		 layout.setBackgroundResource(item.color);
		  		 Button btn = (Button) convertView.findViewById(R.id.main_button_btn);
		  		 btn.setText(item.type_name);
		  		 btn.setTag(item.id);
		  		 btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = null;
						Integer i  = Integer.parseInt(v.getTag().toString());
						switch (i) {
						case 0:
							intent = new Intent(MainActivity.this, PMGoodPoemTitleActivity.class);
							break;
						case 1:
							intent = new Intent(MainActivity.this, PMBooksMainActivity.class);
							intent.putExtra("title","古典藏经阁");
							intent.putExtra("type","1");
							break;	
						case 2:
							intent = new Intent(MainActivity.this, PMBooksMainActivity.class);
							intent.putExtra("title","诗词名人录");
							intent.putExtra("type","3");
							break;
						case 4:
							intent = new Intent(MainActivity.this, PMPoemSearchActivity.class);
							break;
							
						default:
							intent = new Intent(MainActivity.this, PMGoodPoemTitleActivity.class);
							break;
						}
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
					}
				});
                 return convertView;  
        	}
        	
        };  
        listView.setAdapter(adapter); 
       
        
    }  
	
	/**
	 * 获取主的列表
	 * @return
	 */
	private List<PoemTypeVO> getMainMenuList()
	{
		ArrayList<PoemTypeVO> types = new ArrayList<PoemTypeVO>();  
		for(int i=0;i<menus.length;i++)
		{
			PoemTypeVO item = new PoemTypeVO();
			item.type_name = menus[i];
			item.id = i;
			item.color = colors[i < colors.length ? i : (i%colors.length)];
			types.add(item);
		}
		
		return types;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 @Override  
    protected void onDestroy() {  
        super.onDestroy();  
    }  
}
