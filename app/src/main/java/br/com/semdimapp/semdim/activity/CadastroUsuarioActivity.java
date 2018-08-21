package br.com.semdimapp.semdim.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.controller.UsuarioController;
import br.com.semdimapp.semdim.exceptions.UsuarioException;
import br.com.semdimapp.semdim.helper.ToastHelper;

public class CadastroUsuarioActivity extends AppCompatActivity {

    //Atributos
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private Button cadastrarButton;

    private Toast mToast;

    private String username, email, password;

    private UsuarioController cadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        //Recupera os elementos da tela
        usernameEditText = (EditText) findViewById(R.id.tb_cadastro_username);
        emailEditText = (EditText) findViewById(R.id.tb_cadastro_email);
        passwordEditText = (EditText) findViewById(R.id.tb_cadastro_password);

        cadastrarButton = (Button) findViewById(R.id.btn_cadastrar);

        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupera o username e senha
                username = usernameEditText.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                //Verifica se as informações não são nulas
                if(username.length() == 0 && password.length() == 0){
                    ToastHelper.showToast(CadastroUsuarioActivity.this,
                            mToast,
                            getResources().getString(R.string.empty_username_password_edittext).toString(),
                            Toast.LENGTH_SHORT);
                    return;
                }

                //Intancia da classe cadastro
                cadastro = new UsuarioController();

                //Criacao da instacia de usuario
                cadastro.setUsuario(username, email, password);

                try {
                    cadastro.cadastrarUsuario(CadastroUsuarioActivity.this, mToast);
                } catch (UsuarioException e){
                    ToastHelper.showToast(CadastroUsuarioActivity.this,
                            mToast,
                            e.getMessage(),
                            Toast.LENGTH_SHORT);
                } catch (Exception e){
                    ToastHelper.showToast(CadastroUsuarioActivity.this,
                            mToast,
                            e.getMessage(),
                            Toast.LENGTH_SHORT);
                }

                if(cadastro.isSuccess()){
                    abrirLoginActivity();
                }
            }
        });


    }

    public void abrirLoginActivity(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        //Termina essa activity
        finish();
    }
}
