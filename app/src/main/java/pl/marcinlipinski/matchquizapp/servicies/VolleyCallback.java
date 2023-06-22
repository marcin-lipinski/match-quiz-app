package pl.marcinlipinski.matchquizapp.servicies;

public interface VolleyCallback<T> {
    void onSuccess(T result);

    void onFail(String message);
}
