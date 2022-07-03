package nz.co.warehouseandroidtest.ui.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import nz.co.warehouseandroidtest.BarScanActivity
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.databinding.FragmentHomeBinding
import nz.co.warehouseandroidtest.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvScanBarcode.setOnClickListener {
            val intent = Intent()
            intent.setClass(requireActivity(), BarScanActivity::class.java)
            startActivity(intent)
        }
        binding.tvSearch.setOnClickListener{ v ->
            Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_ListFragment)
        }
    }
}