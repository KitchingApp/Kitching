package com.kitching.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitching.common.throttleFirst
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.dto.TeamDTO
import com.kitching.databinding.ItemTeamlistBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class TeamAdapter(private val drawer: DrawerLayout, private val context: Context, private val lifecycleScope: LifecycleCoroutineScope, private val navController: NavController): ListAdapter<TeamDTO, TeamAdapter.TeamViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeamViewHolder {
        val binding = ItemTeamlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TeamViewHolder,
        position: Int
    ) {
        holder.bindTeam(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TeamDTO>() {
            override fun areItemsTheSame(
                oldItem: TeamDTO,
                newItem: TeamDTO
            ): Boolean {
                return oldItem.teamId == newItem.teamId
            }

            override fun areContentsTheSame(
                oldItem: TeamDTO,
                newItem: TeamDTO
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class TeamViewHolder(val binding: ItemTeamlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindTeam(team: TeamDTO) {
            with(binding) {
                teamNameTV.text = team.teamName
                teamListCV.clicks().throttleFirst().onEach {
                    lifecycleScope.launch {
                        PreferencesDataSource(context).saveTeamId(team.teamId)
                        drawer.closeDrawers()
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }
}