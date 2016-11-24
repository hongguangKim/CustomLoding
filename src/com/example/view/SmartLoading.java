package com.example.view;

import java.util.ArrayList;
import java.util.List;

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

import com.example.smartloading.R;

/**
 * Created by growth on 11/19/16.
 */

public class SmartLoading extends ViewGroup {

	private static final int DEFAULT_SQUARE_COLOR = Color.WHITE;
	private static final int DEFAULT_SQUARE_SIZE = 36;
	private static final int DEFAULT_SQUARE_CORNER = 8;
	private static final int DEFAULT_DIVIDER_SIZE = 8;
	private static final int DEFUALT_X_COUNT = 4;
	private static final int DEFUALT_Y_COUNT = 3;
	private static final int DEFAULT_FIRST_INDEX = DEFUALT_X_COUNT
			* (DEFUALT_Y_COUNT - 1);
	private static final int DEFAULT_LAST_INDEX = DEFUALT_X_COUNT - 1;

	private static int mSquareColor = DEFAULT_SQUARE_COLOR;
	private static int mSquareSize = DEFAULT_SQUARE_SIZE;
	private static int mSquareCorner = DEFAULT_SQUARE_CORNER;
	private static int mDividerSize = DEFAULT_DIVIDER_SIZE;
	private static int mXCount = DEFUALT_X_COUNT;
	private static int mYCount = DEFUALT_Y_COUNT;
	private static int mFirstIndex = DEFAULT_FIRST_INDEX;
	private static int mLastIndex = DEFAULT_LAST_INDEX;

	private static int mPaddingTop = 0;
	private static int mPaddingLeft = 0;

	private List<Animation> startAnims = new ArrayList();
	private List<Animation> reverseAnims = new ArrayList();

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
					R.styleable.SquareLoading);

			mSquareColor = a.getColor(R.styleable.SquareLoading_squareColor,
					DEFAULT_SQUARE_COLOR);
			mSquareSize = a.getDimensionPixelSize(
					R.styleable.SquareLoading_squareSize, DEFAULT_SQUARE_SIZE);
			mSquareCorner = a.getDimensionPixelSize(
					R.styleable.SquareLoading_squareCorner,
					DEFAULT_SQUARE_CORNER);
			mDividerSize = a
					.getDimensionPixelSize(
							R.styleable.SquareLoading_dividerSize,
							DEFAULT_DIVIDER_SIZE);

			int xCount = a.getInteger(R.styleable.SquareLoading_xCount,
					DEFUALT_X_COUNT);
			int yCount = a.getInteger(R.styleable.SquareLoading_yCount,
					DEFUALT_Y_COUNT);
			if (xCount >= 2 && xCount <= 6) {
				mXCount = xCount;
			}
			if (yCount >= 1 && yCount <= 6) {
				mYCount = yCount;
			}
			a.recycle();

			mFirstIndex = mXCount * (mYCount - 1);
			mLastIndex = mXCount - 1;
		}
		initSquare(context);
		initAnim();
	}

	private void removeViewsIfNeeded() {
		if (getChildCount() > 0) {
			removeAllViews();
		}
	}

	private void initSquare(Context context) {
		GradientDrawable gradientDrawable = new GradientDrawable();
		gradientDrawable.setColor(mSquareColor);
		gradientDrawable.setSize(mSquareSize, mSquareSize);
		gradientDrawable.setCornerRadius(mSquareCorner);
		for (int i = 0; i < mXCount * mYCount; i++) {
			ImageView image = new ImageView(context);
			image.setImageDrawable(gradientDrawable);
			addView(image);
		}
	}

	private void initAnim() {
        for (int i = 0; i < getChildCount(); i++) {
            ScaleAnimation startAnim = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,   
                    0.5f);
            startAnim.setDuration(300);
            startAnim.setFillAfter(true);
            startAnim.setInterpolator(new DecelerateInterpolator());
            final int finalI = i;
            startAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (finalI != mLastIndex) {
                    	
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
                    if (finalI == mLastIndex) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startReverseAnim(mLastIndex);
                            }
                        }, 300);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            startAnims.add(startAnim);

            ScaleAnimation reverseAnim = new ScaleAnimation(1,0,1,0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,   
                    0.5f);
            reverseAnim.setDuration(300);
            reverseAnim.setFillAfter(true);
            reverseAnim.setInterpolator(new DecelerateInterpolator());
            final int finalI1 = i;
            reverseAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (finalI1 != mFirstIndex) {
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
                    if (finalI1 == mFirstIndex) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startRotateAnim(mFirstIndex);
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
			startRotateAnim(mFirstIndex);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		measureChildren(widthMeasureSpec, heightMeasureSpec);

		int minWidth = mSquareSize * (mXCount + 1) + (mXCount - 1)
				* mDividerSize;
		int minHeight = mSquareSize * (mYCount + 1) + (mYCount - 1)
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
			l = (i % mXCount + 1) * mSquareSize + (i % mXCount) * mDividerSize
					+ mPaddingLeft;
			t = (i / mXCount + 1) * mSquareSize + (i / mXCount) * mDividerSize
					+ mPaddingTop;
			r = ((i % mXCount) + 2) * mSquareSize + (i % mXCount)
					* mDividerSize + mPaddingLeft;
			b = ((i / mXCount) + 2) * mSquareSize + (i / mXCount)
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
			if(mYCount==1)
				return ++i;
			if (i < mXCount) {
				i += (mXCount - 1) * mYCount;
				return i;
			}
			index = i % mXCount + (i / mXCount - 1) * mXCount;
		} else {
			if(mYCount==1)
				return --i;
			if (i > mXCount * (mYCount - 1)) {
				i -= mXCount * (mYCount - 1) + 1;
				return i;
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
