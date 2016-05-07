/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.transaction.local;

import ifpb.pod.proj.appdata.gerenciador.UsuarioGerenciador;
import ifpb.pod.proj.appdata.transaction.TxLocal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class UsuarioGerenciadorTxLocal implements TxLocal{
    private List<Map<String,String>> oldstate;

    @Override
    public void prepare() throws Exception {
        oldstate = new UsuarioGerenciador().listarUsuarios();
    }

    @Override
    public void commit(){
        oldstate = null;
    }

    @Override
    public void rollback() throws Exception {
        if(oldstate != null){
            new UsuarioGerenciador().escreverUsuario(oldstate);
        }
    }
}
