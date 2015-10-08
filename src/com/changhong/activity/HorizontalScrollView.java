package com.changhong.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.changhong.common.AndroidVersionCheckUtils;


public class HorizontalScrollView extends ScrollView {

	private View inner;// 子View

	private float y;// 点击时y坐标

	private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.
	private Rect imgNormal=new Rect();
	private boolean isCount = false;// 是否开始计算
	private boolean isMoveing = false;// 是否开始移动.
	private View mParent;
	private ImageView imageView;
	private View mChild;
	private int initTop, initbottom;// 初始高度
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	public void setChildLayout(View view){
		this.mChild=view;
	}
    public void setParentLayout(View relativelayout){
    	this.mParent=relativelayout;
    }
	public HorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0);
		}
	}
	
	/** touch 事件处理 **/
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}
		return true;
	}
	/**
	 * touch事件拦截处理
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event){
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			if(imgNormal.isEmpty()){
				y=event.getY();
				imgNormal.top=initTop=imageView.getTop();
				imgNormal.left=imageView.getLeft();
				imgNormal.bottom=initbottom=imageView.getBottom();
				imgNormal.right=imageView.getRight();
			   }
		}
		return false;
	}
	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		
		switch (action) {
		case MotionEvent.ACTION_UP:
			isMoveing = false;
			// 手指松开.
			if (isNeedAnimation()) {
				animation();
			}
			break;
		/***
		 * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
		 * 因为此时是MyScrollView的touch事件传递到到了LIstView的孩子item上面.所以从第二次计算开始.
		 * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:

			final float preY = y;// 按下时的y坐标
			float nowY = ev.getY();// 时时y坐标
			int deltaY = (int) (nowY - preY);// 滑动距离
			if (!isCount) {
				deltaY = 0; // 在这里要归0.
			}

			if (deltaY < 0){return;}

			// 当滚动到最上或者最下时就不会再滚动，这时移动布局
			isNeedMove();

			if (isMoveing) {
				// 初始化头部矩形
				if (normal.isEmpty()) {
					// 保存正常的布局位置
					normal.set(inner.getLeft(), inner.getTop(),inner.getRight(), inner.getBottom());
				}
				// 移动布局
				inner.layout(inner.getLeft(), inner.getTop() + deltaY /4,inner.getRight(), inner.getBottom() + deltaY /4);
				
				
				initTop+= (deltaY /8);
				initbottom += (deltaY / 8);
				imageView.layout(
						imageView.getLeft(), initTop, imageView.getRight(), initbottom);
			}

			isCount = true;
			y = nowY;
			break;

		default:
			break;
		}
	}

	/***
	 * 回缩动画
	 */
	public void animation() {
		TranslateAnimation taa = new TranslateAnimation(0, 0, imageView.getTop()-imgNormal.top,
				(imageView.getTop()-imgNormal.top)/32);
		taa.setDuration(200);
		imageView.startAnimation(taa);
//		imageView.layout(imageView.getLeft(), initTop, imageView.getRight(),
//				initbottom);
		imageView.layout(imgNormal.left, imgNormal.top, imgNormal.right, imgNormal.bottom);
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop()-normal.top,
				(inner.getTop()-normal.top)/32);
		ta.setDuration(200);
		inner.startAnimation(ta);
	   // 设置回到正常的布局位置
		inner.layout(normal.left, normal.top, normal.right, normal.bottom);	
		imgNormal.setEmpty();
		normal.setEmpty();
		isCount = false;
		y = 0;// 手指松开要归0.

	}

	// 是否需要开启动画
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}
	/***
	 * 是否需要移动布局 inner.getMeasuredHeight():获取的是控件的总高度
	 * 
	 * getHeight()：获取的是屏幕的高度
	 * 
	 * @return
	 */
	public void isNeedMove() {
		// 0是顶部，后面那个是底部
		if(isClose(mChild,imageView)){
		    isMoveing=true;}
		else 
			isMoveing=false;
		
}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private RectF getRect(View view){
		RectF rectf;
		if (AndroidVersionCheckUtils.hasHoneycomb()) {//Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
			rectf = new RectF(view.getX(), view.getY(), view.getX() + view.getWidth(), view.getY() + view.getHeight());
		} else {
			rectf = new RectF(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
		}
		return rectf;
	}
	private RectF getRelativeRect(View parentView,View childView){
		RectF rectf=new RectF(childView.getLeft(), childView.getTop()+parentView.getTop(), 
				childView.getRight(), childView.getBottom()+parentView.getBottom());
		return rectf;
	}
	private boolean isClose(View src,View target){
	    	RectF rectSrc=getRelativeRect(mParent, src);
	    	RectF rectTar=getRect(target);
		return rectSrc.intersect(rectTar);
	}
//	public interface OnHeaderRefreshListener {
//		public void onHeaderRefresh(MyScrollView view);
//	}
//	
//	public void setOnHeaderRefreshListener(OnHeaderRefreshListener headerRefreshListener) {
//		mOnHeaderRefreshListener = headerRefreshListener;
//	}
}
