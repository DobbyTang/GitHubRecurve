package com.tangpj.repository.ui.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tangpj.recurve.recyclerview.adapter.ModulesAdapter
import com.tangpj.recurve.recyclerview.creator.ItemCreator
import com.tangpj.recurve.recyclerview.creator.RecurveViewHolder
import com.tangpj.repository.databinding.ItemRepositoryBinding
import com.tangpj.repository.vo.Repository
import javax.inject.Inject

class RepositoryCreator constructor(adapter: ModulesAdapter, creatorType: Int)
    : ItemCreator<Repository, ItemRepositoryBinding>(adapter, creatorType) {

    @Inject constructor(adapter: ModulesAdapter): this(adapter, 0)

    override fun onBindItemView(itemHolder: RecurveViewHolder<ItemRepositoryBinding>?,
                                e: Repository?, inCreatorPosition: Int) {
        itemHolder?.let {
            it.binding.repositor = e
        }
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecurveViewHolder<*> =
            RecurveViewHolder(
                    ItemRepositoryBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))

}