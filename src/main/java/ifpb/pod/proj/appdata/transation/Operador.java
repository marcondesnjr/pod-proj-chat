package ifpb.pod.proj.appdata.transation;

import ifpb.pod.proj.appdata.gerenciador.GrupoGerenciador;
import ifpb.pod.proj.appdata.gerenciador.MensagemGerenciador;
import ifpb.pod.proj.appdata.gerenciador.TXTNotificacao;
import ifpb.pod.proj.appdata.gerenciador.UsuarioGerenciador;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class Operador {

    //Operação atomica
    public boolean escreverMensagem(String usrId, String dateTime, String grupoId, String conteudo) throws Exception {
        String id = new MensagemGerenciador().cadastrarMensagem(usrId, dateTime, grupoId, conteudo);
        List<Map<String, String>> list = new UsuarioGerenciador().listaUsuarioPorGrupo(grupoId);
        list.removeIf((Map<String, String> t) -> {
            return t.get("email").equals(usrId);
        });
        new MensagemGerenciador().cadastrarMensagemUsuario(id, list, "pendente");
        return true;
    }

    public void entrarGrupo(String usrEmail, String groupId) throws Exception {
        GrupoGerenciador grupoGerenciador = new GrupoGerenciador();
        Map<String, String> grupo = grupoGerenciador.grupoById(groupId);
        if (grupo != null) {
            grupoGerenciador.entrarGrupo(usrEmail, groupId);
        }
    }

    public List<Map<String, String>> listarMensagensPendentes() throws Exception {
        MensagemGerenciador mensagemGerenciador = new MensagemGerenciador();
        return mensagemGerenciador.listarMensagensPendentes();
    }

    public List<Map<String, String>> listarMensagens() throws Exception {
        MensagemGerenciador mensagemGerenciador = new MensagemGerenciador();
        return mensagemGerenciador.listarMensagens();
    }

    public String criarNotificacao(String text) throws IOException {
        return new TXTNotificacao().criarNotificacao(text);
    }

    public void alterarEstadoNotificado(String id) throws Exception {
        new MensagemGerenciador().estadoNotificado(id);
    }

}
