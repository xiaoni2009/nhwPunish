package com.nhw.nhwrandompunish.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Spinner;

import com.nhw.nhwrandompunish.R;
import com.nhw.nhwrandompunish.ui.Attributes;
import com.nhw.nhwrandompunish.ui.FlatUI;

/**
 * User: chay Date: 23.04.2016 Time: 21:09
 */
public class FlatSpinner extends Spinner implements
		Attributes.AttributeChangeListener {

	private Attributes attributes;

	public FlatSpinner(Context context) {
		super(context);
		init(null);
	}

	public FlatSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public FlatSpinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}

	private void init(AttributeSet attrs) {

		if (attributes == null)
			attributes = new Attributes(this, getResources());

		if (attrs != null) {

			// getting android default tags for textColor and textColorHint
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.fl_FlatSpinner);

			// getting common attributes
			int customTheme = a.getResourceId(
					R.styleable.fl_FlatSpinner_fl_theme,
					Attributes.DEFAULT_THEME);
			attributes.setThemeSilent(customTheme, getResources());

			attributes.setRadius(a.getDimensionPixelSize(
					R.styleable.fl_FlatSpinner_fl_cornerRadius,
					Attributes.DEFAULT_RADIUS_PX));
			attributes.setBorderWidth(a.getDimensionPixelSize(
					R.styleable.fl_FlatSpinner_fl_borderWidth,
					Attributes.DEFAULT_BORDER_WIDTH_PX));


			a.recycle();
		}

		GradientDrawable backgroundDrawable = new GradientDrawable();
		backgroundDrawable.setCornerRadius(attributes.getRadius());
		backgroundDrawable.setColor(Color.WHITE);
		backgroundDrawable.setStroke(attributes.getBorderWidth(),
				attributes.getColor(2));

		setBackgroundDrawable(backgroundDrawable);

	}

	public Attributes getAttributes() {
		return attributes;
	}

	@Override
	public void onThemeChange() {
		init(null);
	}
}
