package com.fibelatti.raffler.views.extensions;

import android.content.Context;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fibelatti.raffler.utils.ViewUtils;

/**
 * Created by fibelatti on 12/11/2016.
 * <p/>
 * Inspired by the work of lsjwzh (https://github.com/lsjwzh/RecyclerViewPager)
 * <p/>
 * This class only implements the needed aspects to make the recycler view scroll
 * one item at a time. As the original library would always change the view to
 * match_parent I decided to strip the necessary code for my needs.
 * <p/>
 * Please check the original implementation in the provided link as lsjwzh
 * provides an amazing lib.
 * <p/>
 * Copyright 2015 lsjwzh
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SingleFlingRecyclerView
        extends RecyclerView {

    private Adapter<?> adapter;
    private float flingFactor = 0.15f;
    private int positionOnTouchDown = -1;
    LinearSnapHelper snapHelper = new LinearSnapHelper();

    public SingleFlingRecyclerView(Context context) {
        this(context, null);
    }

    public SingleFlingRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleFlingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setNestedScrollingEnabled(false);

        snapHelper.attachToRecyclerView(this);
        this.setOnFlingListener(snapHelper);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        super.setAdapter(this.adapter);
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        this.adapter = adapter;
        super.swapAdapter(this.adapter, removeAndRecycleExistingViews);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && getLayoutManager() != null) {
            positionOnTouchDown = getLayoutManager().canScrollHorizontally()
                    ? ViewUtils.getCenterXChildPosition(this)
                    : ViewUtils.getCenterYChildPosition(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean flinging = super.fling((int) (velocityX * flingFactor), (int) (velocityY * flingFactor));
        if (flinging) {
            if (getLayoutManager().canScrollHorizontally()) {
                adjustPositionX(velocityX);
            } else {
                adjustPositionY(velocityY);
            }
        }

        return flinging;
    }

    public void smoothScrollToNextSnap(int position) {
        smoothScrollToPosition(safeTargetPosition(position, getItemCount()));
    }

    public void scrollToNextSnap(int position) {
        scrollToPosition(safeTargetPosition(position, getItemCount()));
        adjustPositionX(1);
    }

    private int getItemCount() {
        return adapter == null ? 0 : adapter.getItemCount();
    }

    protected void adjustPositionX(int velocityX) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = ViewUtils.getCenterXChildPosition(this);
            int childWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            int flingCount = Math.max(-1, Math.min(1, getFlingCount(velocityX, childWidth)));
            int targetPosition = flingCount == 0 ? curPosition : positionOnTouchDown + flingCount;

            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, getItemCount() - 1);
            smoothScrollToNextSnap(targetPosition);
        }
    }

    protected void adjustPositionY(int velocityY) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = ViewUtils.getCenterYChildPosition(this);
            int childHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            int flingCount = Math.max(-1, Math.min(1, getFlingCount(velocityY, childHeight)));
            int targetPosition = flingCount == 0 ? curPosition : positionOnTouchDown + flingCount;

            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, getItemCount() - 1);
            smoothScrollToNextSnap(targetPosition);
        }
    }

    private int getFlingCount(int velocity, int cellSize) {
        float triggerOffset = 0.15f;
        int sign = velocity > 0 ? 1 : -1;

        return (velocity == 0) ? 0
                : (int) (sign * Math.ceil((velocity * sign * flingFactor / cellSize) - triggerOffset));
    }

    private int safeTargetPosition(int position, int count) {
        if (position < 0) return 0;
        if (position >= count) return count - 1;
        return position;
    }
}
