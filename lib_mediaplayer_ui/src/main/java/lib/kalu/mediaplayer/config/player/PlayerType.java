package lib.kalu.mediaplayer.config.player;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.CLASS;

import androidx.annotation.IntDef;
import androidx.annotation.Keep;
import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tv.danmaku.ijk.media.player.IMediaPlayer;

@Documented
@Retention(CLASS)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
@Keep
public @interface PlayerType {

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface CacheType {

        int DOWNLOAD = 1_001;
        int NONE = 1_002;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef(value = {CacheType.DOWNLOAD, CacheType.NONE})
        @Keep
        @interface Value {
        }
    }

    /**
     * 播放模式
     * 普通模式，小窗口模式，正常模式三种其中一种
     */
    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface WindowType {
        //普通模式
        int NORMAL = 2_001;
        //全屏模式
        int FULL = 2_002;
        //窗口模式
        int FLOAT = 2_003;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef({NORMAL, FULL, FLOAT})
        @Keep
        @interface Value {
        }
    }

    /**
     * 播放状态，主要是指播放器的各种状态
     */
    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface StateType {
        int STATE_INIT = 3_001; // 播放未开始，即将进行
        int STATE_INIT_SEEK = 3_002; // 显示进度条
        int STATE_CLEAN = 3_003; //
        int STATE_LOADING_START = 3_004; // 开始转圈
        int STATE_LOADING_STOP = 3_005; // 停止转圈(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
        int STATE_KERNEL_STOP = 3_006;
        int STATE_KERNEL_RESUME = 3_007; // 开始播放
        int STATE_START = 3_008; // 开始播放
        int STATE_START_SEEK = 3_009; // 开始播放
        int STATE_END = 3_010; // 播放完成
        int STATE_PAUSE = 3_011; // 暂停播放
        int STATE_PAUSE_IGNORE = 3_012; // 暂停播放
        int STATE_RESUME = 3_013; // 恢复播放
        int STATE_RESUME_IGNORE = 3_014; // 恢复播放
        int STATE_RESTAER = 3_015; // 重播一次
        int STATE_CLOSE = 3_016; // 暂停播放
        int STATE_BUFFERING_START = 3_017; // 开始缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
        int STATE_BUFFERING_STOP = 3_018; // 停止缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
        int STATE_START_ABORT = 3_019; // 开始播放中止
        int STATE_ONCE_LIVE = 3_020; // 即将开播
        int STATE_FAST_FORWARD_START = 3_021; // 快进
        int STATE_FAST_FORWARD_STOP = 3_022; // 快进
        int STATE_FAST_REWIND_START = 3_023; // 快进
        int STATE_FAST_REWIND_STOP = 3_024; // 快进

        int STATE_ERROR = 3_025; // 错误
        int STATE_ERROR_IGNORE = 3_026; // 错误
        int STATE_COMPONENT_SEEK_SHOW = 3_027; // 显示进度条
        int STATE_RELEASE = 3_028;
        int STATE_RELEASE_EXCEPTION = 3_029;

        int STATE_FULL_START = 3_030;
        int STATE_FULL_STOP = 3_031;
        int STATE_FLOAT_START = 3_032;
        int STATE_FLOAT_STOP = 3_033;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef({
                STATE_FULL_START,
                STATE_FULL_STOP,
                STATE_FLOAT_START,
                STATE_FLOAT_STOP,
                STATE_FAST_FORWARD_START,
                STATE_FAST_FORWARD_STOP,
                STATE_FAST_REWIND_START,
                STATE_FAST_REWIND_STOP,
                STATE_INIT,
                STATE_INIT_SEEK,
                STATE_CLEAN,
                STATE_KERNEL_STOP,
                STATE_KERNEL_RESUME,
                STATE_START,
                STATE_START_SEEK,
                STATE_PAUSE,
                STATE_PAUSE_IGNORE,
                STATE_RESUME,
                STATE_RESUME_IGNORE,
                STATE_RESTAER,
                STATE_CLOSE,
                STATE_BUFFERING_START,
                STATE_BUFFERING_STOP,
                STATE_LOADING_STOP,
                STATE_END,
                STATE_START_ABORT,
                STATE_LOADING_START,
                STATE_ONCE_LIVE,
                STATE_ERROR,
                STATE_ERROR_IGNORE,
                STATE_RELEASE,
                STATE_RELEASE_EXCEPTION,
                STATE_COMPONENT_SEEK_SHOW})
        @Keep
        @interface Value {
        }
    }

