package com.example.laboratoriofirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.laboratoriofirebase.databinding.FragmentLogoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LogoutFragment : Fragment() {
    lateinit var binding: FragmentLogoutBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        auth.signOut()
        val user: FirebaseUser? = auth.currentUser
        val message: TextView
        if (user == null) {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_login) }
            Toast.makeText(context, "La sesion fue cerrada", Toast.LENGTH_SHORT).show()

        } else {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_home) }
        }
    }

    private fun Message(message: String) {
        Toast.makeText(context, "${message}", Toast.LENGTH_SHORT).show()
    }
}