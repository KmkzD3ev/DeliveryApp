package com.example.deliveryapp.Cadastro

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.deliveryapp.R
import com.example.deliveryapp.databinding.ActivityTelaCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.example.deliveryapp.Dialog.Dialogs
import com.example.deliveryapp.model.database
import com.google.firebase.firestore.FirebaseFirestore as FirebaseFirestore

class TelaCadastro : AppCompatActivity() {


    lateinit var binding: ActivityTelaCadastroBinding
    private val auth = FirebaseAuth.getInstance()
    private var fotoPerfil : Uri ? = null
    val usuarioDb = database()



    private val selecionarFoto = registerForActivityResult(ActivityResultContracts.GetContent()){uri->
       if (uri != null) {
           fotoPerfil = uri
           binding.fotouser.setImageURI(fotoPerfil)


       }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialogcarregando = Dialogs(this)







        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nome = binding.telaCbtNome.text.toString()
                val email = binding.TelaCbtEmail.text.toString()
                val senha = binding.telaCbtsenha.text.toString()

                if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty()) {

                    binding.btRetgistrar.isEnabled = true

                    binding.btRetgistrar.setBackgroundColor(
                        ContextCompat.getColor(
                            this@TelaCadastro,
                            R.color.vermelho
                        )
                    )

                } else {
                    binding.btRetgistrar.isEnabled = false
                    binding.btRetgistrar.setBackgroundColor(
                        ContextCompat.getColor(
                            this@TelaCadastro,
                            R.color.cinza_prateado
                        )
                    )

                }
                if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                    binding.btRetgistrar.isEnabled = false
                    binding.btRetgistrar.setBackgroundColor(
                        ContextCompat.getColor(
                            this@TelaCadastro,
                            R.color.vermelho
                        )
                    )
                }

            }

            override fun afterTextChanged(s: Editable?) {
                val email = binding.TelaCbtEmail.text.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.TelaCbtEmail.error = "Digite um email válido"
                }
            }

        }

        binding.telaCbtNome.addTextChangedListener(textWatcher)
        binding.TelaCbtEmail.addTextChangedListener(textWatcher)
        binding.telaCbtsenha.addTextChangedListener(textWatcher)



        binding.btRetgistrar.setOnClickListener { view ->
            val email = binding.TelaCbtEmail.text.toString()
            val senha = binding.telaCbtsenha.text.toString()
            val nome = binding.telaCbtNome.text.toString()

            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os Campos", Snackbar.LENGTH_INDEFINITE)
                snackbar.setTextColor(Color.RED)
                snackbar.show()
            } else {
                auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {

                            fotoPerfil?.let { uri ->
                                val nome = binding.telaCbtNome.text.toString()
                                usuarioDb.salvarDadosUsuarios(nome, uri)
                            }

                            val user = auth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(nome)
                                .build()

                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { profileUpdate ->
                                    if (profileUpdate.isSuccessful) {
                                        dialogcarregando.iniciarDialog()
                                        Handler(Looper.getMainLooper()).postDelayed({
                                            dialogcarregando.liberarDialog()
                                        },1000)


                                        val snackbar = Snackbar.make(
                                            view,
                                            " Cadastro Realizado com Sucesso !",
                                            Snackbar.LENGTH_INDEFINITE
                                        )
                                        snackbar.setTextColor(Color.BLUE)
                                        snackbar.setAction("ok") {
                                            Toast.makeText(this, "Seja Bem Vindo", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                        snackbar.show()
                                        binding.TelaCbtEmail.setText("")
                                        binding.telaCbtsenha.setText("")
                                        binding.telaCbtNome.setText(" ")
                                    }

                                }
                        }
                    }.addOnFailureListener { exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com pelo menos 6 caracteres !"
                            is FirebaseAuthInvalidCredentialsException -> " Digite um email Valido !"
                            is FirebaseAuthUserCollisionException -> "Esse email ja foi Cadastrado !"
                            is FirebaseNetworkException -> "Sem Conexao com a Internet !"

                            else -> "Erro ao Cadastrar Usuario"

                        }
                        val snackbar =
                            Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbar.setTextColor(Color.RED)
                        snackbar.show()
                    }
            }
        }


        binding.btSelecFoto.setOnClickListener {
            Log.d("TelaCadastro", "Botão de seleção de foto clicado")
            selecionarFoto.launch("image/*")
        }

        }


}

































































