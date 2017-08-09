package com.emoge.app.emoge.ui.history;

import com.emoge.app.emoge.model.PaletteMessage;

/**
 * Created by jh on 17. 8. 9.
 */

public interface HistoryAccessible {
    void addHistory(PaletteMessage correction);
    void rollbackPosition(int position);
    void rollbackOrigin();
    void clearHistory();
}
