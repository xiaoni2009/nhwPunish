package com.nhw.nhwrandompunish;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nhw.nhwrandompunish.util.GlobalField;
import com.nhw.nhwrandompunish.util.Rotate3d;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PunishActivity extends Activity {

	Button startRandomPunish ;
	TextView resultText;
	ImageView stormbackImaView;
	ImageView cardFaceImaView;
	ImageView cardPileImaView;
	private SoundPool soundPool;
//	private ViewGroup mContainer;

	int lastReultIndex = GlobalField.ORININDEX ;
	int randomIndex = 0;
	String randomText = "";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_punish);


		startRandomPunish = (Button) findViewById(R.id.button);
		startRandomPunishListener sRPL = new startRandomPunishListener();
		startRandomPunish.setOnClickListener(sRPL);

		resultText = (TextView) findViewById(R.id.result);

		//卡牌背面图，xml已经设置好，不要再设置初始化时显示
		stormbackImaView = (ImageView) findViewById(R.id.stormbackImaView);
//		stormbackImaView.setVisibility(View.VISIBLE);
		//卡牌正面图，xml已经设置好，不要再设置初始化时隐藏
		cardFaceImaView = (ImageView) findViewById(R.id.cardFaceImaView);
//		stormbackImaView.setVisibility(View.INVISIBLE);
		cardPileImaView = (ImageView) findViewById(R.id.cardPileView);

		//加载声音
		soundPool= new SoundPool(2,AudioManager.STREAM_MUSIC,5);
//		soundPool.load(this, R.raw.nature_power, 1);
		soundPool.load(this, R.raw.justice, 1);

		//整个view的容器，这里测试整个容器翻转用，实际我只用图片翻转
//		mContainer = (ViewGroup) findViewById(R.id.container);
	}

	/*
	 * 随机生成惩罚按钮的监听
	 * */
	class startRandomPunishListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Random random = new Random();
			//动画效果前，清空文字
			resultText.setText( "");

			randomIndex = random.nextInt(GlobalField.PUNISH_LIST.length);

			//比较该索引是否跟上一次索引一样，如果一样就重复生成，直到跟上一次不一样为止
			while( lastReultIndex == randomIndex){
				randomIndex = random.nextInt(GlobalField.PUNISH_LIST.length);
				System.out.println("################"+randomIndex);
			}

			//取新的结果
			randomText =GlobalField.PUNISH_LIST[randomIndex];
			System.out.println("################"+randomText);

//	        resultText.setText( "test");
//	        resultText.setText( randomText);//文本赋值放到动画翻转之后

			//隐藏已翻开的牌，第一次运行时，不存在已翻开的牌，所以第一次不需要该动画
			if( lastReultIndex != GlobalField.ORININDEX ){
				Animation animationAlpha = AnimationUtils.loadAnimation(PunishActivity.this, R.anim.translate);
				cardFaceImaView.startAnimation(animationAlpha);
			}
			cardFaceImaView.setVisibility(View.INVISIBLE);
			stormbackImaView.setVisibility(View.INVISIBLE);

			//卡牌堆移动
			//使用AnimationUtils装载动画设置文件
			Animation animation = AnimationUtils.loadAnimation(PunishActivity.this, R.anim.translate_scale);
			animation.setAnimationListener(new TranslateScaleImaViewListener());
			//使用ImageView的startAnimation方法开始执行动画
			cardPileImaView.startAnimation(animation);

			//卡牌图片翻转
			/**
			 *
			 * 调用这个方法，就是整个容器翻转
			 * 参数很简单，大家都应该看得懂
			 * 简单说下，第一个是位置，第二是开始的角度，第三个是结束的角度
			 * 这里需要说明的是，如果是要回到上一张
			 * 把第一个参数设置成-1就行了
			 *
			 */
//			applyContainerRotation(0,0,90);

			//只图片翻转，要等牌堆移动完再翻转
