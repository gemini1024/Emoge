package com.emoge.app.emoge.ui.palette;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by jh on 17. 8. 5.
 */

public class SeekBarNumberTransformers {

    static DiscreteSeekBar.NumericTransformer Subtract(final int transNum) {
        return new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value-transNum;
            }
        };
    }

    static DiscreteSeekBar.NumericTransformer Multiply(final int transNum) {
        return new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value*transNum;
            }
        };
    }
}
