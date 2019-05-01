package com.tangpj.repository.ui.repositories


import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import com.tangpj.github.ui.ModulePagingFragment
import com.tangpj.repository.creator.RepositoryCreator
import com.tangpj.repository.vo.RepoVo
import timber.log.Timber
import javax.inject.Inject

class RepoFragment: ModulePagingFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var repoViewModel: RepositoryViewModel

    lateinit var repositoryCreator : RepositoryCreator

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val arg = RepoFragmentArgs.fromBundle(arguments)
        Timber.d("user name = ${arg.login}")
        repoViewModel.setRepoOwner(arg.login)
    }

    override fun onBindingInit(binding: ViewDataBinding) {
        repoViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RepositoryViewModel::class.java)
        repositoryCreator = RepositoryCreator(adapter, POST_COMPARATOR)
        repoViewModel.pageLoadState.observeForever {
            Timber.d("load state = ${it.status}; netState = ${it.networkState.status}")
            Timber.d("${it.networkState.msg}")
        }

//        loading {
//            resource = repoViewModel.resource
//            retry = {
//            }
//        }

        addItemCreator(repositoryCreator)
        repoViewModel.repos.observeForever { repoVoList ->
            repoVoList?.let {
                repositoryCreator.submitList(it)
            }
        }

    }

    val POST_COMPARATOR = object : DiffUtil.ItemCallback<RepoVo>() {
        override fun areContentsTheSame(oldItem: RepoVo, newItem: RepoVo): Boolean =
                oldItem == newItem

        override fun areItemsTheSame(oldItem: RepoVo, newItem: RepoVo): Boolean =
                oldItem.name == newItem.name

        override fun getChangePayload(oldItem: RepoVo, newItem: RepoVo): Any? {
           return newItem
        }
    }
}