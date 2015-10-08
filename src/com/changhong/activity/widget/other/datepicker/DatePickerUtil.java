package com.changhong.activity.widget.other.datepicker;

import java.util.Collection;
import java.util.Map;

import com.changhong.CHApplication;
import com.changhong.activity.util.DisplayUtil;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;;

/**
 * Window及UI缩放工具
 * 
 */

public class DatePickerUtil {
    /** 自动缩放严格模式标志 */
    private static final String AUTO_RESIZE_STRICT_TAG = "strict_mode";
    /** 状态栏高度 */
    public static int STATUS_BAR_HEIGHT;
    /** 缩放比例:水平 */
    public static float SCALE_RATIO_HORIZONTAL;
    /** 缩放比例:垂直 */
    public static float SCALE_RATIO_VERTICAL;
    /** 缩放比例 */
    public static float SCALE_RATIO;
    /** 屏幕旋转度 */
    public static int WINDOW_ROTATION;
    /** UI设计的竖向高度,单位：px */
    private static final int UI_DESIGN_PORTRAIT_SIZE = 1800;
    /** UI设计的横向高度,单位：px */
    private static final int UI_DESIGN_LANDSCAPE_SIZE = 1080;

    
 
    static{
    	computeScaleRatio();
    	}
    /**
     * 递归重新计算View及其子View的宽高
     * 
     * @param view
     * @return
     */
    public static  boolean resizeRecursively(View view) {
    	
        return resizeRecursively(view, SCALE_RATIO, SCALE_RATIO);
    }
    
    /**
     * View按UI设计大小等比缩放，重新计算view的宽高、边距、文字大小
     * 
     * @param view
     * @return
     */
    public static boolean resize(View view) {
        return resize(view, SCALE_RATIO_HORIZONTAL, SCALE_RATIO);
    }
    /**
     * 递归重新计算view的宽高
     * 
     * @param view
     * @param horizontalRatio
     *            水平缩放比例
     * @param verticalRatio
     *            垂直缩放比例
     * @return
     */
    private static  boolean resizeRecursively(View view, float horizontalRatio, float verticalRatio) {
        if (view == null)
            return false;
        /* 是否为严格模式 */
        boolean strictMode = isStrictMode(view);
        /* 如果当前View需要以严格模式缩放，自动将所有子孙View按照严格模式缩放 */
        if (strictMode)
            return resizeStrictRecursively(view, SCALE_RATIO, SCALE_RATIO);
        resize(view, horizontalRatio, verticalRatio);
        if (!(view instanceof ViewGroup))
            return true;
        ViewGroup group = ((ViewGroup) view);
        int childCount = group.getChildCount();
        View child = null;
        for (int i = 0; i < childCount; i++) {
            child = group.getChildAt(i);
            resizeRecursively(child, horizontalRatio, verticalRatio);
        }
        return true;
    }

    /**
     * 是否为严格缩放模式
     * 
     * @param view
     * @return
     */
    private static boolean isStrictMode(View view) {
        boolean strictMode = false;
        Object tag = view.getTag();
        if (tag == null)
            return false;
        String tagString = String.valueOf(tag);
        if (AUTO_RESIZE_STRICT_TAG.equals(tagString))
            strictMode = true;
        return strictMode;
    }
    
    /**
     * 递归重新计算view的宽高(严格模式)
     * 
     * @param view
     * @param horizontalRatio
     *            水平缩放比例
     * @param verticalRatio
     *            垂直缩放比例
     * @return
     */
    private static boolean resizeStrictRecursively(View view, float horizontalRatio, float verticalRatio) {
        if (view == null)
            return false;
        resizeStrictly(view, horizontalRatio, verticalRatio);
        if (!(view instanceof ViewGroup))
            return true;
        ViewGroup group = ((ViewGroup) view);
        int childCount = group.getChildCount();
        View child = null;
        for (int i = 0; i < childCount; i++) {
            child = group.getChildAt(i);
            resizeStrictRecursively(child, horizontalRatio, verticalRatio);
        }
        return true;
    }
    
    /**
     * 重新计算view的宽高、边距、文本大小<br>
     * (严格模式)
     * 
     * @param view
     * @param horizontalRatio
     * @param verticalRatio
     * @return
     */

    public static boolean resizeStrictly(View view, float horizontalRatio, float verticalRatio) {
        if (view == null)
            return false;
        /* 重新计算宽高 */
        resizeWidthAndHeight(view, horizontalRatio, verticalRatio);
        /* 重新计算内边距 */
        repadding(view, horizontalRatio, verticalRatio);
        /* 重新计算外边距 */
        remargin(view, horizontalRatio, verticalRatio);
        /* 重新计算文本大小 */
        if (view instanceof TextView)
            resizeText((TextView) view);
        return true;
    }
    
    /**
     * 重新计算view的宽高
     * 
     * @param view
     * @param horizontalRatio
     * @param verticalRatio
     * @return
     */

    public static boolean resizeWidthAndHeight(View view, float horizontalRatio, float verticalRatio) {
        if (view == null)
            return false;
        Object tag = view.getTag();
        if (tag instanceof String) {
            String tagString = (String) tag;
            if ("ignoreSize".equals(tagString)) {
                return true;
            }
        }
        LayoutParams params = view.getLayoutParams();
        if (params != null) {
            int width = params.width;
            int height = params.height;
            if (params.width != LayoutParams.MATCH_PARENT && params.width != LayoutParams.WRAP_CONTENT) {
                width = (int) (width * horizontalRatio);
                if (width > 1)
                    params.width = width;
            }
            if (params.height != LayoutParams.MATCH_PARENT && params.height != LayoutParams.WRAP_CONTENT) {
                height = (int) (height * verticalRatio);
                if (height > 1)
                    params.height = height;
            }
            view.setLayoutParams(params);
        }

        return true;
    }

