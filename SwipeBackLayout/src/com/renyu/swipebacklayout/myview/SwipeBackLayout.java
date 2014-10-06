package com.renyu.swipebacklayout.myview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class SwipeBackLayout extends RelativeLayout {
	
	//��ǰ������ͼ�ĸ���ͼ��һ����Ƕ�����ͼ�����ƴ���ͼ���ƶ�
	ViewGroup parentView=null;
	//���������ж�
	int viewWidth=0;
	//��С��������
	int scaledEdgeSlop=0;
	//��һ�δ����¼�������
	int tempx=0;
	int tempy=0;
	//action_down������
	int startX=0;
	int startY=0;
	//�Ƿ���к��򻬶�
	boolean isHSlide=false;
	//�Ƿ��ǹر�ҳ��
	boolean isFinish=false;
	//ȫ��viewpager�ϼ�
	ArrayList<ViewPager> view_pagers=null;
	
	OnSlideFinishListener slideListener;
	
	Scroller scroller=null;

	public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		init(context);
	}

	public SwipeBackLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		init(context);
	}

	public SwipeBackLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		init(context);
	}
	
	private void init(Context context) {
		view_pagers=new ArrayList<ViewPager>();
		scroller=new Scroller(context);
		scaledEdgeSlop=ViewConfiguration.get(context).getScaledEdgeSlop();
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if(changed) {
			getAllViewPager(this);
			parentView=(ViewGroup) getParent();
			viewWidth=getWidth();
		}
		
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//viewpagerֱ��ԭ������
		if(getTouchViewPager(ev)!=null&&getTouchViewPager(ev).getCurrentItem()>0) {
			return super.onInterceptTouchEvent(ev);
		}
		switch(ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX=(int) ev.getRawX();
			startY=(int) ev.getRawY();
			tempx=startX;
			tempy=startY;
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX=(int) ev.getRawX();
			int moveY=(int) ev.getRawY();
			tempx=moveX;
			//�ж��Ƿ���ִ�к���������һ���
			if(moveX-startX>scaledEdgeSlop&&Math.abs(moveY-startY)<scaledEdgeSlop) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch(event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int moveX=(int) event.getRawX();
			int moveY=(int) event.getRawY();
			int deltaX=moveX-tempx;
			tempx=moveX;
			//�ж��Ƿ���ִ�к��򻬶�
			if(Math.abs((moveX-startX))>scaledEdgeSlop&&Math.abs((moveY-startY))<scaledEdgeSlop) {
				isHSlide=true;
			}
			if(moveX>startX&&isHSlide) {
				parentView.scrollBy(-deltaX, 0);
			}
					
			break;
		case MotionEvent.ACTION_UP:
			isHSlide=false;
			if(viewWidth/2>Math.abs(parentView.getScrollX())) {
				scrollToOriginal();
				isFinish=false;
			}
			else if(viewWidth/2<Math.abs(parentView.getScrollX())) {
				scrollToRight();
				isFinish=true;
			}
			break;
		}
		return true;
	}

	/**
	 * �ƶ������ұ�
	 */
	private void scrollToRight() {
		int deltaX=viewWidth+parentView.getScrollX();
		scroller.startScroll(parentView.getScrollX(), 0, -deltaX+1, 0, Math.abs(deltaX));
		postInvalidate();
	}
	
	/**
	 * �ص���ʼλ��
	 */
	private void scrollToOriginal() {
		int deltaX=parentView.getScrollX();
		scroller.startScroll(parentView.getScrollX(), 0, -deltaX, 0, Math.abs(deltaX));
		postInvalidate();
	}
	
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if(scroller.computeScrollOffset()) {
			parentView.scrollTo(scroller.getCurrX(), scroller.getCurrY());
			postInvalidate();
			if(scroller.isFinished()) {
				if(slideListener!=null&&isFinish) {
					slideListener.onSlideFinish();
				}
			}
		}
		
	}
	
	/**
	 * �õ�ȫ��viewPager
	 * @param parent
	 */
	private void getAllViewPager(ViewGroup parent) {
		int num=parent.getChildCount();
		for(int i=0;i<num;i++) {
			View child=parent.getChildAt(i);
			if(child instanceof ViewPager) {
				view_pagers.add((ViewPager) child);
			}
			else if(child instanceof ViewGroup) {
				getAllViewPager((ViewGroup) child);
			}
		}
	}
	
	/**
	 * �õ���ǰ���viewpager
	 * @param ev
	 * @return
	 */
	private ViewPager getTouchViewPager(MotionEvent ev) {
		if(view_pagers.size()==0) {
			return null;
		}
		Rect rect=new Rect();
		for(int i=0;i<view_pagers.size();i++) {
			view_pagers.get(i).getHitRect(rect);
			if(rect.contains((int) ev.getX(), (int) ev.getY())) {
				return view_pagers.get(i);
			}
		}
		return null;
	}
	
	public interface OnSlideFinishListener {
		public void onSlideFinish();
	}
	
	public void setOnSlideFinishListener(OnSlideFinishListener slideListener) {
		this.slideListener=slideListener;
	}

}
