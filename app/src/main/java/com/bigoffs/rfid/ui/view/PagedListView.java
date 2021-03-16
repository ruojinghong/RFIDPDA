package com.bigoffs.rfid.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import com.bigoffs.rfid.network.ICommonResult;
import com.bigoffs.rfid.network.PagedResult;

import okhttp3.Call;

/**
 * Created by okbuy on 17-2-22.
 * 集成分页加载功能的ListView
 * 用法：
 * 1.需要分页加载的页面要实现LoadMoreDelegate接口
 * 2.页面获取到ListView后，调用ListView的setLoadMoreDelegate()方法设置代理
 * 3.页面的loadMore()方法，要调用ListView的pagePlus()方法，使页码+1
 */

public class PagedListView extends ListView {

    // 几个重要的参数
    // 条目个数
    private int mCount;
    // 每页多少个条目
    private int mPageSize;
    // 当前页码
    private int mCurPageId = 1;
    // 是否正在加载
    private boolean isLoading;

    private int mFirstVisibieItem;
    private int mVisibleItemCount;
    private int mTotalItemCount;

    private LoadMoreDelegate mLoadMoreDelegate;
    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int i) {
            if (mCurPageId < mPageSize && i == SCROLL_STATE_IDLE && isLoading == false && mFirstVisibieItem + mVisibleItemCount >= mTotalItemCount) {
                if (mLoadMoreDelegate != null) {
                    isLoading = true;
                    mLoadMoreDelegate.showLoadMoreProgress();
                    mLoadMoreDelegate.loadMore();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mFirstVisibieItem = firstVisibleItem;
            mVisibleItemCount = visibleItemCount;
            mTotalItemCount = totalItemCount;
        }
    };

    ;

    public PagedListView(Context context) {
        super(context);
    }

    public PagedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void pagePlus() {
        mCurPageId++;
    }

    public void pageMinus() {
        mCurPageId--;
    }

    public int getCurPageId() {
        return mCurPageId;
    }

    public void resetPageId() {
        mCurPageId = 1;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public LoadMoreDelegate getLoadMoreDelegate() {
        return mLoadMoreDelegate;
    }

    public void setLoadMoreDelegate(LoadMoreDelegate delegate) {
        mLoadMoreDelegate = delegate;
        setOnScrollListener(mScrollListener);
    }

    // 加载更多的代理
    public static interface LoadMoreDelegate {
        void loadMore();

        void showLoadMoreProgress();

        void hideLoadMoreProgress();
    }

    public static class LoadMoreCallback implements ICommonResult {

        private PagedListView mListView;

        public LoadMoreCallback(PagedListView listView) {
            mListView = listView;
        }

        @Override
        public void onSuccess(String result) {
            PagedResult pagedResult = new PagedResult(result);
            // 保存条目数和页数
            mListView.setCount(pagedResult.getCount());
            mListView.setPageSize(pagedResult.getPageSize());
            if (mListView.getLoadMoreDelegate() != null) {
                mListView.getLoadMoreDelegate().hideLoadMoreProgress();
            }
            mListView.setLoading(false);
        }

        @Override
        public void onFail(Call call, Exception e) {
            // 失败了当前页码-1
            mListView.pageMinus();
            if (mListView.getLoadMoreDelegate() != null) {
                mListView.getLoadMoreDelegate().hideLoadMoreProgress();
            }
            mListView.setLoading(false);
        }

        @Override
        public void onInterrupt(int code, String message) {
            // 失败了当前页码-1
            mListView.pageMinus();
            if (mListView.getLoadMoreDelegate() != null) {
                mListView.getLoadMoreDelegate().hideLoadMoreProgress();
            }
            mListView.setLoading(false);
        }
    }
}
