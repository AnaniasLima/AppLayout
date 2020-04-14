package com.example.applayout

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


// https://www.codeproject.com/Tips/5262254/Learn-Android-programming-with-Kotlin-Using-VideoV

class MainActivity : AppCompatActivity() {
    var position = 0
    var mcontroller : MediaController?=null
    var ultimaMemoria: Long = 0

    private val VIDEO_NAME = "v100"


    private fun getURI(videoname:String): Uri{
        if (URLUtil.isValidUrl(videoname)) {
            //  an external URL
            return Uri.parse(videoname);
        } else { //  a raw resource
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + videoname);
        }
    }

    private fun initPlayer() {
        var videoUri:Uri = getURI(VIDEO_NAME)
        var poucaMemoria = ActivityManager.MemoryInfo().lowMemory

        if (poucaMemoria ) {
            Timber.w("== == == == == == == == POUCA MEMORIA == == == == == == == ==")
        }

        videoView.setVideoURI(videoUri)

        videoView.setOnCompletionListener {
            videoView.seekTo(1);
            videoView.start()
            var memoria = Runtime.getRuntime().freeMemory()

            Timber.i("---Antes: %d   Depois de liberar: =%d  diferença: %d", ultimaMemoria, memoria, (memoria - ultimaMemoria))
            ultimaMemoria = memoria

        }


        videoView.start()
    }

    private fun releasePlayer(){
        var memoriaAntes = Runtime.getRuntime().freeMemory()

        videoView.stopPlayback()

        var memoriaDepois = Runtime.getRuntime().freeMemory()

        Timber.i("---Antes: %d   Depois de liberar: =%d  diferença: %d", memoriaAntes, memoriaDepois, (memoriaDepois - memoriaAntes))
    }




    private fun getMemoryInfo(): String {
        val memoryInfo = ActivityManager.MemoryInfo()
        val runtime = Runtime.getRuntime()

        return ("Available Memory = " + memoryInfo.availMem + "\n"
                + "Total Memory = " + memoryInfo.totalMem + "\n"
                + "Runtime Max Memory = " + runtime.maxMemory() + "\n"
                + "Runtime Total Memory = " + runtime.totalMemory() + "\n"
                + "Runtime Free Memory = " + runtime.freeMemory() + "\n")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener  {
            Timber.i("WWW Vai chamar initPlayer...")
            initPlayer()
            Timber.i("WWW chamou initPlayer")
        }

        btnStop.setOnClickListener  {
            Timber.i("WWW Vai chamar releasePlayer...")
            releasePlayer()
            Timber.i("WWW chamou releasePlayer")
        }


        videoView.setOnClickListener {
            Timber.i("WWW Vai chamar releasePlayer...")
            releasePlayer()
            Timber.i("WWW chamou releasePlayer")
        }
    }
}
