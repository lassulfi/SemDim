package br.com.semdimapp.semdim.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.adapter.GruposAdapter;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.GrupoController;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.model.Grupo;
import br.com.semdimapp.semdim.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class GruposFragment extends Fragment {

    //Atributos
    private static final String DATABASEERROR = "database:databaseError";

    private ListView listView;
    private TextView emptyTextView;

    private ArrayAdapter adapter;
    private ArrayList<Grupo> grupos;

    private GrupoController grupoController;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    public GruposFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        grupoController = GrupoController.getInstance(getContext());
        grupos = grupoController.getGrupos();

        View view = inflater.inflate(R.layout.fragment_grupos, container, false);

        listView = (ListView) view.findViewById(R.id.grupos_listview);
        emptyTextView = (TextView) view.findViewById(R.id.grupos_empty_listview);
        emptyTextView.setText(getResources().getString(R.string.grupos_empty_listview));
        listView.setEmptyView(emptyTextView);

        adapter = new GruposAdapter(getActivity(), grupos);

        listView.setAdapter(adapter);

        //Recupera o usuario ID do usuario logado
        Preferences preferences = new Preferences(getActivity());
        String idUsuarioLogado = preferences.getUsuarioID();

        databaseReference = FirebaseConfig.getDatabaseReference()
                .child("grupos").child(idUsuarioLogado);

        valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Apaga os grupos previamente cadastrados
                grupos.clear();
                grupoController.apagarTodos();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Grupo grupo = data.getValue(Grupo.class);
                    grupoController.adicionarGruposNaLista(grupo);
                }

                grupos = grupoController.getGrupos();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATABASEERROR, databaseError.getMessage());
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        databaseReference.removeEventListener(valueEventListener);
    }
}
