package lib.kalu.mediaplayer.core.player;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.HashMap;

import lib.kalu.mediaplayer.R;
import lib.kalu.mediaplayer.config.player.PlayerBuilder;
import lib.kalu.mediaplayer.config.player.PlayerManager;
import lib.kalu.mediaplayer.config.player.PlayerType;
import lib.kalu.mediaplayer.config.start.StartBuilder;
import lib.kalu.mediaplayer.core.kernel.video.KernelApi;
import lib.kalu.mediaplayer.core.kernel.video.KernelApiEvent;
import lib.kalu.mediaplayer.core.kernel.video.KernelFactoryManager;
import lib.kalu.mediaplayer.core.render.RenderApi;
import lib.kalu.mediaplayer.util.BitmapUtil;
import lib.kalu.mediaplayer.util.MPLogUtil;

interface PlayerApiKernel extends PlayerApiListener,
        PlayerApiBuriedEvent,
        PlayerApiComponent,
        PlayerApiRender,
        PlayerApiDevice,
        PlayerApiExternalMusic {

    default void setData(@NonNull String data) {
        try {
            ((View) this).setTag(R.id.module_mediaplayer_id_player_data, data);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => setData => " + e.getMessage());
        }
    }

    default String getData() {
        try {
            Object tag = ((View) this).getTag(R.id.module_mediaplayer_id_player_data);
            if (null == tag)
                throw new Exception("tag error: null");
            return (String) tag;
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => getData => " + e.getMessage());
            return null;
        }
    }

    default void start(@NonNull String url) {
        StartBuilder.Builder builder = new StartBuilder.Builder();
        StartBuilder build = builder.build();
        start(build, url);
    }

    default void start(@NonNull StartBuilder builder, @NonNull String playUrl) {
        try {
            if (null == playUrl || playUrl.length() == 0)
                throw new Exception("playUrl error: " + playUrl);
            MPLogUtil.log("PlayerApiKernel => start = > playUrl = " + playUrl);
            callPlayerEvent(PlayerType.StateType.STATE_INIT);
            // 1
            PlayerBuilder config = PlayerManager.getInstance().getConfig();
            MPLogUtil.setLogger(config);
            // 5
            createKernel(builder, config);
            // 4
            createRender(false);
            // 6
            initKernel(builder, playUrl);
            // 7
            attachRender();
            // 8
            updatePlayerData(builder, playUrl);
            // 9
            updateExternalMusicData(builder);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => start => " + e.getMessage());
        }
    }

    default void updatePlayerData(@NonNull StartBuilder data, @NonNull String playUrl) {
        try {
            if (null == data)
                throw new Exception("data error: null");
            ((View) this).setTag(R.id.module_mediaplayer_id_player_url, playUrl);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_external_enable, data.isExternalEnable());
            ((View) this).setTag(R.id.module_mediaplayer_id_player_looping, data.isLoop());
            ((View) this).setTag(R.id.module_mediaplayer_id_player_looping_release, data.isLoopRelease());
            ((View) this).setTag(R.id.module_mediaplayer_id_player_window_visibility_changed_release, data.isWindowVisibilityChangedRelease());
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => updatePlayerData => " + e.getMessage());
        }
    }

    default StartBuilder getStartBuilder() {
        try {
            String url = getUrl();
            if (null == url || url.length() <= 0) throw new Exception();
            StartBuilder.Builder builder = new StartBuilder.Builder();
            builder.setMax(getMax());
            builder.setSeek(getSeek());
            builder.setLoop(isLooping());
            builder.setLive(isLive());
            builder.setMute(isMute());
            builder.setExternalMusicUrl(getExternalMusicPath());
            builder.setExternalMusicLooping(isExternalMusicLooping());
            builder.setExternalMusicSeek(isExternalMusicSeek());
            builder.setExternalMusicPlayWhenReady(isExternalMusicPlayWhenReady());
            builder.setWindowVisibilityChangedRelease(isWindowVisibilityChangedRelease());
            return builder.build();
        } catch (Exception e) {
            return null;
        }
    }

    default long getDuration() {
        try {
            KernelApi kernel = getKernel();
            long duration = kernel.getDuration();
            if (duration < 0L) {
                duration = 0L;
            }
            return duration;
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => getDuration => " + e.getMessage());
            return 0L;
        }
    }

    default long getPosition() {
        try {
            KernelApi kernel = getKernel();
            long position = kernel.getPosition();
            if (position < 0L) {
                position = 0L;
            }
            MPLogUtil.log("PlayerApiKernel => getPosition => position = " + position);
            return position;
        } catch (Exception e) {
            return 0L;
        }
    }

    default void setVolume(@FloatRange(from = 0f, to = 1f) float left, @FloatRange(from = 0f, to = 1f) float right) {
        try {
            MPLogUtil.log("PlayerApiKernel => setVolume => left = " + left + ", right = " + right);
            KernelApi kernel = getKernel();
            kernel.setVolume(left, right);
        } catch (Exception e) {
        }
    }

    default void setMute(boolean enable) {
        try {
            MPLogUtil.log("PlayerApiKernel => setMute => enable = " + enable);
            KernelApi kernel = getKernel();
            kernel.setMute(enable);
        } catch (Exception e) {
        }
    }

    default void setLooping(boolean looping) {
        try {
            MPLogUtil.log("PlayerApiKernel => setLooping => looping = " + looping);
            KernelApi kernel = getKernel();
            kernel.setLooping(looping);
        } catch (Exception e) {
        }
    }


    default void release() {
        release(true, true);
    }

    default void release(@NonNull boolean releaseTag, boolean isFromUser) {
        try {
            checkKernel();
            removeRender();
            if (releaseTag) {
                setData(null);
                releaseTag();
            }
            releaseRender(false);
            releaseKernel(isFromUser);
            cleanPlayerChangeListener();
            callPlayerEvent(PlayerType.StateType.STATE_RELEASE);
        } catch (Exception e) {
            callPlayerEvent(PlayerType.StateType.STATE_RELEASE_EXCEPTION);
            MPLogUtil.log("PlayerApiKernel => release => " + e.getMessage());
        }
    }

    default void toggle() {
        toggle(false);
    }

    default void toggle(boolean ignore) {
        try {
            MPLogUtil.log("PlayerApiKernel => toggle => ignore = " + ignore);
            boolean playing = isPlaying();
            if (playing) {
                // 埋点
                onBuriedEventPause();
                pause(ignore);
            } else {
                // 埋点
                onBuriedEventResume();
                resume();
            }
        } catch (Exception e) {
        }
    }

    default void pause() {
        pause(false);
    }

    default void pause(boolean ignore) {
        setPlayWhenReady(false);
        pauseKernel(ignore);
    }

    default void stop() {
        try {
            stopKernel(true);
        } catch (Exception e) {
        }
    }


    default void restart() {
        try {
            String url = getUrl();
            if (null == url || url.length() <= 0)
                throw new Exception("url error: " + url);
            StartBuilder builder = getStartBuilder();
            if (null == builder)
                throw new Exception("builder error: null");
            callPlayerEvent(PlayerType.StateType.STATE_RESTAER);
            start(builder, url);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => restart => " + e.getMessage());
        }
    }

    default void resume() {
        resume(false);
    }

    default void resume(boolean ignore) {
        setPlayWhenReady(true);
        try {
            MPLogUtil.log("PlayerApiKernel => resume => ignore = " + ignore);
            checkKernel();
            // 1
            resumeExternalMusic();
            // 2
            resumeKernel(ignore);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => resume => " + e.getMessage());
        }
    }

    default boolean isLooping() {
        try {
            KernelApi kernel = getKernel();
            return kernel.isLooping();
        } catch (Exception e) {
            return (boolean) ((View) this).getTag(R.id.module_mediaplayer_id_player_looping);
        }
    }

    default boolean isLoopingRelease() {
        try {
            return (Boolean) ((View) this).getTag(R.id.module_mediaplayer_id_player_looping_release);
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isWindowVisibilityChangedRelease() {
        try {
            return (Boolean) ((View) this).getTag(R.id.module_mediaplayer_id_player_window_visibility_changed_release);
        } catch (Exception e) {
            return false;
        }
    }

    default long getSeek() {
        try {
            KernelApi kernel = getKernel();
            Object tag = ((View) this).getTag(R.id.module_mediaplayer_id_player_position);
            if (null != tag) {
                kernel.setSeek((Long) tag);
            }
            return kernel.getSeek();
        } catch (Exception e) {
            return 0L;
        }
    }

    default void updateSeek() {
        try {
            KernelApi kernel = getKernel();
            long position = kernel.getPosition();
            ((View) this).setTag(R.id.module_mediaplayer_id_player_position, position);
        } catch (Exception e) {
        }
    }

    default long getMax() {
        try {
            KernelApi kernel = getKernel();
            return kernel.getMax();
        } catch (Exception e) {
            return 0L;
        }
    }

    default String getUrl() {
        try {
            return (String) ((View) this).getTag(R.id.module_mediaplayer_id_player_url);
        } catch (Exception e) {
            return null;
        }
    }

    default void releaseTag() {
        try {
            ((View) this).setTag(R.id.module_mediaplayer_id_player_url, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_position, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_looping, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_looping_release, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_window_visibility_changed_release, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_external_music_url, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_external_music_looping, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_external_music_seek, null);
            ((View) this).setTag(R.id.module_mediaplayer_id_player_external_music_play_when_ready, null);
        } catch (Exception e) {
        }
    }

    default void seekTo(@NonNull boolean force) {
        StartBuilder.Builder builder = new StartBuilder.Builder();
        builder.setMax(getMax());
        builder.setSeek(getSeek());
        builder.setLoop(isLooping());
        builder.setLive(isLive());
        builder.setMute(isMute());
        builder.setWindowVisibilityChangedRelease(isWindowVisibilityChangedRelease());
        StartBuilder build = builder.build();
        seekTo(force, build);
    }

    default void seekTo(@NonNull long seek) {
        seekTo(false, seek, getMax(), isLooping());
    }

    default void seekTo(@NonNull long seek, @NonNull long max) {
        seekTo(false, seek, max, isLooping());
    }

    default void seekTo(@NonNull boolean force, @NonNull long seek) {
        seekTo(force, seek, getMax(), isLooping());
    }

    default void seekTo(@NonNull boolean force, @NonNull long seek, @NonNull long max, @NonNull boolean loop) {
        StartBuilder.Builder builder = new StartBuilder.Builder();
        builder.setMax(max);
        builder.setSeek(seek);
        builder.setLoop(loop);
        builder.setLive(isLive());
        builder.setWindowVisibilityChangedRelease(isWindowVisibilityChangedRelease());
        StartBuilder build = builder.build();
        seekTo(force, build);
    }

    default void seekTo(@NonNull boolean force, @NonNull StartBuilder builder) {

        try {
            // 1
            checkKernel();
            // 2
            if (force) {
                updateKernel(builder);
            }
            // 3
            long seek = builder.getSeek();
            seekToKernel(seek);
        } catch (Exception e) {
        }
    }

    default boolean isLive() {
        try {
            KernelApi kernel = getKernel();
            return kernel.isLive();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isPlaying() {
        try {
            KernelApi kernel = getKernel();
            return kernel.isPlaying();
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isMute() {
        try {
            KernelApi kernel = getKernel();
            return kernel.isMute();
        } catch (Exception e) {
            return false;
        }
    }

    default void setSpeed(float speed) {
        try {
            KernelApi kernel = getKernel();
            kernel.setSpeed(speed);
        } catch (Exception e) {
        }
    }

    default float getSpeed() {
        try {
            KernelApi kernel = getKernel();
            return kernel.getSpeed();
        } catch (Exception e) {
            return 1F;
        }
    }

    /*********************/

    default void checkKernel() throws Exception {
        KernelApi kernel = getKernel();
        if (null == kernel)
            throw new Exception("check kernel is null");
    }

    default void resumeKernel(@NonNull boolean ignore) {
        setPlayWhenReady(true);
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            MPLogUtil.log("PlayerApiKernel => resumeKernel => ignore = " + ignore + ", kernel = " + kernel);
            kernel.start();
            setScreenKeep(true);
            if (ignore) {
                callPlayerEvent(PlayerType.StateType.STATE_RESUME_IGNORE);
            } else {
                callPlayerEvent(PlayerType.StateType.STATE_RESUME);
                callPlayerEvent(PlayerType.StateType.STATE_KERNEL_RESUME);
            }
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => resumeKernel => " + e.getMessage());
        }
    }

    default void stopKernel(@NonNull boolean call) {
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            MPLogUtil.log("PlayerApiKernel => stopKernel => kernel = " + kernel);
            kernel.stop();
            setScreenKeep(false);
            if (!call) return;
            callPlayerEvent(PlayerType.StateType.STATE_KERNEL_STOP);
            callPlayerEvent(PlayerType.StateType.STATE_CLOSE);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => stopKernel => " + e.getMessage());
        }
    }

    default void pauseKernel(@NonNull boolean ignore) {
        setPlayWhenReady(false);
        try {
            // 1
            checkKernel();
            // 2
            pauseExternalMusic();
            // 3
            getKernel().pause();
            // 4
            updateSeek();
            // 5
            setScreenKeep(false);
            callPlayerEvent(ignore ? PlayerType.StateType.STATE_PAUSE_IGNORE : PlayerType.StateType.STATE_PAUSE);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => pauseKernel => " + e.getMessage());
        }
    }

    default void releaseKernel(boolean isFromUser) {
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            MPLogUtil.log("PlayerApiKernel => releaseKernel => kernel = " + kernel);
            kernel.releaseDecoder(isFromUser);
            setKernel(null);
            setScreenKeep(false);
            MPLogUtil.log("PlayerApiKernel => releaseKernel => succ");
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => releaseKernel => " + e.getMessage());
        }
    }

    default void seekToKernel(long milliSeconds) {
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            MPLogUtil.log("PlayerApiKernel => seekToKernel => milliSeconds = " + milliSeconds + ", kernel = " + kernel);
            kernel.seekTo(milliSeconds, false);
            setScreenKeep(true);
            if (milliSeconds <= 0)
                return;
            callPlayerEvent(PlayerType.StateType.STATE_INIT_SEEK);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => seekToKernel => " + e.getMessage());
        }
    }

    default void updateKernel(@NonNull StartBuilder builder) {
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            MPLogUtil.log("PlayerApiKernel => updateKernel => kernel = " + kernel);
            long seek = builder.getSeek();
            long max = builder.getMax();
            boolean loop = builder.isLoop();
            kernel.update(seek, max, loop);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => updateKernel => " + e.getMessage());
        }
    }

    default void initKernel(@NonNull StartBuilder bundle, @NonNull String playUrl) {
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            MPLogUtil.log("PlayerApiKernel => initKernel => kernel = " + kernel);
            kernel.initDecoder(getBaseContext(), playUrl, bundle);
            setScreenKeep(true);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => initKernel => " + e.getMessage());
        }
    }

    default void playEnd() {
        hideReal();
        setScreenKeep(false);
    }

    /***************************/

    default void setKernel(@PlayerType.KernelType.Value int v) {
        try {
            PlayerManager.getInstance().setKernel(v);
        } catch (Exception e) {
        }
    }

    default void createKernel(@NonNull StartBuilder builder, @NonNull PlayerBuilder config) {

        // 1
        try {
            checkKernel();
            release(false, false);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => createKernel => keenel warning: null");
        }

        // 2
        try {
            int type = PlayerManager.getInstance().getConfig().getKernel();
            KernelApi kernel = KernelFactoryManager.getKernel((PlayerApi) this, type, new KernelApiEvent() {

                @Override
                public void onUpdateTimeMillis(@NonNull boolean isLooping, @NonNull long max, @NonNull long seek, @NonNull long position, @NonNull long duration) {

                    boolean reset;
                    if (max > 0) {
                        long playTime = (position - seek);
                        if (playTime > max) {
                            reset = true;
                        } else {
                            reset = false;
                        }
                    } else {
                        reset = false;
                    }

                    // end
                    if (reset) {
                        // loop
                        if (isLooping) {
                            // 1
                            hideReal();
                            // 2
                            boolean seekHelp = config.isSeekHelp();
                            if (seekHelp) {
                                seekToKernel(1);
                            } else {
                                seekTo(true);
                            }
                        }
                        // stop
                        else {
                            // step1
                            pause(true);
                            // step2
                            playEnd();
                        }
                    }
                    // next
                    else {

                        // 1
                        callUpdateTimeMillis(seek, position, duration);
                        // 2
                        callProgressListener(position, duration);
                    }
                }

                @Override
                public void onEvent(int kernel, int event) {
                    MPLogUtil.log("PlayerApiKernel => onEvent = " + kernel + ", event = " + event);
                    switch (event) {
                        // 网络拉流开始
                        case PlayerType.EventType.EVENT_OPEN_INPUT:
                            hideReal();
                            break;
                        // 初始化开始 => loading start
                        case PlayerType.EventType.EVENT_LOADING_START:
                            callPlayerEvent(PlayerType.StateType.STATE_LOADING_START);
                            break;
                        // 初始化完成 => loading stop
                        case PlayerType.EventType.EVENT_LOADING_STOP:
                            callPlayerEvent(PlayerType.StateType.STATE_LOADING_STOP);
                            break;
                        // 缓冲开始
                        case PlayerType.EventType.EVENT_BUFFERING_START:
                            pauseExternalMusic();
                            callPlayerEvent(PlayerType.StateType.STATE_BUFFERING_START);
                            break;
                        // 缓冲结束
                        case PlayerType.EventType.EVENT_BUFFERING_STOP:
                            resumeExternalMusic();
                            callPlayerEvent(PlayerType.StateType.STATE_BUFFERING_STOP);
                            break;
                        // 播放开始-快进
                        case PlayerType.EventType.EVENT_VIDEO_START_SEEK:
                            // step1
                            callPlayerEvent(PlayerType.StateType.STATE_START_SEEK);
                            // step2
                            showReal();
                            // step4
                            resume(true);
                            // step5
                            checkExternalMusic(getBaseContext());
                            // step6
                            MPLogUtil.log("PlayerApiKernel => onEvent => event_video_start_seek => playWhenReady = " + isPlayWhenReady());
                            if (!isPlayWhenReady()) {
                                pause();
                            }
                            break;
                        // 播放开始
                        case PlayerType.EventType.EVENT_VIDEO_START:
//                        case PlayerType.EventType.EVENT_VIDEO_SEEK_RENDERING_START: // 视频开始渲染
//            case PlayerType.MediaType.MEDIA_INFO_AUDIO_SEEK_RENDERING_START: // 视频开始渲染

                            // 埋点
                            onBuriedEventPlaying();

                            callPlayerEvent(PlayerType.StateType.STATE_START);

                            // step2
                            showReal();
                            // step3
                            checkReal();
                            // step4
                            checkExternalMusic(getBaseContext());
                            // step5
                            MPLogUtil.log("PlayerApiKernel => onEvent => event_video_start_seek => playWhenReady = " + isPlayWhenReady());
                            if (!isPlayWhenReady()) {
                                pause();
                            }
                            break;
                        // 播放错误
                        case PlayerType.EventType.EVENT_ERROR_URL:
                        case PlayerType.EventType.EVENT_ERROR_RETRY:
                        case PlayerType.EventType.EVENT_ERROR_SOURCE:
                        case PlayerType.EventType.EVENT_ERROR_PARSE:
                        case PlayerType.EventType.EVENT_ERROR_NET:

                            // 埋点
                            onBuriedEventError();

                            setScreenKeep(false);
                            callPlayerEvent(PlayerType.StateType.STATE_ERROR);

                            break;
                        // 播放结束
                        case PlayerType.EventType.EVENT_VIDEO_END:

                            // 埋点
                            onBuriedEventCompletion();
                            callPlayerEvent(PlayerType.StateType.STATE_END);

                            boolean looping = isLooping();
                            boolean loopingRelease = isLoopingRelease();
                            MPLogUtil.log("PlayerApiKernel => onEvent = 播放结束 => looping = " + looping + ", loopingRelease = " + loopingRelease);
                            // loop1
                            if (looping && loopingRelease) {
                                release(false, false);
                                restart();
                            }
                            // loop2
                            else if (looping) {
                                hideReal();
                                seekTo(true, builder);
                            }
                            // sample
                            else {
                                playEnd();
                            }

                            break;
                    }
                }

                @Override
                public void onMeasure(int kernel, int videoWidth, int videoHeight, @PlayerType.RotationType.Value int rotation) {
                    MPLogUtil.log("PlayerApiKernel => onMeasure = kernel = " + kernel + ", videoWidth = " + videoWidth + ", videoHeight = " + videoHeight + ", rotation = " + rotation);
                    setVideoSize(videoWidth, videoHeight);
                    setVideoRotation(rotation);
                }
            });
            // 4
            setKernel(kernel);
            // 5
            createDecoder(config);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => createKernel => " + e.getMessage());
        }
    }

    default void attachRender() {
        try {
            RenderApi render = getRender();
            KernelApi kernel = getKernel();
            render.setKernel(kernel);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => attachRender => " + e.getMessage());
        }
    }

    default void createDecoder(@NonNull PlayerBuilder config) {
        try {
            // 1
            checkKernel();
            // 2
            KernelApi kernel = getKernel();
            boolean log = config.isLog();
            int seekParameters = config.getExoSeekParameters();
            kernel.createDecoder(getBaseContext(), log, seekParameters);
        } catch (Exception e) {
            MPLogUtil.log("PlayerApiKernel => createDecoder => " + e.getMessage());
        }
    }

