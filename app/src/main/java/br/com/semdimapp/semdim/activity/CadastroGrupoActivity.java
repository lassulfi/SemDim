package br.com.semdimapp.semdim.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.adapter.ContatoAdapter;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.ContatoController;
import br.com.semdimapp.semdim.controller.GrupoController;
import br.com.semdimapp.semdim.controller.UsuarioController;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.helper.ToastHelper;
import br.com.semdimapp.semdim.model.Contato;
import br.com.semdimapp.semdim.model.Usuario;

public class CadastroGrupoActivity extends AppCompatActivity {

    //Atributos
    private static final String DATABASEERROR_ONCANCELLED = "database:onCancelled";

    private EditText nomeGrupoTextView;
    private TextView emptyTextView;
    private ListView contatosListView;
    private Button criarGrupoButton;

    private Toolbar toolbar;

    private ArrayList<Contato> contatos;
    private ArrayList<Contato> contatosSelecionados;
    private Usuario usuario;

    private ContatoController contatoController;
    private UsuarioController usuarioController;
    private GrupoController grupoController;

    private Toast mToast;

    private ArrayAdapter adapter;

    private DatabaseReference usuarioDatabaseReference;
    private ValueEventListener usuarioEventListener;

    private DatabaseReference grupoDatabaseReference;
    private ValueEventListener grupoValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);

        //Recuera a instancia do usuario logado no App
        recuperarUsuarioLogado();
        usuario = usuarioController.getUsuario();

        //Recupera a lista de contatos cadastrados
        contatoController = ContatoController.getInstance(CadastroGrupoActivity.this);
        contatos = contatoController.getContatos();

        //Instancia da lista que armazena os contatos selecionados
        contatosSelecionados = new ArrayList<>();

        //Configuração da toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cadastrogrupoactivity_title);
        setSupportActionBar(toolbar);

        //Recupera as textviews
        nomeGrupoTextView = (EditText) findViewById(R.id.group_name_edittext);
        emptyTextView = (TextView) findViewById(R.id.contatos_empty_view);

        //Recupera os botoes
        criarGrupoButton = (Button) findViewById(R.id.grupos_criar_button);

        contatosListView = (ListView) findViewById(R.id.grupos_contatos_listview);
        contatosListView.setEmptyView(emptyTextView);

        adapter = new ContatoAdapter(CadastroGrupoActivity.this, contatos);

        contatosListView.setAdapter(adapter);

        //Adiciona um evento de clique a lista para selecionar um contato
        contatosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Recupera o contato clicado
                Contato contatoSelecionacionado = contatos.get(position);

                contatosSelecionados.add(contatoSelecionacionado);
            }
        });

        //Define a açao do botão
        criarGrupoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupera o nome do grupo
                String nomeDoGrupo = nomeGrupoTextView.getText().toString();

                //Se nao for inforado o nome do grupo, exibe um
                if(nomeDoGrupo.isEmpty()){
                    ToastHelper.showToast(CadastroGrupoActivity.this,
                            mToast,
                            getResources().getString(R.string.nome_grupo_invalido),
                            Toast.LENGTH_SHORT);
                    return;
                }

                //Verifica se nenhum contato foi selecionado
                if(contatosSelecionados.isEmpty()){
                    ToastHelper.showToast(CadastroGrupoActivity.this,
                            mToast,
                            getResources().getString(R.string.nenhum_contato_selecionado),
                            Toast.LENGTH_SHORT);
                    return;
                }

                //Cadastra um novo grupo no banco de dados
                grupoController = GrupoController.getInstance(CadastroGrupoActivity.this);
                grupoController.criarGrupo(nomeDoGrupo,
                        contatosSelecionados);

                //Recupera a DatabaseReference do Grupo
                grupoDatabaseReference = grupoController.getGruposDatabaseReference();

                //Recupera o ValueEventListener do Grupo
                grupoValueEventListener = grupoController.getGrupoEventListener();

                grupoDatabaseReference.addListenerForSingleValueEvent(grupoValueEventListener);

                if(grupoController.isSucess()){
                    ToastHelper.showToast(CadastroGrupoActivity.this,
                            mToast,
                            getResources().getString(R.string.cadastrar_grupo_sucesso),
                            Toast.LENGTH_SHORT);
                    //Finaliza a Activity
                    finish();
                } else {
                    ToastHelper.showToast(CadastroGrupoActivity.this,
                            mToast,
                            getResources().getString(R.string.cadastrar_grupo_falha),
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuarioDatabaseReference.addListenerForSingleValueEvent(usuarioEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioDatabaseReference.removeEventListener(usuarioEventListener);
        grupoDatabaseReference.removeEventListener(grupoValueEventListener);
    }

    /**
     * Recupera o usuario logado no firebase
     */
    private void recuperarUsuarioLogado(){

        usuarioController = UsuarioController.getInstance();

        Preferences preferences = new Preferences(CadastroGrupoActivity.this);
        final String idUsuarioLogado = preferences.getUsuarioID();

        usuarioDatabaseReference = FirebaseConfig.getDatabaseReference()
                .child("usuarios")
                .child(idUsuarioLogado);

        usuarioEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                  usuarioController.setUsuario(dataSnapshot.getValue(Usuario.class));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATABASEERROR_ONCANCELLED, databaseError.getMessage());
            }
        };
    }
}
