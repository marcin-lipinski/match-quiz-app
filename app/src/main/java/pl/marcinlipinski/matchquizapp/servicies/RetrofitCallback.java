package pl.marcinlipinski.matchquizapp.servicies;

public interface RetrofitCallback<T> {
    void onSuccess(T result);

    void onFail(String message);
}
