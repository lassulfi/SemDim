package br.com.semdimapp.semdim.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.UsuarioController;
import br.com.semdimapp.semdim.exceptions.UsuarioException;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.ToastHelper;
import br.com.semdimapp.semdim.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    //Atributos
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private Button cadastrarButton;

    private Toast mToast;

    private String username, email, password;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    private ValueEventListener mValueEventListener;

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
                cadastro = UsuarioController.getInstance();

                //Criacao da instacia de usuario
                cadastro.setUsuario(username, email, password);

                mAuth = FirebaseConfig.getFirebaseAuth();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser mUser = task.getResult().getUser();

                            String mUserId = Base64Custom.encodeBase64(mUser.getEmail());

                            Usuario usuario = cadastro.getUsuario();
                            usuario.setId(mUserId);

                            cadastro.saveUserToDatabase(usuario);

                            ToastHelper.showToast(CadastroUsuarioActivity.this, mToast,
                                    "Cadastro relizado com sucesso",Toast.LENGTH_SHORT);

                            abrirLoginActivity();
                        } else {
                            ToastHelper.showToast(CadastroUsuarioActivity.this, mToast,
                                    "Erro ao realizar o cadastro",Toast.LENGTH_SHORT);
                        }
                    }
                });
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
