package br.com.semdimapp.semdim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.semdimapp.semdim.model.Grupo;

public class GruposAdapter extends ArrayAdapter<Grupo>{

    private ArrayList<Grupo> grupos;
    private Context context;

    /**
     * Construtor
     * @param context
     * @param objects
     */
    public GruposAdapter(Context context, ArrayList<Grupo> objects) {
        super(context, 0, objects);
    }


}
