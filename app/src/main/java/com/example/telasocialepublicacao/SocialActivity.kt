package com.example.telasocialepublicacao

import com.example.myapplication.R

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SocialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_social_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    FeedFragment()
                )
                .commit()
        }
    }
}