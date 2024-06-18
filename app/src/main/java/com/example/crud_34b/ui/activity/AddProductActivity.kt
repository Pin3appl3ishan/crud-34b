package com.example.crud_34b.ui.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_34b.databinding.ActivityAddProductBinding
import com.example.crud_34b.model.ProductModel
import com.example.crud_34b.repository.ProductRepositoryImpl
import com.example.crud_34b.utils.ImageUtils
import com.example.crud_34b.utils.LoadingUtils
import com.example.crud_34b.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class AddProductActivity : AppCompatActivity() {
    lateinit var addProductBinding: ActivityAddProductBinding

    var imageUri : Uri? = null

    lateinit var imageUtils: ImageUtils
    lateinit var productViewModel: ProductViewModel

    lateinit var loadingUtils: LoadingUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addProductBinding = ActivityAddProductBinding.inflate((layoutInflater))
        setContentView(addProductBinding.root)

        loadingUtils = LoadingUtils(this)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity{url ->
            url.let{
                imageUri = it
                Picasso.get().load(it).into(addProductBinding.imageBrowse)
            }
        }

        // there is repository in parameter of ProductViewModel
        var repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)

        addProductBinding.imageBrowse.setOnClickListener{
           imageUtils.launchGallery(this@AddProductActivity)
        }

        addProductBinding.btnPost.setOnClickListener {
      uploadImage()
        }
    }

    fun uploadImage() {
        loadingUtils.showLoading()
         val imageName = UUID.randomUUID().toString()
        imageUri?.let {
            productViewModel.uploadImage(imageName,it) {
                success, imageUrl ->
                if(success) {
                    addProduct(imageUrl.toString(), imageName.toString())
                }
            }
        }
    }

    fun addProduct(url:String, imageName: String) {
        var name: String =addProductBinding.editTextProductName.text.toString()
        var price: Int =addProductBinding.editTextProductPrice.text.toString().toInt()
        var desc: String =addProductBinding.editTextProductDesc.text.toString()

        var data = ProductModel("",name,price,desc,url,imageName)
        productViewModel.addProduct(data) {
            success,message->
            if (success) {
                loadingUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            } else {
                loadingUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        }
    }

}