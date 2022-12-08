package com.example.musicplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var handler:Handler
    private lateinit var runnable: Runnable
    private var mediaPlayer:MediaPlayer?=null
    private lateinit var seekBar: SeekBar
    private lateinit var tvPlayedTime:TextView
    private lateinit var tvDueTime:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fabPlay=findViewById<FloatingActionButton>(R.id.fabPlay)
        val fabPause=findViewById<FloatingActionButton>(R.id.fabPause)
        val fabStop=findViewById<FloatingActionButton>(R.id.fabStop)
        tvPlayedTime=findViewById(R.id.tvPlayedTime)
        tvDueTime=findViewById(R.id.tvDueTime)
        seekBar=findViewById(R.id.seekBar)
        handler=Handler(Looper.getMainLooper())

        fabPlay.setOnClickListener {
            if(mediaPlayer==null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.ciecles)
                initialiseSeekBar()
            }
            mediaPlayer?.start()
        }

        fabPause.setOnClickListener {
            mediaPlayer?.pause()
        }
        fabStop.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer=null
            handler.removeCallbacks(runnable)
            seekBar.progress=0
            tvPlayedTime.text="0"
            tvDueTime.text="0"

        }
    }

    private fun initialiseSeekBar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seekBar.max=mediaPlayer!!.duration
        runnable= Runnable {
            seekBar.progress=mediaPlayer!!.currentPosition
            handler.postDelayed(runnable,1000)
            val currTime:Int= (mediaPlayer!!.currentPosition)/1000
            val dueTime:Int= (mediaPlayer!!.duration)/1000 - currTime
            tvPlayedTime.text=currTime.toString()
            tvDueTime.text=dueTime.toString()
        }
        handler.postDelayed(runnable,1000)
    }
}