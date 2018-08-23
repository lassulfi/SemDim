package br.com.semdimapp.semdim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.model.Contato;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    //Atributos
    private ArrayList<Contato> contatos;
    private Context context;

    /**
     * Construtor da classe
     *
     * @param context
     * @param objects
     */
    public ContatoAdapter(Context context, ArrayList<Contato> objects) {
        super(context, 0, objects);
        this.contatos = objects;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Criando a view
        View view = null;

        //Valida se a lista de contatos está vazia
        if (contatos != null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta a view a partir do xml
            view = inflater.inflate(R.layout.lista_contatos, parent, false);

            //Recupera os elementos para exibicao
            TextView nomeContato = (TextView) view.findViewById(R.id.nome_contato_textview);
            TextView valorContato = (TextView) view.findViewById(R.id.valor_contato_textview);

            //Passa os dados da arraylist para a classe
            Contato contato = contatos.get(position);
            nomeContato.setText(contato.getNome());
            valorContato.setText(String.valueOf(contato.getValor()));

        }

        return view;
    }
}
