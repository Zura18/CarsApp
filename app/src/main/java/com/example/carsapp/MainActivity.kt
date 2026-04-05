package com.example.carsapp

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.carsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back ღილაკის კონტროლი
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    binding.fragmentContainer.visibility = View.GONE
                } else {
                    finish()
                }
            }
        })

        // მანქანების სია, რომლებზეც კლიკი უნდა მუშაობდეს
        val carViews = listOf(
            binding.bmwF80,
            binding.mercedesCla,
            binding.porsche911,
            binding.ferari488
        )

        val clickListener = View.OnClickListener { view ->
            // ვიღებთ მონაცემებს XML-ში გაწერილი tag-იდან
            val tagData = view.tag?.toString()?.split("|")

            if (tagData != null && tagData.size == 4) {
                val imageFileName = tagData[1]
                val title = tagData[2]
                val price = tagData[3]

                // სურათის სახელით (String) ვიღებთ მის ID-ს (Int)
                val imageRes = resources.getIdentifier(imageFileName, "drawable", packageName)

                // გადავდივართ გადახდის გვერდზე
                openPaymentFragment(imageRes, title, price)
            }
        }

        // მივაბათ ლისენერი ყველა მანქანას
        carViews.forEach { it.setOnClickListener(clickListener) }
    }

    private fun openPaymentFragment(imageRes: Int, title: String, price: String) {
        //  გამოვაჩინოთ კონტეინერი (რომელიც XML-ში GONE უნდა იყოს)
        binding.fragmentContainer.visibility = View.VISIBLE

        val bundle = Bundle().apply {
            putInt("imageRes", imageRes)
            putString("title", title)
            putString("price", price)
        }

        val fragment = Payment()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // საშუალებას გვაძლევს "Back" ღილაკით დავბრუნდეთ სიაში
            .commit()
    }
}