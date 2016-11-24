package com.example.view;

import java.util.ArrayList;
import java.util.List;

import com.example.smartloading.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by growth on 11/19/16.
 */

public class SmartLoading extends ViewGroup {

	private static final int DEFAULT_SMART_COLOR = Color.WHITE;
	private static final int DEFAULT_SMART_SIZE = 36;
	private static final int DEFAULT_SMART_CORNER = 8;
	private static final int DEFAULT_DIVIDER_SIZE = 8;
	private static final int DEFUALT_X_COUNT = 4;
	private static final int DEFUALT_Y_COUNT = 3;
	private static final int DEFAULT_FIRST_INDEX = DEFUALT_X_COUNT
			* (DEFUALT_Y_COUNT - 1);
	private static final int DEFAULT_LAST_INDEX = DEFUALT_X_COUNT - 1;

	private static int mSmartColor = DEFAULT_SMART_COLOR;
	private static int mSmartSize = DEFAULT_SMART_SIZE;
	private static int mSmartCorner = DEFAULT_SMART_CORNER;
	private static int mDividerSize = DEFAULT_DIVIDER_SIZE;
	private static int mXCount = DEFUALT_X_COUNT;
	private static int mYCount = DEFUALT_Y_COUNT;
	private static int mFirstIndex = DEFAULT_FIRST_INDEX;
	private static int mLastIndex = DEFAULT_LAST_INDEX;

	private static int mPaddingTop = 0;
	private static int mPaddingLeft = 0;

	private List<Animation> startAnims = new ArrayList();
	private List<Animation> reverseAnims = new ArrayList();

	private int startIndex= mFirstIndex,endIndex=mLastIndex;
	public SmartLoading(Context context) {
		super(context);
		init(context, null);
	}

