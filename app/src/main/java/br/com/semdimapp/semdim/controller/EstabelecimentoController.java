package br.com.semdimapp.semdim.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.model.Estabelecimento;
import br.com.semdimapp.semdim.model.Promocao;

/**
 * Classe responsável pela gestão de estabelecimentos
 */
public class EstabelecimentoController {

    //Atributos
    private static EstabelecimentoController instance = null;

    private Estabelecimento estabelecimento;

    private ArrayList<Estabelecimento> estabelecimentos;

    private DatabaseReference databaseReference;

    /**
     * Construtor da classe
     */
    private EstabelecimentoController(){
        estabelecimentos = new ArrayList<>();
    }

    /**
     * Retorna uma instancia do gestor de estabelecimentos
     * @return
     */
    public static EstabelecimentoController getInstance() {
        if(instance == null){
            instance = new EstabelecimentoController();
            //Adiciona estabelecimentos falsos
            instance.adicionarEstabelecimentosFalsos();
        }

        return  instance;
    }

    /**
     * Adiciona as promoções e ordena pelo menor preço
     * @param estabelecimento
     * @param nome
     * @param descricao
     * @param valor
     */
    public void adicionarPromocao(Estabelecimento estabelecimento,
                                      String nome, String descricao, float valor){
        Promocao promocao = new Promocao(nome, descricao, valor);
        estabelecimento.addPromocao(promocao);
        //Ordenar as promocoes
        estabelecimento.ordenarPromocoes();
    }

    public float obterMenorValor(Estabelecimento estabelecimento){
        return estabelecimento.obterMenorValor();
    }

    private void adicionarEstabelecimentosFalsos(){

        //Estabelecimento 1 - Gordão lanches
        Estabelecimento estabelecimento1 = new Estabelecimento();
        estabelecimento1.setNome("Gordão lanches");
        estabelecimento1.setEmail("gordaolanches@gordaolanches.com.br");
        String id1 = Base64Custom.encodeBase64(estabelecimento1.getEmail());
        estabelecimento1.setId(id1);
        estabelecimento1.setEndereco("Avenida Francisco Glicerio, 1320, Campinas - SP");
        estabelecimento1.setCoordenadas(-22.902802, -47.060448);

        adicionarPromocao(estabelecimento1, "Promoção Combo X-Salada",
                "Promoção do nosso combo do X-salada",
                60.0f);
        adicionarPromocao(estabelecimento1, "Promoção do Almoço",
                "Promoção do almoço",
                50.0f);
        adicionarPromocao(estabelecimento1, "Promoção de Verão",
                "Compre nossos sorvetes",
                30.0f);

        estabelecimentos.add(estabelecimento1);

        //Estabelecimento 2 - Pizzaria Oliva
        Estabelecimento estabelecimento2 = new Estabelecimento();
        estabelecimento2.setNome("Pizzaria Oliva");
        estabelecimento2.setEmail("falecom@pizzariaoliva.com.br");
        String id2 = Base64Custom.encodeBase64(estabelecimento2.getEmail());
        estabelecimento2.setId(id2);
        estabelecimento2.setEndereco("Rua José Paulino, 1566 - Vila Itapura, Campinas - SP");
        estabelecimento2.setCoordenadas(-22.900776, -47.06391);

        adicionarPromocao(estabelecimento2, "Pizza de quarta-feira",
                "Experimente nossas pizzas as quartas-feiras",
                20.0f);
        adicionarPromocao(estabelecimento2, "Domingo com pizza",
                "Termine seu final de semana com uma deliciosa pizza",
                20.0f);
        adicionarPromocao(estabelecimento2, "Pizza, refri e sobremesa",
                "Na compra de duas pizzas ganhe refri e sobremesa",
                60.0f);

        estabelecimentos.add(estabelecimento2);

        //Estabelecimento 3 - City Bar
        Estabelecimento estabelecimento3 = new Estabelecimento();
        estabelecimento3.setNome("City Bar");
        estabelecimento3.setEmail("city@citybar.com.br");
        String id3 = Base64Custom.encodeBase64(estabelecimento3.getEmail());
        estabelecimento3.setId(id3);
        estabelecimento3.setEndereco("Avenida Júlio de Mesquita, 450 - Cambuí, Campinas - SP");
        estabelecimento3.setCoordenadas(-22.901329, -47.053769);

        adicionarPromocao(estabelecimento3, "Promoção do bolinho de bacalhau",
                "Compre 20 bolinhos e ganhe mais 4",
                45.0f);
        adicionarPromocao(estabelecimento3, "Domingo na praça",
                "Visite o Centro de convivência e venha almoçar com a gente",
                50.0f);
        adicionarPromocao(estabelecimento3, "Futebol, cerveja e bolinho",
                "Traga a sua torcida e venha se divertir com a gente",
                60.0f);

        estabelecimentos.add(estabelecimento3);

        //Recupera a instancia do firebase
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();

        //Adicionando ao banco de dados
        int count = 1;
        for(Estabelecimento e: estabelecimentos){
            databaseReference.child("estabelecimentos").child(String.valueOf(count)).setValue(e);
            count++;
        }
    }

    public void adicionarEstabelecimento(Estabelecimento estabelecimento){
        estabelecimentos.add(estabelecimento);
    }

    /**
     * Apaga todos os estabelecimentos
     */
    public void apagarTodos(){
        estabelecimentos.clear();
    }

    /**
     * Retorna todos os estabelecimentos
     * @return
     */
    public ArrayList<Estabelecimento> getEstabelecimentos() {
        return estabelecimentos;
    }
}
