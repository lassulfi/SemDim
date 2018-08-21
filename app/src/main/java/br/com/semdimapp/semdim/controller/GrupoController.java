package br.com.semdimapp.semdim.controller;

import br.com.semdimapp.semdim.model.Grupo;
import br.com.semdimapp.semdim.model.Usuario;

/**
 * Classe responsável pela gestão dos grupos no app
 */
public class GrupoController {

    //Atributos
    private Grupo grupo;
    private UsuarioController usuarioController;
    private ContatoController contatoController;

    //Construtor
    public GrupoController(){
        this.usuarioController = new UsuarioController();
        this.contatoController = new ContatoController();
    }

    /**
     * Cria um novo grupo no app
     */
    public void criarGrupo(){

        //Recupera o usuario criador do grupo
        Usuario criador = usuarioController.getUsuario();

        //Cria uma instancia da classe Grupo
        grupo = new Grupo(criador);
    }


}
