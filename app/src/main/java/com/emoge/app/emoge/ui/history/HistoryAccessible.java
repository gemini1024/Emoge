package com.emoge.app.emoge.ui.history;

/**
 * Created by jh on 17. 8. 9.
 */

public interface HistoryAccessible {
    void addHistory();
    boolean rollbackOneStep();
    void rollbackPosition(int position);
    void rollbackOrigin();
    void clearHistory();
}
