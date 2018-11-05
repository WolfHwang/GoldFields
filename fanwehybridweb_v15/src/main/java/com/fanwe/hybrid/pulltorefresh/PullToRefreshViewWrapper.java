package com.fanwe.hybrid.pulltorefresh;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.lib.pulltorefresh.FPullToRefreshView;

/**
 * 对下拉刷新进行包裹，避免更换框架的时候要修改大量代码
 */
public class PullToRefreshViewWrapper implements IPullToRefreshViewWrapper<FPullToRefreshView>
{
    private FPullToRefreshView mPullToRefreshView;
    private OnRefreshCallbackWrapper mOnRefreshCallbackWrapper;

    @Override
    public void setPullToRefreshView(FPullToRefreshView pullToRefreshView)
    {
        if (mPullToRefreshView != pullToRefreshView)
        {
            mPullToRefreshView = pullToRefreshView;

            if (pullToRefreshView != null)
            {
                pullToRefreshView.setOnRefreshCallback(mInternalOnRefreshCallback);
                pullToRefreshView.setDebug(ApkConstant.DEBUG);
            }
        }
    }

    private FPullToRefreshView.OnRefreshCallback mInternalOnRefreshCallback = new FPullToRefreshView.OnRefreshCallback()
    {
        @Override
        public void onRefreshingFromHeader(FPullToRefreshView view)
        {
            if (mOnRefreshCallbackWrapper != null)
            {
                mOnRefreshCallbackWrapper.onRefreshingFromHeader();
            }
        }


        @Override
        public void onRefreshingFromFooter(FPullToRefreshView view)
        {
            if (mOnRefreshCallbackWrapper != null)
            {
                mOnRefreshCallbackWrapper.onRefreshingFromFooter();
            }
        }
    };

    @Override
    public FPullToRefreshView getPullToRefreshView()
    {
        return mPullToRefreshView;
    }

    @Override
    public void setOnRefreshCallbackWrapper(OnRefreshCallbackWrapper onRefreshCallbackWrapper)
    {
        mOnRefreshCallbackWrapper = onRefreshCallbackWrapper;
    }

    @Override
    public void setModePullFromHeader()
    {
        getPullToRefreshView().setMode(FPullToRefreshView.Mode.PULL_FROM_HEADER);
    }

    @Override
    public void setModePullFromFooter()
    {
        getPullToRefreshView().setMode(FPullToRefreshView.Mode.PULL_FROM_FOOTER);
    }

    @Override
    public void setModeDisable()
    {
        getPullToRefreshView().setMode(FPullToRefreshView.Mode.DISABLE);
    }

    @Override
    public void startRefreshingFromHeader()
    {
        getPullToRefreshView().startRefreshingFromHeader();
    }

    @Override
    public boolean isRefreshing()
    {
        return getPullToRefreshView().isRefreshing();
    }

    @Override
    public void stopRefreshing()
    {
        getPullToRefreshView().stopRefreshing();
    }
}
