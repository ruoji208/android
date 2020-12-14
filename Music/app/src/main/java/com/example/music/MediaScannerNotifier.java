package com.example.music;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

public class MediaScannerNotifier implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mConnection;
    private String mPath;
    private OnScanListener onScanListener;

    private MediaScannerNotifier(Context context, String path,OnScanListener onScanListener) {
        mPath = path;
        this.onScanListener = onScanListener;
        mConnection = new MediaScannerConnection(context, this);
        mConnection.connect();
    }

    public static void scan(Context context, String path,OnScanListener onScanListener) {
        new MediaScannerNotifier(context, path,onScanListener);
    }

    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, null);
    }

    public void onScanCompleted(String path, Uri uri) {
        mConnection.disconnect();
        if(onScanListener!=null){
            onScanListener.onComplete();
        }
    }

    public interface  OnScanListener{
        void  onComplete();
    }
}
