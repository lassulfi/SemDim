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

            view = inflater.inflate(R.layout.lista_estabelecimentos, parent, false);

            TextView nomeEstabelecimento = (TextView) view.findViewById(R.id.nome_estabelecimento_textview);
            TextView valorPromocao = (TextView) view.findViewById(R.id.valor_promocao_textview);

            String valorPromocaoText = context.getString(R.string.valor_promocao_vazio_textview);

            Estabelecimento estabelecimento = estabelecimentos.get(position);

            nomeEstabelecimento.setText(estabelecimento.getNome());
            valorPromocaoText += estabelecimento.obterMenorValor();

            valorPromocao.setText(valorPromocaoText);
        }

        return view;
    }
}
