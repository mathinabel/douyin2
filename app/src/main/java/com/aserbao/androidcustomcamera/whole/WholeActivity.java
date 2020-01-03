package com.aserbao.androidcustomcamera.whole;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aserbao.androidcustomcamera.R;
import com.aserbao.androidcustomcamera.base.activity.RVBaseActivity;
import com.aserbao.androidcustomcamera.base.beans.ClassBean;
import com.aserbao.androidcustomcamera.whole.record.RecorderActivity;

import java.util.List;

public class WholeActivity extends RVBaseActivity {

    @Override
    public List<ClassBean> initData() {
        mClassBeans.add(new ClassBean("视频录制这边走", RecorderActivity.class));
        return mClassBeans;
    }
}
