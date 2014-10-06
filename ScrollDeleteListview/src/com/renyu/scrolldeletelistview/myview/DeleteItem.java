package com.renyu.scrolldeletelistview.myview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2014/9/17.
 */
public class DeleteItem extends LinearLayout {

    //����������ͼ�Ŀ��
    int backWidth=0;
    Scroller scroller=null;
    //��һ���������Xλ��
    int startTouchX=0;
    //��һ�ε��������X
    int firstDownX=0;
    //�Ƿ��Item getScrollX���ӳ٣���ֻ�ܼӱ�־λ�ж�
    boolean isOpenItem=false;

    public DeleteItem(Context context) {
        super(context);
        init(context);
    }

    public DeleteItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DeleteItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        scroller=new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int num=getChildCount();
        for (int i=0;i<num;i++) {
            View view=getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if (i==1) {
                backWidth=view.getMeasuredWidth();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int num=getChildCount();
        int startX=0;
        for (int i=0;i<num;i++) {
            //��������
            View view=getChildAt(i);
            view.layout(startX, 0, startX+view.getMeasuredWidth(), view.getMeasuredHeight());
            startX+=view.getMeasuredWidth();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX=(int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouchX=currentX;
                firstDownX=currentX;
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollX()>backWidth) {
                    scrollTo(backWidth, 0);
                }
                else if (getScrollX()<0) {
                    scrollTo(0, 0);
                }
                else {
                    if(DeleteListView.isLock) {
                        scrollBy(startTouchX-currentX, 0);
                    }
                }
                startTouchX=currentX;
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX()>backWidth/2) {
                	scrollState();
                }
                else {
                	resetState();
                }
                //ΪsetOnItemClickListener����
                if(Math.abs(currentX-firstDownX)<5) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * �ر�item
     */
    public void resetState() {
        isOpenItem=false;
        scrollTo(0, 0);
    }

    /**
     * չ��item
     */
    public void scrollState() {
        isOpenItem=true;
        scrollTo(backWidth, 0);
    }

    public boolean isScrollToBackWidth() {
    	System.out.println(isOpenItem);
        return isOpenItem?true:false;
    }
}
