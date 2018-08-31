package br.com.semdimapp.semdim.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import br.com.semdimapp.semdim.R;
import br.com.semdimapp.semdim.adapter.ViewPageAdapter;
import br.com.semdimapp.semdim.controller.ContatoController;
import br.com.semdimapp.semdim.controller.LoginController;
import br.com.semdimapp.semdim.fragment.ContatoFragment;
import br.com.semdimapp.semdim.fragment.EstabelecimentosFragment;
import br.com.semdimapp.semdim.fragment.GruposFragment;
import br.com.semdimapp.semdim.helper.Base64Custom;
import br.com.semdimapp.semdim.helper.ToastHelper;

public class MainActivity extends AppCompatActivity {

    //Atributos
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPageAdapter viewPageAdapter;

    private LoginController loginController;

    private ContatoController contatoController;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Recupera a chave de hash
        /*
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("br.com.semdimapp.semdim",
                            PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature singnature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(singnature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e){

        } catch (NoSuchAlgorithmException e){

        }
        */

        setContentView(R.layout.activity_main);

        //Recupera os elementos da tela

        //Configuração da toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //Configuração do viewpager
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new GruposFragment(),
                getResources().getString(R.string.grupos_fragment_title));
        viewPageAdapter.addFragment(new ContatoFragment(),
                getResources().getString(R.string.contato_fragment_title));
        viewPageAdapter.addFragment(new EstabelecimentosFragment(),
                getString(R.string.estabelecimento_fragment_title));
        viewPager.setAdapter(viewPageAdapter);

        //Configuração do tabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_new_contact:
                adicionarNovoContato();
                return true;
            case R.id.action_add_new_group:
                adicionarNovoGrupo();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_sair:
                logOffUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void adicionarNovoGrupo() {
        Intent intent = new Intent(MainActivity.this, CadastroGrupoActivity.class);
        startActivity(intent);
    }

    /**
     * Adiciona um novo contato
     */
    private void adicionarNovoContato() {

        contatoController = ContatoController.getInstance(MainActivity.this);

        //Configuração do Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(R.string.main_activity_new_contact_alertdialog_title);
        alertDialog.setMessage(R.string.main_activity_new_contact_alertdialog_message);

        final EditText editText = new EditText(MainActivity.this);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS|
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton(R.string.main_activity_cadastrar_button,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailContato = editText.getText().toString();
                if(emailContato.isEmpty()){
                    ToastHelper.showToast(MainActivity.this,
                            mToast,
                            getResources()
                                    .getString(R.string.main_activity_new_contact_empty_edittext),
                            Toast.LENGTH_SHORT);
                } else {
                    if(contatoController.adicionarContato(emailContato)){
                        ToastHelper.showToast(MainActivity.this,
                                mToast,
                                getResources()
                                        .getString(R.string.main_activity_new_contact_success),
                                Toast.LENGTH_SHORT);
                    } else {
                        ToastHelper.showToast(MainActivity.this,
                                mToast,
                                getResources()
                                        .getString(R.string.main_activity_new_contact_fail),
                                Toast.LENGTH_SHORT);
                    };
                }
            }
        });

        alertDialog.setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    /**
     * Faz o logoff da aplicação
     */
    private void logOffUser() {
        if (loginController == null){
            loginController = new LoginController();
        }
        loginController.fazerLogoff();
        //Retorna a tela de login
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
