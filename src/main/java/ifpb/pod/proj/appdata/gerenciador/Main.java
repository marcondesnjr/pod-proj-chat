/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.gerenciador;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new MensagemGerenciador().cadastrarMensagemUsuario("idmesg", "usrId", "pendente");
    }
}
