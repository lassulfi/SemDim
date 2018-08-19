package br.com.semdimapp.semdim.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.controller.LoginController;
import br.com.semdimapp.semdim.helper.ToastHelper;

public class LoginActivity extends AppCompatActivity {

    //Atributos
    private Button loginButton;
    private Button facebookLoginButon;
    private TextView cadastroTextView;
    private EditText emailEditText;
    private EditText passwordEditText;

    private Toast mToast;

    private LoginController loginController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Recupera os elementos da tela
        emailEditText = (EditText) findViewById(R.id.tb_username);
        passwordEditText = (EditText) findViewById(R.id.tb_password);

        loginButton = (Button) findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupera os elementos digitados
                String email = emailEditText.getText().toString();
                String password = emailEditText.getText().toString();

                //Valida se o usuario passou alguma informação
                if(email.length() == 0 || password.length() == 0){
                    ToastHelper.showToast(LoginActivity.this,
                            mToast,
                            getResources().getString(R.string.empty_username_password_edittext).toString(),
                            Toast.LENGTH_SHORT);
                    return;
                }

                //Instancia da classe LoginController
                loginController = new LoginController();

                //Verifica se o usuario já está logado
                if(loginController.verificarUsuarioLogado()){
                    abrirTelaPrincipal();
                } else{
                    //Realiza o processo de login
                    loginController.logarUsuario(email, password, LoginActivity.this);
                    //Se o login ocorreu com sucesso, abre a tela principal
                    if(loginController.isSuccess()){
                        abrirTelaPrincipal();
                    } else{
                        ToastHelper.showToast(LoginActivity.this,
                                mToast,
                                getResources().getString(R.string.falha_login_message).toString(),
                                Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    /**
     * Abre a tela de cadastro de usuario
     * @param view
     */
    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);

        startActivity(intent);
    }

    /**
     * Abre a MainActivity
     */
    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
