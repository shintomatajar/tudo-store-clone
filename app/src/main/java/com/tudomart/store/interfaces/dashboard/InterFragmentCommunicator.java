package com.tudomart.store.interfaces.dashboard;

public interface InterFragmentCommunicator {
    void onClickOrderItem(String OrderId, int orderStatus, String timeStamp);
    void updateToolbar(int count);
}
