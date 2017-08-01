package com.emoge.app.emoge.ui.correction;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emoge.app.emoge.model.Frame;
import com.emoge.app.emoge.ui.frame.FrameAddImplAdapter;
import com.emoge.app.emoge.ui.frame.FrameAdder;

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


    public CorrectImplAdapter(@NonNull RecyclerView recyclerView,
                              @NonNull List<Frame> frames,
                              @NonNull FrameAdder frameAdder,
                              @NonNull Correcter correcter) {
        super(recyclerView, frames, frameAdder);
        this.correcter = correcter;
        this.stageFrames = new ArrayList<>();
        this.tmpFrames = new ArrayList<>();
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        if(!stageFrames.isEmpty()) {
            stageFrames.add(toPosition, stageFrames.remove(fromPosition));
        }
        return super.move(fromPosition, toPosition);
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


    // FPS(재생 속도) 변경
    @Override
    public void setFps(int value) {
        correcter.setCurrentFps(value);
    }

    public int getFps() {
        return correcter.getCurrentFps();
    }


    // Frame 전체 밝기 변경
    @Override
    public void setBrightness(int value) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setBrightness(getFrames(), value);
        notifyDataSetChanged();
    }

    // Frame 전체 대비 변경
    @Override
    public void setContrast(int value) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setContrast(getFrames(), value);
        notifyDataSetChanged();
    }

    // Frame 전체 감마 변경
    @Override
    public void setGamma(int value) {
        tmpFrames = stageFrames;
        stageFrames = correcter.setGamma(getFrames(), value);
        notifyDataSetChanged();
    }

    // 변경 사항 적용. stage frame -> original frame
    @Override
    public void apply() {
        super.clear();
        super.setFrames(stageFrames);
        stageFrames = new ArrayList<>();
        notifyDataSetChanged();
    }

    // 변경 사항 제거
    @Override
    public void reset() {
        clearStage();
        stageFrames = new ArrayList<>();
        notifyDataSetChanged();
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
}
