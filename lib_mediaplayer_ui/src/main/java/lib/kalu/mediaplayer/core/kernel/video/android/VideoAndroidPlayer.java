package lib.kalu.mediaplayer.core.kernel.video.android;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.core.kernel.video.KernelApiEvent;
import lib.kalu.mediaplayer.core.kernel.video.base.BasePlayer;
import lib.kalu.mediaplayer.core.player.PlayerApi;
import lib.kalu.mediaplayer.util.MPLogUtil;

@Keep
public final class VideoAndroidPlayer extends BasePlayer {

    private long mSeek = 0L; // 快进
    private long mMax = 0L; // 试播时常
    private boolean mLoop = false; // 循环播放
    private boolean mLive = false;
    private boolean mMute = false;

    private MediaPlayer mMediaPlayer = null;
    private boolean mPlayWhenReady = true;

    public VideoAndroidPlayer(@NonNull PlayerApi musicApi, @NonNull KernelApiEvent eventApi) {
        super(musicApi, eventApi);
    }

    @NonNull
    @Override
    public VideoAndroidPlayer getPlayer() {
        return this;
    }

    @Override
    public void releaseDecoder(boolean isFromUser) {
        MPLogUtil.log("VideoAndroidPlayer22 => releaseDecoder => mMediaPlayer = " + mMediaPlayer + ", isFromUser = " + isFromUser);
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayerCollects error: null");
            if (isFromUser) {
                setEvent(null);
            }
            stopExternalMusic(true);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnInfoListener(null);
            mMediaPlayer.setOnBufferingUpdateListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnSeekCompleteListener(null);
            mMediaPlayer.setOnVideoSizeChangedListener(null);
            mMediaPlayer.setSurface(null);
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => releaseDecoder => " + e.getMessage());
        }
    }

    @Override
    public void createDecoder(@NonNull Context context, @NonNull boolean logger, @NonNull int seekParameters) {
        MPLogUtil.log("VideoAndroidPlayer22 => createDecoder => mMediaPlayer = " + mMediaPlayer);
        try {
            releaseDecoder(false);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setLooping(false);
            setVolume(1F, 1F);
            initListener();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => createDecoder => " + e.getMessage());
        }
    }

    @Override
    public void startDecoder(@NonNull Context context, @NonNull String url) {
        MPLogUtil.log("VideoAndroidPlayer22 => startDecoder => mMediaPlayer = " + mMediaPlayer + ", url = " + url);
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            if (url == null || url.length() == 0)
                throw new IllegalArgumentException("url error: " + url);
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_LOADING_START);
            mMediaPlayer.setDataSource(context, Uri.parse(url), null);
            mMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            MPLogUtil.log("VideoAndroidPlayer => startDecoder => " + e.getMessage());
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_LOADING_STOP);
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_ERROR_URL);
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => startDecoder => " + e.getMessage());
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_LOADING_STOP);
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_ERROR_PARSE);
        }
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
    }

    /**
     * MediaPlayer视频播放器监听listener
     */
    private void initListener() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            mMediaPlayer.setOnErrorListener(onErrorListener);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setOnInfoListener(onInfoListener);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => initListener => " + e.getMessage());
        }
    }

//    /**
//     * 用于播放raw和asset里面的视频文件
//     */
//    @Override
//    public void setDataSource(AssetFileDescriptor fd) {
//        try {
//            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
//        } catch (Exception e) {
//            MPLogUtil.log("VideoAndroidPlayer => " + e.getMessage());
//        }
//    }

    /**
     * 播放
     */
    @Override
    public void start() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            mMediaPlayer.start();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => start => " + e.getMessage());
        }
    }

    /**
     * 暂停
     */
    @Override
    public void pause() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            mMediaPlayer.pause();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => pause => " + e.getMessage());
        }
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            mMediaPlayer.pause();
            mMediaPlayer.stop();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => stop => " + e.getMessage());
        }
    }

    /**
     * 是否正在播放
     */
    @Override
    public boolean isPlaying() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            return mMediaPlayer.isPlaying();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => isPlaying => " + e.getMessage());
            return false;
        }
    }

    /**
     * 调整进度
     */
    @Override
    public void seekTo(long seek, @NonNull boolean seekHelp) {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            if (seek < 0)
                throw new Exception("seek error: " + seek);
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_BUFFERING_START);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                mMediaPlayer.seekTo(seek, MediaPlayer.SEEK_CLOSEST);
                mMediaPlayer.seekTo((int) seek);
            } else {
                mMediaPlayer.seekTo((int) seek);
            }
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => seekTo => " + e.getMessage());
        }
    }

    /**
     * 获取当前播放的位置
     */
    @Override
    public long getPosition() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            return mMediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => getPosition => " + e.getMessage());
            return 0;
        }
    }

    /**
     * 获取视频总时长
     */
    @Override
    public long getDuration() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            return mMediaPlayer.getDuration();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => getDuration => " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void setSurface(@NonNull Surface surface, int w, int h) {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            if (null == surface)
                throw new Exception("surface error: null");
            mMediaPlayer.setSurface(surface);
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => setSurface => " + e.getMessage());
        }
    }

    @Override
    public void setPlayWhenReady(boolean playWhenReady) {
        this.mPlayWhenReady = playWhenReady;
    }

    @Override
    public boolean isPlayWhenReady() {
        return mPlayWhenReady;
    }

    @Override
    public float getSpeed() {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                throw new Exception("only support above Android M");
            return mMediaPlayer.getPlaybackParams().getSpeed();
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => getSpeed => " + e.getMessage());
            return 1f;
        }
    }

    @Override
    public void setSpeed(float speed) {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                throw new Exception("only support above Android M");
            mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => setSpeed => " + e.getMessage());
        }
    }

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            try {
                if (what == -38)
                    throw new Exception("what warning: " + what);
                MPLogUtil.log("VideoAndroidPlayer => onError => what = " + what);
                onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_LOADING_STOP);
                onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_ERROR_PARSE);
            } catch (Exception e) {
                MPLogUtil.log("VideoAndroidPlayer => onError => " + e.getMessage());
            }
            return true;
        }
    };

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            MPLogUtil.log("VideoAndroidPlayer => onCompletion =>");
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_VIDEO_END);
        }
    };

    private MediaPlayer.OnInfoListener onInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            MPLogUtil.log("VideoAndroidPlayer => onInfo => what = " + what);
            // 缓冲开始
            if (what == PlayerType.EventType.EVENT_BUFFERING_START) {
                long position = getPosition();
                long seek = getSeek();
                long duration = getDuration();
                if (duration > 0 && position > seek) {
                    onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_BUFFERING_START);
                }
            }
            // 缓冲结束
            else if (what == PlayerType.EventType.EVENT_BUFFERING_STOP) {
                onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_BUFFERING_STOP);
                onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_LOADING_STOP);
            }
            // 开始播放
            else if (what == PlayerType.EventType.EVENT_VIDEO_START) {
                onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_LOADING_STOP);
                onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_VIDEO_START);
                if (!mPlayWhenReady) {
                    pause();
                }
            }
            return true;
        }
    };

    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
        @Override
        public void onSeekComplete(MediaPlayer mediaPlayer) {
            onEvent(PlayerType.KernelType.ANDROID, PlayerType.EventType.EVENT_BUFFERING_STOP);
            try {
                MPLogUtil.log("VideoAndroidPlayer => onSeekComplete =>");
                start();
            } catch (Exception e) {
                MPLogUtil.log("VideoAndroidPlayer => onSeekComplete => " + e.getMessage());
            }
        }
    };

    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
