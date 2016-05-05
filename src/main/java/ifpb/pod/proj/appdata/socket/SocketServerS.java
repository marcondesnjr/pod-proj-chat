/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.socket;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import ifpb.pod.proj.appdata.gerenciador.UsuarioGerenciador;
import ifpb.pod.proj.appdata.transation.Operador;
import ifpb.pod.proj.appdata.util.StringCommand;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class SocketServerS {

    public void init() throws IOException {
        ServerSocket serverSocket = new ServerSocket(10999);
        while (true) {
            String resp = null;
            Socket socket = serverSocket.accept();
            new ExecComand(socket).start();
        }
    }

    private class ExecComand extends Thread {

        private Socket socket;

        public ExecComand(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                byte[] b = new byte[1024];
                socket.getInputStream().read(b);
                Map<String, String> map = StringCommand.convert(new String(b).trim());
                if (map.get("command").equals("cadastrarUsuario")) {
                    new UsuarioGerenciador().cadastrarUsuario(map.get("nome"), map.get("email"), map.get("senha"));
                }else if(map.get("command").equals("hasUsuario")){
                    Map<String,String> usr = new UsuarioGerenciador().usuarioByEmailSenha(map.get("email"), map.get("senha"));
                    if(usr != null)
                        socket.getOutputStream().write("true".getBytes());
                    else{
                        socket.getOutputStream().write("false".getBytes());
                    }
                }else if(map.get("command").equals("escreverMensagem")){
                    new Operador().escreverMensagem(map.get("email"), map.get("dataTime"), map.get("grupoId"), map.get("conteudo"));
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketServerS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(SocketServerS.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(SocketServerS.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
