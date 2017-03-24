# 介绍 #
本Demo是ViewDragHelper使用的示例项目

# ViewDragHepler #
ViewDragHelper是v4包中的一个工具类，主要是帮助实现自定义ViewGroup来完成拖拽孩子的功能。

# ViewDragHeper的使用姿势 #

## 1. 初始化 ##
	mViewDragHelper = ViewDragHelper.create(this, mCallback);

## 2. 处理Touch事件 ##

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

## 3. 实现回调方法 ##
	private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        /**
         * 是否拖拽孩子
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


# 案例：音乐播放界面 #

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, mCallback);
        //监听是否完成布局
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //获取最开始是图片的位置
                mCoverImageInitX = mImageCover.getLeft();
                mCoverImageInitY = mImageCover.getTop();
                //计算拖拽的最大距离
                mMaxTop = getHeight() - mBottomBar.getHeight();
            }
        });
        mCoverImagePivotX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        mCoverImagePivotY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
    }

    @Override
    protected void onFinishInflate() {
        mCover = findViewById(R.id.cover);
        mBottomBar = findViewById(R.id.bottom_bar);
        mImageCover = findViewById(R.id.image_cover);
    }


    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {


        /**
         *  只允许拖拽播放界面
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mCover;
        }

        /**
         * 只处理竖直方向的拖动
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int max = getHeight() - mBottomBar.getHeight();//计算竖直方向top的最大值
            /* 越界处理
             if (top > max) {
                return max;
            } else if(top <= 0) {
                return 0;
            }
            return top;
            */
            return Math.min(Math.max(0, top), max);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            float rate = top * 1.0f / mMaxTop;//计算拖动的距离占最大值的比例

            //当拖动页面往下移动多少时，底部条就往上移动多少
            mBottomBar.setTranslationY(-top);
            //缩放底部条 大小比例从1到3/5
            mBottomBar.setScaleX(1 - rate * 2 / 5);//[1, 3/5]
            mBottomBar.setScaleY(1 - rate * 2 / 5);//[1, 3/5]
            //设置缩放中心, 默认缩放中心为底部条的中心
            mBottomBar.setPivotX(mBottomBar.getWidth());


            //设置图片的偏移量
            mImageCover.setTranslationX(rate * - mCoverImageInitX);
            mImageCover.setTranslationY(rate * - mCoverImageInitY);

            //缩放图片，大小比例从1到1/4
            mImageCover.setScaleX(1 - rate * 3/4);
            mImageCover.setScaleY(1 - rate * 3/4);

            //调整图片的缩放中心
            mImageCover.setPivotX(mCoverImagePivotX);
            mImageCover.setPivotY(mCoverImagePivotY);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            //当松开时往下拖拽的比例超过一半时则全部滚到下面
            if (mCover.getTop() > mMaxTop / 2) {
                mViewDragHelper.smoothSlideViewTo(releasedChild, 0, mMaxTop);
            } else {
                //当松开时往下拖拽的比例不超过一半时则全部滚到上面
                mViewDragHelper.smoothSlideViewTo(releasedChild, 0, 0);
            }
            invalidate();//触发重新绘制
        }
    };

    /**
     * 计算新的滚动偏移量
     */
    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();//触发重新绘制
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
