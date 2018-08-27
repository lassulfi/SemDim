package br.com.semdimapp.semdim.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.adapter.ContatoAdapter;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.ContatoController;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.model.Contato;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatoFragment extends Fragment {

    //Atributos
    private ArrayList<Contato> contatos;
    private ContatoController contatoController;

    private ListView listView;
    private ArrayAdapter adapter;
    private TextView emptyTextView;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private ProgressBar progressBar;

    public ContatoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Recupera a ArrayList de Contatos
        contatoController = ContatoController.getInstance(getContext());
        contatos = contatoController.getContatos();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contato, container, false);

        //Recupera a progressbar
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        //Recupera a listView
        listView = (ListView) view.findViewById(R.id.contatos_listview);
        emptyTextView = (TextView) view.findViewById(R.id.contatos_empty_view);
        emptyTextView.setText(R.string.contato_empty_listview);
        listView.setEmptyView(emptyTextView);

        adapter = new ContatoAdapter(getActivity(), contatos);

        listView.setAdapter(adapter);

        //Recupera a instancia do Firebase
        Preferences preferences = new Preferences(getActivity());
        String idUsuarioLogado = preferences.getUsuarioID();
        databaseReference = FirebaseConfig.getDatabaseReference()
                .child("contatos").child(idUsuarioLogado);

        //Listener para recuperar os dados
        valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Apaga todos os contatos
                contatoController.apagarTodos();
                contatos.clear();

                progressBar.setVisibility(View.VISIBLE);

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Contato contato = data.getValue(Contato.class);
                    contatoController.adicionarContato(contato);
                }

                contatos = contatoController.getContatos();

                progressBar.setVisibility(View.INVISIBLE);
                
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("database:databaseError", databaseError.getMessage());
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
