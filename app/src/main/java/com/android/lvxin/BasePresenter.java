package com.android.lvxin;

/**
 * @ClassName: BasePresenter
 * @Description: Presenter的基类
 * @Author: lvxin
 * @Date: 6/12/16 15:28
 */
public interface BasePresenter {
    /**
     * presenter开始获取数据并调用view中方法改变界面显示,其调用时机是在Fragment类的onResume方法中
     */
    void start();
}
