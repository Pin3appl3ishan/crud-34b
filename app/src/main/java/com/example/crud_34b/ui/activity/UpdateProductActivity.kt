package com.example.crud_34b.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34b.R
import com.example.crud_34b.databinding.ActivityUpdateProductBinding
import com.example.crud_34b.model.ProductModel
import com.example.crud_34b.repository.ProductRepositoryImpl
import com.example.crud_34b.utils.ImageUtils
import com.example.crud_34b.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class UpdateProductActivity : AppCompatActivity() {
    lateinit var updateProductBinding: ActivityUpdateProductBinding

    var id = ""
    var imagename = ""

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var imageUri : Uri? = null

    //create instance of view model
    lateinit var productViewModel: ProductViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    lateinit var imageUtils: ImageUtils


    fun uploadImage() {
        imageUri?.let{
            productViewModel.uploadImage(imagename, it) {
                success, imageUrl ->
                if (success) {
                    updateProduct(imageUrl.toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        updateProductBinding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(updateProductBinding.root)

        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity {
            imageUri = it
            Picasso.get().load(it).into(updateProductBinding.updateImageBrowse)
        }

        var product: ProductModel? = intent.getParcelableExtra("product")
        id = product?.id.toString()
        imagename = product?.imageName.toString()

        updateProductBinding.editTextProductNameUpdate.setText(product?.name)
        updateProductBinding.editTextProductPriceUpdate.setText(product?.price.toString())
        updateProductBinding.editTextProductDescUpdate.setText(product?.description)

        Picasso.get().load(product?.url).into(updateProductBinding.updateImageBrowse)

        updateProductBinding.btnPostUpdate.setOnClickListener {
            uploadImage()
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun updateProduct(url: String) {
        var updatedName: String =updateProductBinding.editTextProductNameUpdate.text.toString()
        var updatedPrice: Int =updateProductBinding.editTextProductPriceUpdate.text.toString().toInt()
        var updatedDesc: String =updateProductBinding.editTextProductDescUpdate.text.toString()

        var data = mutableMapOf<String,Any>()
        data["name"] = updatedName
        data["price"] = updatedPrice
        data["description"] = updatedDesc
        data["url"] = url

        productViewModel.updateProduct(id, data) {
            success, message ->
            if (success) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG). show()
            }
        }
    }
}