package com.kitchingapp.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kitchingapp.R
import com.kitchingapp.common.throttleFirst
import com.kitchingapp.data.database.dto.DepartmentDTO
import com.kitchingapp.data.database.dto.TeamDTO
import com.kitchingapp.data.database.repository.LocalRepository
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.LocalType
import com.kitchingapp.data.database.usecase.LocalTypeUseCase
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import com.kitchingapp.databinding.ItemTeamlistBinding
import com.kitchingapp.domain.entities.Team
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks

class TeamAdapter(private val drawer: DrawerLayout, private val context: Context, private val lifecycleScope: LifecycleCoroutineScope, private val navController: NavController): ListAdapter<TeamDTO, TeamAdapter.TeamViewHolder>(diffUtil) {

    private val localRepository: LocalRepository by lazy {
        LocalTypeUseCase(context).selectLocalType(LocalType.DATASTORE)
    }

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
                        localRepository.saveTeamId(team.teamId)
                        drawer.closeDrawers()
                    }
                }.launchIn(lifecycleScope)
            }
        }
    }
}