//	        cardFaceImaView.setVisibility(View.INVISIBLE);
//	        stormbackImaView.setVisibility(View.INVISIBLE);
//	        applyImaViewRotation(0,0,90);

			//播放制裁声音

			soundPool.play(1,1,1,0,0,1);

			//刷新上一次索引
			lastReultIndex = randomIndex;

		}

	}

	//监听移动牌堆动画完成
	private  final class TranslateScaleImaViewListener implements Animation.AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
			//只有当牌堆移动完成后，再翻转牌
			applyImaViewRotation(0,0,90);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}


	//只翻转图片-----------------------------------------
	private void applyImaViewRotation(int position, float start, float end) {
		// Find the center of the container
		final float centerX = stormbackImaView.getWidth() / 2.0f;
		final float centerY = stormbackImaView.getHeight() / 2.0f;

		final Rotate3d rotation =
				new Rotate3d(start, end, centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		//加速动画
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayImaViewNextView(position));

		stormbackImaView.startAnimation(rotation);
	}

	private final class DisplayImaViewNextView implements Animation.AnimationListener {
		private final int mPosition;

		private DisplayImaViewNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			//翻转完隐藏卡背
			stormbackImaView.setVisibility(View.INVISIBLE);
			cardFaceImaView.setVisibility(View.VISIBLE);

			//开始显示卡正面,不需要开新线程吧
//        	stormbackImaView.post(new SwapImaViewViews(mPosition));

			final float centerX = cardFaceImaView.getWidth() / 2.0f;
			final float centerY = cardFaceImaView.getHeight() / 2.0f;
			Rotate3d rotation;
			System.out.println("######mPosition="+mPosition);

			if (mPosition > -1) {
//                rotation = new Rotate3d(90, 180, centerX, centerY, 310.0f, false);//按照这个旋转最后图片出来是水平翻转的
				rotation = new Rotate3d(270, 360, centerX, centerY, 310.0f, false);
			} else {
//                rotation = new Rotate3d(90, 0, centerX, centerY, 310.0f, false);
				rotation = new Rotate3d(90, 0, centerX, centerY, 310.0f, false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			//减速动画
			rotation.setInterpolator(new DecelerateInterpolator());
			rotation.setAnimationListener(new LastImaViewAnimListener());

			cardFaceImaView.startAnimation(rotation);
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	private final class LastImaViewAnimListener implements Animation.AnimationListener {

		@Override
		public void onAnimationEnd(Animation animation) {
//        	cardFaceImaView.setVisibility(View.INVISIBLE);
			cardFaceImaView.clearAnimation();
			resultText.setText( randomText);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
//    private final class SwapImaViewViews implements Runnable {
//        private final int mPosition;
//
//        public SwapImaViewViews(int position) {
//            mPosition = position;
//        }
//
//        public void run() {
//            final float centerX = cardFaceImaView.getWidth() / 2.0f;
//            final float centerY = cardFaceImaView.getHeight() / 2.0f;
//            Rotate3d rotation;
//            
//            if (mPosition > -1) {
////            	stormbackImaView.setVisibility(View.VISIBLE);
////            	stormbackImaView.requestFocus();
//
//                rotation = new Rotate3d(90, 180, centerX, centerY, 310.0f, false);
//            } else {
////            	stormbackImaView.setVisibility(View.GONE);
//
//                rotation = new Rotate3d(90, 0, centerX, centerY, 310.0f, false);
//            }
//
//            rotation.setDuration(500);
//            rotation.setFillAfter(true);
//            rotation.setInterpolator(new DecelerateInterpolator());
//
//            cardFaceImaView.startAnimation(rotation);
//        }
//    }

	//整个容器翻转函数-----------------------------------------------------------------------
//	private void applyContainerRotation(int position, float start, float end) {
//        // Find the center of the container
//        final float centerX = mContainer.getWidth() / 2.0f;
//        final float centerY = mContainer.getHeight() / 2.0f;
//
//        final Rotate3d rotation =
//                new Rotate3d(start, end, centerX, centerY, 310.0f, true);
//        rotation.setDuration(500);
//        rotation.setFillAfter(true);
//        rotation.setInterpolator(new AccelerateInterpolator());
//        rotation.setAnimationListener(new DisplayNextView(position));
//
//        mContainer.startAnimation(rotation);
//    }
//
//	private final class DisplayNextView implements Animation.AnimationListener {
//        private final int mPosition;
//
//        private DisplayNextView(int position) {
//            mPosition = position;
//        }
//
//        public void onAnimationStart(Animation animation) {
//        }
//
//        public void onAnimationEnd(Animation animation) {
//            mContainer.post(new SwapViews(mPosition));
//        }
//
//        public void onAnimationRepeat(Animation animation) {
//        }
//    }
//
//    /**
//     * This class is responsible for swapping the views and start the second
//     * half of the animation.
//     */
//    private final class SwapViews implements Runnable {
//        private final int mPosition;
//
//        public SwapViews(int position) {
//            mPosition = position;
//        }
//
//        public void run() {
//            final float centerX = mContainer.getWidth() / 2.0f;
//            final float centerY = mContainer.getHeight() / 2.0f;
//            Rotate3d rotation;
//            
//            if (mPosition > -1) {
//            	stormbackImaView.setVisibility(View.VISIBLE);
//            	stormbackImaView.requestFocus();
//
//                rotation = new Rotate3d(90, 180, centerX, centerY, 310.0f, false);
//            } else {
//            	stormbackImaView.setVisibility(View.GONE);
//
//                rotation = new Rotate3d(90, 0, centerX, centerY, 310.0f, false);
//            }
//
//            rotation.setDuration(500);
//            rotation.setFillAfter(true);
//            rotation.setInterpolator(new DecelerateInterpolator());
//
//            mContainer.startAnimation(rotation);
//        }
//    }

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
