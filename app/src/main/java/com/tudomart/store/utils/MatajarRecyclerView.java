package com.tudomart.store.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Implementation of {@link RecyclerView} that will dismiss keyboard when scrolling.
 *
 * @author milan
 */
public class MatajarRecyclerView extends RecyclerView {
    private OnScrollListener onKeyboardDismissingScrollListener;
    private InputMethodManager inputMethodManager;

    public MatajarRecyclerView(Context context) {
        this(context, null);
        init();
    }

    public MatajarRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
        init();
    }

    public MatajarRecyclerView(final Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setOnKeyboardDismissingListener();
    }

    private void init() {
        setItemAnimator(null); //removes item refresh animation
    }

    /**
     * Creates {@link OnScrollListener} that will dismiss keyboard when scrolling if the keyboard
     * has not been dismissed internally before
     */
    private void setOnKeyboardDismissingListener() {
        onKeyboardDismissingScrollListener = new OnScrollListener() {
            boolean isKeyboardDismissedByScroll;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                switch (state) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        if (!isKeyboardDismissedByScroll) {
                            hideKeyboard();
                            isKeyboardDismissedByScroll = !isKeyboardDismissedByScroll;
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        isKeyboardDismissedByScroll = false;
                        break;
                }
            }
        };
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addOnScrollListener(onKeyboardDismissingScrollListener);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeOnScrollListener(onKeyboardDismissingScrollListener);
    }

    /**
     * Hides the keyboard
     */
    public void hideKeyboard() {
        getInputMethodManager().hideSoftInputFromWindow(getWindowToken(), 0);
        clearFocus();
    }

    /**
     * Returns an {@link InputMethodManager}
     *
     * @return input method manager
     */
    public InputMethodManager getInputMethodManager() {
        if (null == inputMethodManager) {
            inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        return inputMethodManager;
    }
}
