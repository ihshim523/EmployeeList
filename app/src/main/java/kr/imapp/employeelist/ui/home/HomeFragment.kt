package kr.imapp.employeelist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = EmployeeListAdapter()
        recyclerView.adapter = adapter
        binding.pullToRefresh.setOnRefreshListener {
            viewModel.onPullToRefresh()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeViewState.Loading -> {
                    loading.visibility = View.VISIBLE
                    loadingError.visibility = View.GONE
                }
                is HomeViewState.Success -> {
                    loading.visibility = View.GONE
                    loadingError.visibility = View.GONE
                    adapter.submitList(state.list)
                }
                is HomeViewState.Error -> {
                    loading.visibility = View.GONE
                    loadingError.visibility = View.VISIBLE
                    loadingError.text = getString(R.string.loading_error, state.exception.localizedMessage)
                }
            }
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
