package com.tangpj.repository

import com.tangpj.github.BuildConfig
import com.tangpj.github.GithubApp
import com.tangpj.github.di.DaggerGithubComponent
import com.tangpj.repository.di.DaggerRepositoryComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class RepositoryApp : GithubApp(){

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(object : Timber.Tree(){
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    //release包不打印日志
                }
            })
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val githubComponent = DaggerGithubComponent.factory().create(this)
        return DaggerRepositoryComponent
                .builder()
                .githubComponent(githubComponent)
                .bindContext(this)
                .create()
    }
}