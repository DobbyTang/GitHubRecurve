package com.tangpj.mvvmrecurve

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.tangpj.mvvmrecurve.databinding.ActivityMainBinding
import com.tangpj.recurve.recyclerview.adapter.ModulesAdapter

class MainActivity : AppCompatActivity() {

    private val adapter = ModulesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }

    fun add(v: View){
    }
}
