package com.example.saluslink.utilits

import android.media.MediaPlayer
import java.io.File

class AppVoicePlayer {

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        mFile = File(APP_ACTIVITY.filesDir, messageKey)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile){
            startPlay{
                function()
            }
        } else{
            mFile.createNewFile()
            getFileFromStorage(mFile, fileUrl){
                startPlay{
                    function()
                }
            }
        }
    }

    private fun startPlay(function: () -> Unit) {
        try{
            mMediaPlayer.setDataSource(mFile.absolutePath)
            mMediaPlayer.prepare()
            mMediaPlayer.start()
            mMediaPlayer.setOnCompletionListener {
                stop{
                    function()
                }
            }
        } catch (e:Exception){
            showToast("Не удалось воспроизвести файл")
        }
    }

    fun stop(function: () -> Unit) {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
            function()
        }catch (e:Exception){
            showToast("Не удалось воспроизвести файл")
            function()
        }
    }

    fun release(){
        mMediaPlayer.release()
    }

    fun init(){
        mMediaPlayer = MediaPlayer()
    }
}