//    default String getScreenshotPath() {
//        try {
//            String url = getUrl();
//            MPLogUtil.log("PlayerApiKernel => getScreenshotPath => url => " + url);
//            if (null == url || url.length() == 0)
//                throw new Exception("url error: " + url);
//            long position = getPosition();
//            if (position < 0)
//                throw new Exception("position error: " + position);
//            long duration = getDuration();
//            if (duration < 0)
//                throw new Exception("duration error: " + duration);
//            // android 2.3及其以上版本使用
//            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
////            retriever.setDataSource(getBaseContext(), Uri.parse(url));
//            retriever.setDataSource(url, new HashMap<String, String>());
//            // 这一句是必须的
//            String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            // 获取总长度,这一句也是必须的
//            long titalTime = Long.parseLong(timeString) * 1000;
//            // 通过这个计算出想截取的画面所在的时间
//            long videoPosition = titalTime * position / duration;
//            if (videoPosition < 0)
//                throw new Exception("videoPosition error: " + videoPosition);
//            Bitmap bitmap = retriever.getFrameAtTime(videoPosition, MediaMetadataRetriever.OPTION_CLOSEST);
//            return BitmapUtil.saveScreenshot(getBaseContext(), bitmap);
//        } catch (Exception e) {
//            MPLogUtil.log("PlayerApiKernel => getScreenshotPath => " + e.getMessage());
//            return null;
//        }
//    }
}