	public SmartLoading(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SmartLoading(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	}

	private void init(Context context, AttributeSet attrs) {
		removeViewsIfNeeded();
		if (attrs != null) {
			TypedArray a = getContext().obtainStyledAttributes(attrs,
					R.styleable.SmartLoading);

			mSmartColor = a.getColor(R.styleable.SmartLoading_smartColor,
					DEFAULT_SMART_COLOR);
			mSmartSize = a.getDimensionPixelSize(
					R.styleable.SmartLoading_smartSize, DEFAULT_SMART_SIZE);
			mSmartCorner = a.getDimensionPixelSize(
					R.styleable.SmartLoading_smartCorner,
					DEFAULT_SMART_CORNER);
			mDividerSize = a
					.getDimensionPixelSize(
							R.styleable.SmartLoading_dividerSize,
							DEFAULT_DIVIDER_SIZE);

			int xCount = a.getInteger(R.styleable.SmartLoading_xCount,
					DEFUALT_X_COUNT);
			int yCount = a.getInteger(R.styleable.SmartLoading_yCount,
					DEFUALT_Y_COUNT);
			if (xCount >= 1 && xCount <= 6) {
				mXCount = xCount;
			}
			if (yCount >= 1 && yCount <= 6) {
				mYCount = yCount;
			}
			a.recycle();

			startIndex=mFirstIndex = mXCount * (mYCount - 1);
			endIndex=mLastIndex = mXCount - 1;
		}
		initSmart(context);
		initAnim();
	}

	private void removeViewsIfNeeded() {
		if (getChildCount() > 0) {
			removeAllViews();
		}
	}

	private void initSmart(Context context) {
		GradientDrawable gradientDrawable = new GradientDrawable();
		gradientDrawable.setColor(mSmartColor);
		gradientDrawable.setSize(mSmartSize, mSmartSize);
		gradientDrawable.setCornerRadius(mSmartCorner);
		for (int i = 0; i < mXCount * mYCount; i++) {
			ImageView image = new ImageView(context);
			image.setImageDrawable(gradientDrawable);
			addView(image);
		}
	}

	private void initAnim() {
        for (int i = 0; i < getChildCount(); i++) {
            ScaleAnimation startAnim = new ScaleAnimation(1,0,1,0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,   
                    0.5f);
            startAnim.setDuration(300);
            startAnim.setFillAfter(true);
            startAnim.setInterpolator(new DecelerateInterpolator());
            final int finalI = i;
            startAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (finalI != endIndex) {
                    	final int index = getNextAnimChild(true, finalI);
                        if (index > mXCount * (mYCount - 1)) {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                	 startRotateAnim(index);
                                }
                            }, 100);
                        }else {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startRotateAnim(index);
                                }
                            }, 50);
                        }
                    }
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (finalI == endIndex) {
                    	mLastIndex = endIndex;
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startReverseAnim(endIndex);
                            }
                        }, 300);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            startAnims.add(startAnim);

            ScaleAnimation reverseAnim = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,   
                    0.5f);
            reverseAnim.setDuration(300);
            reverseAnim.setFillAfter(true);
            reverseAnim.setInterpolator(new DecelerateInterpolator());
            final int finalI1 = i;
            reverseAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (finalI1 != startIndex) {
                    	final int index = getNextAnimChild(false, finalI);
                        if (index < mXCount) {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startReverseAnim(index);
                                }
                            }, 100);
                        } else {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startReverseAnim(index);
                                }
                            }, 50);
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (finalI1 == startIndex) {
                    	mFirstIndex=startIndex;
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startRotateAnim(startIndex);
                            }
                        }, 300);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            reverseAnims.add(reverseAnim);
        }
    }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (getChildCount() > 0) {
			startRotateAnim(startIndex);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int minWidth = mSmartSize * (mXCount + 1) + (mXCount - 1)
				* mDividerSize;
		int minHeight = mSmartSize * (mYCount + 1) + (mYCount - 1)
				* mDividerSize;

		if (widthMode == MeasureSpec.AT_MOST
				|| (widthMode == MeasureSpec.EXACTLY && sizeWidth < minWidth)) {
			sizeWidth = minWidth;
		}
		if (heightMode == MeasureSpec.AT_MOST
				|| (heightMode == MeasureSpec.EXACTLY && sizeHeight < minHeight)) {
			sizeHeight = minHeight;
		}

		if (sizeHeight > minHeight) {
			mPaddingTop = (sizeHeight - minHeight) / 2;
		}
		if (sizeWidth > minWidth) {
			mPaddingLeft = (sizeWidth - minWidth) / 2;
		}

		childLayout();
		setMeasuredDimension(sizeWidth, sizeHeight);
	}

	private void childLayout() {
		int l, t, r, b;
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			l = (i % mXCount + 1) * mSmartSize + (i % mXCount) * mDividerSize
					+ mPaddingLeft;
			t = (i / mXCount + 1) * mSmartSize + (i / mXCount) * mDividerSize
					+ mPaddingTop;
			r = ((i % mXCount) + 2) * mSmartSize + (i % mXCount)
					* mDividerSize + mPaddingLeft;
			b = ((i / mXCount) + 2) * mSmartSize + (i / mXCount)
					* mDividerSize + mPaddingTop;
			child.layout(l, t, r, b);
		}
	}

	private void startRotateAnim(final int index) {
		if (startAnims != null && startAnims.size() > index) {
			getChildAt(index).startAnimation(startAnims.get(index));
		}
	}

	private void startReverseAnim(final int index) {
		if (reverseAnims != null && reverseAnims.size() > index) {
			getChildAt(index).startAnimation(reverseAnims.get(index));
		}
	}
	
	private int getNextAnimChild(boolean isStart, int i) {
		Log.i("index","index="+i);
		int index;
		if (isStart) {
			if (i < mXCount) {
				return ++mFirstIndex;
			}
			index = i % mXCount + (i / mXCount - 1) * mXCount;
		} else {
			if (i > mXCount * (mYCount - 1)) {
				return --mLastIndex;
			}
			index = i + mXCount;
		}
		return index;

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (startAnims != null) {
			for (int i = 0; i < startAnims.size(); i++) {
				startAnims.get(i).cancel();
			}
			startAnims.clear();
		}
		if (reverseAnims != null) {
			for (int i = 0; i < reverseAnims.size(); i++) {
				reverseAnims.get(i).cancel();
			}
			reverseAnims.clear();
		}
	}
}
