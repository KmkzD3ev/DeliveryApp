package com.example.deliveryapp.Cadastro

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import com.example.deliveryapp.Dialog.Dialogs
import com.example.deliveryapp.databinding.ActivityFormLoginBinding
import com.example.deliveryapp.view.HomeTela
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider

class Form_login : AppCompatActivity() {
    lateinit var binding: ActivityFormLoginBinding
    private val auth = FirebaseAuth.getInstance()
    lateinit var clienteGoogle: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        googleSignInOptions()
         val dialogcarregando = Dialogs(this)





        binding.txtCriarConta.setOnClickListener { view ->
            val GotelaCadastro = Intent(this, TelaCadastro::class.java)
            startActivity(GotelaCadastro)
        }




        binding.btEntrar.setOnClickListener { view ->
            val email = binding.txtemail.text.toString()
            val senha = binding.txtSenha.text.toString()




            if (email.isEmpty() || senha.isEmpty()) {
                val snackbar =
                    Snackbar.make(view, "Preencha todos os Campos", Snackbar.LENGTH_SHORT)
                snackbar.setTextColor(Color.RED)
                snackbar.show()

            } else {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { autenticacao ->
                        if (autenticacao.isSuccessful) {
                           dialogcarregando.iniciarDialog()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val navegarHome = Intent(this, HomeTela::class.java)
                                startActivity(navegarHome)
                                finish()
                                dialogcarregando.liberarDialog()
                            },3000)



                        }


                    }.addOnFailureListener { exception ->
                        val mensagemErro = when (exception) {
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com pelo menos 6 caracteres !"
                            is FirebaseAuthInvalidCredentialsException -> " Digite um email Valido !"
                            is FirebaseAuthUserCollisionException -> "Esse email ja esta sendo Ultilizado !"
                            is FirebaseNetworkException -> "Sem Conexao com a Internet !"

                            else -> "Erro ao Logar Usuario"

                        }
                        val snackbar =
                            Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbar.setTextColor(Color.RED)
                        snackbar.show()
                    }


            }
        }




        binding.Googlebt.setOnClickListener { view ->
            Log.d("TelaLogin", "Botão de login do Google clicado.")
            loginGoogle()
        }

    }

    private fun googleSignInOptions() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("783501334189-kka71uosoc70gkvl2k1tl70961uruecg.apps.googleusercontent.com")
            .requestEmail()
            .build()
        clienteGoogle = GoogleSignIn.getClient(this, gso)
    }


    private fun loginGoogle() {
        Log.d("TelaLogin", "Iniciando login do Google.")
        val intentEntrar = clienteGoogle.signInIntent
        resultadologin.launch(intentEntrar)


    }

    fun autenticardorGoogle(idToken: String) {
        Log.d("TelaLogin", "Autenticando usuário do Google.")
        val credencial = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credencial).addOnCompleteListener { autenticacao ->
            if (autenticacao.isSuccessful) {
                Log.d("TelaLogin", "Login do Google bem-sucedido.")
                val navegarHome = Intent(this, HomeTela::class.java)
                startActivity(navegarHome)


            } else {
                Log.e("TelaLogin", "Falha ao autenticar o usuário do Google.")
            }


        }


    }

    val resultadologin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado: ActivityResult ->

            if (resultado.resultCode == RESULT_OK) {
                val tarefa = GoogleSignIn.getSignedInAccountFromIntent(resultado.data)
                val conta = tarefa.getResult(ApiException::class.java)
                autenticardorGoogle(conta.idToken!!)

            }
        }


    override fun onStart() {
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null) {
            val navegarCadastro = Intent(this, HomeTela::class.java)
            startActivity(navegarCadastro)


        }

    }
}




























