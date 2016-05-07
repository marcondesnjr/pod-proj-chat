/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.socket;

import ifpb.pod.proj.appdata.transaction.Operador;
import ifpb.pod.proj.appdata.util.StringCommand;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class AppDataPublicServer extends Thread {

    public void init() throws IOException {
        ServerSocket serverSocket = new ServerSocket(10666);
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                byte[] b = new byte[1024];
                socket.getInputStream().read(b);
                Map<String, String> map = StringCommand.convert(new String(b).trim());
                if (map.get("command").equals("getNotificacao")) {
                    String resp = new Operador().getNotificacao(map.get("token"));
                    socket.getOutputStream().write(resp.getBytes("UTF-8"));
                }
            } catch (Exception ex) {
                Logger.getLogger(AppDataPublicServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException ex) {
            Logger.getLogger(AppDataPublicServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
