package com.example.crud_34b

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.databinding.ActivityAddProductBinding
import com.example.crud_34b.databinding.ActivityUpdateProductBinding
import com.example.crud_34b.model.ProductMModel
import com.google.firebase.database.FirebaseDatabase

class UpdateProductActivity : AppCompatActivity() {
    lateinit var updateProductBinding: ActivityUpdateProductBinding
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")

    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)

        var product: ProductMModel? = intent.getParcelableExtra("product")
        id = product?.id.toString()

        updateProductBinding.editTextProductNameUpdate.setText(product?.name)
        updateProductBinding.editTextProductPriceUpdate.setText(product?.price.toString())
        updateProductBinding.editTextProductDescUpdate.setText(product?.description)

        updateProductBinding.btnPostUpdate.setOnClickListener {
            var updatedName: String =updateProductBinding.editTextProductNameUpdate.text.toString()
            var updatedPrice: Int =updateProductBinding.editTextProductPriceUpdate.text.toString().toInt()
            var updatedDesc: String =updateProductBinding.editTextProductDescUpdate.text.toString()

            var data = mutableMapOf<String,Any>()
            data["name"] = updatedName
            data["price"] = updatedPrice
            data["description"] = updatedDesc

            ref.child(id).updateChildren(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Data added",
                        Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(applicationContext, it.exception?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}