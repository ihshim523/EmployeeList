package kr.imapp.employeelist.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kr.imapp.employeelist.R
import kr.imapp.employeelist.data.Employee
import kr.imapp.employeelist.databinding.ViewholderEmployeeBinding

class EmployeeListAdapter : ListAdapter<Employee, EmployeeListAdapter.HomeViewHolder>(
    diffItemCallback
) {
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val employee = getItem(position) ?: return
        holder.bind(employee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.viewholder_employee, parent, false)
        return HomeViewHolder(view)
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ViewholderEmployeeBinding.bind(itemView)

        fun bind(employee: Employee) {
            binding.fullName.text = employee.fullName
            binding.team.text = employee.team
            Picasso.get().load(employee.smallPhotoUrl).into(binding.photo);
        }
    }

    companion object {
        private val diffItemCallback = object : DiffUtil.ItemCallback<Employee>() {
            override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean = oldItem.uuid == newItem.uuid

            override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean = oldItem.uuid == newItem.uuid
        }
    }
}
