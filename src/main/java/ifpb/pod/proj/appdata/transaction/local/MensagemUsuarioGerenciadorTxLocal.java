/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.transaction.local;

import ifpb.pod.proj.appdata.gerenciador.MensagemUsuarioGerenciador;
import ifpb.pod.proj.appdata.transaction.TxLocal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class MensagemUsuarioGerenciadorTxLocal implements TxLocal{

    private List<Map<String, String>> oldState;
    
    @Override
    public void prepare() throws Exception {
        oldState = new MensagemUsuarioGerenciador().listarMensagensUsuario();
    }

    @Override
    public void commit(){
        oldState = null;
    }

    @Override
    public void rollback() throws Exception {
        if(oldState != null){
            new MensagemUsuarioGerenciador().escreverMensagemUsuario(oldState);
        }
    }
    
}
