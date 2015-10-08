package com.changhong.activity.widget.other.datepicker;

import java.util.LinkedList;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import com.llw.AppStore.R;

/**
 * 滚轮
 * 
 * @since 2013-12-26
 */
public class WheelView extends WheelScrollableView implements OnClickListener {
    private float lineSplitHeight;
    private Paint textPaintFirst;
    private Paint textPaintSecond;
    private Paint textPaintThird;
    private Paint linePaint;
    private float textBaseY;
    private float secondRectTop;
    private float secondRectBottom;
    private float thirdRectTop;
    private float thirdRectBottom;
    private float FirstTextSize,SecondTextSize,ThirdTextSize;
    private float itemHeight;
    private int textGravity;
    private WheelAdapter adapter;

    private float contentHeight;

    private float lastEventY;

    private float scrollY;

    private VelocityTracker velocityTracker;

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInterpolator(context,new DecelerateInterpolator());
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.wheel);
        int textColorFirst = a.getColor(R.styleable.wheel_textColorFirst, -1);
        int textColorSecond = a.getColor(R.styleable.wheel_textColorSecond, -1);
        int textColorThird = a.getColor(R.styleable.wheel_textColorThird, -1);
        this.FirstTextSize = a.getDimension(R.styleable.wheel_textSizeFirst, -1);
