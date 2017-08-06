package com.emoge.app.emoge.utils;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

/**
 * Created by jh on 17. 8. 5.
 * DiscreteSeekBar 에 표시되는 수 변경
 */

public class SeekBarNumberTransformers {

    public static DiscreteSeekBar.NumericTransformer Subtract(final int transNum) {
        return new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value-transNum;
            }
        };
    }

    public static DiscreteSeekBar.NumericTransformer Multiply(final int transNum) {
        return new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                return value*transNum;
            }
        };
    }
}
