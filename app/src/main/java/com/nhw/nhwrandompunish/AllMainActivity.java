package com.nhw.nhwrandompunish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.nhw.nhwrandompunish.util.GlobalField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllMainActivity extends Activity {

	private GridView gview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	// 图片封装为一个数组
	private int[] icon = { R.drawable.clock, R.drawable.games_control };
	private String[] iconName = { "随机惩罚", "比划来猜"};

	public List<Map<String, Object>> getData(){
		//cion和iconName的长度是相同的，这里任选其一都可以
		for(int i=0;i<icon.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			data_list.add(map);
		}

		return data_list;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allmain);

		gview = (GridView) findViewById(R.id.gview);
		//新建List
		data_list = new ArrayList<Map<String, Object>>();
		//获取数据
		getData();
		//新建适配器
		String [] from ={"image","text"};
		int [] to = {R.id.image,R.id.text};
		sim_adapter = new SimpleAdapter(this, data_list, R.layout.gridview_item, from, to);
		//配置适配器
		gview.setAdapter(sim_adapter);
		//添加消息处理
		gview.setOnItemClickListener(new ItemClickListener());

	}


	//当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
	class  ItemClickListener implements AdapterView.OnItemClickListener
	{
		public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
								View arg1,//The view within the AdapterView that was clicked
								int arg2,//The position of the view in the adapter
								long arg3//The row id of the item that was clicked
		) {
			//在本例中arg2=arg3
//			HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
			//显示所选Item的ItemText
//			setTitle((String)item.get("text"));
			switch (arg2){
				case 0:{//跳转到随机惩罚
					Intent intent = new Intent();
					intent.setClass(AllMainActivity.this, PunishActivity.class);
					startActivity(intent);
					break;
				}
				case 1:{//跳转到比划来猜
					Intent intent = new Intent();
					intent.setClass(AllMainActivity.this, GuessActivity.class);
					startActivity(intent);
					break;
				}
			}
		}

	}

	//菜单相关-------------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		menu.add(0, GlobalField.ABOUT, 1, R.string.app_about);//增加按钮
		return true;
	}

	//用户点击菜单按钮的时候就会触发该函数
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//			System.out.println("itemId------>" + item.getItemId() );
		if( item.getItemId() == GlobalField.ABOUT ){
			Intent intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
