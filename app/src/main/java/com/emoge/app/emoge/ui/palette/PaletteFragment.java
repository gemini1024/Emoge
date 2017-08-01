package com.emoge.app.emoge.ui.palette;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emoge.app.emoge.R;
import com.emoge.app.emoge.model.PaletteMessage;
import com.emoge.app.emoge.ui.correction.Correcter;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaletteFragment extends Fragment implements DiscreteSeekBar.OnProgressChangeListener {
    private static final String LOG_TAG = PaletteFragment.class.getSimpleName();

    public static final String ARG_PALETTE_TYPE     = "TYPE";
    public static final String ARG_PALETTE_VALUE    = "VALUE";
    private int mPaletteType;
    private int mPreValue;


    @BindView(R.id.palette_label)
    TextView mLabel;
    @BindView(R.id.palette_seek)
    DiscreteSeekBar mSeekBar;


    public PaletteFragment() {
    }

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
            mPreValue       = getArguments().getInt(ARG_PALETTE_VALUE);
        }
    }

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
                return getString(R.string.fps);
        }
    }


    private void setSeekBarByType(int type) {
        switch (type) {
            case Correcter.CORRECT_BRIGHTNESS :
                mSeekBar.setMin(0);
                mSeekBar.setMax(100);
                mSeekBar.setProgress(mPreValue);
                break;
            case Correcter.CORRECT_CONTRAST :
                mSeekBar.setMin(0);
                mSeekBar.setMax(200);
                mSeekBar.setProgress(mPreValue);
                break;
            case Correcter.CORRECT_GAMMA :
                mSeekBar.setMin(0);
                mSeekBar.setMax(255);
                mSeekBar.setProgress(mPreValue);
                break;
            default :
                mSeekBar.setMin(100);
                mSeekBar.setMax(3000);
                mSeekBar.setProgress(mPreValue);
                break;
        }
    }



    @Override
    public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
        EventBus.getDefault().post(new PaletteMessage(mPaletteType, value));
    }

    @Override
    public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

    }
}
