package br.com.semdimapp.semdim.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.activity.MainActivity;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.exceptions.UsuarioException;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.helper.ToastHelper;
import br.com.semdimapp.semdim.model.Usuario;

public class UsuarioController {

    //Atributos

    private static final String DATABASEERROR_ONCANCELLED = "database:onCancelled";

    private Usuario usuario;
    private boolean success;
    private String cadastroMessage;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private static UsuarioController instance = null;

    //Construtor
    private UsuarioController(){
        success = false;
        this.cadastroMessage = null;
    }

    public static UsuarioController getInstance() {
        if(instance == null){
            instance = new UsuarioController();
        }

        return instance;
    }

    /**
     * Define o atributo usuario
     * @param nome nome do usuario
     * @param email e-mail do usuario
     * @param senha senha informada
     */
    public void setUsuario(String nome, String email, String senha){
        usuario = new Usuario(nome, email, senha);
    }

    /**
     * Define um usuario a partir de um objeto
     * @param usuario objeto da classe Usuario
     */
    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    //Métodos

    /**
     * Cadastra o usuario no banco de dados do Firebase
     */
    public void cadastrarUsuario(final Context context, final Toast mToast) throws UsuarioException{

        if(usuario == null){
            success = false;
            throw new UsuarioException("Dados de usuario inválido");
        }

        //Recupera a instancia de autenticacao do Firebase
        auth = FirebaseConfig.getFirebaseAuth();

        //Cadastro do usuario no firebase
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ToastHelper.showToast(context,
                            mToast,
                            context.getResources().getString(R.string.sucesso_cadastro),
                            Toast.LENGTH_SHORT);
                    //Definicao do ID do usuario definido pelo firebase
                    FirebaseUser firebaseUser = task.getResult().getUser();

                    String userIdentifier = Base64Custom.encodeBase64(usuario.getEmail());

                    usuario.setId(userIdentifier);

                    saveUserToDatabase(usuario);

                    cadastroMessage = "Cadastro realizado com sucesso";
                    success = true;
                } else {
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        cadastroMessage = "Digite uma senha mais forte, " +
                                "contendo caracteres e números";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        cadastroMessage = "Email inválido. Informe um e-mail válido";
                    } catch (FirebaseAuthUserCollisionException e){
                        cadastroMessage = "E-mail já cadastrado.";
                    }
                    catch (Exception e){
                        cadastroMessage = "Erro ao efetuar cadastro.";
                    }

                }
            }
        });
    }

    /**
     * Retorna o status da operação.
     * @return
     */
    public boolean isSuccess(){
        return success;
    }

    public String getUsername(){
        return usuario.getNome();
    }

    /**
     * Retorna o usuario cadastrado
     * @return objeto Usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Retorna o usuario logado no app
     * @return usuario que está logado no App
     */
    public void encontrarUsuarioLogado(Context context){

        usuario = null;

        Preferences preferences = new Preferences(context);

        final String idUsuarioLogado = preferences.getUsuarioID();

        //Recupera a instancia do DatabaseReference
        databaseReference = FirebaseConfig.getDatabaseReference()
                .child("usuarios")
                .child(idUsuarioLogado);

        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    usuario = dataSnapshot.getValue(Usuario.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATABASEERROR_ONCANCELLED, databaseError.getMessage());
            }
        };

        databaseReference.addListenerForSingleValueEvent(eventListener);
    }

    /**
     * Adiciona o usuario ao banco de dados
     * @param u Objeto Usuario
     */
    public void saveUserToDatabase(Usuario u){
        //Instancia a referencia do banco de dados do Firebase
        databaseReference = FirebaseConfig.getDatabaseReference();
        //Cria o nó do Firebase que adiciona o usuario
        databaseReference.child("usuarios").child(u.getId()).setValue(u);
    }

    /**
     * Retorna a mensagem de erro de exceção do Firebase
     * @return
     */
    public String getCadastroMessage(){
        return cadastroMessage;
    }
}
