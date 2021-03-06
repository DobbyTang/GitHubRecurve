package com.tangpj.repository.ui.detail.viewer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.recurve.core.resource.Resource
import com.tangpj.repository.repository.FileRepository
import com.tangpj.repository.valueObject.query.GitObjectQuery
import com.tangpj.repository.entity.domain.file.FileContent
import javax.inject.Inject

class ViewerViewModel @Inject constructor(private val fileRepository: FileRepository) : ViewModel(){

    private val _gitObjectQuery = MutableLiveData<GitObjectQuery>()

    val fileContent: LiveData<Resource<FileContent>>
            = Transformations.switchMap(_gitObjectQuery){
        fileRepository.loadFileContent(it)
    }

    fun loadFileContentByQuery(gitObjectQuery: GitObjectQuery){
        val update = GitObjectQuery(gitObjectQuery.repoDetailQuery, gitObjectQuery.branch, gitObjectQuery.path)
        if (update == _gitObjectQuery.value){
            return
        }
        _gitObjectQuery.value = update
    }

    fun retry(){
        val gitObjectQuery = _gitObjectQuery.value
        gitObjectQuery ?: return
        _gitObjectQuery.value = GitObjectQuery(gitObjectQuery.repoDetailQuery, gitObjectQuery.branch, gitObjectQuery.path)
    }

}