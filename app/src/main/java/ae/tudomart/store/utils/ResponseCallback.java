package ae.tudomart.store.utils;

public interface ResponseCallback<T> {
    void onSuccess(T jsonObject);

    void onError(String error);
}
