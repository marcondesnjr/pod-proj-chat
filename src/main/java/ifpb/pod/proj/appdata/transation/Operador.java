
package ifpb.pod.proj.appdata.transation;

import ifpb.pod.proj.appdata.gerenciador.MensagemGerenciador;
import ifpb.pod.proj.appdata.gerenciador.UsuarioGerenciador;
import java.util.List;
import java.util.Map;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class Operador {

    //Operação atomica
    public void escreverMensagem(String usrId, String dataTime, String grupoId, String conteudo) throws Exception {
        String id = new MensagemGerenciador().cadastrarMensagem(usrId, dataTime, grupoId, conteudo);
        List<Map<String, String>> list = new UsuarioGerenciador().listaUsuarioPorGrupo(grupoId);
        for (Map<String, String> usr : list) {
            new MensagemGerenciador().cadastrarMensagemUsuario(id, usr.get("id"), "pendente");
        }
    }
    
    
}
