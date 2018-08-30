package br.com.semdimapp.semdim.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.activity.ConversaGrupoActivity;
import br.com.semdimapp.semdim.adapter.GruposAdapter;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.GrupoController;
import br.com.semdimapp.semdim.helper.Preferences;
import br.com.semdimapp.semdim.model.Contato;
import br.com.semdimapp.semdim.model.Grupo;

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), ConversaGrupoActivity.class);

                Grupo grupo = grupos.get(position);

                //Cria dois vetores, um com os membros do grupo e outro com email de cada grupo
                ArrayList<Contato> contatos = grupo.getContatos();
                String[] idContatos = new String[contatos.size()];
                String[] nomeContatos = new String[contatos.size()];

                for(int i = 0; i < contatos.size(); i++){
                    idContatos[i] = contatos.get(i).getId();
                    nomeContatos[i] = contatos.get(i).getNome();
                }

                intent.putExtra("NOME_DO_GRUPO", grupo.getNome());
                intent.putExtra("ID_DO_GRUPO", grupo.getId());
                intent.putExtra("CRIADOR_GRUPO", grupo.getCriador().getNome());
                intent.putExtra("ID_CRIADOR", grupo.getCriador().getId());
                intent.putExtra("ID_CONTATOS", idContatos);
                intent.putExtra("NOMES_CONTATOS", nomeContatos);

                startActivity(intent);

            }
        });

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
