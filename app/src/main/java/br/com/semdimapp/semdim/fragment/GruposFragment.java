package br.com.semdimapp.semdim.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.model.Grupo;
import br.com.semdimapp.semdim.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class GruposFragment extends Fragment {

    //Atributos
    private ListView listView;
    private TextView emptyTextView;

    private ArrayAdapter adapter;
    private ArrayList<Grupo> grupos;

    public GruposFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        grupos = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_grupos, container, false);

        listView = (ListView) view.findViewById(R.id.grupos_listview);
        emptyTextView = (TextView) view.findViewById(R.id.grupos_empty_listview);
        emptyTextView.setText(getResources().getString(R.string.grupos_empty_listview));
        listView.setEmptyView(emptyTextView);

        return view;
    }



}
