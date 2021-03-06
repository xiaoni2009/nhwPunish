package com.nhw.nhwrandompunish;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nhw.nhwrandompunish.ui.FlatUI;
import com.nhw.nhwrandompunish.util.GlobalField;
import com.nhw.nhwrandompunish.views.FlatButton;
import com.nhw.nhwrandompunish.views.FlatProgressBar;
import com.nhw.nhwrandompunish.views.FlatSeekBar;
import com.nhw.nhwrandompunish.views.FlatSpinner;

public class GuessActivity extends Activity {

	private final int APP_THEME = R.array.blood;
	FlatButton startGuessButton ;
	Button passWordButton ;
	Button rightButton ;
	Button againButton ;
	TextView intro;
	TextView word;
	TextView chance;
	FlatSpinner difficultyLever;
	ArrayAdapter<String> difficultyLeverAd;
	FlatSpinner type;
	ArrayAdapter<String> typeAd;
	FlatProgressBar timeBar;
	RelativeLayout r1;

	static int passWordChanceLeft = GlobalField.GUESSPASS_CHANCELEFT;//可过词机会剩余数量
	static int score = 0;//答对词数量
	static int goalScore = 100;//取胜需要的答对数量，根据难度不同而不同
	String[] wordToGuess;
	String[] wordToGuessSource;
	static int timeLeft= GlobalField.TIME_INITAL;
	Thread timeCountThread = null;