    /**
     * 重新计算view的Padding(非严格模式)
     * 
     * @param view
     * @return
     */
    public static boolean repadding(View view) {
        return repadding(view, SCALE_RATIO_HORIZONTAL, SCALE_RATIO_VERTICAL);
    }

    /**
     * 重新计算view的Padding(严格模式)
     * 
     * @param view
     * @return
     */
    public static boolean repadding(View view, float horizontalRatio, float verticalRatio) {
        if (view == null)
            return false;
        view.setPadding(
                (int) (view.getPaddingLeft() * horizontalRatio), (int) (view.getPaddingTop() * verticalRatio),
                (int) (view.getPaddingRight() * horizontalRatio), (int) (view.getPaddingBottom() * verticalRatio));
        return true;
    }

    /**
     * 重新计算view的Margin
     * 
     * @param view
     * @return
     */
    public static void remargin(View view) {
        remargin(view, SCALE_RATIO_HORIZONTAL, SCALE_RATIO_VERTICAL);
    }

    /**
     * 重新计算view的Margin
     * 
     * @param view
     * @return
     */
    public static void remargin(View view, float horizontalRatio, float verticalRatio) {
        MarginLayoutParams marginParams = null;
        try {
            marginParams = (MarginLayoutParams) view.getLayoutParams();
        } catch (ClassCastException e) {
            return;
        }
        if (marginParams == null)
            return;
        int left = (int) (marginParams.leftMargin * horizontalRatio);
        int top = (int) (marginParams.topMargin * verticalRatio);
        int right = (int) (marginParams.rightMargin * horizontalRatio);
        int bottom = (int) (marginParams.bottomMargin * verticalRatio);
        marginParams.setMargins(left, top, right, bottom);
        view.setLayoutParams(marginParams);
    }

    /**
     * 重新计算TextView中文本的大小
     * 
     * @param view
     * @return
     */
    public static boolean resizeText(TextView view) {
        if (view == null)
            return false;
        Object tag = view.getTag();
        if (tag instanceof String) {
            String tagString = (String) tag;
            if ("ignoreSize".equals(tagString)) {
                return true;
            }
        }
        float textSize = view.getTextSize();
        float ratio = SCALE_RATIO;
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize * ratio);
        return true;
    }

    /**
     * 重新计算view的宽高(高度及宽度均按照水平缩放比例)
     * 
     * @param view
     * @return
     */
    public static boolean resizeWithHorizontalRatio(View view) {
        return resize(view, SCALE_RATIO_HORIZONTAL, SCALE_RATIO_HORIZONTAL);
    }
    
    /**
     * 重新计算view的宽高、边距、文本大小<br>
     * （其中宽高、文本大小按照相同缩放系数；内外边距水平方向按照水平比例系数，垂直方向按照垂直比例系数）
     * 
     * @param view
     * @param horizontalRatio
     * @param verticalRatio
     * @return
     */

    public static boolean resize(View view, float horizontalRatio, float verticalRatio) {
        if (view == null)
            return false;
        /* 重新计算宽高 */
        resizeWidthAndHeight(view, horizontalRatio, verticalRatio);
        /* 重新计算内边距 */
        repadding(view);
        /* 重新计算外边距 */
        remargin(view);
        /* 重新计算文本大小 */
        if (view instanceof TextView)
            resizeText((TextView) view);
        return true;
    }
    
    /**
     * 重新计算尺寸像素值，并乘以缩放系数ratio
     * 
     * @return
     */
    public static int computeScaledSize(int size) {
        return (int) (size * SCALE_RATIO);
    }

    /**
     * 重新计算尺寸像素值，并乘以缩放系数ratio
     * 
     * @return
     */
    public static int computeScaledSize(float size) {
        return (int) (size * SCALE_RATIO);
    }

    /**
     * 计算UI/字体缩放比例
     */
    public static  void computeScaleRatio() {
        int windowWidth = DisplayUtil.getScreenWidth();
        int windowHeight = DisplayUtil.getScreenHeight();
        if (windowWidth == 0 || windowHeight == 0)
            return;
        int designedWidth = (windowWidth > windowHeight) ? UI_DESIGN_PORTRAIT_SIZE : UI_DESIGN_LANDSCAPE_SIZE;
        int designedHeight = (windowWidth > windowHeight) ? UI_DESIGN_LANDSCAPE_SIZE : UI_DESIGN_PORTRAIT_SIZE;
        SCALE_RATIO_HORIZONTAL = (float) windowWidth / (float) designedWidth;
        SCALE_RATIO_VERTICAL = (float) windowHeight / (float) designedHeight;
        float ratioDesigned = (float) UI_DESIGN_PORTRAIT_SIZE / (float) UI_DESIGN_LANDSCAPE_SIZE;
        float ratioDevice = (float) windowHeight / (float) windowWidth;
        /* 当设备宽高比例与UI设计的比例相同，或者设备宽高比例比UI设计的比例瘦长时按照宽度等比缩放(主流)，相反，如果比UI设计的比例胖扁时按照高度缩放 */
        SCALE_RATIO = ratioDevice >= ratioDesigned ? SCALE_RATIO_HORIZONTAL : SCALE_RATIO_VERTICAL;
    }
}