package com.emoge.app.emoge.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.emoge.app.emoge.R;

/**
 * Created by jh on 17. 8. 4.
 * 서버 전송할 때의 Title 및 이미지 저장시 FileName 입력받는 용도의 Dialog
 */

public class EditorDialog extends CustomDialog {
    private static final String LOG_TAG = EditorDialog.class.getSimpleName();

    private String selectedCategory;
    private int selectedQuality;
    private AppCompatEditText editText;

    public EditorDialog(Activity activity) {
        super(activity, R.layout.dialog_with_editor);
        initCategorySpinner();
        initQualitySpinner();
        initEditor(activity);
    }

    private void initCategorySpinner() {
        AppCompatSpinner spinner = (AppCompatSpinner)findViewById(R.id.dialog_category);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = parent.getItemAtPosition(0).toString();
            }
        });
    }

    private void initQualitySpinner() {
        AppCompatSpinner spinner = (AppCompatSpinner)findViewById(R.id.dialog_quality);
        spinner.setSelection(1); // 중화질
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuality = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedQuality = 1;
            }
        });
    }

    private void initEditor(final Activity activity) {
        editText = (AppCompatEditText)findViewById(R.id.dialog_editor);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });
    }

    public String getCategory() {
        return selectedCategory;
    }

    public int getQuality() {
        return selectedQuality;
    }

    public String getContent() {
        return editText.getText().toString();
    }

    public void setError(String error) {
        editText.setError(error);
    }

    public EditorDialog setSaveButtonListener(View.OnClickListener listener) {
        Button saveButton = (Button)findViewById(R.id.dialog_bt_save);
        saveButton.setOnClickListener(listener);
        return this;
    }
}
