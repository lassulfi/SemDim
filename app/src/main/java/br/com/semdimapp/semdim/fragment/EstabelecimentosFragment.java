package br.com.semdimapp.semdim.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.adapter.EstabelecimentoAdapter;
import br.com.semdimapp.semdim.config.FirebaseConfig;
import br.com.semdimapp.semdim.controller.EstabelecimentoController;
import br.com.semdimapp.semdim.model.Estabelecimento;

/**
 * A simple {@link Fragment} subclass.
 */

public class EstabelecimentosFragment extends Fragment {

    //Atributos
    private final static String DATABASE_ERROR = "database:Error";

    private ArrayList<Estabelecimento> estabelecimentos;
    private EstabelecimentoController estabelecimentoController;

    private ListView listView;
    private TextView emptyTextView;
    private ArrayAdapter adapter;

    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    public EstabelecimentosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        estabelecimentoController = EstabelecimentoController.getInstance();
        estabelecimentos = estabelecimentoController.getEstabelecimentos();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_estabelecimentos, container, false);

        listView = (ListView) view.findViewById(R.id.estabelecimentos_listview);
        emptyTextView = (TextView) view.findViewById(R.id.estabelecimentos_empty_textview);
        emptyTextView.setText(R.string.estabelecimento_empty_listview);
        listView.setEmptyView(emptyTextView);

        adapter = new EstabelecimentoAdapter(getActivity(), estabelecimentos);

        listView.setAdapter(adapter);

        //Recupera a intancia do firebase
        databaseReference = FirebaseConfig.getDatabaseReference().child("estabelecimentos");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Apaga todos os dados na instacia do controller
                estabelecimentoController.apagarTodos();
                estabelecimentos.clear();

                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++){
                    String id = String.valueOf(i + 1);
                    Estabelecimento estabelecimento = dataSnapshot.child(id)
                            .getValue(Estabelecimento.class);
                    estabelecimentoController.adicionarEstabelecimento(estabelecimento);
                }

                estabelecimentos = estabelecimentoController.getEstabelecimentos();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(DATABASE_ERROR, databaseError.getMessage());
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
