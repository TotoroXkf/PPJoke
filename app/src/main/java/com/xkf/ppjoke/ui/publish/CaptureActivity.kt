package com.xkf.ppjoke.ui.publish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xkf.libannotation.ActivityDestination
import com.xkf.ppjoke.R

@ActivityDestination(pageUrl = "main/tabs/capture")
class CaptureActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
    }
}
