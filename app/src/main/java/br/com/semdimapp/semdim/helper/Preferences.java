package br.com.semdimapp.semdim.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe para salvar as preferencias do usuario no SharedPreferences
 *
 */
public class Preferences {

    //Atributos
    private Context context;
    private SharedPreferences preferences;
    private final String FILE_NAME = "semdim.Preferences";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String KEY_IDENTIFIER = "identificadorUsuarioLogado";
    private final String KEY_USERNAME = "nomeUsuarioLogado";

    /**
     * Construtor da classe Preferences
     * @param context contexto da Activity
     */
    public Preferences(Context context){

        this.context = context;
        this.preferences = context.getSharedPreferences(FILE_NAME, MODE);
        this.editor = preferences.edit();
    }

    public void saveUserPreferences(String usuarioID, String username){
        editor.putString(KEY_IDENTIFIER, usuarioID);
        editor.putString(KEY_USERNAME, username);

        editor.commit();
    }

    public String getUsuarioID(){
        return preferences.getString(KEY_IDENTIFIER, null);
    }

    public String getUsername(){
        return preferences.getString(KEY_USERNAME, null);
    }

}
