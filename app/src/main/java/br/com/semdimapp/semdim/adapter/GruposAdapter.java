package br.com.semdimapp.semdim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.semdimapp.semdim.R;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if (grupos != null){
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_grupos, parent, false);

            //Recupera os elementos para exibição
            TextView nomeGrupoTextView = (TextView) view.findViewById(R.id.nome_grupo_tv);
            TextView valorGrupoTextView = (TextView) view.findViewById(R.id.valor_total_grupo_tv);

            Grupo grupo = grupos.get(position);
            nomeGrupoTextView.setText(grupo.getNome());
            valorGrupoTextView.setText(String.valueOf(grupo.getValor()));

        }
        
        return view;
    }
}
