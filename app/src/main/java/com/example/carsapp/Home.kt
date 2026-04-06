package com.example.carsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carsapp.databinding.FragmentHomeBinding

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val carViews = listOf(
            binding.bmwF80,
            binding.mercedesCla,
            binding.porsche911,
            binding.ferari488
        )

        val clickListener = View.OnClickListener { view ->
            val tagData = view.tag?.toString()?.split("|")

            if (tagData != null && tagData.size == 4) {
                val imageRes = resources.getIdentifier(
                    tagData[1],
                    "drawable",
                    requireContext().packageName
                )

                val title = tagData[2]
                val price = tagData[3]

                openPaymentFragment(imageRes, title, price)
            }
        }

        carViews.forEach { it.setOnClickListener(clickListener) }
    }

    private fun openPaymentFragment(imageRes: Int, title: String, price: String) {
        val bundle = Bundle().apply {
            putInt("imageRes", imageRes)
            putString("title", title)
            putString("price", price)
        }

        val fragment = Payment()
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}