package com.example.crud_34b.viewmodel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud_34b.model.ProductModel
import com.example.crud_34b.repository.ProductRepository

class ProductViewModel(val repository: ProductRepository) : ViewModel() {
    fun uploadImage(imageName: String, imageUrl: Uri, callback: (Boolean, String?) -> Unit) {
        repository.uploadImage(imageName,imageUrl) { success, imageUrl ->
            callback(success, imageUrl)
        }
    }


    fun addProduct(productModel: ProductModel, callback: (Boolean, String?,) -> Unit) {
        repository.addProduct(productModel, callback)
    }

    private var _productList = MutableLiveData<List<ProductModel>?>()

    var productList = MutableLiveData<List<ProductModel>?>()
        get() = _productList

     var _loadingState = MutableLiveData<Boolean>()

    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState



    fun fetchProduct(context: Context) {
        _loadingState.value = true
        repository.getAllProduct {products, success, message ->

            if (success && products != null) {
                _loadingState.value = false
                _productList.value = products
            }else{
                message.let {
                    Toast.makeText(context,it.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun updateProduct(id:String,data: MutableMap<String, Any>?,callback: (Boolean, String?) -> Unit) {
        repository.updateProduct(id, data, callback)
    }

    fun deleteData(id:String, callback: (Boolean, String?) -> Unit) {
        repository.deleteData(id,callback)
    }

    fun deleteImage(imageName:String, callback: (Boolean, String?) -> Unit) {
        repository.deleteImage(imageName, callback)
    }

}