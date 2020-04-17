package com.example.applayout

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.MediaController
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


// https://www.codeproject.com/Tips/5262254/Learn-Android-programming-with-Kotlin-Using-VideoV

class MainActivity : AppCompatActivity() {
    var position = 0
    var mcontroller : MediaController?=null
    var ultimaMemoria: Long = 0
    lateinit var videoArray: ArrayList<String>
    var lastPlayedVideo: Int = -1


    private fun getURI(videoname:String): Uri {
        if (URLUtil.isValidUrl(videoname)) {
            //  an external URL
            return Uri.parse(videoname)
        } else { //  a raw resource
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + videoname);
        }
    }


    private fun prepareVideoArray() {
        videoArray = arrayListOf()
        videoArray.add("v100")
        videoArray.add("v15")
        videoArray.add("v01")
        videoArray.add("v201")
        videoArray.add("v202")
    }

    private fun playNextVideo() {
        if (videoArray.isEmpty()) {
            return
        }

        lastPlayedVideo++

        if (lastPlayedVideo == videoArray.size ) {
            lastPlayedVideo = 0
        } else {
            if ( videoArray[lastPlayedVideo].isEmpty() ) {
                lastPlayedVideo = 0
            }
        }

        println("WWW PLAYING NEXT VIDEO $lastPlayedVideo  Video:${videoArray[lastPlayedVideo]} Max:${videoArray.size}")

//        videoView.setVideoPath(getURI(videoArray[lastPlayedVideo]))
        videoView.setVideoURI(getURI(videoArray[lastPlayedVideo]))

        videoView.start()
    }


    private fun initPlayer() {
        var poucaMemoria = ActivityManager.MemoryInfo().lowMemory

        if (poucaMemoria ) {
            Timber.w("== == == == == == == == POUCA MEMORIA == == == == == == == ==")
        }

        videoView.setVisibility(View.VISIBLE)

        lastPlayedVideo = 0
        videoView.setVideoURI(getURI(videoArray[lastPlayedVideo]))

//        videoView.setVideoPath(getURI(videoArray[lastPlayedVideo]))

        videoView.setOnCompletionListener {
            try {
                playNextVideo()
            } catch (e: Exception) {
                e.printStackTrace()
            }

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

        prepareVideoArray()

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


        btnInvisivel.setOnClickListener {
            Timber.i("WWW Vai chamar releasePlayer...")
            releasePlayer()
            videoView.setVisibility(View.INVISIBLE)

            Timber.i("WWW chamou releasePlayer")
        }
    }
}
