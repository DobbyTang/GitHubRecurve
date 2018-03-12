/*
 * Copyright (C) 2018 Tang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tang.com.recurve.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup


/**
 * Created by tang on 2018/3/10.
 */
abstract class ModulesAdapter
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var creatorList: MutableList<Creator<*,*>>
            = mutableListOf()


    fun setCreator(creatorList: MutableList<Creator<*,*>>){
        val creatorMap = creatorList.groupBy { it.getItemViewType() }
        for (entry in creatorMap) {
            if (entry.value.size > 1){
                throw IllegalArgumentException("Creator ItemViewType can't not equal")
            }
        }
        this.creatorList = creatorList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val viewTypeList = creatorList.groupBy { it.getItemViewType() }[viewType]
        return viewTypeList?.first()?.onCreateItemView(parent)
                ?: creatorList.first().onCreateItemView(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val creator = creatorList[getCreatorIndex(position)]
        val modulesStartPosition = getModulesStartPosition(creator)
        val inCreatorPosition = position - modulesStartPosition
        creator.onBindItemView(holder,inCreatorPosition)
    }

    override fun getItemCount(): Int  = creatorList.sumBy { it -> it.getItemCount() }

    override fun getItemViewType(position: Int): Int {
        var sum = 0
        creatorList.forEach {
            sum += it.getItemCount()
            if (sum > position)
                return@getItemViewType it.getItemViewType()
        }
        return -1
    }

    fun notifyModulesItemSetChange(creator: Creator<*,*>){
        notifyModulesItemRangeChange(creator ,0,creator.getItemCount() - 1)
    }

    fun notifyModulesItemRangeChange(creator: Creator<*,*>, aimsStartPosition: Int, aimsEndPosition: Int){
        val startPosition = getModulesStartPosition(creator)
        notifyItemRangeChanged(startPosition + aimsStartPosition,
                startPosition + aimsEndPosition)
    }

    fun notifyModulesItemChanged(creator: Creator<*,*>, aimsPosition: Int){
        val startPosition = getModulesStartPosition(creator)
        notifyItemChanged(startPosition + aimsPosition)
    }

    fun notifyModulesItemInserted(creator: Creator<*,*>, aimsPosition: Int){
        val startPosition = getModulesStartPosition(creator)
        notifyItemInserted(startPosition + aimsPosition)
    }

    fun notifyModulesItemRemoved(creator: Creator<*,*>, aimsPosition: Int){
        val startPosition = getModulesStartPosition(creator)
        notifyItemRemoved(startPosition + aimsPosition)
    }

    private fun getModulesStartPosition(creator: Creator<*,*>): Int{
        val creatorPosition = creatorList.indexOf(creator)
        var startPosition = 0
        creatorList.forEachIndexed { index, iCreator ->
            if( index == creatorPosition) return@forEachIndexed
            else startPosition += iCreator.getItemCount()
        }
        return startPosition
    }

    private fun getCreatorIndex(position: Int): Int{
        var startPosition = 0
        var resultIndex = 0
        creatorList.forEachIndexed { index, iCreator ->
            if( startPosition < position) startPosition += iCreator.getItemCount()
            else {
                resultIndex = index
                return@forEachIndexed
            }
        }
        return resultIndex
    }


}


