package br.com.semdimapp.semdim.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.helper.Preferences;

//TODO: condificar o chat em grupo
public class ConversaGrupoActivity extends AppCompatActivity {

    //Atributos

    //Elementos da tela
    private Toolbar toolbar;
    private EditText mensagemEditText;
    private ImageButton enviarButton;
    private ListView conversaListView;

    private String idUsuarioLogado, nomeUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa_grupo);

        //Recupera os elementos da tela
        toolbar = (Toolbar) findViewById(R.id.toolbar_conversa_grupo);
        mensagemEditText = (EditText) findViewById(R.id.mensagem_edittext);
        enviarButton = (ImageButton) findViewById(R.id.enviar_imagebutton);
        conversaListView = (ListView) findViewById(R.id.conversa_listview);

        //Recupera o id e nome do usuario logado
        Preferences preferences = new Preferences(ConversaGrupoActivity.this);
        idUsuarioLogado = preferences.getUsuarioID();
        nomeUsuarioLogado = preferences.getUsername();

        Bundle extra = getIntent().getExtras();


    }
}
