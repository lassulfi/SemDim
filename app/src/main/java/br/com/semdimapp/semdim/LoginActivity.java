package br.com.semdimapp.semdim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    //Atributos
    private Button loginButton;
    private Button facebookLoginButon;
    private TextView cadastroTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Abre a tela de cadastro de usuario
     * @param view
     */
    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);

        startActivity(intent);
    }
}
