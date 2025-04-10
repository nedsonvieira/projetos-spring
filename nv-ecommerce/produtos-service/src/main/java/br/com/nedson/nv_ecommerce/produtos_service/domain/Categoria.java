package br.com.nedson.nv_ecommerce.produtos_service.domain;

public enum Categoria {

    ELETRONICOS,
    ALIMENTOS,
    ROUPAS,
    LIVROS,
    MOVEIS,
    COSMETICOS,
    ESPORTES,
    SAUDE,
    BRINQUEDOS;

    public String getDescricao() {
        return switch (this) {
            case ELETRONICOS -> "Produtos relacionados a tecnologia e eletrônicos.";
            case ALIMENTOS -> "Comidas, bebidas e outros itens alimentícios.";
            case ROUPAS -> "Vestimentas e acessórios de moda.";
            case LIVROS -> "Livros físicos e digitais.";
            case MOVEIS -> "Móveis e itens para decoração.";
            case COSMETICOS -> "Produtos de beleza e cuidados pessoais.";
            case ESPORTES -> "Equipamentos e acessórios esportivos.";
            case SAUDE -> "Itens de saúde e bem-estar.";
            case BRINQUEDOS -> "Brinquedos e jogos para todas as idades.";
            default -> "Categoria desconhecida.";
        };
    }

}