    /**
     * 播放视频缩放类型
     */
    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface ScaleType {
        int SCREEN_SCALE_SCREEN_CROP = 4_001; // 填充屏幕, 裁剪
        int SCREEN_SCALE_SCREEN_MATCH = 4_002; // 填充屏幕, 不裁剪, 可能会变形
        int SCREEN_SCALE_VIDEO_ORIGINAL = 4_003; // 视频尺寸, 可能存在黑边
        int SCREEN_SCALE_16_9 = 4_004; //16：9比例类型，最为常见
        int SCREEN_SCALE_4_3 = 4_005;  //4：3比例类型，也比较常见

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef({SCREEN_SCALE_SCREEN_CROP,
                SCREEN_SCALE_SCREEN_MATCH,
                SCREEN_SCALE_VIDEO_ORIGINAL,
                SCREEN_SCALE_16_9,
                SCREEN_SCALE_4_3})
        @Keep
        @interface Value {
        }
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface RotationType {
        int Rotation_0 = 5_001;
        int Rotation_90 = 5_002;
        int Rotation_180 = 5_003;
        int Rotation_270 = 5_004;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef({Rotation_0,
                Rotation_90,
                Rotation_180,
                Rotation_270})
        @Keep
        @interface Value {
        }
    }

    /**
     * 通过注解限定类型
     */
    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface KernelType {
        int ANDROID = 6_001; // MediaPlayer，基于原生自带的播放器控件
        int EXO_V1 = 6_002; // exo
        int EXO_V2 = 6_003; // exo
        int IJK = 6_004; // ijk
        int IJK_MEDIACODEC = 6_005; // ijk_mediacodec
        int VLC = 6_006; // vlc
        int FFPLAYER = 6_007; // ffplayer

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef({IJK,
                IJK_MEDIACODEC,
                ANDROID,
                EXO_V1,
                EXO_V2,
                VLC,
                FFPLAYER})
        @Keep
        @interface Value {
        }
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface EventType {
        int EVENT_ERROR_URL = 7_001;
        int EVENT_ERROR_RETRY = 7_002;
        int EVENT_ERROR_SOURCE = 7_003;
        int EVENT_ERROR_PARSE = 7_004;
        int EVENT_ERROR_NET = 7_005;
        int EVENT_LOADING_START = 7_006; // 开始转圈
        int EVENT_LOADING_STOP = 7_007; // 停止转圈(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放
        // 播放结束
        //        // 开始渲染视频画面
//        int EVENT_VIDEO_SEEK_RENDERING_START = IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START;
//        // 开始渲染视频画面
//        int EVENT_AUDIO_SEEK_RENDERING_START = IMediaPlayer.MEDIA_INFO_AUDIO_SEEK_RENDERING_START;
//        // 开始渲染视频画面
//        int EVENT_AUDIO_RENDERING_START = IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START;
        // 开始渲染视频画面
        int EVENT_VIDEO_END = 7_008;
        int EVENT_VIDEO_START = IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;
        int EVENT_VIDEO_START_SEEK = IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START;
        //        int EVENT_VIDEO_SEEK_COMPLETE_B = IMediaPlayer.MEDIA_INFO_VIDEO_SEEK_RENDERING_START;
        //        int EVENT_VIDEO_STOP = IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;
//        int EVENT_VIDEO_END = IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START;
        // 缓冲开始
        int EVENT_OPEN_INPUT = IMediaPlayer.MEDIA_INFO_OPEN_INPUT;
        // 缓冲开始
        int EVENT_BUFFERING_START = IMediaPlayer.MEDIA_INFO_BUFFERING_START;
        // 缓冲结束
        int EVENT_BUFFERING_STOP = IMediaPlayer.MEDIA_INFO_BUFFERING_END;
        // 视频旋转信息
//        int EVENT_VIDEO_ROTATION_CHANGED = IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED;
//        int EVENT_AUDIO_DECODED_START = IMediaPlayer.MEDIA_INFO_AUDIO_DECODED_START;
//        int EVENT_VIDEO_DECODED_START = IMediaPlayer.MEDIA_INFO_VIDEO_DECODED_START;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef({
                EVENT_ERROR_URL,
                EVENT_ERROR_RETRY,
                EVENT_ERROR_SOURCE,
                EVENT_ERROR_PARSE,
                EVENT_ERROR_NET,
                EVENT_OPEN_INPUT,
                EVENT_LOADING_START,
                EVENT_LOADING_STOP,
//                EVENT_VIDEO_SEEK_RENDERING_START,
//                EVENT_AUDIO_SEEK_RENDERING_START,
//                EVENT_AUDIO_RENDERING_START,
                EVENT_VIDEO_START,
                EVENT_VIDEO_START_SEEK,
                EVENT_VIDEO_END,
//                EVENT_VIDEO_STOP,
//                EVENT_VIDEO_END,
                EVENT_BUFFERING_START,
                EVENT_BUFFERING_STOP,
//                EVENT_VIDEO_ROTATION_CHANGED,
//                EVENT_AUDIO_DECODED_START,
//                EVENT_VIDEO_DECODED_START
        })
        @Keep
        @interface Value {
        }
    }