//            try {
//                if (null == mp)
//                    throw new Exception("mp error: null");
//                int videoWidth = mp.getVideoWidth();
//                int videoHeight = mp.getVideoHeight();
//                MPLogUtil.log("VideoAndroidPlayer => onPrepared => videoWidth = " + videoWidth+", videoHeight = "+videoHeight);
//                if (videoWidth < 0 || videoHeight < 0)
//                    throw new Exception("videoWidth error: " + videoWidth + ", videoHeight error: " + videoHeight);
//                onChanged(PlayerType.KernelType.ANDROID, videoWidth, videoHeight, PlayerType.RotationType.Rotation_0);
//            } catch (Exception e) {
//                MPLogUtil.log("VideoAndroidPlayer => onPrepared => " + e.getMessage());
//            }
            try {
                long seek = getSeek();
                if (seek <= 0)
                    throw new Exception("seek warning: " + seek);
                seekTo(seek, false);
            } catch (Exception e) {
                MPLogUtil.log("VideoAndroidPlayer => onPrepared => " + e.getMessage());
                start();
            }
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            try {
                if (null == mp)
                    throw new Exception("mp error: null");
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();
                MPLogUtil.log("VideoAndroidPlayer => onVideoSizeChanged => videoWidth = " + videoWidth + ", videoHeight = " + videoHeight);
                if (videoWidth < 0 || videoHeight < 0)
                    throw new Exception("videoWidth error: " + videoWidth + ", videoHeight error: " + videoHeight);
                onMeasure(PlayerType.KernelType.ANDROID, videoWidth, videoHeight, PlayerType.RotationType.Rotation_0);
            } catch (Exception e) {
                MPLogUtil.log("VideoAndroidPlayer => onVideoSizeChanged => " + e.getMessage());
            }
        }
    };

    /****************/

    @Override
    public void setVolume(float v1, float v2) {
        try {
            if (null == mMediaPlayer)
                throw new Exception("mMediaPlayer error: null");
            float value;
            if (isMute()) {
                value = 0F;
            } else {
                value = Math.max(v1, v2);
            }
            if (value > 1f) {
                value = 1f;
            }
            mMediaPlayer.setVolume(value, value);
        } catch (Exception e) {
            MPLogUtil.log("VideoAndroidPlayer => setVolume => " + e.getMessage());
        }
    }

    @Override
    public boolean isMute() {
        return mMute;
    }

    @Override
    public void setMute(boolean v) {
        mMute = v;
        setVolume(v ? 0f : 1f, v ? 0f : 1f);
    }

    @Override
    public long getSeek() {
        return mSeek;
    }

    @Override
    public void setSeek(long seek) {
        if (seek < 0)
            return;
        mSeek = seek;
    }

    @Override
    public long getMax() {
        return mMax;
    }

    @Override
    public void setMax(long max) {
        if (max < 0)
            return;
        mMax = max;
    }

    @Override
    public boolean isLive() {
        return mLive;
    }

    @Override
    public void setLive(@NonNull boolean live) {
        this.mLive = live;
    }

    @Override
    public void setLooping(boolean loop) {
        this.mLoop = loop;
    }

    @Override
    public boolean isLooping() {
        return mLoop;
    }
}
