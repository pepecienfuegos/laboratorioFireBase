package com.example.laboratoriofirebase.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.laboratoriofirebase.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        welcome()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun welcome(){
        val user = auth.currentUser
        user?.let {
            val email = user.email
            val dataUser = email?.let { it1 -> db.collection("users").document(it1) }
            dataUser?.get()
                ?.addOnSuccessListener {
                    if( it != null){
                        binding.textHome.text = "La sesión fue iniciada para,"
                        binding.tvUsername.text = "${it.data?.get("name")}"
                        binding.tvEmail.text = "${it.data?.get("email")}"
                    }
                }?.addOnFailureListener { exception ->
                    Log.d("firestore", "get failed with ", exception)
                }

        }
    }
}