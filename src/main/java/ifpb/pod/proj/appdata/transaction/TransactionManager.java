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
public interface TransactionManager {
    void prepareAll() throws Exception;
    void commitAll();
    void rollbackAll();
}
