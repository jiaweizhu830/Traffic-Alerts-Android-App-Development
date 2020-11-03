package com.laioffer.matrix;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReportDialog extends Dialog {

    private int cx;
    private int cy;
    private RecyclerView mRecyclerView;
    private ReportRecyclerViewAdapter mRecyclerViewAdapter;
    private ViewSwitcher mViewSwitcher;
    private String mEventype;
    private String mPrefillText;

    // event specs
    private Button mBackButton;
    private Button mSendButton;
    private ImageView mImageCamera;
    private EditText mCommentEditText;
    private ImageView mEventTypeImg;
    private TextView mTypeTextView;

    private DialogCallBack mDialogCallBack;

    interface DialogCallBack {
        void onSubmit(String editString, String event_type);
        void startCamera();
    }

    public ReportDialog(@NonNull Context context) {
        super(context, R.style.MyAlertDialogStyle);
    }

    public ReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static ReportDialog newInstance(Context context, int cx, int cy) {
        ReportDialog dialog = new ReportDialog(context, R.style.MyAlertDialogStyle);
        dialog.cx = cx;
        dialog.cy = cy;

        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View dialogView = View.inflate(getContext(), R.layout.dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // set up animation
//        setOnShowListener(new OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                animateDialog(dialogView, true);
//            }
//        });
//
//        setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                if (i == KeyEvent.KEYCODE_BACK) {
//                    animateDialog(dialogView, false);
//                    return true;
//                }
//                return false;
//            }
//        });

        setupRecyclerView(dialogView);
        mViewSwitcher = (ViewSwitcher) dialogView.findViewById(R.id.viewSwitcher);

        Animation slide_in_left = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(getContext(),
                android.R.anim.slide_out_right);
        mViewSwitcher.setInAnimation(slide_in_left);
        mViewSwitcher.setOutAnimation(slide_out_right);

        setUpEventSpecs(dialogView);
    }

    private void setupRecyclerView(View dialogView) {
        mRecyclerView = dialogView.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mRecyclerViewAdapter = new ReportRecyclerViewAdapter(getContext(), Config.listItems);
        mRecyclerViewAdapter.setClickListener(new ReportRecyclerViewAdapter.onClickListener() {
            @Override
            public void setItem(String item) {
                // to switch item
                showNextViewSwitcher(item);
            }
        });

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
    }

    private void showNextViewSwitcher(String item) {
        mEventype = item;
        if (mViewSwitcher != null) {
            mViewSwitcher.showNext();
            mTypeTextView.setText(mEventype);
            mEventTypeImg.setImageDrawable(ContextCompat.getDrawable(getContext(), Config.trafficMap.get(mEventype)));
        }
    }

    public void updateImage(Bitmap bitmap) {
        mImageCamera.setImageBitmap(bitmap);
    }

    private void setUpEventSpecs(final View dialogView) {
        mImageCamera = (ImageView) dialogView.findViewById(R.id.event_camera_img);
        mBackButton = (Button) dialogView.findViewById(R.id.event_back_button);
        mSendButton = (Button) dialogView.findViewById(R.id.event_send_button);
        mCommentEditText = (EditText) dialogView.findViewById(R.id.event_comment);
        mEventTypeImg = (ImageView) dialogView.findViewById(R.id.event_type_img);
        mTypeTextView = (TextView) dialogView.findViewById(R.id.event_type);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogCallBack.onSubmit(mCommentEditText.getText().toString(), mEventype);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewSwitcher.showPrevious();
            }
        });

        // link to camera app
        mImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ReportDialog", "link to camera app");
                mDialogCallBack.startCamera();
            }
        });
    }

    public void setDialogCallBack(DialogCallBack dialogCallBack) {
        mDialogCallBack = dialogCallBack;
    }

    public void setVoiceInfo(String event_type, String prefillText) {
        mEventype = event_type;
        mPrefillText = prefillText;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mEventype != null) {
            showNextViewSwitcher(mEventype);
        }

        if (mPrefillText != null) {
            mCommentEditText.setText(mPrefillText);
//            mDialogCallBack.onSubmit();
        }
    }
}
