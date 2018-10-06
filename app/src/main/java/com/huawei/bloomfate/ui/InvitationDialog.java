package com.huawei.bloomfate.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class InvitationDialog extends DialogFragment {

    private static final String TAG = "InvitationDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Context context = getActivity();
        if (context == null) {
            Log.e(TAG, "Activity is not exist");
            return super.onCreateDialog(savedInstanceState);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("约会确认").setMessage("你确定要接受TA的邀请的吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("再考虑一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("狠心拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();

    }
}
