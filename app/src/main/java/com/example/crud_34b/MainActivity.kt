package com.example.crud_34b

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_34b.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    var auth : FirebaseAuth = FirebaseAuth.getInstance()  // establish connection with firebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.btnRegister.setOnClickListener {
            var email : String = mainBinding.userName.text.toString()
            var password: String = mainBinding.userPass.text.toString()

            auth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(applicationContext,
                                "Registration success", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(applicationContext,
                                it.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
        }

    }
}