package com.example.leon.viewdraghelperdemo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Leon on 2017/3/24.
 */

public class MyViewGroup extends FrameLayout {

    private ViewDragHelper mViewDragHelper;


    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        /**
         *
         * @param child 点击到的准备拖拽的孩子
         * @param pointerId
         * @return true表示允许拖拽孩子child
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         *  水平方向拖动孩子时返回孩子的左边位置
         *
         * @param child 拖拽的孩子
         * @param left 拖拽孩子参考的左边位置
         * @param dx 拖拽孩子x轴的变化量
         * @return 返回孩子新的左边位置
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }

        /**
         * 竖直方向拖动孩子时孩子的顶边位置
         *
         * @param child 拖拽的孩子
         * @param top 拖拽孩子参考的顶边位置
         * @param dy 拖拽孩子y轴的变化量
         * @return 返回孩子新的顶边位置
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        /**
         * 当位置发生变化的回调
         *
         * @param changedView 位置发生变化的孩子
         * @param left changedView的左边位置
         * @param top changedView的顶边位置
         * @param dx changedView的x轴的变化量
         * @param dy changedView的y轴的变化量
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * 当松开孩子时的回调
         * @param releasedChild 松开的孩子
         * @param xvel 松开时孩子的左边位置
         * @param yvel 松开时孩子的右边位置
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
