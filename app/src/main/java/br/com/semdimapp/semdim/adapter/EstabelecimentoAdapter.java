package br.com.semdimapp.semdim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.semdimapp.semdim.model.Estabelecimento;

public class EstabelecimentoAdapter extends ArrayAdapter<Estabelecimento> {

    //Atributos
    private Context context;
    private ArrayList<Estabelecimento> estabelecimentos;

    public EstabelecimentoAdapter( Context context, ArrayList<Estabelecimento> objects) {
        super(context, 0, objects);
        this.context = context;
        this.estabelecimentos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Criando a view
        View view = null;

        if(estabelecimentos != null){
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);


        }

        return view;
    }
}