	int guessIndex = 0;
	String randomText = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess);

		// converts the default values to dp to be compatible with different
		// screen sizes
		FlatUI.initDefaultValues(this);

		// Default theme should be set before content view is added
		FlatUI.setDefaultTheme(APP_THEME);

		intro = (TextView) findViewById(R.id.intro);
		word =  (TextView) findViewById(R.id.word);
		chance =  (TextView) findViewById(R.id.chance);

		difficultyLever = (FlatSpinner)  findViewById(R.id.difficultyLever);
		//将可选内容与ArrayAdapter连接起来
		difficultyLeverAd = new ArrayAdapter<String>(this,R.layout.mysimple_spinner_item,GlobalField.DIFFICULT_LV);
		//设置下拉列表的风格
		difficultyLeverAd.setDropDownViewResource(R.layout.mysimple_spinner_dropdown_item);
		//将adapter 添加到spinner中
		difficultyLever.setAdapter(difficultyLeverAd);
		//添加事件Spinner事件监听
		difficultyLever.setOnItemSelectedListener(new difficultyLeverSpinnerSelectedListener());

		type = (FlatSpinner)  findViewById(R.id.type);
		typeAd = new ArrayAdapter<String>(this,R.layout.mysimple_spinner_item,GlobalField.GUESS_TYPE);
		typeAd.setDropDownViewResource(R.layout.mysimple_spinner_dropdown_item);
		type.setAdapter(typeAd);
		type.setOnItemSelectedListener(new typeSpinnerSelectedListener());

		timeBar = (FlatProgressBar)  findViewById(R.id.timeBar);

		startGuessButton = (FlatButton) findViewById( R.id.startGuessButton );
		startGuessButtonListener startGuessBL = new startGuessButtonListener();
		startGuessButton.setOnClickListener(startGuessBL);

		passWordButton = (Button) findViewById(R.id.passWordButton);
		passWordButtonListener passWordBL = new passWordButtonListener();
		passWordButton.setOnClickListener(passWordBL);

		rightButton = (Button) findViewById(R.id.rightButton);
		rightButtonListener rightBL = new rightButtonListener();
		rightButton.setOnClickListener(rightBL);

		againButton = (Button) findViewById(R.id.againButton);
		againButtonListener againBL = new againButtonListener();
		againButton.setOnClickListener(againBL);

		r1 = (RelativeLayout) findViewById(R.id.guessLayout);

	}

	/**
	 * 难度选择下拉框
	 */
	private class difficultyLeverSpinnerSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			System.out.println("difficulty####################position:" + position + ",id:" + id);
			switch (position){
				case 0:{
					goalScore = GlobalField.GOALSCORE_LOW;
					break;
				}
				case 1:{
					goalScore = GlobalField.GOALSCORE_MID;
					break;
				}
				case 2:{
					goalScore = GlobalField.GOALSCORE_HIGH;
					break;
				}
				default:goalScore = GlobalField.GOALSCORE_LOW;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			System.out.println("difficulty####################position:nothing");
		}
	}

	/**
	 * 词语类别选择下拉框
	 */
	private class typeSpinnerSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			System.out.println("type####################position:"+position+",id:"+id);
			switch (position){
				case 0:{//成语
					wordToGuessSource = GlobalField.GUESSWORD_IDIOM;
					break;
				}
				case 1:{//人名
					wordToGuessSource = GlobalField.GUESSWORD_NAME;
					break;
				}
				case 2:{//物品
					wordToGuessSource = GlobalField.GUESSWORD_THING;
					break;
				}
				default:wordToGuessSource = GlobalField.GUESSWORD_IDIOM;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			System.out.println("type####################position:nothing");
		}
	}



	/**
	 * 开始比划来猜按钮监听器
	 */
	class startGuessButtonListener implements OnClickListener{

		@Override
		public void onClick(View v){
			word.setVisibility(View.VISIBLE);
			passWordButton.setVisibility(View.VISIBLE);
			rightButton.setVisibility(View.VISIBLE);
			timeBar.setVisibility(View.VISIBLE);
			chance.setVisibility(View.VISIBLE);
			againButton.setVisibility(View.VISIBLE);

			intro.setVisibility(View.INVISIBLE);
			difficultyLever.setVisibility(View.INVISIBLE);
			type.setVisibility(View.INVISIBLE);
			startGuessButton.setVisibility(View.INVISIBLE);

			passWordChanceLeft = GlobalField.GUESSPASS_CHANCELEFT;//可过词机会剩余数量
			score = 0;//答对词数量
			timeLeft = GlobalField.TIME_INITAL;
			guessIndex = 0;

			//根据难度从词源随机取词
			wordToGuess = getRandomNoRepeateWord(wordToGuessSource,goalScore+4);//因为可能要过3个词，所以需要多取几个词
			getAGuessWord();//第一次取一个词
			try {
				flushChance();
				if(timeCountThread != null) {
//					timeCountThread.stop();
					timeCountThread.interrupt();
				}
				//只有在线程中断或者死亡的情况下，才启动线程。一个线程运行一个runnable实例。
//				if(timeCountThread.isInterrupted() || !timeCountThread.isAlive() ) {
				timeCountThread = new Thread(
						new Runnable() {
							@Override
							public void run() {
								Message message = new Message();
								try {
									while (timeLeft >= 1) {
										//不加这句会报：This message is already in use错误
										message = GuessActivity.this.flushChanceHandler.obtainMessage();
										Thread.currentThread().sleep(1000);//毫秒
										timeLeft--;
										message.what = GlobalField.FLUSH_CHANCE_MSG;
										// 发送消息
										GuessActivity.this.flushChanceHandler.sendMessage(message);
									}
									if (timeLeft <= 0) {
										timeLeft = 0;
										message = GuessActivity.this.flushChanceHandler.obtainMessage();
										message.what = GlobalField.FAILED_MSG;
										// 发送消息
										GuessActivity.this.flushChanceHandler.sendMessage(message);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
				);

				timeCountThread.start();
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//因为不同难度的词汇最多是9个，所以我都取9个就行了
	private String[] getRandomNoRepeateWord(String[] wordList,int length){
		String[] result = new String[length];
		String[] temp = new String[wordList.length];
		//获取一个词源的副本，以便可以换词序
		for(int i =0; i< wordList.length; i++) {
			temp[i] = wordList[i];
		}

		int n = temp.length;
		// 该for循环为产生不重复的中奖序列的核心代码
		for(int i = 0; i < result.length; i++) {
			int r = (int) (Math.random() * n);  // 随机产生一个从0——(n-1)的数字,Math.random()
			// 随机产生一个[0, 1)范围的double型数值,
			// 将该随机数字作为数组的下标，
			result[i] = temp[r];				// 将该下标对应的值赋给result[i]
			temp[r] = temp[n - 1];        // 将temp数组的temp[n-1]的值，赋给刚已赋
			// 值过的temp[r]。相当于把已抽到的词放到最后，然后数组下标前移一位。
			n--;   // 将n-1,从而下一次循环产生的随机的原数组下标的范围从0——（n-1）-1,
			// 保证了上一步中，已经赋值给数组中其他数的temp[n-1]，不会在下次循环中给取
			// 得，从而保证了产生的中奖数组result为不重复的。
		}

		return result;
	}


	/**
	 * 过到下一个词按钮监听器
	 */
	class passWordButtonListener implements OnClickListener{

		@Override
		public void onClick(View v){
			if(passWordChanceLeft <= GlobalField.GUESSPASS_CHANCELEFT && passWordChanceLeft >=1 ) {
				passWordChanceLeft--;//过一次剩余次数就减少一次
				//刷新机会数
				flushChance();

				//重新选词
				getAGuessWord();
				if(passWordChanceLeft == 0){
					hidePassButton();
				}
			}
			else{
				Toast toast= Toast.makeText(getApplicationContext(), "过词次数已用完", Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}

	//当过词次数用尽，隐藏过词按钮
	private void hidePassButton() {
		passWordButton.setVisibility(View.INVISIBLE);
	}

	/**
	 * 猜对啦按钮监听器
	 */
	class rightButtonListener implements OnClickListener{

		@Override
		public void onClick(View v){
			//重新选词
			getAGuessWord();
			if( score < goalScore && score >= 0) {
				score++;
				//刷新分数
				flushChance();
			}
			//成功过关
			if(score == goalScore){
				//提示成功
				successShow();
			}
		}
	}

	/**
	 * 获取随机词汇的算法与随机惩罚不同，它要求一次游戏中词汇不能重复
	 * 所以我们先取简单7歌词，中等8个词，高级9歌词，各含3个可过词
	 */
	private void getAGuessWord(){
		word.setText("");

		//取新的结果
		randomText =wordToGuess[guessIndex];
		word.setText( randomText );
		if(guessIndex < wordToGuess.length && guessIndex >= 0){
			guessIndex++;
		}
	}

	//提示成功界面
	void successShow(){
		word.setText("恭喜闯关成功！");
		passWordButton.setVisibility(View.INVISIBLE);
		rightButton.setVisibility(View.INVISIBLE);
		//停止倒计时
		try {
			timeCountThread.interrupt();
//			timeCountThread.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//提示失败界面
	void failShow(){
		word.setText( "失败了，再接再厉！");
		passWordButton.setVisibility(View.INVISIBLE);
		rightButton.setVisibility(View.INVISIBLE);
	}

	//刷新分数、过剩余次数显示
	void flushChance(){
		//格式化字符串，其中在string.xml中%1$s代表第一个参数
		Resources res = getResources();
		String text = String.format(res.getString(R.string.chanceLeft), passWordChanceLeft, score,goalScore,timeLeft);
		chance.setText(text);

		timeBar.setProgress(timeLeft);
	}

	//在handler中更新UI的TextView，这个可以跨线程更新
	Handler flushChanceHandler = new Handler(){

		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GlobalField.FLUSH_CHANCE_MSG: {
					flushChance();
					break;
				}
				case GlobalField.FAILED_MSG: {
					failShow();
					break;
				}
				default:{}
			}
			super.handleMessage(msg);
		}

	};


	/**
	 * 再来一次按钮监听器
	 */
	private class againButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			try {
				initial();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//初始化应用参数值状态
	private void initial(){

		word.setVisibility(View.INVISIBLE);
		passWordButton.setVisibility(View.INVISIBLE);
		rightButton.setVisibility(View.INVISIBLE);
		timeBar.setVisibility(View.INVISIBLE);
		chance.setVisibility(View.INVISIBLE);
		againButton.setVisibility(View.INVISIBLE);

		intro.setVisibility(View.VISIBLE);
		difficultyLever.setVisibility(View.VISIBLE);
		type.setVisibility(View.VISIBLE);
		startGuessButton.setVisibility(View.VISIBLE);

		passWordChanceLeft = GlobalField.GUESSPASS_CHANCELEFT;//可过词机会剩余数量
		score = 0;//答对词数量
		timeLeft = GlobalField.TIME_INITAL;
		guessIndex = 0;
		flushChance();
		word.setText("");

		try {
			if(timeCountThread != null) {
//				timeCountThread.stop();
				timeCountThread.interrupt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//返回键关闭倒计时线程
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if ( keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			try {
				if(timeCountThread != null) {
					timeCountThread.interrupt();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			return true;
		}
		return super.onKeyDown(keyCode, event);
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

	@Override
	protected void onResume(){
		super.onResume();
//		try {
//			if(timeCountThread != null) {
//				timeCountThread.interrupt();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
