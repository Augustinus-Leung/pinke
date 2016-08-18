package com.jam.pinkenew.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by augustinus on 16/8/18.
 */

public class Container extends HorizontalScrollView {

    private int menu_width;//菜单栏的宽度
    private int screen_width;//屏幕宽度
    private int layout_times;
    private boolean is_first_measure;
    private boolean is_menu_open;
    private int right_padding = 50;//侧拉栏出现的时候,右边内容的大小:150dp
    private View menu,content;
    private boolean is_dispatchchild;//时间是否分发给子view
    private int position;//记录当前所在页面
    private OnMenuStateChangeListener listener;

    public Container(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    private void init(){
        is_first_measure = true;
        layout_times = 0;
        is_menu_open = false;
        is_dispatchchild = false;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screen_width = windowManager.getDefaultDisplay().getWidth();
        right_padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,right_padding,getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (is_first_measure){
            is_first_measure = false;
            LinearLayout layout = (LinearLayout) getChildAt(0);
            menu = layout.getChildAt(0);
            //菜单的宽度
            menu_width = menu.getLayoutParams().width = screen_width - right_padding;
            //主界面的大小
            content = layout.getChildAt(1);
            content.getLayoutParams().width = screen_width;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (layout_times == 1){
            //自动移到content
            moveToContent();
        }else if (layout_times<2){
            ++layout_times;
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (l == 0&&listener!=null){
            listener.onMenuStateChanged(true);
        }else if (l==menu_width&&listener!=null){
            listener.onMenuStateChanged(false);
        }
        //缩放的值
        float scale = l*1.0f/menu_width;
        //        float left_scale=1.0f-0.3f*scale;
//        float right_scale=0.8f+0.2f*scale;
//        float alpha=0.5f+0.5f*(1-scale);
//        //菜单变化效果
        ViewHelper.setTranslationX(menu, scale);
//        ViewHelper.setScaleY(menu, left_scale);
//        ViewHelper.setScaleX(menu, left_scale);
//        ViewHelper.setAlpha(menu, alpha);
//
//        //content 的动画效果
        ViewHelper.setTranslationX(content,1-scale);
//        ViewHelper.setPivotY(content,0);
//        ViewHelper.setPivotY(content,content.getHeight()/2);
//        ViewHelper.setScaleX(content, right_scale);
//        ViewHelper.setScaleY(content,right_scale);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!is_menu_open&&ev.getAction()==MotionEvent.ACTION_DOWN&&ev.getX()>50){//事件由子view处理
            is_dispatchchild = true;
        }
        if (is_dispatchchild){
            if (ev.getAction()==MotionEvent.ACTION_UP)//up事件之后,事件分发结束
                is_dispatchchild = false;
            content.dispatchTouchEvent(ev);
            return  true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_UP){//滑动不超过一般返回menu
            int s = getScrollX();
            if (s>=menu_width/2)
                moveToContent();
            else
                moveToMenu();
            return true;
        }
        return super.onTouchEvent(ev);
    }

    //移动到主要界面
    public void moveToContent(){
        scrollTo(menu_width,0);
        is_menu_open = false;
    }
    //移动到菜单
    public void moveToMenu(){
        scrollTo(0,0);
        is_menu_open = true;
    }
    public void exchange()//在content和menu
    {
        if(is_menu_open) {
            moveToContent();
            if(listener!=null)
            {
//                listener.onMenuStateChanged(false);
            }
        }
        else {
            moveToMenu();
//            listener.onMenuStateChanged(true);
        }
    }
    private boolean is_first_pager()
    {
        return position==0;
    }
    public void setPosition(int position)
    {
        this.position=position;
    }
    public interface OnMenuStateChangeListener {
        public void onMenuStateChanged(boolean isOpen);
    }
    public  void setMenuChangeListener(OnMenuStateChangeListener listener)
    {
        this.listener=listener;
    }
}
