package com.aserbao.androidcustomcamera;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.aserbao.androidcustomcamera.base.MyApplication;
import com.aserbao.androidcustomcamera.base.activity.RVBaseActivity;
import com.aserbao.androidcustomcamera.base.beans.ClassBean;
import com.aserbao.androidcustomcamera.douyin.homepage.viewpager.OnViewPagerListener;
import com.aserbao.androidcustomcamera.douyin.homepage.viewpager.ViewPagerLayoutManager;
import com.aserbao.androidcustomcamera.whole.WholeActivity;
import com.aserbao.androidcustomcamera.whole.record.RecorderActivity;
import com.bumptech.glide.Glide;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    private ImageView takephoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_view_pager_layout_manager);

        initView();

        initListener();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recycler);

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        takephoto = findViewById(R.id.take_video_or_camera);
    }

    private void initListener() {
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                playVideo(0);
            }

            @Override
            public void onLayoutComplete() {
                playVideo(0);
            }

        });

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RecorderActivity.class);
                startActivity(intent);
                //   overridePendingTransition(R.anim.bottom_enter, R.anim.bottom_enter);
            }
        });
    }

    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                Log.e(TAG, "onInfo");
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared");
                //   videoView.start();


            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private int[] imgs = {R.mipmap.default_img};
        private int[] videos = {R.raw.video_1, R.raw.video_2};
        private List list = new ArrayList();

        String VIDEO_URL = "https://t002.obs.cn-east-2.myhuaweicloud.com/";

        public MyAdapter() {

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
           // holder.img_thumb.setImageResource(imgs[0]);



            // holder.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videos[position % 2]));
            // holder.videoView.setMediaController(new MediaController(MainActivity.this));
            // HttpProxyCacheServer proxy = MyApplication.getProxy();

            String NEWURL = VIDEO_URL + ((int) (Math.random() * 35)) + ".mp4";
            // String proxyUrl = proxy.getProxyUrl(NEWURL);
            // Glide.with((holder.img_thumb.getContext())).load(Uri.parse(NEWURL)).into(holder.img_thumb);
            holder.videoView.setVideoPath(NEWURL);


          /*  holder.commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    takephoto.setVisibility(View.INVISIBLE);
                    final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(holder.ll_content_bottom_sheet);
                    //   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    //       bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetBehavior.setPeekHeight(1600);
                    bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {

                            switch (newState) {
                                case BottomSheetBehavior.STATE_COLLAPSED://默认的折叠状态
                                    Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");

                                    break;
                                case BottomSheetBehavior.STATE_DRAGGING://过渡状态此时用户正在向上或者向下拖动bottom sheet
                                    Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                                    //   bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                    break;
                                case BottomSheetBehavior.STATE_EXPANDED: //处于完全展开的状态
                                    Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                                    break;
                                case BottomSheetBehavior.STATE_HIDDEN://下滑动完全隐藏 bottom sheet
                                    Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                                    break;
                                case BottomSheetBehavior.STATE_SETTLING: // 视图从脱离手指自由滑动到最终停下的这一小段时间
                                    Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                                    break;
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            if (slideOffset > 100f) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            } else if (slideOffset < -100f) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                            }
                        }
                    });
                    //     String[] data ={"aa","bb","cc","dd","aa","bb","cc","dd","aa","bb"};
                    //      holder.listView.setAdapter(new ArrayAdapter<String>(MainActivity.this,R.layout.commits_list_contact,data));

                }
            });*/

        }


        @Override
        public int getItemCount() {
            return 400;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            TextView commit;
            LinearLayout ll_content_bottom_sheet;

            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                commit = itemView.findViewById(R.id.commits);
                ll_content_bottom_sheet = itemView.findViewById(R.id.ll_content_bottom_sheet);
            }
        }


    }
}