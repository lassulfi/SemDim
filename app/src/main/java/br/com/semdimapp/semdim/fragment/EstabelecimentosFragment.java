package br.com.semdimapp.semdim.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.model.Estabelecimento;

/**
 * A simple {@link Fragment} subclass.
 */

//TODO: codificar o fragment de estabelecimentos
public class EstabelecimentosFragment extends Fragment {

    //Atributos
    private ArrayList<Estabelecimento> estabelecimentos;

    private ListView listView;
    private TextView emptyTextView;
    private ArrayAdapter adapter;

    public EstabelecimentosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estabelecimentos, container, false);

        listView = (ListView) view.findViewById(R.id.estabelecimentos_listview);
        emptyTextView = (TextView) view.findViewById(R.id.estabelecimentos_empty_textview);
        emptyTextView.setText(R.string.estabelecimento_empty_listview);
        listView.setEmptyView(emptyTextView);

        return view;
    }

}
