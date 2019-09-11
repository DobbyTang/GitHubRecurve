package com.tangpj.repository.ui.creator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tangpj.github.ui.creator.BaseDiffUtil
import com.tangpj.github.ui.creator.EntryDiffUtil
import com.tangpj.paging.PagedItemCreator
import com.tangpj.repository.databinding.ItemCommitBinding
import com.tangpj.repository.vo.CommitVo
import javax.inject.Inject

class CommitCreator @Inject constructor(
        diffUtil: CommitDiffUtil)
    : PagedItemCreator<CommitVo, ItemCommitBinding>(0, diffUtil){
    override fun onBindItemView(binding: ItemCommitBinding, e: CommitVo, inCreatorPosition: Int) {
        binding.commit = e
    }

    override fun onCreateItemBinding(parent: ViewGroup, viewType: Int): ItemCommitBinding  =
            ItemCommitBinding.inflate(LayoutInflater.from(parent.context), parent, false)


}

class CommitDiffUtil @Inject constructor() : BaseDiffUtil<CommitVo>() {
    override fun areItemsTheSame(oldItem: CommitVo, newItem: CommitVo): Boolean =
            oldItem.commit.id == newItem.commit.id
}