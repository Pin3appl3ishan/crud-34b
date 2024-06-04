package com.example.crud_34b

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_34b.adapter.ProductAdapter
import com.example.crud_34b.databinding.ActivityDashBoardBinding
import com.example.crud_34b.model.ProductMModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoardActivity : AppCompatActivity() {
    lateinit var dashBoardBinding: ActivityDashBoardBinding
    var database : FirebaseDatabase = FirebaseDatabase.getInstance()
     var productList = ArrayList<ProductMModel>()
    lateinit var productAdapter: ProductAdapter

    var ref : DatabaseReference = database.reference.child("products")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  
        dashBoardBinding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(dashBoardBinding.root)
        productAdapter = ProductAdapter(this@DashBoardActivity, productList)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

           /* add: setValue
            update: updateChildren
            remove: removeValue*/


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = productAdapter.getProductID(viewHolder.adapterPosition)
                ref.child(id).removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(applicationContext, "Data deleted",
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, it.exception?.message,
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }).attachToRecyclerView(dashBoardBinding.recyclerView)

        dashBoardBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@DashBoardActivity, AddProductActivity::class.java)
            startActivity(intent)
        }

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()
                Log.d("snapshot data ",snapshot.children.toString())
                for(eachData in snapshot.children){

                    var product = eachData.getValue(ProductMModel::class.java)
                    if(product!=null){
                        Log.d("data from firebase",product.name)
                        Log.d("data from firebase",product.description)
                        Log.d("data from firebase",product.price.toString())

                        productList.add(product)
                    }
                }

                dashBoardBinding.recyclerView.layoutManager =
                    LinearLayoutManager(this@DashBoardActivity)

                dashBoardBinding.recyclerView.adapter = productAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}