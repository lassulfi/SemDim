package br.com.semdimapp.semdim.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
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
import br.com.semdimapp.semdim.exceptions.UsuarioException;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.helper.ToastHelper;
import br.com.semdimapp.semdim.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    //Atributos
    private static final String DATABASEERROR = "databaseError:Error";

    private static final String TAG = "FACEBOOKLOGIN";

    private Button loginButton;
    private LoginButton facebookLoginButon;
    private EditText emailEditText;
    private EditText passwordEditText;

    private UsuarioController usuarioController;

    private String email, password, loggedUserId;

    private Toast mToast;

    private LoginController loginController;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private ValueEventListener valueEventListener;

    private CallbackManager mCallbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cria um CallbackManager
        mCallbackManager = CallbackManager.Factory.create();

        loginController = new LoginController();

        //Recupera os elementos da tela
        emailEditText = (EditText) findViewById(R.id.tb_username);
        passwordEditText = (EditText) findViewById(R.id.tb_password);

        loginButton = (Button) findViewById(R.id.btn_login);

        facebookLoginButon = (LoginButton) findViewById(R.id.btn_facebook_login);
        facebookLoginButon.setReadPermissions("email", "public_profile");
        facebookLoginButon.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"facebook:onSuccess" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

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

        facebookLoginButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void handleFacebookAccessToken(AccessToken token){
        Log.d(TAG, "handleFacebookAccessToken" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithCredentials:success");
                    final FirebaseUser user = auth.getCurrentUser();

                    String userIdEmail = user.getEmail();
                    String userId = Base64Custom.encodeBase64(userIdEmail);

                    Preferences preferences = new Preferences(LoginActivity.this);
                    preferences.saveUserPreferences(loggedUserId, user.getEmail());

                    //Recupera a instancia do Firebase
                    databaseReference = FirebaseConfig.getDatabaseReference()
                            .child("usuarios")
                            .child(loggedUserId);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){

                                //Caso o usuario já esteja cadastrado apenas salva o seu id nas preferencias
                                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                //Salva o email do usuario nas preferencias
                                Preferences preferences = new Preferences(LoginActivity.this);
                                preferences.saveUserPreferences(loggedUserId, usuario.getNome());

                                Log.i("PREFERENCES:USERNAME ", preferences.getUsername());
                                Log.i("PREFERENCES:USERID ", preferences.getUsuarioID());
                            } else {
                                //Caso ainda não esteja cadastrado cria uma instancia no banco de dados
                                usuarioController = UsuarioController.getInstance();
                                usuarioController.setUsuario(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
                                try {
                                    usuarioController.cadastrarUsuario(LoginActivity.this, mToast);
                                } catch (UsuarioException e){

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(DATABASEERROR, databaseError.getMessage());
                        }
                    };
                    databaseReference.addListenerForSingleValueEvent(valueEventListener);

                    abrirTelaPrincipal();

                } else {
                    Log.d(TAG, "siginWithCredentials:failure", task.getException());
                    ToastHelper.showToast(LoginActivity.this, mToast,
                            "Falha ao autenticar com o Facebook", Toast.LENGTH_SHORT);
                }

            }
        });
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
