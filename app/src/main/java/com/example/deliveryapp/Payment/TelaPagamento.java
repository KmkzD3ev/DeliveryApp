package com.example.deliveryapp.Payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deliveryapp.InterfaceMercadoPago.ComunicacaoServidorMp;
import com.example.deliveryapp.databinding.ActivityTelaPagamentoBinding;
import com.example.deliveryapp.model.Produto;
import com.example.deliveryapp.model.database;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class TelaPagamento extends AppCompatActivity {
    private TextView textViewMensagem;
    ActivityTelaPagamentoBinding binding;
    private String nome = "Test Test";
    private String preco;

    private final String Public_Key = "TEST-c50147f5-26ef-43f7-b6d0-0dc46bbb64fa";
    private final String Access_Token = "TEST-6984668882020681-120921-a07f8f74c43b0cce9507e1cf83190a1b-1557334042";
    private JsonArray listaPedidos = new JsonArray(); // Corrigindo o tipo da variável
    private String nomeUsuario;

    private LinearLayout mercadoLayout;
    database db = new database();
    private String UsuarioId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private  String pedidoId = UUID.randomUUID().toString();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityTelaPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d("TelaPagamento", "Criando TelaPagamento");







        Serializable listaPedidosSerializable = getIntent().getExtras().getSerializable("listaPedidos");
         nomeUsuario = getIntent().getStringExtra("nomeUsuario"); // Movido para o onCreate
         nome = getIntent().getExtras().getString("nome");
        preco = String.valueOf(getIntent().getExtras().getDouble("preco"));


        // Adicionando logs para verificar como a lista de pedidos é recebida
        Log.d("TelaPagamento", "Recebendo nome do usuário: " + nomeUsuario);
        Log.d("TelaPagamento", "Recebendo lista de pedidos: " + listaPedidosSerializable);
        Log.d("TelaPagamento", "Nome: " + nome);
        Log.d("TelaPagamento", "Preço: " + preco);


        if (listaPedidosSerializable instanceof ArrayList) {
            listaPedidos = new Gson().toJsonTree(listaPedidosSerializable).getAsJsonArray();
        }
        somarLista();


        binding.editPagFinal.setOnClickListener(view -> {


            String bairro = binding.editPayBairro.getText().toString();
            String ruaNume = binding.editPayRuAeNum.getText().toString();
            String PontoRefe = binding.editpayPontoReferen.getText().toString();
            String celular = binding.editpayNumberPhone.getText().toString();

            if (bairro.isEmpty() || ruaNume.isEmpty() || PontoRefe.isEmpty() || celular.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os Campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            } else {
                criarJsonObject();


            }
        });

        binding.editPagFisico.setOnClickListener(view -> {

            String bairro = binding.editPayBairro.getText().toString();
            String ruaNume = binding.editPayRuAeNum.getText().toString();
            String PontoRefe = binding.editpayPontoReferen.getText().toString();
            String celular = binding.editpayNumberPhone.getText().toString();

            if (bairro.isEmpty() || ruaNume.isEmpty() || PontoRefe.isEmpty() || celular.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os Campos", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            } else {
                realizarPagamentoFisico();


            }
        });


    }


    private JsonObject criarJsonObject() {
        JsonObject dados = new JsonObject();
        JsonObject excluirBoleto = new JsonObject();
        JsonArray ids = new JsonArray();

        // Criação do array de items
        JsonArray itemsArray = new JsonArray();

        List<Produto> produtos = (List<Produto>) getIntent().getExtras().getSerializable("listaPedidos");

        // Adiciona os itens do pedido ao array
        if (produtos != null && !produtos.isEmpty()) {
            for (Produto produto : produtos) {
                JsonObject item = new JsonObject();
                item.addProperty("title", produto.getNome());
                item.addProperty("picture_url", produto.getFoto());
                item.addProperty("quantity", 1);
                item.addProperty("unit_price", produto.getPreco());
                itemsArray.add(item);
            }
        }

        // Adiciona o array de items aos dados
        dados.add("items", itemsArray);

        // Adiciona a estrutura do pagador (payer)
        JsonObject payer = new JsonObject();
        ;
        dados.add("payer", payer);


        // Adiciona o e-mail ao pagador
       // String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
       String emailUsuario = "test_user_1533971737@testuser.com";
        if (emailUsuario != null) {
            payer.addProperty("email", emailUsuario);
        }
        // JsonObject identification = new JsonObject();
        //identification.addProperty("name", nomeUsuario);
        // payer.add("identification", identification);

        //Removendo a opçao boleto para lanchonetes
        JsonObject removeBoleto = new JsonObject();
        removeBoleto.addProperty("id", "ticket");
        ids.add(removeBoleto);
        excluirBoleto.add("excluded_payment_types", ids);
        excluirBoleto.addProperty("installments", 2);

        dados.add("payment_methods", excluirBoleto);

        // Agora você pode imprimir os dados completos
        Log.d("j", dados.toString());
        criarPreferenciaPagamento(dados);


        return dados;
    }

    private void criarPreferenciaPagamento(JsonObject dados) {
        String site = "https://api.mercadopago.com";
        String url = "/checkout/preferences?access_token=" + Access_Token;
        Log.d("TelaPagamento", "Enviando pagamento para o servidor: " + dados.toString());


        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(site)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        ComunicacaoServidorMp conexaoPagamento = retrofit.create(ComunicacaoServidorMp.class);
        Call<JsonObject> request = conexaoPagamento.enviarPagamento(url, dados);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String preferenciaId = response.body().get("id").getAsString();
                    iniciarMercadoPago(preferenciaId);


                    Log.d("TelaPagamento", "Corpo da resposta: " + response.body().toString());


                } else {
                    // Lide com a resposta não bem-sucedida ou com body nulo
                }


            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


    }


    private void iniciarMercadoPago(String preferenciaId) {
        final AdvancedConfiguration advancedConfiguration = new AdvancedConfiguration.Builder()
                .setBankDealsEnabled(false).build();
        Log.d("TelaPagamento", "Preferencia ID: " + preferenciaId);

        new MercadoPagoCheckout.Builder(Public_Key, preferenciaId)
                .setAdvancedConfiguration(advancedConfiguration).build()
                .startPayment(this, 123);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            Log.d("TelaPagamento", "Resultado do pagamento - Código: " + resultCode);
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {


                final Payment pagamento = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                repostaMercadoPago(pagamento);

            } else if (resultCode == RESULT_FIRST_USER) {

            } else {


            }


        }
    }

    private void repostaMercadoPago(Payment pagamento) {

        String statusPgamento = pagamento.getPaymentStatus();
        String bairro = binding.editPayBairro.getText().toString();
        String rua_Nume = binding.editPayRuAeNum.getText().toString();
        String Ponto_Refe = binding.editpayPontoReferen.getText().toString();
        String celular = binding.editpayNumberPhone.getText().toString();



        String endereco = " Bairro :  " + bairro + "       " + "   RUA E Numero : " + "        " + rua_Nume + "   PONTO DE REFERENCIA:" + "        " + Ponto_Refe;
        String status = "Status Pagamento " + "     " + "Pagmento Pendente";
        String entregaStatus = " Status De Entrega : " + " " + "Em andamento ";




        StringBuilder listaDosPedidos = new StringBuilder();
        String valorTotalPedidos = String.valueOf(0);

        List<Produto> produtos = (List<Produto>) getIntent().getExtras().getSerializable("listaPedidos");

        if (produtos != null && !produtos.isEmpty()) {
            for (Produto produto : produtos) {
                listaDosPedidos.append(produto.getNome()).append(" - ").append(produto.getPreco()).append("\n");
                valorTotalPedidos += produto.getPreco();
            }


            String mensagem = "Produtos: \n" + listaDosPedidos.toString() +
                    "Valor Total dos Pedidos: " + valorTotalPedidos;


            if (statusPgamento.equalsIgnoreCase("approved")) {
                Snackbar snackbar = Snackbar.make(binding.MercadoLayout, "Sucesso ao realizar pagamento", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.BLUE);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
                db.salvarPedidosUser(endereco, celular, statusPgamento, produtos, Ponto_Refe, entregaStatus,pedidoId,UsuarioId);
                db.salvarPedidoAdm(endereco, celular, statusPgamento, produtos, Ponto_Refe, entregaStatus,pedidoId,UsuarioId,nomeUsuario);
                listaPedidos = new JsonArray();



            } else if (statusPgamento.equalsIgnoreCase("rejectd")) {

                Snackbar snackbar = Snackbar.make(binding.MercadoLayout, "ERRO ao realizar pagamento", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();

            }


        }
    }

    private void realizarPagamentoFisico() {


        String statusPgamento = " ";
        String bairro = binding.editPayBairro.getText().toString();
        String rua_Nume = binding.editPayRuAeNum.getText().toString();
        String Ponto_Refe = binding.editpayPontoReferen.getText().toString();
        String celular = binding.editpayNumberPhone.getText().toString();

        String endereco = " Bairro :  " + bairro + "       " + "   RUA E Numero : " + "        " + rua_Nume + "   PONTO DE REFERENCIA:" + "        " + Ponto_Refe;
        String status = "Status Pagamento " + "     " + "Pagmento Pendente";
        String entregaStatus = " Status De Entrega : " + " " + "Em andamento ";

        List<Produto> produtos = (List<Produto>) getIntent().getExtras().getSerializable("listaPedidos");


        // Salvar os pedidos no banco de dados
        db.salvarPedidosUser(endereco, celular, " Pagamento Pendente ", produtos, Ponto_Refe, entregaStatus,pedidoId,UsuarioId);
        db.salvarPedidoAdm(endereco, celular, " Pagamento Pendente ", produtos, Ponto_Refe, entregaStatus,pedidoId,UsuarioId,nomeUsuario);
        listaPedidos = new JsonArray();


        Snackbar snackbar = Snackbar.make(binding.MercadoLayout, "Pedidos salvos com sucesso", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.BLUE);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();
    }


    private void somarLista() {
        double totalPedidos = 0;
        List<Produto> produtos = (List<Produto>) getIntent().getExtras().getSerializable("listaPedidos");


        if (produtos != null && !produtos.isEmpty()) {
            for (Produto produto : produtos) {
                totalPedidos += produto.getPreco();
            }

            // Log do valor total antes de definir na TextView
            Log.d("TelaPagamento", "Valor total dos produtos: " + totalPedidos);

            // Definição do texto na TextView precoLista
            String valorTotalFormatado = String.format("%.2f", totalPedidos);
            binding.precoLista.setText("Total: R$ " + valorTotalFormatado);


        }


    }

}
