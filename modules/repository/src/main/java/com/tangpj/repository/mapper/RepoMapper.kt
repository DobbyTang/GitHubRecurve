package com.tangpj.repository.mapper

import com.tangpj.github.domain.PageInfo
import com.tangpj.repository.StartRepositoriesQuery
import com.tangpj.repository.WatchRepositoriesQuery
import com.tangpj.repository.valueObject.result.StarRepoResult
import com.tangpj.repository.fragment.PageInfoDto
import com.tangpj.repository.fragment.RepoDto
import com.tangpj.repository.valueObject.Owner
import com.tangpj.repository.vo.Repo

fun RepoDto.mapperToRepo(): Repo{
    val languages = languages?.nodes
    val languageDto = if (languages != null && languages.size > 0){
        languages[0].fragments.languageDto
    }else{
        null
    }
    val localOwner = Owner(
            id = owner.id,
            login = owner.login,
            avatarUrl = owner.avatarUrl)
    return Repo(
            id = id,
            name = name,
            owner = localOwner,
            fullName = "${owner.login}/$name",
            language = languageDto?.name ?: "unknown",
            languageColor = languageDto?.color ?: "unknown",
            description = description ?: "",
            stars = stargazers.totalCount,
            forks = forks.totalCount)
}

/**
 * 把Apollo框架生成的PageInfo转换成本地的[PageInfo]
 *
 * @method: mapperToLocalPageInfo
 * @author create by Tang
 * @date 2019-05-15 21:53
 */
fun PageInfoDto.mapperToLocalPageInfo() = PageInfo(
        hasNextPage = isHasNextPage,
        hasPreviousPage = isHasPreviousPage,
        startCursor = startCursor ?: "",
        endCursor = endCursor ?: "")

/**
 * [starRepoResultListener] 如果不为空，则生成并回调[StarRepoResult]的值对象
 * 这样设计的目的是减少数组遍历，提高性能
 *
 * @method: mapperToRepoVoList
 * @author create by Tang
 * @date 2019-05-15 21:47
 *
 */
fun StartRepositoriesQuery.Data.mapperToRepoVoList() : List<Repo>{
    val edges = this.user?.starredRepositories?.edges
    edges?.size ?: return mutableListOf()
    val repoVoList = edges.map { edge ->
        val repoDto = edge.node.fragments.repoDto
        repoDto.mapperToRepo()
    }
    return repoVoList

}

fun StartRepositoriesQuery.Data.getPageInfo() : PageInfo? =
        user?.starredRepositories?.pageInfo?.fragments?.pageInfoDto?.mapperToLocalPageInfo()

fun WatchRepositoriesQuery.Data.getRepoDtoList() =
        this.user?.watching?.nodes?.map {
            it.fragments.repoDto
        }