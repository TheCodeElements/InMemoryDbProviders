package de.codeelements.bhfliveutil.ui.home

import android.database.ContentObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.codeelements.bhfliveutil.databinding.FragmentHomeBinding
import de.codeelements.bhfliveutil.provider.InMemoryDbProviderInterface
import de.codeelements.utilities.contentprovider.InMemoryDbProviderViewModel
import kotlin.concurrent.fixedRateTimer


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var dataChangeObserver : ContentObserver? = null

    private val onTrackingChangedListener = CompoundButton.OnCheckedChangeListener { switch, isChecked ->
        InMemoryDbProviderInterface.setValue(requireContext(), "#tracking", if(isChecked) "true" else "false")
    }

    private val onTippsChangedListener = CompoundButton.OnCheckedChangeListener { switch, isChecked ->
        InMemoryDbProviderInterface.setValue(requireContext(), "#tipps", if(isChecked) "true" else "false")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createDataChangeObserver()
    }

    private fun createDataChangeObserver() {
        dataChangeObserver =
            InMemoryDbProviderInterface.registerValueChangeObserver(requireContext()) { key, value ->

                when (key) {
                    "versioncode" -> binding.versionCode.text = value
                    "versionname" -> binding.versionName.text = value
                    "trackingstate" -> {
                        binding.tracking.also {
                            it.setOnCheckedChangeListener(null);
                            it.isChecked = value == "CONSENTED"
                            it.setOnCheckedChangeListener(onTrackingChangedListener);
                        }
                    }

                    "tippsstate" -> {
                        binding.tipps.also {
                            it.setOnCheckedChangeListener(null);
                            it.isChecked = value == "true"
                            it.setOnCheckedChangeListener(onTippsChangedListener);
                        }
                    }

                    else -> {}
                }

            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inMemoryDbProviderViewModel =
            ViewModelProvider(this)[InMemoryDbProviderViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        inMemoryDbProviderViewModel.providerValid.observe(viewLifecycleOwner) {
            it?.also {itValid ->
                binding.providerNotFound.visibility = if(itValid) View.GONE else View.VISIBLE

                if(itValid)
                    refresh()
            }
        }


        binding.getVersionCode.setOnClickListener {
            InMemoryDbProviderInterface.setValue(requireContext(), "#getversioncode", "get")
        }

        binding.getVersionName.setOnClickListener {
            InMemoryDbProviderInterface.setValue(requireContext(), "#getversionname", "get")
        }

        binding.tracking.setOnCheckedChangeListener(onTrackingChangedListener)
        binding.tipps.setOnCheckedChangeListener(onTippsChangedListener)


        val timer = fixedRateTimer("Timer", true, 1000, 5000) {

            val providerIsValid = InMemoryDbProviderInterface.isContentProviderValid(requireContext())

            if(providerIsValid) {
                if(dataChangeObserver==null)
                    createDataChangeObserver()
            }
            else {
                dataChangeObserver?.also {
                    InMemoryDbProviderInterface.unregisterValueChangeObserver(
                        requireContext(),
                        it
                    )
                    dataChangeObserver=null
                }
            }

            inMemoryDbProviderViewModel.setProviderValid(providerIsValid)
         }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun refresh() {
        InMemoryDbProviderInterface.setValue(requireContext(), "#gettrackingstate")
        InMemoryDbProviderInterface.setValue(requireContext(), "#gettippsstate")
        InMemoryDbProviderInterface.setValue(requireContext(), "#getversioncode", "get")
        InMemoryDbProviderInterface.setValue(requireContext(), "#getversionname", "get")
    }

}