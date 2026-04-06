package com.example.carsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carsapp.databinding.PaymentBinding

class Payment : Fragment() {

    private var _binding: PaymentBinding? = null
    private val binding get() = _binding!!

    private var baseCarPrice: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PaymentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val imageRes = it.getInt("imageRes")
            val title = it.getString("title")
            val priceStr = it.getString("price") ?: "$0"

            binding.carImage.setImageResource(imageRes)
            binding.carName.text = title
            binding.carPrice.text = priceStr

            // ტექსტიდან ვიღებთ მხოლოდ ციფრებს და ვაქცევთ Int-ად
            baseCarPrice = priceStr.replace(Regex("[^\\d]"), "").toIntOrNull() ?: 0

            // საწყისი ჯამური ფასის გამოჩენა
            updateTotal()
        }

        // Standard Shipping ლოგიკა
        binding.rbStandard.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rbExpress.isChecked = false
                updateTotal()
            }
        }

        // Express Shipping ლოგიკა
        binding.rbExpress.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.rbStandard.isChecked = false
                updateTotal()
            }
        }

        // ფასდაკლების (Discount) Checkbox-ის ლოგიკა
        binding.discountCheckBox.setOnCheckedChangeListener { _, _ ->
            updateTotal()
        }

        binding.btnPay.setOnClickListener {
            openSuccessFragment()
        }
    }

    /**
     * ფუნქცია, რომელიც ითვლის საბოლოო ფასს ყველა პირობის გათვალისწინებით
     */
    private fun updateTotal() {
        var carValue = baseCarPrice.toDouble()

        if (binding.discountCheckBox.isChecked) {
            carValue -= (carValue * 0.05)//აქ სპეციალურად მხოლოდ მანქანის ფასზე გავაკეთე დისქაუნთის ლოგიკა, აითემის გაყოლებაზე რაკი ეწერა
        }

        val shippingCost = if (binding.rbExpress.isChecked) 1700.0 else 0.0

        val finalTotal = carValue + shippingCost

        binding.totalPriceText.text = "$${finalTotal.toInt()}"
    }

    /**
     *  ახალ Fragment-ზე გადასვლა
     */
    private fun openSuccessFragment() {
        val fragment = Payment() // შექმენი ეს Fragment

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment) // მთლიანად ცვლის
            .addToBackStack(null) // თუ გინდა უკან დაბრუნება
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}