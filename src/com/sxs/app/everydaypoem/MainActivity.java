package com.sxs.app.everydaypoem;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sxs.app.book.BookFragment;
import com.sxs.app.common.BaseActivity;
import com.sxs.app.exam.ExamFragment;
import com.sxs.app.goodpoem.GoodPoemFragment;
import com.sxs.app.usercenter.UserFragment;
import com.sxs.app.utils.TabHostUtil;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends BaseActivity {

public static final String MAIN_TAB = "MAIN_TAB";
    
    public static final int TYPE_GOODPOEM_TAB = 0; //好诗词
    public static final int TYPE_BOOK_TAB = 1; //古典文集
    public static final int TYPE_EXAM_TAB = 2; //测试
    public static final int TYPE_USER_TAB = 3; //个人中心
    @ViewInject(R.id.th_main)    private FragmentTabHost tabHost;
    
    private int currentTab = TYPE_GOODPOEM_TAB; //当前默认的选项
    private Class<?>[] fragmentArray = {GoodPoemFragment.class, BookFragment.class, ExamFragment.class, UserFragment.class};  
    private int[] tabIconArray = {R.drawable.sl_first_nav_main, R.drawable.sl_first_nav_main, R.drawable.sl_first_nav_main, R.drawable.sl_first_nav_main};
    private String[] tabTextArray = {"诗词", "文学", "闯关", "我"};
    private List<View> tabs;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		initConment();
//		DJManager.getInstance(MainActivity.this).init("73922e402a9e04d37aaa50f5c1823a76", false);
//		DJPushManager.startDoujinPush(this);
	}
	
	public void initConment() {  
		tabHost.setup(this, getSupportFragmentManager(), R.id.fl_main);
		tabs = TabHostUtil.createTabs(this, tabHost, tabTextArray, tabIconArray, fragmentArray, onTabItemClickListener);
    }  
	//点击切换显示
    private View.OnClickListener onTabItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View tab) {
            int index = tabs.indexOf(tab);
            realNavigateToTab(index);
        }
    };
    @Override
    protected void onPostResume() {
        super.onPostResume();
        realNavigateToTab(currentTab);
    }
    /**
     * 切换页面根据索引
     * @param index
     */
    private void realNavigateToTab(int index){
        currentTab = index;
        tabHost.setCurrentTab(index);
    }
    private void navTab(Intent intent){
        //检查是显示那个tab
        if (intent != null && intent.hasExtra(MAIN_TAB)) {
            int type = intent.getIntExtra(MAIN_TAB, TYPE_GOODPOEM_TAB);
            realNavigateToTab(type);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        navTab(intent);
    }
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        navTab(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	 @Override  
    protected void onDestroy() {  
        super.onDestroy();  
    }

}
