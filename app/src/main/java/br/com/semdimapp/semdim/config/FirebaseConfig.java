package br.com.semdimapp.semdim.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Classe de configuração do Firebase
 */
public final class FirebaseConfig {

    //Atributos
    private static DatabaseReference databaseReference;
    private static FirebaseAuth firebaseAuth;

    //Métodos

    /**
     * Retorna uma instancia do banco de dados do firebase
     * @return DatabaseReference
     */
    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAuth(){

        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }

        return firebaseAuth;
    }

}
