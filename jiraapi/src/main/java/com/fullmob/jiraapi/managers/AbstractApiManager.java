package com.fullmob.jiraapi.managers;

class AbstractApiManager<T> {
    protected T api;

    AbstractApiManager(T api) {
        this.api = api;
    }
}
