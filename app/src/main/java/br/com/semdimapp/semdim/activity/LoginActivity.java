package br.com.semdimapp.semdim.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.LoginController;
import br.com.semdimapp.semdim.controller.UsuarioController;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.helper.ToastHelper;
import br.com.semdimapp.semdim.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    //Atributos
    private static final String DATABASEERROR = "databaseError:Error";

    private Button loginButton;
    private Button facebookLoginButon;
    private EditText emailEditText;
    private EditText passwordEditText;

    private String email, password, loggedUserId;

    private Toast mToast;

    private LoginController loginController;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController = new LoginController();

        //Recupera os elementos da tela
        emailEditText = (EditText) findViewById(R.id.tb_username);
        passwordEditText = (EditText) findViewById(R.id.tb_password);

        loginButton = (Button) findViewById(R.id.btn_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recupera os elementos digitados
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                //Valida se o usuario passou alguma informação
                if (email.isEmpty() || password.isEmpty()) {
                    ToastHelper.showToast(LoginActivity.this,
                            mToast,
                            getResources().getString(R.string.empty_username_password_edittext).toString(),
                            Toast.LENGTH_SHORT);
                    return;
                }

                logarUsuario();
            }
        });
    }

    private void logarUsuario(){

        auth = FirebaseConfig.getFirebaseAuth();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loggedUserId = Base64Custom.encodeBase64(email);

                    FirebaseUser user = auth.getCurrentUser();

                    Preferences preferences = new Preferences(LoginActivity.this);
                    preferences.saveUserPreferences(loggedUserId, user.getEmail());

                    //Recupera a instancia do Firebase
                    databaseReference = FirebaseConfig.getDatabaseReference()
                            .child("usuarios")
                            .child(loggedUserId);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuario = dataSnapshot.getValue(Usuario.class);

                            //Salva o email do usuario nas preferencias
                            Preferences preferences = new Preferences(LoginActivity.this);
                            preferences.saveUserPreferences(loggedUserId, usuario.getNome());

                            Log.i("PREFERENCES:USERNAME ", preferences.getUsername());
                            Log.i("PREFERENCES:USERID ", preferences.getUsuarioID());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(DATABASEERROR, databaseError.getMessage());
                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListener);

                    abrirTelaPrincipal();
                } else {
                    ToastHelper.showToast(LoginActivity.this,
                            mToast,
                            getString(R.string.falha_login_message),
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(loginController.verificarUsuarioLogado(LoginActivity.this)){
            abrirTelaPrincipal();
        }
    }

    /**
     * Abre a tela de cadastro de usuario
     *
     * @param view
     */
    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);

        startActivity(intent);
    }

    /**
     * Abre a MainActivity
     */
    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
