package br.com.semdimapp.semdim.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.model.Usuario;

/**
 * Classe responsável pelas ações de login do Usuario no sistema
 */
public class LoginController {

    //Atributos
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private String loggedUserId;

    private boolean success;

    private ValueEventListener userValueEventListener;

    private UsuarioController usuarioController;

    public LoginController(){

        this.firebaseAuth = FirebaseConfig.getFirebaseAuth();
        this.success = false;

        this.usuarioController = new UsuarioController();
    }

    public boolean verificarUsuarioLogado(){

        if(firebaseAuth.getCurrentUser() != null){
            return true;
        }

        return false;
    }

    public void logarUsuario(final String email, String password, final Context context){

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Verifica se o processo de login foi bem sucedido
                if(task.isSuccessful()){

                    loggedUserId = Base64Custom.encodeBase64(email);

                    //Recupera a instancia do Firebase
                    databaseReference = FirebaseConfig.getDatabaseReference()
                            .child("usuarios")
                            .child(loggedUserId);

                    //Realiza a consulta no Firebase
                    userValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            usuarioController.setUsuario(dataSnapshot.getValue(Usuario.class));

                            //Salva o email do usuario nas preferencias
                            Preferences preferences = new Preferences(context);
                            preferences.saveUserPreferences(loggedUserId,
                                    usuarioController.getUsername());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    success = true;
                }
            }
        });

    }

    public boolean isSuccess(){
        return success;
    }

    /**
     * Faz o logoff do aplicativo
     */
    public void fazerLogoff(){
        firebaseAuth.signOut();
    }
}
