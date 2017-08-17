package com.emoge.app.emoge.ui.view;

import android.content.Context;
import android.util.AttributeSet;

import com.lid.lib.LabelImageView;

/**
 * Created by jh on 17. 8. 17.
 * Label 있는 정사각형 ImageView
 */

public class LabelSquareImageView extends LabelImageView {
    public LabelSquareImageView(Context context) {
        super(context);
    }

    public LabelSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, widthSize);
        } else if(heightMode == MeasureSpec.EXACTLY && widthMode != MeasureSpec.EXACTLY) {
            setMeasuredDimension(heightSize, heightSize);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
