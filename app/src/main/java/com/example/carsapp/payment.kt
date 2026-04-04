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
            // გადახდის ლოგიკა
        }
    }

    /**
     * ფუნქცია, რომელიც ითვლის საბოლოო ფასს ყველა პირობის გათვალისწინებით
     */
    private fun updateTotal() {
        // 1. ვიღებთ მანქანის სუფთა ფასს (მაგ: 100,000)
        var carValue = baseCarPrice.toDouble()

        // 2. თუ ფასდაკლება ჩართულია, ვაკლებთ 5%-ს მხოლოდ მანქანის ფასს
        // 100,000 * 0.05 = 5,000 -> 100,000 - 5,000 = 95,000
        if (binding.discountCheckBox.isChecked) {
            carValue -= (carValue * 0.05)
        }

        // 3. ვიღებთ მიწოდების ფასს (ცალკე ცვლადად)
        val shippingCost = if (binding.rbExpress.isChecked) 1700.0 else 0.0

        // 4. საბოლოო ჯამი არის (ფასდაკლებული მანქანა) + (მიწოდება)
        val finalTotal = carValue + shippingCost

        binding.totalPriceText.text = "$${finalTotal.toInt()}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}