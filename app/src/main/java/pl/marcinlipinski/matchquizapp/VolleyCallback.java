package pl.marcinlipinski.matchquizapp;

import java.util.Map;

public interface VolleyCallback<T> {
    void onSuccess(T result);
    void onFail();
}
