# CustomLoding
制作了一个loding的animation custom view<br>
通过XxX的小方框组成的view一次播放多方（1x1 ~ 6x6）<br>
# Demo
![demo](https://raw.githubusercontent.com/hongguangKim/CustomLoding/master/Demo/demo.PNG)
# Logic
![demo](https://raw.githubusercontent.com/hongguangKim/CustomLoding/master/Demo/animation.png)
Source
================
播放顺序相关代码：
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
