package com.tangpj.github.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.tangpj.github.R

class WrapperDialogFragment : DialogFragment(){

    var navHostFragment = MutableLiveData<NavHostFragment>()
    lateinit var arg : WrapperDialogFragmentArgs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            arg = WrapperDialogFragmentArgs.fromBundle(it)
            val layoutId =  R.layout.fragment_nav_container
            val binding =
                    DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, container, false)
            val fragment = NavHostFragment.create(arg.graphId, arg.args)
            childFragmentManager.beginTransaction().add(R.id.nav_host_container, fragment)
                    .setPrimaryNavigationFragment(fragment).commitNow()
            navHostFragment.value = fragment
            return binding.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val mWindow = dialog?.window
        mWindow ?: return
        mWindow.attributes?.apply {
            mWindow.setLayout(if (arg.width == 0){
                width
            }else{
                arg.width
            }, if (arg.height == 0){
                height
            }else{
                arg.height
            })
        }
    }
}