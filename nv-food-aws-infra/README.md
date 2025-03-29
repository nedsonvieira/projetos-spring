# 🍽️ NvFood - Infraestrutura com AWS CDK em Java

Este projeto utiliza **AWS CDK (Cloud Development Kit) em Java** para provisionar a infraestrutura necessária para a aplicação **NvFood**.

## 🏗️ Infraestrutura Provisionada

O projeto define e provisiona os seguintes recursos na AWS:

- 🌐 **VPC (Virtual Private Cloud)**: Rede isolada para os serviços da aplicação.
- 🏗️ **ECS Cluster (Elastic Container Service)**: Gerenciamento de contêineres para execução dos serviços.
- 🗄️ **RDS (Relational Database Service) com MySQL**: Banco de dados relacional para armazenamento das informações dos pedidos.
- 🚀 **Fargate Service para Pedidos**: Serviço serverless para processamento de pedidos no ECS.

## 📌 Tecnologias Utilizadas

- ☕ **Java**
- ☁️ **AWS CDK**
- 🛠️ **Amazon VPC, ECS, RDS (MySQL), Fargate**

## 📂 Estrutura do Projeto

```
├── src/main/java/com/myorg
│   ├── NvFoodAwsInfraApp.java  # Aplicação principal CDK
│   ├── NvFoodVpcStack.java    # Definição da VPC
│   ├── NvFoodClusterStack.java # Configuração do ECS Cluster
│   ├── NvFoodRdsPedidosStack.java    # Configuração do banco de dados MySQL
│   ├── NvFoodServicePedidosStack.java # Serviço de pedidos no Fargate
│
├── cdk.json            # Configuração do CDK
├── pom.xml             # Dependências Maven
```

## 📌 Contribuição

Sinta-se à vontade para abrir issues e pull requests para melhorias e correções. 🚀

## 📝 Licença

Este projeto está licenciado sob a **MIT License..**
