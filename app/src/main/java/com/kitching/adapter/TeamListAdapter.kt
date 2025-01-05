package com.kitching.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.MainActivity
import com.kitching.common.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.TeamDTO
import com.kitching.databinding.ItemTeamlistBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class TeamListAdapter(private val lifecycleScope: LifecycleCoroutineScope, private val context: Context): ListAdapter<TeamDTO, TeamListAdapter.ViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemTeamlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TeamDTO>() {
            override fun areItemsTheSame(
                oldItem: TeamDTO,
                newItem: TeamDTO,
            ): Boolean {
                return oldItem.teamId == newItem.teamId
            }

            override fun areContentsTheSame(
                oldItem: TeamDTO,
                newItem: TeamDTO,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class ViewHolder(private val binding: ItemTeamlistBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(team: TeamDTO) {
            with(binding) {
                teamNameTV.text = team.teamName
                teamListCV.clicks().throttleFirst().onEach {
                    lifecycleScope.launch {
                        PreferencesDataSource(context).saveTeamId(team.teamId)

                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }
}