    /**
     * 通过注解限定类型
     */
    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface RenderType {
        int TEXTURE_VIEW = 8_001;
        int SURFACE_VIEW = 8_002;

        @IntDef({TEXTURE_VIEW,
                SURFACE_VIEW})
        @Retention(RetentionPolicy.SOURCE)
        @Keep
        @interface Value {
        }
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface SeekType {

        int EXO_SEEK_DEFAULT = 9_001;
        int EXO_SEEK_CLOSEST_SYNC = 9_002;
        int EXO_SEEK_PREVIOUS_SYNC = 9_003;
        int EXO_SEEK_NEXT_SYNC = 9_004;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef(value = {
                SeekType.EXO_SEEK_DEFAULT,
                SeekType.EXO_SEEK_CLOSEST_SYNC,
                SeekType.EXO_SEEK_PREVIOUS_SYNC,
                SeekType.EXO_SEEK_NEXT_SYNC})
        @Keep
        @interface Value {
        }
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface FFmpegType {
        int EXO_RENDERER_ONLY_MEDIACODEC = 10_001;
        int EXO_RENDERER_ONLY_MEDIACODEC_AUDIO = 10_002;
        int EXO_RENDERER_ONLY_MEDIACODEC_VIDEO = 10_003;
        int EXO_RENDERER_ONLY_FFMPEG = 10_004;
        int EXO_RENDERER_ONLY_FFMPEG_AUDIO = 10_005;
        int EXO_RENDERER_ONLY_FFMPEG_VIDEO = 10_006;
        int EXO_RENDERER_VIDEO_MEDIACODEC_AUDIO_FFMPEG = 10_007;
        int EXO_RENDERER_VIDEO_FFMPEG_AUDIO_MEDIACODEC = 10_008;

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @IntDef(value = {
                FFmpegType.EXO_RENDERER_ONLY_MEDIACODEC,
                FFmpegType.EXO_RENDERER_ONLY_MEDIACODEC_AUDIO,
                FFmpegType.EXO_RENDERER_ONLY_MEDIACODEC_VIDEO,
                FFmpegType.EXO_RENDERER_ONLY_FFMPEG,
                FFmpegType.EXO_RENDERER_ONLY_FFMPEG_AUDIO,
                FFmpegType.EXO_RENDERER_ONLY_FFMPEG_VIDEO,
                FFmpegType.EXO_RENDERER_VIDEO_MEDIACODEC_AUDIO_FFMPEG,
                FFmpegType.EXO_RENDERER_VIDEO_FFMPEG_AUDIO_MEDIACODEC})
        @Keep
        @interface Value {
        }
    }

    @Documented
    @Retention(CLASS)
    @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
    @Keep
    @interface SchemeType {

        String RTMP = "rtmp";
        String RTSP = "rtsp";
        String _MPD = ".mpd";
        String _M3U = ".m3u";
        String _M3U8 = ".m3u8";
        String _MATCHES = ".*\\.ism(l)?(/manifest(\\(.+\\))?)?";

        @Documented
        @Retention(CLASS)
        @Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE, ANNOTATION_TYPE, PACKAGE})
        @StringDef(value = {
                SchemeType.RTMP,
                SchemeType.RTSP,
                SchemeType._MPD,
                SchemeType._M3U,
                SchemeType._M3U8,
                SchemeType._MATCHES})
        @Keep
        @interface Value {
        }
    }
}