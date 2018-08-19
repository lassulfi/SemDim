package br.com.semdimapp.semdim.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import br.com.semdimapp.semdim.activity.CadastroUsuarioActivity;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.exceptions.UsuarioException;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.model.Usuario;

public class UsuarioController {

    //Atributos
    private Usuario usuario;
    private boolean success;
    private String firabaseExcepionMessage;

    //Construtor
    public UsuarioController(){
        this.success = false;
        this.firabaseExcepionMessage = null;
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
    public void cadastrarUsuario(final Context context) throws UsuarioException{

        if(usuario == null){
            success = false;
            throw new UsuarioException("Dados de usuario inválido");
        }

        //Recupera a instancia de autenticacao do Firebase
        FirebaseAuth auth = FirebaseConfig.getFirebaseAuth();

        //Cadastro do usuario no firebase
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Definicao do ID do usuario definido pelo firebase
                    FirebaseUser firebaseUser = task.getResult().getUser();

                    String userIdentifier = Base64Custom.encodeBase64(usuario.getEmail());

                    usuario.setId(userIdentifier);

                    saveUserToDatabase(usuario);

                    //Salva o nome de usuario e senha nas preferencias para fazer o login
                    Preferences preferences = new Preferences(context);
                    preferences.saveUserPreferences(userIdentifier, usuario.getSenha());

                    success = true;
                } else {
                    try{
                        success = false;
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        firabaseExcepionMessage = "Digite uma senha mais forte, " +
                                "contendo caracteres e números";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        firabaseExcepionMessage = "Email inválido. Informe um e-mail válido";
                    } catch (FirebaseAuthUserCollisionException e){
                        firabaseExcepionMessage = "E-mail já cadastrado.";
                    }
                    catch (Exception e){
                        firabaseExcepionMessage = "Erro ao efetuar cadastro.";
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
     * Adiciona o usuario ao banco de dados
     * @param u Objeto Usuario
     */
    public void saveUserToDatabase(Usuario u){
        //Instancia a referencia do banco de dados do Firebase
        DatabaseReference databaseReference = FirebaseConfig.getDatabaseReference();
        //Cria o nó do Firebase que adiciona o usuario
        databaseReference.child("usuarios").setValue(u);
    }

    /**
     * Retorna a mensagem de erro de exceção do Firebase
     * @return
     */
    public String getFirabaseExcepionMessage(){
        return firabaseExcepionMessage;
    }
}
