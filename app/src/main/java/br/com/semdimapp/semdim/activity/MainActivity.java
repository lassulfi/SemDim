package br.com.semdimapp.semdim.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.semdimapp.semdim.R;

public class MainActivity extends AppCompatActivity {

    //Atributos
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recupera os elementos da tela

        //Configuração da toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //Configuração do viewpager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Configuração do tabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }
}
