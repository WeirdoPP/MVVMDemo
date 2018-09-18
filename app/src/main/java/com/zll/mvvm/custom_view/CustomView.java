package com.zll.mvvm.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc 自定义view 详细解析view的绘制,测量,事件分发,触摸事件,动画等
 * @since 2018/09/14
 * <p>
 * 绘制流程 :
 * 将decor传递给ViewRoot，这样ViewRoot就和DecorView建立了关联。
 * ViewRoot类的成员函数setView会调用ViewRoot类的另外一个成员函数requestLayout来请求
 * 对顶层视图decor 作第一次布局以及显示。
 * 调用ViewRootImpl类的performTraversals方法，
 * performTraversals方法会依次调用performMeasure方法、performLayout方法和performDram方法来
 * 完成顶层视图decor的测量过程 （measure）、布局过程（layout）和绘制过程（draw）。
 * <p>
 * https://www.cnblogs.com/jycboy/p/6219915.html
 */

public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测量:
     * ViewGroup类型的View和非ViewGroup类型的View的测量过程是不同的，
     * 非ViewGroup类型的View通过onMeasure方法就完成了其测量过程，
     * 而ViewGroup类型的View除了通过onMeasure方法就完成自身的测量过程外，
     * 还要在onMeasure方法中完成遍历子View的measure方法，各个子View再去递归执行这个流程。
     * <p>
     * 在某些极端情况下，系统可能需要多次measure才能确定最终的测量宽高，
     * 在这种情况下，在onMeasure方法中拿到的测量宽高很可能是不准确的，
     * 一个好的习惯是在onLayout方法中去获取View最终的测量宽高
     * <p>
     * 在activity中获取宽高:
     * 1 在Activity/View#onWindowFocusChanged方法中获取
     * 2 在Activity中的onStart方法中执行View.post获取
     * 3 通过ViewTreeObserver获取
     * 4 通过手动执行View.measure获取
     * <p>
     * 总结：
     * 1> 父View会遍历测量每一个子View(通常使用ViewGroup类的measureChildWithMargins方法)，然后调用子View的measure方法并且将测量后的宽高作为measure方法的参数，但是这只是父View的建议值，子View可以通过继承onMeasure来改变测量值。
     * 2> 非ViewGroup类型的View自身的测量是在非ViewGroup类型的View的onMeasure方法中进行测量的
     * 3> ViewGroup类型的View自身的测量是在ViewGroup类型的View的onMeasure方法中进行测量的
     * 4>直接继承ViewGroup的自定义控件需要重写onMeasure方法并且设置wrap_content时的自身大小，否者在布局中使用wrap_content就相当于使用math_parent，具体原因通过上面的表格可以说明。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