//        this.textSize = DatePickerUtil.computeScaledSize(textSize);
        this.FirstTextSize = DatePickerUtil.computeScaledSize(FirstTextSize);
        this.SecondTextSize = a.getDimension(R.styleable.wheel_textSizeSecond, -1);
        this.SecondTextSize = DatePickerUtil.computeScaledSize(SecondTextSize);
        this.ThirdTextSize = a.getDimension(R.styleable.wheel_textSizeThird, -1);
        this.ThirdTextSize = DatePickerUtil.computeScaledSize(ThirdTextSize);
        int lineColor = a.getColor(R.styleable.wheel_splitLineColor, -1);
        this.lineSplitHeight = a.getDimension(R.styleable.wheel_lineSplitHeight, -1);
        this.lineSplitHeight = DatePickerUtil.computeScaledSize(this.lineSplitHeight);
        this.itemHeight = this.ThirdTextSize + this.lineSplitHeight;
        this.textGravity = a.getInt(R.styleable.wheel_textGravity, -1);
        a.recycle();

        this.textPaintFirst = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        this.textPaintFirst.setTextSize(FirstTextSize);
        this.textPaintFirst.setColor(textColorFirst);
        this.textPaintSecond = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        this.textPaintSecond.setTextSize(SecondTextSize);
        this.textPaintSecond.setColor(textColorSecond);
        this.textPaintThird = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        this.textPaintThird.setTextSize(ThirdTextSize);
        this.textPaintThird.setColor(textColorThird);

        this.linePaint = new Paint();
        this.linePaint.setColor(lineColor);
        this.linePaint.setStrokeWidth(1.618f);
    }

    @Override
    protected void onInit() {
        super.onInit();
//        setInterpolator(new DecelerateInterpolator());
        setOnClickListener(this);
        velocityTracker = VelocityTracker.obtain();
    }

    public void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        this.render();
        this.adapter.setWheelView(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.adapter == null || this.adapter.getCount() <= 0)
            return;
        drawSplitLine(canvas);
        canvas.save();
        
        Rect firstRect = new Rect(0,0, getMeasuredWidth(), (int) this.secondRectTop);
        drawClipText(canvas, firstRect, textPaintFirst);
        firstRect.setEmpty();
        firstRect.set(0, (int)this.secondRectBottom, getMeasuredWidth(), (int) getMeasuredHeight());
        drawClipText(canvas, firstRect, textPaintFirst);
        
        Rect secondRect = new Rect(0, (int) this.secondRectTop, getMeasuredWidth(), (int) this.thirdRectTop);
        drawClipText(canvas, secondRect, textPaintSecond);
        secondRect.setEmpty();
        secondRect.set(0, (int) this.thirdRectBottom, getMeasuredWidth(),  (int) (this.thirdRectBottom+itemHeight));
        drawClipText(canvas, secondRect, textPaintSecond);
            
        Rect thirdRect = new Rect(0, (int) this.thirdRectTop, getMeasuredWidth(), (int) this.thirdRectBottom);
        drawClipText(canvas, thirdRect, textPaintThird);
        
    }

   /**
    * 切分画布进行绘制
    * @param canvas
    * @param rect
    * @param paint
    */
    private void drawClipText(Canvas canvas,Rect rect,Paint paint){
        canvas.clipRect(rect);
        drawText(canvas, paint);
        canvas.restore();
        canvas.save();
    }
    /**
     * 画分割线
     * 
     * @param canvas
     */
    private void drawSplitLine(Canvas canvas) {
        canvas.drawLine(0, this.thirdRectTop, getMeasuredWidth(), this.thirdRectTop, linePaint);
        canvas.drawLine(0, this.thirdRectBottom, getMeasuredWidth(), this.thirdRectBottom, linePaint);
    }

    /**
     * 画文本
     * 
     * @param canvas
     * @param paint
     */
    private void drawText(Canvas canvas, Paint paint) {
        float maxWidth = this.adapter.getMaxWidth(paint);
        int count = this.adapter.getCount();
        int px = getMeasuredWidth();
        float textWidth = 0f;
        float x = 0f;
        float y = 0f;
        String text ="";
        for (int index = 0; index < count; index++) {
            text = this.adapter.getItem(index);
            textWidth = paint.measureText(text);
            x = computeTextX(maxWidth, px, textWidth);
            y = this.textBaseY + scrollY;
            canvas.drawText(text, x, y, paint);
            canvas.translate(0, itemHeight);
        }

    }

    private float computeTextX(float maxWidth, int px, float textWidth) {
//        if (this.textGravity == 0)
            return px / 2 - textWidth / 2 + getPaddingLeft() - getPaddingRight();
//        return px / 2 - maxWidth / 2 + maxWidth - textWidth + getPaddingLeft() - getPaddingRight();
    }

    public void computeTextBaseY() {
        if (this.adapter == null || this.adapter.getCount() <= 0)
            return;
        int count = this.adapter.getCount();
        this.contentHeight = count * this.ThirdTextSize + (count - 1) * this.lineSplitHeight;
        this.textBaseY = -textPaintThird.getFontMetrics().top + this.getMeasuredHeight() / 2 - this.ThirdTextSize / 2;
    }

    private void computeCenterRect() {
        if (this.adapter == null || this.adapter.getCount() <= 0)
            return;
        this.thirdRectTop = (getMeasuredHeight() / 2 - this.ThirdTextSize / 2)-10;
        this.thirdRectBottom = this.thirdRectTop + this.ThirdTextSize + this.lineSplitHeight / 2;
        this.secondRectTop = this.thirdRectTop - this.itemHeight;
        this.secondRectBottom = this.thirdRectBottom + this.itemHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        computeTextBaseY();
        computeCenterRect();
    }

    @Override
    public void onClick(View v) {
        LinkedList<WheelStep> stepList = new LinkedList<WheelStep>();
        WheelStep step1 = WheelStep.createDistanceStep(200, 200, 2000);
        WheelStep step2 = WheelStep.createDistanceStep(-200, -200);
        WheelStep step3 = WheelStep.createDistanceStep(200, -200);
        WheelStep step4 = WheelStep.createDistanceStep(0, 0);
        stepList.add(step1);
        stepList.add(step2);
        stepList.add(step3);
        stepList.add(step4);
        move(stepList);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        velocityTracker.addMovement(event);
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            stopScroll();
            break;
        case MotionEvent.ACTION_MOVE:
            int dy = (int) (event.getY() - lastEventY);
            scrollY += dy;
            makeSureScrollVisible();
            render();
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_OUTSIDE:
            fling();
            break;
        }
        lastEventY = event.getY();
        return true;
    }

    private int getCurrentY() {
        return (int) -scrollY;
    }

    @SuppressWarnings("deprecation")
    private void fling() {
        velocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
        float velocityY = velocityTracker.getYVelocity();
        int currentY = getCurrentY();
        int duration = WheelStep.DEFAULT_DURATION;
        int distanceY = (int) (velocityY / 1000 * duration);
        int direction = velocityY > 0 ? -1 : 1;
        int maxDistance = (int) (velocityY > 0 ? Math.abs(this.scrollY) : this.contentHeight - Math.abs(this.scrollY));
        distanceY = direction * Math.min(Math.abs(distanceY), maxDistance);
        if (Math.abs(velocityY) < ViewConfiguration.getMinimumFlingVelocity()) {
            scrollToRightPosition();
            return;
        }
        smoothScrollBy(0, currentY, 0, distanceY, duration);
        scrollToRightPositionDelayed(duration);
    }

    private void scrollToRightPositionDelayed(int duration) {
        postDelayed(new Runnable() {

            @Override
            public void run() {
                scrollToRightPosition();
            }
        }, duration);
    }

    private void scrollToRightPosition() {
        int index = getCurrentItemIndex();
        int position = (int) getItemPosition(index);
        smoothScrollTo(0, -(int) scrollY, 0, position, 250);
    }

    @Override
    public void doScrollTo(int x, int y) {
        scrollY = y;
        makeSureScrollVisible();
    }

    public void makeSureScrollVisible() {
        if (scrollY > this.lineSplitHeight)
            scrollY = this.lineSplitHeight;
        else if (scrollY < -(this.contentHeight)) {
            scrollY = -(int)(this.contentHeight);
            scrollToRightPosition();
        }
    }

    /**
     * 获取当前选择的条目索引
     * 
     * @return
     */
    public int getCurrentItemIndex() {
    	
    	if(this.adapter==null){
    		return 0;
    	}
  
    	int position = 0;
    	if(this.adapter!=null){
			
			if (scrollY > 0) {
				position = 0;
				return position;
			}
			position = (int) (Math.abs(scrollY) / itemHeight);
			int reminer = (int) (Math.abs(scrollY) % itemHeight);
			if (reminer > itemHeight / 2)
				position++;
			if (position > this.adapter.getCount() - 1)
				// Log.e("wheel",
				// "------adapterCount------->"+this.adapter.getCount());
				position = this.adapter.getCount() - 1;
			return position;
        }
		return position;
    }

    public String getCurrentItemString() {
        return this.adapter.getItem(getCurrentItemIndex());
    }

    public int getCurrentValue() {
        return this.adapter.getValue(getCurrentItemIndex());
    }

    /**
     * 获取指定条目在视图中的位置
     * 
     * @param itemIndex
     * @return
     */
    private float getItemPosition(int itemIndex) {
        return this.itemHeight * itemIndex;
    }

    public void select(int index) {
        this.scrollY = -getItemPosition(index);
        render();
    }

    public void setCurrentValue(int value) {
        int index = this.adapter.getValueIndex(value);
        select(index);
    }
    public void setCurrentStringValue(String str){
    	int index=this.adapter.getStringValueIndex(str);
    	select(index);
    }

    public void setStartValue(int value) {
        this.adapter.setStartValue(value);
        this.adapter.notifyChanged();
    }
}
