/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.transaction;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public interface TxLocal {
    void prepare() throws Exception;
    void commit();
    void rollback() throws Exception;
}
