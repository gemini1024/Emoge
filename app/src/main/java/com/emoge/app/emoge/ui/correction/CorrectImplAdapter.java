package com.emoge.app.emoge.ui.correction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.model.History;
import com.emoge.app.emoge.ui.frame.FrameAddImplAdapter;
import com.emoge.app.emoge.ui.frame.FrameAdder;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jh on 17. 8. 1.
 * 보정기능을 추가한 어댑터.
 */

public class CorrectImplAdapter extends FrameAddImplAdapter implements Correctable {
    private static final String LOG_TAG = CorrectImplAdapter.class.getSimpleName();

    private Correcter correcter;        // 보정 작업 처리
    private List<Frame> stageFrames;    // 현 상태 Preview 용. Palette 제거 시 같이 제거
    private List<Frame> tmpFrames;      // View Looper 용. 변경된 이미지가 View 에서 내려온 후 제거
    private History modifiedValues;     // apply() 직전 get()하여 가져갈 수 있는 변경 내역

    public CorrectImplAdapter(@NonNull RecyclerView recyclerView,
                              @NonNull List<Frame> frames,
                              @NonNull FrameAdder frameAdder,
                              @NonNull Correcter correcter) {
        super(recyclerView, frames, frameAdder);
        this.correcter = correcter;
        this.stageFrames = new ArrayList<>();
        this.tmpFrames = new ArrayList<>();
        this.setDefualtValues();
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        if(!stageFrames.isEmpty()) {
            stageFrames.add(toPosition, stageFrames.remove(fromPosition));
        }
        return super.move(fromPosition, toPosition);
    }

    @Override
    public void removeItem(int position) {
        super.removeItem(position);
        if(!stageFrames.isEmpty()) {
            if (inRange(position)) {
                stageFrames.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, stageFrames.size());
            } else {
                Log.e(LOG_TAG, "잘못된 frame list 접근");
            }
        }
    }

    // stage 존재 시 stage 를 보여줌. 존재X -> 원본 Frame
    @NonNull
    @Override
    public Frame getItem(int position) {
        if(inRange(position)) {
            if(stageFrames.isEmpty()) {
                return getFrames().get(position);
            } else {
                return stageFrames.get(position);
            }
        } else {
            Log.e(LOG_TAG, "잘못된 frame list 접근");
            return new Frame(0, null);
        }
    }

    // 보정 작업
    public void correct(int type, int value) {
        switch (type) {
            case Correcter.MOD_FRAME_DELAY:
                setFps(value);
                break;
            case Correcter.CORRECT_BRIGHTNESS :
                setBrightness(value);
                break;
            case Correcter.CORRECT_CONTRAST :
                setContrast(value);
                break;
            case Correcter.CORRECT_GAMMA :
                setGamma(value);
                break;
            case Correcter.CORRECT_REVERSE :
                reverse();
                break;
            case Correcter.CORRECT_APPLY :
                apply();
                break;
        }
    }


    // FPS(재생 속도) 변경
    private void setDefualtValues() {
        modifiedValues = new History(Correcter.DEFAULT_BRIGHTNESS,
                Correcter.DEFAULT_CONTRAST, Correcter.DEFAULT_GAMMA);
    }

    @Override
    public void setFps(int value) {
        correcter.setCurrentDelay(value);
    }

    public int getFps() {
        return correcter.getCurrentDelay();
    }


    // Frame 전체 밝기 변경
    @Override
    public void setBrightness(int value) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setBrightness(getFrames(), value);
        modifiedValues.setModifiedBrightness(value);
        notifyDataSetChanged();
    }

    // Frame 전체 대비 변경
    @Override
    public void setContrast(int value) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setContrast(getFrames(), value);
        modifiedValues.setModifiedContrast(value);
        notifyDataSetChanged();
    }

    // Frame 전체 감마 변경
    @Override
    public void setGamma(int value) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setGamma(getFrames(), value);
        modifiedValues.setModifiedGamma(value);
        notifyDataSetChanged();
    }

    // Frame 전체 Filter 적용
    @Override
    public void setFilter(Filter filter) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setFilter(getFrames(), filter);
        modifiedValues.setAppliedFilter(filter);
        notifyDataSetChanged();
    }

    // 변경 사항 적용. stage frame -> original frame
    @Override
    public void apply() {
        if(!stageFrames.isEmpty()) {
            super.clear();
            super.setFrames(stageFrames);
            stageFrames = new ArrayList<>();
            setDefualtValues();
            notifyDataSetChanged();
            Log.i(LOG_TAG, "apply");
        }
    }

    // 변경 내역 가져 감. 실행 위치 : 보정 후 - apply() 전
    public History getModifiedValues() {
        return modifiedValues;
    }

    // stageFrames recycle
    private void clearStage() {
        for (Frame frame : stageFrames) {
            if (frame.getBitmap() != null) {
                frame.getBitmap().recycle();
            }
        }
        stageFrames.clear();
    }

    // tmpFrames recycle
    public void clearPreviousFrames() {
        for (Frame frame : tmpFrames) {
            if (frame.getBitmap() != null) {
                frame.getBitmap().recycle();
            }
        }
        tmpFrames.clear();
    }

    @Override
    public void clear() {
        super.clear();
        clearStage();
        clearPreviousFrames();
    }
}
