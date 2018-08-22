package br.com.semdimapp.semdim.controller;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.exceptions.ContatoException;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.model.Contato;
import br.com.semdimapp.semdim.model.Usuario;

/**
 * Classe responsável pela gestão de contatos no app
 */
public class ContatoController {

    //Atributos
    private Contato contato;
    private ArrayList<Contato> contatos;

    private Context context;

    private static ContatoController instance = null;

    private DatabaseReference databaseReference;

    private boolean result;

    /**
     * Construtor padrao
     */
    private ContatoController(Context context){
        contatos = new ArrayList<>();
        this.context = context;
    }

    /**
     * Padrão singleton
     * @param context
     * @return
     */
    public static ContatoController getInstance(Context context) {
        if (instance == null){
            instance = new ContatoController(context);
        }

        return instance;
    }

    /**
     * Adiciona um novo contato
     * É necessário que o contato esteja cadastrado no app
     *
     * @param email Email do contato (String)
     */
    public boolean adicionarContato(String email){

        //Converte o e-mail para base 64
        final String idContato = Base64Custom.encodeBase64(email);

        //Recupera a instancia do Firebase onde será realizada a pesquisa
        databaseReference = FirebaseConfig.getDatabaseReference().child("usuarios")
                .child(idContato);

        //Adiciona o listerner para a consulta
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Verifica se o Listener retornou dados
                if(dataSnapshot.getValue() != null){

                    Usuario uContato = dataSnapshot.getValue(Usuario.class);

                    //Recupera o e-mail do usuario logado
                    Preferences preferences = new Preferences(context);
                    String idUsuarioLogado = preferences.getUsuarioID();

                    //Adiciona o nó ao banco de dados
                    databaseReference = FirebaseConfig.getDatabaseReference();
                    databaseReference = databaseReference.child("contatos")
                            .child(idUsuarioLogado)
                            .child(idContato);

                    //Instancia da classe contatos
                    Contato contato = new Contato(uContato.getNome(), uContato.getEmail());
                    contato.setId(idContato);
                    contato.setValor(uContato.getValor());

                    databaseReference.setValue(contato);

                    //Adiciona a lista e contatos
                    contatos.add(contato);

                    result = true;
                } else {
                    result = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return result;
    }

    /**
     * Retorna a lista de contatos
     * @return ArrayList de contatos
     */
    public ArrayList<Contato> getContatos() {
        return contatos;
    }
}
