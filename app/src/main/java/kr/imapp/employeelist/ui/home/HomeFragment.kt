package kr.imapp.employeelist.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kr.imapp.employeelist.R
import kr.imapp.employeelist.data.EmployeeApiFactory
import kr.imapp.employeelist.data.EmployeeRepositoryImpl
import kr.imapp.employeelist.databinding.FragmentHomeBinding
import kr.imapp.employeelist.util.viewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var menuName: MenuItem
    private lateinit var menuTeam: MenuItem

    private val viewModel: HomeViewModel by viewModels {
        viewModelFactory { savedState ->
            HomeViewModel(
                EmployeeRepositoryImpl(
                    EmployeeApiFactory.create()
                ),
                savedState
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = EmployeeListAdapter()
        recyclerView.adapter = adapter
        pullToRefresh.setOnRefreshListener {
            viewModel.onPullToRefresh()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeViewState.Loading -> {
                    pullToRefresh.isRefreshing = true
                    recyclerView.visibility = View.GONE
                    loadingError.visibility = View.GONE
                }
                is HomeViewState.Success -> {
                    pullToRefresh.isRefreshing = false
                    loadingError.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    when (state.sortType) {
                        SortType.BY_NAME -> menuName.isChecked = true
                        SortType.BY_TEAM -> menuTeam.isChecked = true
                    }
                    adapter.submitList(state.list)
                }
                is HomeViewState.Error -> {
                    pullToRefresh.isRefreshing = false
                    loadingError.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    loadingError.text = getString(R.string.loading_error,
                        state.errorMessage ?: getString(state.errorString)
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        menuName = menu.findItem(R.id.by_name)
        menuTeam = menu.findItem(R.id.by_team)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.by_name -> {
                viewModel.onSortChanged(SortType.BY_NAME)
            }
            R.id.by_team -> {
                viewModel.onSortChanged(SortType.BY_TEAM)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
