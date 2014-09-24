package com.renyu.myfloatscrollview.myview;

import com.renyu.myfloatscrollview.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {
	
	//������ͼ��ʼtopλ��
	int floatPos=0;

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		ViewGroup parent=(ViewGroup) getChildAt(0);
		int childNum=parent.getChildCount();
		for(int i=0;i<childNum;i++) {
			//��Ҫ��xml�����úø�����ͼ�Լ����������ͼ�������������ͼ��������Ϊ������ͼ����
			if(parent.getChildAt(i).getId()==R.id.target_view) {
				floatPos=parent.getChildAt(i).getTop();
			}
			if(parent.getChildAt(i).getId()==R.id.float_view) {
				parent.getChildAt(i).layout(parent.getChildAt(i).getLeft(), floatPos, parent.getChildAt(i).getLeft()+parent.getChildAt(i).getMeasuredWidth(), floatPos+parent.getChildAt(i).getMeasuredHeight());
			}
		}
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		ViewGroup parent=(ViewGroup) getChildAt(0);
		int childNum=parent.getChildCount();
		for(int i=0;i<childNum;i++) {
			if(parent.getChildAt(i).getId()==R.id.float_view) {
				//�˴�layout������������ڸ���ͼ�ģ������ڻ�������Ļ�����Ϸ�֮ǰ����Ҫά��֮ǰ�����꣬һ������������Ļ���Ϸ�����Ҫͨ���ı�topֵ��ʹ��ά������Ļ���Ϸ�
				int startY=t<floatPos?floatPos:t;
				parent.getChildAt(i).layout(parent.getChildAt(i).getLeft(), startY, parent.getChildAt(i).getLeft()+parent.getChildAt(i).getMeasuredWidth(), startY+parent.getChildAt(i).getMeasuredHeight());				
			}
		}
	}

}
