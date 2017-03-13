package com.fullmob.jiraboard.ui;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;

public abstract class AbstractPresenter<T extends BaseView> {
    private WeakReference<T> view;

    private List<Disposable> disposables;
    private List<Cancellable> cancellables;

    public AbstractPresenter(T view) {
        this.view = new WeakReference<T>(view);
        disposables = new ArrayList<>();
        cancellables = new ArrayList<>();
    }

    public T getView() {
        return view.get();
    }

    public void unbind() {
        for (Disposable disposable : disposables) {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        for (Cancellable cancellable : cancellables) {
            if (cancellable != null) {
                try {
                    cancellable.cancel();
                } catch (Exception e) {

                }
            }
        }
    }

    public void destroy() {
        unbind();
    }

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    protected void addCancellable(Cancellable cancellable) {
        cancellables.add(cancellable);
    }


}
