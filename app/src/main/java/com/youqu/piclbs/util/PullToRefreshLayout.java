package com.youqu.piclbs.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Pull-To-Refresh Layout
 * <p>
 * Created by sum on 15-7-22.
 */
public class PullToRefreshLayout extends FrameLayout {

    interface AnimationSimulatable {
        void simulateAnimation();
    }

    interface PullDownListener {
        void onPullDown(float offsetY);

        void onPullDownRelease();

        void onPullDownRefreshFinish();
    }

    public interface OnPullDownRefreshListener {
        void onPullDownRefresh();
    }

    interface PullUpListener {
        void onPullUp(float offsetY);

        void onPullUpRelease();

        void onPullUpLoadingFinish();
    }

    public interface OnPullUpLoadingListener {
        void onPullUpLoading();
    }

    private PullDownListener pullDownListener;
    private OnPullDownRefreshListener onPullDownRefreshListener;
    private PullUpListener pullUpListener;
    private OnPullUpLoadingListener onPullUpLoadingListener;

    private View refreshingView;
    private View loadingView;

    private boolean isPullDownRefreshing;
    private boolean isPullUpLoading;

    private float onDownY;

    private View contentView;

    public PullToRefreshLayout(Context context) {
        this(context, null, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        initAfterInflate();
    }

    private void initAfterInflate() {
        contentView = getChildAt(0);
        if (null == contentView) {
            throw new RuntimeException("The content view is null.");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isPullDownRefreshing || isPullUpLoading) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onDownY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float crrTouchY = event.getY();
                float offsetY = crrTouchY - onDownY;

                if (offsetY > 0 && !canContentViewScrollUp()) {
                    if (null != pullDownListener) {
                        isPullDownRefreshing = true;
                        return true;
                    }
                }

                if (offsetY < 0 && !canContentViewScrollDown()) {
                    if (null != pullUpListener) {
                        isPullUpLoading = true;
                        return true;
                    }
                }

                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    private boolean canContentViewScrollUp() {
        int negative = -1;
        return ViewCompat.canScrollVertically(contentView, negative);
    }

    private boolean canContentViewScrollDown() {
        int positive = 1;
        return ViewCompat.canScrollVertically(contentView, positive);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isPullDownRefreshing) {
            return handlePullDownEvent(event);
        }

        if (isPullUpLoading) {
            return handlePullUpEvent(event);
        }

        return super.onTouchEvent(event);
    }

    private boolean handlePullDownEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float crrTouchY = event.getY();
            float offsetY = crrTouchY - onDownY;
            pullDownListener.onPullDown(offsetY);
            return true;

        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            pullDownListener.onPullDownRelease();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private boolean handlePullUpEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float crrTouchY = event.getY();
            float offsetY = crrTouchY - onDownY;
            pullUpListener.onPullUp(offsetY);
            return true;

        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            pullUpListener.onPullUpRelease();
            return true;
        }

        return super.onTouchEvent(event);
    }

    View getContentView() {
        return contentView;
    }

    void setPullDownListener(PullDownListener pullDownListener) {
        this.pullDownListener = pullDownListener;
    }

    void setPullUpListener(PullUpListener pullUpListener) {
        this.pullUpListener = pullUpListener;
    }

    void notifyAnimationStart() {
        if (isPullDownRefreshing) {
            if (null != onPullDownRefreshListener) {
                onPullDownRefreshListener.onPullDownRefresh();
            }
        }

        if (isPullUpLoading) {
            if (null != onPullUpLoadingListener) {
                onPullUpLoadingListener.onPullUpLoading();
            }
        }
    }

    void notifyAnimationFinish() {
        isPullDownRefreshing = false;
        isPullUpLoading = false;
    }

    public void setOnPullDownRefreshListener(OnPullDownRefreshListener onPullDownRefreshListener) {
        this.onPullDownRefreshListener = onPullDownRefreshListener;
    }

    public void setOnPullUpLoadingListener(OnPullUpLoadingListener onPullUpLoadingListener) {
        this.onPullUpLoadingListener = onPullUpLoadingListener;
    }

    public void finishRefresh() {
        if (isPullDownRefreshing) {
            pullDownListener.onPullDownRefreshFinish();
        }
    }

    public void finishLoading() {
        if (isPullUpLoading) {
            pullUpListener.onPullUpLoadingFinish();
        }
    }

    public boolean isPullDownRefreshing() {
        return isPullDownRefreshing;
    }

    public boolean isPullUpLoading() {
        return isPullUpLoading;
    }

    public void setRefreshingView(View refreshingView) {
        this.refreshingView = refreshingView;
    }

    public void setLoadingView(View loadingView) {
        this.loadingView = loadingView;
    }

    public void simulatePullDownAnimation() {
        if (refreshingView != null) {
            if (refreshingView instanceof AnimationSimulatable) {
                isPullDownRefreshing = true;
                ((AnimationSimulatable) refreshingView).simulateAnimation();
            }
        }
    }

    public void simulatePullUpAnimation() {
        if (loadingView != null) {
            if (loadingView instanceof AnimationSimulatable) {
                isPullUpLoading = true;
                ((AnimationSimulatable) loadingView).simulateAnimation();
            }
        }
    }
}
