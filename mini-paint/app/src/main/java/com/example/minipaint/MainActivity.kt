package com.example.minipaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //create variable of MyCanvasView
        val myCanvasView = MyCanvasView(this)

        //set myCanvasView to full screen
        myCanvasView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //adding content description to myCanvasView
        myCanvasView.contentDescription = getString(R.string.canvasContentDescription)

        setContentView(myCanvasView)

    }
}