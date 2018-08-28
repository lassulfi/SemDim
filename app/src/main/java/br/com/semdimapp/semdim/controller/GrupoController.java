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
    private static final String DATABASEERROR = "database:onCanceled";

    private Grupo grupo;
    private UsuarioController usuarioController;
    private ContatoController contatoController;

    private DatabaseReference databaseReference;

    private ArrayList<Grupo> grupos;

    private Context context;

    private boolean sucess;


    private static GrupoController instance = null;

    //Construtor
    private GrupoController(Context context){
        this.context = context;

        this.usuarioController = UsuarioController.getInstance();
        this.contatoController = ContatoController.getInstance(context);

        this.sucess = false;

        this.grupos = new ArrayList<>();
    }

    public static GrupoController getInstance(Context context) {
        if (instance == null){
            instance = new GrupoController(context);
        }
        return instance;
    }

    //TODO: finalizar a codificação da criacao do grupo
    /**
     * Cria um novo grupo no app
     * @param nome String contendo o nome do grupo
     * @param contatos ArrayList de contatos selecionados para o grupo
     */
    public void criarGrupo(String nome, ArrayList<Contato> contatos){

        //Recupera o usuario criador do grupo
        usuarioController.encontrarUsuarioLogado(context);
        Usuario criador = usuarioController.getUsuario();
        String idUsuarioLogado = criador.getId();


        //Cria uma instancia da classe Grupo
        grupo = new Grupo(criador);
        grupo.setNome(nome);
        grupo.setId(idUsuarioLogado + "_" + nome);

        for(Contato contato: contatos){
            grupo.addContato(contato);
        }

        grupos.add(grupo);

        //Salva o grupo no Firebase
        databaseReference = FirebaseConfig.getDatabaseReference();
        databaseReference.child("grupos")
                .child(criador.getId())
                .child(grupo.getId())
                .setValue(grupo);

        //Adiciona o grupo para cada contato do firebase
        for (Contato contato: contatos){
            databaseReference.child("grupos")
                    .child(contato.getId())
                    .child(grupo.getId())
                    .setValue(grupo);
        }

        sucess = true;
    }

    public DatabaseReference getGruposDatabaseReference(){

        //Adiciona ao banco de dados
        Usuario criador = grupo.getCriador();

        databaseReference = FirebaseConfig.getDatabaseReference()
                .child("grupos")
                .child(criador.getId());

        return databaseReference;
    }

    public ValueEventListener getGrupoEventListener(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    databaseReference.child(grupo.getId()).setValue(grupo);
                }
                sucess = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATABASEERROR, databaseError.getMessage());
            }
        };

        return valueEventListener;
    }

    public void apagarTodos(){
        grupos.clear();
    }

    public void adicionarGruposNaLista(Grupo grupo){
        grupos.add(grupo);
    }

    public ArrayList<Grupo> getGrupos(){
        return grupos;
    }

    public boolean isSucess(){
        return sucess;
    }
}
