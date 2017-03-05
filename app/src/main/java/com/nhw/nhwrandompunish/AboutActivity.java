package com.nhw.nhwrandompunish;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.content.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AboutActivity extends Activity{

	Button copyQQBt ;
	Button copyWechatBt;
	Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		copyQQBt = (Button)findViewById(R.id.qqButton);
		CopyQQBtListener copyQQBL = new CopyQQBtListener();
		copyQQBt.setOnClickListener(copyQQBL);

		copyWechatBt = (Button)findViewById(R.id.wechatButton);
		CopyWechatBtListener copyWechatBL = new CopyWechatBtListener();
		copyWechatBt.setOnClickListener(copyWechatBL);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressWarnings("deprecation")
	public void copyAndShowToast(String copyStr , String toastStr){
		ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//		clip.getText(); // 粘贴
		clip.setText(copyStr); // 复制

		toast=Toast.makeText(getApplicationContext(), toastStr,Toast.LENGTH_SHORT );
		toast.show();
	}

	/*
	 * 复制QQ号码按钮的监听
	 * */
	class CopyQQBtListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			copyAndShowToast("237561375","QQ群号已复制到剪切板！");
		}

	}

	/*
	 * 复制微信公众号按钮的监听
	 * */
	class CopyWechatBtListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			copyAndShowToast("nahaowan2015","微信公众号名称已复制到剪切板！");
		}

	}
}
