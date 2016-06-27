package com.android.lvxin;

/**
 * @ClassName: BaseView
 * @Description: 所有View的基类
 * @Author: lvxin
 * @Date: 6/12/16 15:26
 */
public interface BaseView<T> {
    void setPresenter(T presenter);
}
