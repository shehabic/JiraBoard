package com.fullmob.jiraboard.ui;

import java.lang.ref.WeakReference;

public abstract class AbstractPresenter<T extends BaseView> {
    private WeakReference<T> view;

    public AbstractPresenter(T view) {
        this.view = new WeakReference<T>(view);
    }

    public T getView() {
        return view.get();
    }

    public void unbind() {

    }

    public void destroy() {
        unbind();
    }
}
