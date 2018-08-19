package br.com.semdimapp.semdim.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Classe que contém um método estático que exibe um Toast
 */
public class ToastHelper {

    public static void showToast(Context context, Toast mToast, String message, int duration){
        if(mToast != null){
            mToast.cancel();
        }

        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }
}
