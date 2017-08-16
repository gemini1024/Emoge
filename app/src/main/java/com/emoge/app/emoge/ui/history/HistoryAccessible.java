package com.emoge.app.emoge.ui.history;

/**
 * Created by jh on 17. 8. 9.
 * History List 를 가진 Adapter 에 접근해서 기능 수행
 */

interface HistoryAccessible {
    void addHistory();
    boolean rollbackOneStep();
    void rollbackPosition(int position);
    void rollbackOrigin();
    void clearHistory();
}
