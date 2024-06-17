package com.example.crud_34b.repository

import android.net.Uri
import android.util.Log
import com.example.crud_34b.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProductRepositoryImpl : ProductRepository {
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("products")

    var firebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = firebaseStorage.reference.child("products")

    override fun uploadImage(imageName: String, imageUrl: Uri, callback: (Boolean, String?) -> Unit) {
       // val imageName = UUID.randomUUID().toString()  // generates random id
        var imageReference = storageRef.child(imageName) // it only create a ref for image. actual image has not arrived here yet

        imageUrl?.let { url->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {downloadUrl->
                    var imagesUrl = downloadUrl.toString()
                    callback(true, imagesUrl)
                }
            }.addOnFailureListener {
                callback(false, "")
            }
        }
    }

    override fun addProduct(
        productModel: ProductModel,
        callback: (Boolean, String?) -> Unit
    ) {
        var id = ref.push().key.toString()

        productModel.id = id

        ref.child(id).setValue(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                    callback(true, "Data uploaded successfully")
            } else {
                    callback(false, "Unable to upload data")
            }
        }    }

    override fun getAllProduct(callback: (List<ProductModel>?, Boolean, String?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var productList = mutableListOf<ProductModel>()

                for(eachData in snapshot.children){
                    var product = eachData.getValue(ProductModel::class.java)
                    if(product!=null){
                        Log.d("data from firebase",product.name)
                        Log.d("data from firebase",product.description)
                        Log.d("data from firebase",product.price.toString())

                        productList.add(product)
                    }
                }
                callback(productList,true,"DATA FETCHED")
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Unable to fetch ${error.message}")
            }
        })
    }

    override fun updateProduct(id: String, data:MutableMap<String, Any>?, callback: (Boolean, String?) -> Unit) {
        data?.let {
            ref.child(id).updateChildren(it).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Your data has been updated")
                } else {
                    callback(false, "Unable to update data")
                }
            }
        }
    }

    override fun deleteData(id: String, callback: (Boolean, String?) -> Unit) {
        ref.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Image deleted")
            } else {
                callback(false, "Unable to delete image")
            }
        }
    }

    override fun deleteImage(imageName: String, callback: (Boolean, String?) -> Unit) {
        ref.child("products").removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Image deleted")
            } else {
                callback(false, "Unable to delete image")
            }
        }
    }



}