package com.zll.mvvm.view.layout_adapter;

import google.architecture.common.base.BaseActivity;
import google.architecture.common.base.BaseViewModel;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/11
 * <p>
 * 屏幕适配
 * 定义 : 使得某一元素在Android不同尺寸、不同分辨率的手机上具备相同的显示效果
 * 屏幕尺寸 含义：手机对角线的物理尺寸  单位：英寸（inch），1英寸=2.54cm
 * <p>
 * 屏幕分辨率
 * 含义：手机在横向、纵向上的像素点数总和
 * 一般描述成屏幕的"宽x高”=AxB
 * 含义：屏幕在横向方向（宽度）上有A个像素点，在纵向方向
 * （高）有B个像素点
 * 例子：1080x1920，即宽度方向上有1080个像素点，在高度方向上有1920个像素点
 * 单位：px（pixel），1px=1像素点
 * <p>
 * 屏幕像素密度
 * 含义：每英寸的像素点数
 * 单位：dpi（dots per ich）
 * 假设设备内每英寸有160个像素，那么该设备的屏幕像素密度=160dpi
 * 安卓手机对于每类手机屏幕大小都有一个相应的屏幕像素密度：
 * 密度类型	代表的分辨率（px）	屏幕像素密度（dpi）
 * 低密度（ldpi）	240x320	120
 * 中密度（mdpi）	320x480	160
 * 高密度（hdpi）	480x800	240
 * 超高密度（xhdpi）	720x1280	320
 * 超超高密度（xxhdpi）	1080x1920	480
 * <p>
 * 密度无关像素
 * 含义：density-independent pixel，叫dp或dip，与终端上的实际物理像素点无关。
 * 单位：dp，可以保证在不同屏幕像素密度的设备上显示相同的效果
 * <p>
 * dp与px的转换
 * 因为ui设计师给你的设计图是以px为单位的，Android开发则是使用dp作为单位的，那么我们需要进行转
 * 在Android中，规定以160dpi（即屏幕分辨率为320x480）为基准：1dp=1px
 * <p>
 * 独立比例像素
 * 含义：scale-independent pixel，叫sp或sip
 * 单位：sp
 * Android开发时用此单位设置文字大小，可根据字体大小首选项进行缩放
 * 推荐使用12sp、14sp、18sp、22sp作为字体设置的大小，不推荐使用奇数和小数，容易造成精度的丢失问题；小于12sp的字体会太小导致用户看不清
 * <p>
 * 本质1：使得布局元素自适应屏幕尺寸
 * 对于线性布局（Linearlayout）、相对布局（RelativeLayout）和帧布局（FrameLayout）需要根据需求进行选择，但要记住：
 * RelativeLayout
 * 布局的子控件之间使用相对位置的方式排列，因为RelativeLayout讲究的是相对位置，即使屏幕的大小改变，视图之前的相对位置都不会变化，与屏幕大小无关，灵活性很强
 * LinearLayout
 * 通过多层嵌套LinearLayout和组合使
 * 用"wrap_content"和"match_parent"已经可以构建出足够复杂的布局。但是LinearLayout无法准确地控制子视图之间的位置关系，只能简单的一个挨着一个地排列
 * 所以，对于屏幕适配来说，使用相对布局（RelativeLayout）将会是更好的解决方案
 * <p>
 * 本质2：根据屏幕的配置来加载相应的UI布局
 * 应用场景：需要为不同屏幕尺寸的设备设计不同的布局
 * 做法：使用限定符
 * 作用：通过配置限定符使得程序在运行时根据当前设备的配置（屏幕尺寸）自动加载合适的布局资源
 * 限定符类型：
 * 尺寸（size）限定符 : 这种方式只适合Android 3.2版本之前。(layout-large)
 * 最小宽度（Smallest-width）限定符 : 定义：通过指定某个最小宽度（以 dp 为单位）来精确定位屏幕从而加载不同的UI资源 sw xxxdp无论是宽度还是高度，只要大于600dp，就采用layout-sw 600dp目录
 * 布局别名 : 解决文件名的重复从而带来一些列后期维护的问题
 * 屏幕方向（Orientation）限定符
 * <p>
 * https://blog.csdn.net/wangwangli6/article/details/63258270/
 */

public class LayoutAdapterActivity extends BaseActivity<BaseViewModel> {

//    安卓手机对于每类手机屏幕大小都有一个相应的屏幕像素密度：
//    密度类型	代表的分辨率（px）	屏幕像素密度（dpi）
//    低密度（ldpi）	240x320	120
//    中密度（mdpi）	320x480	160
//    高密度（hdpi）	480x800	240
//    超高密度（xhdpi）	720x1280	320
//    超超高密度（xxhdpi）	1080x1920	480

//    对于线性布局（Linearlayout）、相对布局（RelativeLayout）和帧布局（FrameLayout）需要根据需求进行选择，但要记住：
//
//    RelativeLayout
//    布局的子控件之间使用相对位置的方式排列，因为RelativeLayout讲究的是相对位置，即使屏幕的大小改变，视图之前的相对位置都不会变化，与屏幕大小无关，灵活性很强
//            LinearLayout
//    通过多层嵌套LinearLayout和组合使
//    用"wrap_content"和"match_parent"已经可以构建出足够复杂的布局。但是LinearLayout无法准确地控制子视图之间的位置关系，只能简单的一个挨着一个地排列
//    所以，对于屏幕适配来说，使用相对布局（RelativeLayout）将会是更好的解决方案

}
