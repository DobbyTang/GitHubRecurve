package com.tangpj.repository.ui.repositories

import android.os.Bundle
import androidx.navigation.NavController
import com.tangpj.github.ui.BaseActivity
import com.tangpj.github.entity.domain.RepoFlag
import com.tangpj.repository.R

class ReposActivity : BaseActivity(){

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = initContentFragment(
                R.navigation.repositories)


        val defaultArgs = ReposFragmentArgs
                .Builder()
                .setLogin("Tangpj")
                .setType(RepoFlag.STAR)
                .build()
                .toBundle()

        navController.navigate(R.id.repositories, defaultArgs)
        appbar{
            title = getString(R.string.app_name)
        }
    }
}