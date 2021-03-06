package com.tangpj.oauth2.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tangpj.github.GithubApp
import com.tangpj.oauth2.domain.GithubToken
import com.tangpj.oauth2.databinding.FragmentOauth2Binding
import com.recurve.dagger2.RecurveDaggerFragment
import com.recurve.core.resource.Resource
import com.recurve.core.resource.Status
import com.recurve.core.util.openInCustomTabOrBrowser
import javax.inject.Inject

class AuthorizationFragment : RecurveDaggerFragment(){

    private lateinit var authorizationViewModel: AuthorizationViewModel
    private lateinit var binding: FragmentOauth2Binding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val authorizeListener = View.OnClickListener{ authorize() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        authorizationViewModel = ViewModelProviders.of(this, viewModelFactory)[AuthorizationViewModel::class.java]
        arguments?.let{
            val params = AuthorizationFragmentArgs.fromBundle(it)
            getToken(params.code)
        }
        authorizationViewModel.token.observe(this, Observer<Resource<GithubToken>>{
            if(it.networkState.status == Status.SUCCESS){
                val intent = Intent("com.tangpj.github.loginTransfer")
                intent.putExtra("access_token", it.data)
                activity?.sendBroadcast(intent)
                authorizationViewModel.token.removeObservers(this)

            }
        })


    }
    override fun onCreateBinding(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): ViewDataBinding? {
        binding = FragmentOauth2Binding.inflate(inflater,container, false)
        binding.authorizeListener = authorizeListener
        return binding
    }

    private fun authorize(){
        val uri = authorizationViewModel.buildAuthorizeUri()
        openInCustomTabOrBrowser(context, uri)
    }

    private fun getToken(code: String?){
        code?.let {
            authorizationViewModel.refreshCode(code)
        }

    }
}