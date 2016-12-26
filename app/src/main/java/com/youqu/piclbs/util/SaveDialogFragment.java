package com.youqu.piclbs.util;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.youqu.piclbs.MainActivity;
import com.youqu.piclbs.R;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hujiang on 2016/12/26.
 */

public class SaveDialogFragment extends DialogFragment {

    @BindView(R.id.iv_save_close)
    ImageView ivSaveClose;
    @BindView(R.id.btn_save_bt)
    Button btnSaveBt;
    private final int REQUEST_IMAGE = 0x111;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_save, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.iv_save_close, R.id.btn_save_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_save_close:
                dismiss();
                break;
            case R.id.btn_save_bt:
                dismiss();
                MultiImageSelector.create()
                        .showCamera(false)
                        .single()
                        .start(getActivity(), REQUEST_IMAGE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path != null && path.size() > 0) {
                    final String tmpAvatarLocalPath = path.get(0);
                    String url = ImageFormatUtil.getImageUri(getActivity(), tmpAvatarLocalPath);
                    File file = new File(url);
                    if (!file.exists()) {
                        Toast.makeText(getActivity(), "图片不存在", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        SharedPreferencesUtil.getString(getActivity(),"url","");
                        startActivity(intent);
                    }

                }
            }
        }
    }
}
