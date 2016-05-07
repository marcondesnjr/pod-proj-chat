package ifpb.pod.proj.appdata;

import ifpb.pod.proj.appdata.socket.AppDataPublicServer;
import ifpb.pod.proj.appdata.socket.SocketServerS;
import java.io.IOException;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class Main {
    public static void main(String[] args) throws IOException {
        new AppDataPublicServer().start();
        new SocketServerS().init();
    }
}
