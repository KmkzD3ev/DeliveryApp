﻿# DeliveryApp

Sistema de E-comerce digital para lanchonetes ,supermercados e loja em geral
Compostos por duas partes
Parte cliente -
Disponibilize seus produtos e serviços de forma dinâmica 
Permitindo fidelização através da criação de conta com email e senha ou login via Google 
Receba pagamento e ofereça parcelamento através da API de pagamento do Mercado pago,salvando pedidos de clientes on e offline 
Parte Administrador -
Controle o estoque que será exibido aos consumidores 
Criando novos produtos e cadastrando 
Atualizando produtos já existentes ou deletando os mesmo
Somente colaborador ou proprietário terão acesso via login cadastrado
Escrito em koltin com sdk 34 e Firebase 
Inclusão com banco de dados em nuvem ,evitando perda de dados importantes

Sistemas de login via Email e senha padrao
ou conta Google

Usamos RecyclerView para listagem dos itens e aproveitamento de tela 
Com scroolView horizontal

Duas classe modelos ,para Produtos e outra para servicos
juntamante com um Adapter pra indicar onde os itens irao ser posicionados
Usei putExtras para indicar a passagem de dados entre as telas de forma dinamica
Conta ainda com Api de pagamento Mercado-Pago,para comodidade dos usuarios ou ainda a opçao de pagar na entrega
Toda lista e salva de modo On-line e Off-line
Modo On-line foi ultilizado o firebase ,salvando e devolvendo a listagem quando nescessario
Modo Off-line e usando share-preference como banco local armazenando lista e dados para entrega

Na parte do Administrador ,foi usado sistema de validaçao com login pre-definido
Evitando uso de pessoas nao autorizadas
Nessa parte da aplicaçao ,temos acesso a toda lista de produtos cadastrados
Podemos Ler, Excluir,Atualizar ou Deletar qualquer item 
Recebemos tbm os a lista de pedidos de todos os clientes separando-os por Nome e IDs unicos para cada usuario











 
 ![WhatsApp Image 2024-01-15 at 19 31 15](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/97ce62d1-3eb4-4b79-9141-1c4c97a9428d)
![WhatsApp Image 2024-01-15 at 19 31 16](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/580c285d-9e6e-445f-a9fb-536b49716ebb)
 ![WhatsApp Image 2024-01-15 at 19 31 16 (5)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/f0252df2-2865-4ce5-a699-7f244556c66f)
![WhatsApp Image 2024-01-15 at 19 31 16 (4)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/bf5d47e0-ee5c-4b46-825c-f80c7e297047)
![WhatsApp Image 2024-01-15 at 19 31 16 (3)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/94bcf223-cacf-477c-b9cc-2f63e52bd2ed)
![WhatsApp Image 2024-01-15 at 19 31 16 (2)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/8c54082b-c189-4272-b5c6-6168171759e6)
![WhatsApp Image 2024-01-15 at 19 31 16 (1)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/913f3b4a-ef46-48d0-85be-a62406657c01)
![WhatsApp Image 2024-01-15 at 19 31 15 (3)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/fb0630b0-8a65-4b60-836d-ed872e0979cd)
![WhatsApp Image 2024-01-15 at 19 31 15 (2)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/9ae49f09-5cfc-476b-87e7-a46adc34c576)
![WhatsApp Image 2024-01-15 at 19 45 11](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/8042a014-f08f-4a83-8cbf-4673adea59a5)
![WhatsApp Image 2024-01-15 at 19 31 15 (1)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/ad3e6bee-aa58-481e-a613-3803b101a701)
![WhatsApp Image 2024-01-15 at 19 31 16 (7)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/8e2e2cca-efa5-4419-917d-2b52cfc22074)
![WhatsApp Image 2024-01-15 at 19 31 16 (6)](https://github.com/KmkzD3ev/DeliveryApp/assets/141889210/d7f531eb-bff1-49f8-83c0-2a67ad128fb1)

