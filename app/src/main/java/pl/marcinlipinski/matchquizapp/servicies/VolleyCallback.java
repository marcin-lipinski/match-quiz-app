package pl.marcinlipinski.matchquizapp.servicies;

import java.util.Map;

public interface VolleyCallback<T> {
    void onSuccess(T result);
    void onFail();
}
