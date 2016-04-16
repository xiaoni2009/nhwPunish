package com.nhw.nhwrandompunish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nhw.nhwrandompunish.util.GlobalField;

public class AllMainActivity extends Activity {

	Button punishButton ;
	Button guessButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allmain);

		punishButton = (Button) findViewById( R.id.punishButton );
		toPunishButtonListener toPunishL = new toPunishButtonListener();
		punishButton.setOnClickListener(toPunishL);

		guessButton = (Button) findViewById( R.id.guessGameButton );
		toGuessButtonListener toGuessL = new toGuessButtonListener();
		guessButton.setOnClickListener(toGuessL);
	}

	/**
	 * 跳转到随机惩罚工具Activity
	 */
	class toPunishButtonListener implements OnClickListener{

		@Override
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(AllMainActivity.this, PunishActivity.class);
			startActivity(intent);
		}
	}

	/**
	 * 跳转到比划来猜工具Activity
	 */
	class toGuessButtonListener implements OnClickListener{

		@Override
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(AllMainActivity.this, GuessActivity.class);
			startActivity(intent);
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
