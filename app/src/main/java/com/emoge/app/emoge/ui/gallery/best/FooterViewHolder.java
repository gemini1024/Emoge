package com.emoge.app.emoge.ui.gallery.best;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.emoge.app.emoge.ui.server.ServerActivity;
import com.emoge.app.emoge.utils.NetworkStatus;

/**
 * Created by jh on 17. 8. 17.
 * ServerImageViewHolder Footer. 클릭시 서버로 이동
 */

class FooterViewHolder extends ServerImageViewHolder  {

    private Activity calledActivity;

    FooterViewHolder(Activity activity, View itemView) {
        super(itemView);
        this.calledActivity = activity;
    }

    @Override
    public void onClick(View v) {
        NetworkStatus.executeWithCheckingNetwork(calledActivity, new NetworkStatus.RequireIntentTask() {
            @Override
            public void Task() {
                calledActivity.overridePendingTransition(0, android.R.anim.fade_in);
                calledActivity.startActivity(new Intent(calledActivity, ServerActivity.class));
            }
        });
    }
}
