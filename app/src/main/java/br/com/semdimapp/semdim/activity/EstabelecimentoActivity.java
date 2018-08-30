package br.com.semdimapp.semdim.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.adapter.PromocoesAdapter;
import br.com.semdimapp.semdim.model.Estabelecimento;
import br.com.semdimapp.semdim.model.Promocao;

//TODO: codificar a Activity de Estabelecimentos
public class EstabelecimentoActivity extends AppCompatActivity implements OnMapReadyCallback{
    //Atributos

    //Elementos de tela
    private TextView nomeEstabecimentoTextView;
    private TextView enderecoEstabelecimentoTextView;
    private ImageView estabelecimentoImageView;
    private ListView promocoesListView;
    private Toolbar toolbar;

    private GoogleMap mMap;

    private final static String ESTABELECIMENTO_INTENT = "estabelecimento";

    private ArrayAdapter adapter;
    private Estabelecimento estabelecimento;
    private ArrayList<Promocao> promocoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estabelecimento);

        //Recupera a classe com as informações do estabelecimento
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            estabelecimento = (Estabelecimento) extra.getParcelable(ESTABELECIMENTO_INTENT);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar_estabelecimento);
        toolbar.setTitle(R.string.estabelecimento_activity_title);
        setSupportActionBar(toolbar);

        //Recupera os elementos de tela
        nomeEstabecimentoTextView = (TextView) findViewById(R.id.nome_estabelecimento_textview);
        enderecoEstabelecimentoTextView = (TextView) findViewById(R.id.endereco_estabelecimento_textview);
        estabelecimentoImageView = (ImageView) findViewById(R.id.estabelecimento_imageView);
        promocoesListView = (ListView) findViewById(R.id.promocoes_listview);

        //Recupera o map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        mapFragment.getMapAsync(EstabelecimentoActivity.this);

        //Passando as informacoes para os elementos da tela
        nomeEstabecimentoTextView.setText(estabelecimento.getNome());
        enderecoEstabelecimentoTextView.setText(estabelecimento.getEndereco());

        //Recupera a lista de promocoes da classe
        promocoes = estabelecimento.getPromocoes();

        adapter = new PromocoesAdapter(EstabelecimentoActivity.this, promocoes);

        promocoesListView.setAdapter(adapter);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng localizacao = new LatLng(estabelecimento.getLatitude(),
                estabelecimento.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(localizacao).title(estabelecimento.getNome()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(localizacao));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
