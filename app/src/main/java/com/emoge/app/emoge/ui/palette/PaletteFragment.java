package com.emoge.app.emoge.ui.palette;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.PaletteMessage;
import com.emoge.app.emoge.ui.correction.Correcter;
import com.emoge.app.emoge.utils.SeekBarNumberTransformers;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * 보정 작업을 위한 Fragment
 */
public class PaletteFragment extends Fragment implements DiscreteSeekBar.OnProgressChangeListener {
    private static final String LOG_TAG = PaletteFragment.class.getSimpleName();

    public static final String ARG_PALETTE_TYPE     = "TYPE";
    public static final String ARG_PALETTE_VALUE    = "VALUE";
    private int mPaletteType;
    private int mDefaultValue;


    @BindView(R.id.palette_label)   TextView mLabel;
    @BindView(R.id.palette_seek)    DiscreteSeekBar mSeekBar;
    @BindView(R.id.palette_button)  Button mApplyButton;


    public PaletteFragment() {
    }

    // type, previous value 유지 위함
    public static PaletteFragment newInstance(int paletteType, int preValue) {
        PaletteFragment fragment = new PaletteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PALETTE_TYPE, paletteType);
        args.putInt(ARG_PALETTE_VALUE, preValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaletteType    = getArguments().getInt(ARG_PALETTE_TYPE);
            mDefaultValue   = getArguments().getInt(ARG_PALETTE_VALUE);
        }
    }

    // type 별 설정
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_palette, container, false);
        ButterKnife.bind(this, view);
        mLabel.setText(getLabelByType(mPaletteType));
        setSeekBarByType(mPaletteType);
        mSeekBar.setOnProgressChangeListener(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().post(new PaletteMessage(Correcter.CORRECT_APPLY, 0));
    }


    @NonNull
    private String getLabelByType(int type) {
        switch (type) {
            case Correcter.CORRECT_BRIGHTNESS :
                return getString(R.string.brightness);
            case Correcter.CORRECT_CONTRAST :
                return getString(R.string.contrast);
            case Correcter.CORRECT_GAMMA :
                return getString(R.string.gamma);
            default :
                return getString(R.string.frame_delay);
        }
    }


    private void setSeekBarByType(int type) {
        switch (type) {
            case Correcter.CORRECT_BRIGHTNESS :
                // -50 ~ 50
                setSeekBarValues(null /* DefaultNumericTransformer */, -50, 50, mDefaultValue);
                break;
            case Correcter.CORRECT_CONTRAST :
                // -50 ~ 50
                setSeekBarValues(SeekBarNumberTransformers.Subtract(100), 50, 150, mDefaultValue);
                break;
            case Correcter.CORRECT_GAMMA :
                // -50 ~ 50
                setSeekBarValues(SeekBarNumberTransformers.Subtract(128), 78, 178, mDefaultValue);
                break;
            default : // MOD_FRAME_DELAY (fps 변경)
                // 10 ~ 2000
                setSeekBarValues(SeekBarNumberTransformers.Multiply(10), 1, 200, mDefaultValue/10);
                mApplyButton.setVisibility(View.VISIBLE);
                mApplyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new PaletteMessage(Correcter.CORRECT_REVERSE, 0));
                    }
                });
                break;
        }
    }

    private void setSeekBarValues(DiscreteSeekBar.NumericTransformer transformers,
                                  int min, int max, int current) {
        mSeekBar.setNumericTransformer(transformers);
        mSeekBar.setMin(min);
        mSeekBar.setMax(max);
        mSeekBar.setProgress(current);
    }

    // (보정) SeekBar 변경
    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        if(mPaletteType == Correcter.MOD_FRAME_DELAY) {
            EventBus.getDefault().post(new PaletteMessage(mPaletteType, value*10));
        } else {
            EventBus.getDefault().post(new PaletteMessage(mPaletteType, value));
        }
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }

}
