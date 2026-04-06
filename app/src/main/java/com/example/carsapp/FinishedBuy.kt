package com.example.carsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carsapp.databinding.FragmentFinisedBuyBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val carViews = listOf<View>(
            view.findViewById<View>(R.id.bmwF80),
            view.findViewById<View>(R.id.mercedesCla),
            view.findViewById<View>(R.id.porsche911),
            view.findViewById<View>(R.id.ferari488)
        )

        val clickListener = View.OnClickListener { v ->
            val tagData = v.tag.toString().split("|")

            val imageRes = resources.getIdentifier(tagData[1], "drawable", requireContext().packageName)
            val title = tagData[2]
            val price = tagData[3]

            val bundle = Bundle().apply {
                putInt("imageRes", imageRes)
                putString("title", title)
                putString("price", price)
            }

            val fragment = Payment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        carViews.forEach { it.setOnClickListener(clickListener) }
    }
}