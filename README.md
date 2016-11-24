# CustomLoding
制作了一个loding的animation custom view<br>
在SquareLoading项目基础上修改了一些问题和逻辑<br>
通过XxX的小方框组成的view一次播放小方块（1x1 ~ 6x6）<br>
# Demo
![demo](https://raw.githubusercontent.com/hongguangKim/CustomLoding/master/Demo/demo.PNG)
# Logic
![demo](https://raw.githubusercontent.com/hongguangKim/CustomLoding/master/Demo/animation.png)
<br>当然只有一行时as below<br>
![demo](https://raw.githubusercontent.com/hongguangKim/CustomLoding/master/Demo/animation2.png)
Source
================
layout相关代码：<br>
```
 <com.example.view.SmartLoading
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#E91E63"
        custom:dividerSize="2dp"
        custom:smartColor="@android:color/white"
        custom:smartCorner="2dp"
        custom:smartSize="12dp"
        custom:xCount="6"
        custom:yCount="6" />
```

播放顺序相关代码：<br>
```
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
```
你可以在initAnim()中随意修改你期望的动画如移动，旋转等等<br>
主要逻辑就是指定一个startIndex，endIndex<br>
从startIndex开始到endIndex一次播放动画<br>
当播放到endIndex后，开始播放revert animation<br>
其中需要结合getNextAnimChild()方法来协调完成动画
