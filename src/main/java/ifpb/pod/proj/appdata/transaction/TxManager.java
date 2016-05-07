/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.transaction;

import ifpb.pod.proj.appdata.gerenciador.GrupoGerenciador;
import ifpb.pod.proj.appdata.transaction.local.GrupoGerenciadorTxLocal;
import ifpb.pod.proj.appdata.transaction.local.MensagemGerenciadorTxLocal;
import ifpb.pod.proj.appdata.transaction.local.MensagemUsuarioGerenciadorTxLocal;
import ifpb.pod.proj.appdata.transaction.local.UsuarioGerenciadorTxLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class TxManager implements TransactionManager {

    private List<TxLocal> transations;

    public TxManager() {
        transations = new ArrayList<>();
        transations.add(new GrupoGerenciadorTxLocal());
        transations.add(new MensagemGerenciadorTxLocal());
        transations.add(new MensagemUsuarioGerenciadorTxLocal());
        transations.add(new UsuarioGerenciadorTxLocal());
    }

    @Override
    public void prepareAll() throws Exception {
        for (TxLocal transation : transations) {
            transation.prepare();
        }
    }

    @Override
    public void commitAll() {
        for (TxLocal transation : transations) {
            transation.commit();
        }
    }

    @Override
    public void rollbackAll() {
        for (TxLocal transation : transations) {
            while (true) {
                try {
                    transation.rollback();
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(TxManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
