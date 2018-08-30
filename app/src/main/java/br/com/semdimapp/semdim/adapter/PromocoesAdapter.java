package br.com.semdimapp.semdim.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.helper.ToastHelper;
import br.com.semdimapp.semdim.model.Promocao;

/**
 * Adapter para a listview de promocoes
 */
public class PromocoesAdapter extends ArrayAdapter<Promocao>{

    //Atributos
    private Context context;
    private ArrayList<Promocao> promocoes;

    private Toast mToast;


    /**
     * Construtor
     * @param context
     * @param objects
     */
    public PromocoesAdapter(Context context, ArrayList<Promocao> objects) {
        super(context, 0, objects);
        this.context = context;
        this.promocoes = objects;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        View view = null;

        if(promocoes != null){

            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_promocoes, parent, false);

            //Recupera os elementos para exibicao
            ImageView promocaoImageView = (ImageView) view.findViewById(R.id.promocao_imageview);
            TextView nomePromocao = (TextView) view.findViewById(R.id.nome_promocao_textview);
            TextView descricaoPromocao = (TextView) view.findViewById(R.id.descricao_promocao_textview);
            TextView valorPromocao = (TextView) view.findViewById(R.id.valor_promocao_textview);
            ImageButton comprar = (ImageButton) view.findViewById(R.id.btn_comprar);

            Promocao promocao = promocoes.get(position);

            nomePromocao.setText(promocao.getNome());
            descricaoPromocao.setText(promocao.getDescricao());

            String moeda = context.getString(R.string.simbolo_moeda);

            valorPromocao.setText(moeda + promocao.getValor());

            //Ao clicar no botao de compra, exibe uma mensagem efetuando que a compra foi realizada
            comprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String compra = context.getString(R.string.compra_efetuada_sucesso).toString();

                    ToastHelper.showToast(context, mToast, compra, Toast.LENGTH_SHORT);
                }
            });
        }

        return view;
    }
}
