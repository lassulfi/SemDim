package br.com.semdimapp.semdim.controller;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.model.Contato;
import br.com.semdimapp.semdim.model.Grupo;
import br.com.semdimapp.semdim.model.Usuario;

/**
 * Classe responsável pela gestão dos grupos no app
 */
public class GrupoController {

    //Atributos
    private Grupo grupo;
    private UsuarioController usuarioController;
    private ContatoController contatoController;

    private DatabaseReference databaseReference;

    private ArrayList<Contato> contatos;

    private ArrayList<Grupo> grupos;

    private Context context;


    private static GrupoController instance = null;

    //Construtor
    private GrupoController(Context context){
        this.context = context;
        this.usuarioController = new UsuarioController();
        contatoController = ContatoController.getInstance(context);
    }

    public static GrupoController getInstance(Context context) {
        if (instance == null){
            instance = new GrupoController(context);
        }
        return instance;
    }

    /**
     * Cria um novo grupo no app
     * @param nome String contendo o nome do grupo
     * @param context Context
     * @param contatos ArrayList de contatos selecionados para o grupo
     */
    public void criarGrupo(String nome, ArrayList<Contato> contatos,Context context){

        //Recupera o usuario criador do grupo
        Usuario criador = usuarioController.getUsuarioLogado(context);

        //Cria uma instancia da classe Grupo
        grupo = new Grupo(criador);
        grupo.setNome(nome);

        for(Contato contato: contatos){
            grupo.addContato(contato);
        }

        grupos.add(grupo);

        //Adiciona ao banco de dados
        String idUsuarioLogado = criador.getId();
        final String idGrupo = Base64Custom.encodeBase64(nome);

        databaseReference = FirebaseConfig.getDatabaseReference()
                .child("grupos")
                .child(idUsuarioLogado);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    databaseReference.child(idGrupo).setValue(grupo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("database:databaseError", databaseError.getMessage());
            }
        });
    }

    public ArrayList<Grupo> getGrupos(){
        return grupos;
    }


}
