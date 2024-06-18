package com.example.crud_34b.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_34b.R
import com.example.crud_34b.adapter.ProductAdapter
import com.example.crud_34b.databinding.ActivityDashBoardBinding
import com.example.crud_34b.repository.ProductRepositoryImpl
import com.example.crud_34b.viewmodel.ProductViewModel

class DashBoardActivity : AppCompatActivity() {
    lateinit var dashBoardBinding: ActivityDashBoardBinding

    lateinit var productAdapter: ProductAdapter

    lateinit var productViewModel : ProductViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  
        dashBoardBinding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(dashBoardBinding.root)

        val repo = ProductRepositoryImpl()
        productViewModel = ProductViewModel(repo)
        productViewModel.fetchProduct(this@DashBoardActivity)

        productAdapter = ProductAdapter(this@DashBoardActivity, ArrayList())

        dashBoardBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashBoardActivity)
            adapter = productAdapter
        }

        productViewModel.loadingState.observe(this) { loading->
            if (loading) {
                dashBoardBinding.progressBar3.visibility = View.VISIBLE
            } else {
                dashBoardBinding.progressBar3.visibility = View.GONE
            }
        }

        productViewModel.productList.observe(this) { products ->
            products?.let {
                productAdapter.updateData(it)
            }

        }

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
                var imageName = productAdapter.getImageName(viewHolder.adapterPosition)

               productViewModel.deleteData(id) {
                   success, message ->
                   if(success) {
                       Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                       productViewModel.deleteImage(imageName) {
                               success, message ->
                       }
                   } else{
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                       }

               }
           }
       }).attachToRecyclerView(dashBoardBinding.recyclerView)

        dashBoardBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@DashBoardActivity, AddProductActivity::class.java)
            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}