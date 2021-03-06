package com.nhw.nhwrandompunish.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.nhw.nhwrandompunish.R;
import com.nhw.nhwrandompunish.ui.Attributes;

/**
 * User: eluleci Date: 24.10.2013 Time: 23:03
 */
public class FlatProgressBar extends ProgressBar implements
		Attributes.AttributeChangeListener {

	private Attributes attributes;

	public FlatProgressBar(Context context) {
		super(context);
		init(null);
	}

	public FlatProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public FlatProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs) {

		if (attributes == null)
			attributes = new Attributes(this, getResources());

		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.fl_FlatSeekBar);

			// getting common attributes
			int customTheme = a.getResourceId(
					R.styleable.fl_FlatSeekBar_fl_theme,
					Attributes.DEFAULT_THEME);
			attributes.setThemeSilent(customTheme, getResources());

			attributes.setSize(a.getDimensionPixelSize(
					R.styleable.fl_FlatSeekBar_fl_size,
					Attributes.DEFAULT_SIZE_PX));

			a.recycle();
		}

		// progress
		PaintDrawable progress = new PaintDrawable(attributes.getColor(1));
		progress.setCornerRadius(attributes.getSize());
		progress.setIntrinsicHeight(attributes.getSize());
		progress.setIntrinsicWidth(attributes.getSize());
		progress.setDither(true);
		ClipDrawable progressClip = new ClipDrawable(progress, Gravity.LEFT,
				ClipDrawable.HORIZONTAL);

		// secondary progress
		PaintDrawable secondary = new PaintDrawable(attributes.getColor(2));
		secondary.setCornerRadius(attributes.getSize());
		secondary.setIntrinsicHeight(attributes.getSize());
		ClipDrawable secondaryProgressClip = new ClipDrawable(secondary,
				Gravity.LEFT, ClipDrawable.HORIZONTAL);

		// background
		PaintDrawable background = new PaintDrawable(attributes.getColor(3));
		background.setCornerRadius(attributes.getSize());
		background.setIntrinsicHeight(attributes.getSize());

		// applying drawable
		LayerDrawable ld = (LayerDrawable) getProgressDrawable();
		ld.setDrawableByLayerId(android.R.id.background, background);
		ld.setDrawableByLayerId(android.R.id.progress, progressClip);
		ld.setDrawableByLayerId(android.R.id.secondaryProgress,
				secondaryProgressClip);
	}

	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void onThemeChange() {
		init(null);
	}
}
