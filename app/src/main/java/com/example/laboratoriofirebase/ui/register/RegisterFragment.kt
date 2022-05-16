package com.example.laboratoriofirebase.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.laboratoriofirebase.R
import com.example.laboratoriofirebase.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.util.*


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnSignUp.setOnClickListener{
            if( validaForm() ){
                register()
            }
            else {
                Toast.makeText(context,"Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLogin.setOnClickListener{
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_login) }
        }

        return binding.root
    }


    private fun register(){
        val name: EditText = binding.etNombre
        val edad: EditText = binding.etEdad
        val email: EditText = binding.email
        val password: EditText = binding.password

        //Crea una cuenta
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
            if(it.isSuccessful){
                //Registro en la database

                    val user = hashMapOf(
                    "name" to name.text.toString(),
                    "edad" to edad.text.toString(),
                    "email" to email.text.toString(),
                    "password" to password.text.toString(),
                    "createdAt" to Timestamp(Date().time)
                )

                //Add a new document
                db.collection("users").document(email.text.toString())
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(context,"¡Registro exitoso!", Toast.LENGTH_LONG).show()
                        auth.signOut()
                        view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.nav_login) }
                    }
                    .addOnFailureListener { e ->
                        Log.w("FireStore", "Error adding document", e)
                    }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }

    }

    private fun validaForm() : Boolean {
        if( validateName() && validaEdad() && validateEmail() && validatePassword() ) return true
        else return false
    }

    private fun validateName() : Boolean {
        if(binding.etNombre.text.toString() != "" ) return true
        else {
            binding.etNombre.error = "Ingrese su nombre completo"
            binding.etNombre.requestFocus()
            return false
        }
    }

    private fun validaEdad() : Boolean {
        val edadPattern = "^([0-9]+)\$"
        if( binding.etEdad.text.toString().matches(edadPattern.toRegex())) return true

        else {
            binding.etEdad.error = "Ingrese su edad solo números"
            binding.etEdad.requestFocus()
            return false
        }
    }

    private fun validatePassword(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{8}\$"
        if( binding.password.text.toString().matches(passwordPattern.toRegex())) return true
        else {
            binding.password.error = "La contraseña debe contener 8 caracteres, con almenos un número y una letra"
            binding.password.requestFocus()
            return false
        }
    }

    private fun validateEmail(): Boolean {
        //Paso 0. Reg Pattern
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        //Paso 1. Compares Pattern with input
        if ( binding.email.text.toString().matches(emailPattern.toRegex()) ) return true
        else {
            binding.email.error = "Ingrese un E-mail válido"
            binding.email.requestFocus()
            return false
        }
    }


    }
