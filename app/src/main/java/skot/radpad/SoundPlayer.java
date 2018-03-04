package skot.radpad;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.PlaybackParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by skot on 3/3/18.
 */


public class SoundPlayer implements AudioTrack.OnPlaybackPositionUpdateListener {

    final int MAX_BUFFER_SIZE = 1000000;

    private ByteBuffer buffer;
    private int bufferLength = 0;
    private int sampleRate = 44100;
    private AudioTrack audioTrack = null;
    private boolean loopingMode = false;
    private int playbackStart;
    private int playbackEnd;
    private int loopStart;
    private int loopEnd;

    private File sourceFile;


    public SoundPlayer() {
    }

    public void setSampleSource(final File sourceFile) {

        this.sourceFile = sourceFile;
        loadSampleFromDisk(sourceFile);
        init();
    }

    private void loadSampleFromDisk(final File file) {

        buffer = ByteBuffer.allocate((int)file.length());
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        System.out.println(">>> LOADING " + file.getName() + "...");

        try {
            FileInputStream in = new FileInputStream(file);

            bufferLength = in.read(buffer.array());
            System.out.println(">>> " + bufferLength + " BYTES READ");

            bufferLength = (int) (file.length() / (Short.SIZE / Byte.SIZE));
//            if (bufferLength % 2 != 0)
//                bufferLength = bufferLength - (bufferLength % 2);
//
            System.out.println(">>> " + bufferLength + " BYTES READ");

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                1000000,
                AudioTrack.MODE_STATIC);

        playbackStart = 0;
        playbackEnd = bufferLength - 1;
        loopStart = 0;
        loopEnd = bufferLength - 1;

        audioTrack.write(buffer, buffer.array().length, AudioTrack.WRITE_NON_BLOCKING);
        reset();
    }

    private synchronized void reset() {

        audioTrack.stop();
        audioTrack.write(buffer, buffer.array().length, AudioTrack.WRITE_NON_BLOCKING);
        audioTrack.setPlaybackHeadPosition(playbackStart);
//        audioTrack.setNotificationMarkerPosition(playbackEnd);
//        audioTrack.setLoopPoints(loopStart, loopEnd, loopingMode ? -1 : 0);
    }

    public synchronized void setPlaybackRate(final float percent) {
        sampleRate = (int) (44100 * percent);
        System.out.println("sampleRate = " + sampleRate);
        audioTrack.setPlaybackRate(sampleRate);
    }

    public synchronized void playSound() {

        if (audioTrack == null)
            return;

        reset();
        audioTrack.play();
    }


    public void setVolume(float v) {
        audioTrack.setVolume(v);
    }

    public void setSampleStart(final float percentPosition) {
//        playbackStart = (int)(percentPosition * buffer.length);
//        System.out.println("percentPosition = " + percentPosition);
//        System.out.println("playbackStart = " + playbackStart);
//        reset();
    }

    public void setLoopStart(final float percentPosition) {
//        loopingMode = true;
//        loopStart = (int)(percentPosition * buffer.length);
//        setSampleStart(loopStart);
//        //audioTrack.setPlaybackHeadPosition((int)(percentPosition * buffer.length));
//        reset();
    }

    public void setLoopEnd(final float percentPosition) {
//        loopingMode = true;
//        this.loopEnd = (int)(percentPosition * buffer.length);
//        reset();
    }


    public void setToLoop() {
        loopingMode = true;
        reset();
    }

    public void setToOneshot() {
//        System.out.println(">>> SET TO ONE-SHOT");
//        loopStart = 0;
//        loopEnd = buffer.length - 1;
//        reset();
//        loopingMode = false;
    }


    public synchronized void stopPlaying() {
        audioTrack.pause();
        reset();
    }

    public synchronized PlaybackParams getPlaybackParams() {
        return audioTrack.getPlaybackParams();
    }

    public synchronized void setPlaybackParams(PlaybackParams params) {
        if (audioTrack != null) {
            audioTrack.setPlaybackParams(params);
        }
    }

    public void releaseResources() {

        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }

        buffer = null;
    }


    @Override
    public void onMarkerReached(AudioTrack audioTrack) {
        if (!loopingMode) {
            System.out.println(">>> MARKER REACHED (" + playbackEnd + ")");
        }
    }

    @Override
    public void onPeriodicNotification(AudioTrack audioTrack) {

    }


}

