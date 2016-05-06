/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.repositorio;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.v1.DbxClientV1;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v1.DbxWriteMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class DBoxRepositorio implements Repositorio {

    private DbxClientV1 getClient() throws IOException, DbxException {
        // Get your app key and secret from the Dropbox developers website.
        final String APP_KEY = "tulnlckjybrc0u9";
        final String APP_SECRET = "gafb0hecfrimqfg";
        final String APP_TOKEN = "PEnCJ8aOSw0AAAAAAAAAFGl_5y4XJ05pI_9Y2WOUN5jUNjDguR6xWpn7PLeVc1DZ";

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("pod-proj-chat/1.0",
                Locale.getDefault().toString());
        DbxClientV1 client = new DbxClientV1(config, APP_TOKEN);
        return client;
    }

    @Override
    public void updateFile(File fl, BibliotecaArquivos b) throws Exception {
        if (b == b.MENSAGEM_USUARIO) {
            updateMensagemUsuarioFile(fl);
        }
    }

    private void updateMensagemUsuarioFile(File fl) throws DbxException, IOException {
        try (FileInputStream inputStream = new FileInputStream(fl)) {
            DbxEntry.File uploadedFile = getClient().uploadFile("/mensagem_usuario.xml",
                    DbxWriteMode.force(), fl.length(), inputStream);
            System.out.println("Uploaded: " + uploadedFile.toString());
        }
    }

    @Override
    public File downloadFile(BibliotecaArquivos b) throws Exception {
        if (b == BibliotecaArquivos.MENSAGEM_USUARIO) {
            return downloadMensagemUsuarioFile();
        }
        return null;
    }

    private File downloadMensagemUsuarioFile() throws IOException, DbxException, FileNotFoundException {
        try (FileOutputStream outputStream = new FileOutputStream(this.getClass().getResource("/downloads/mensagem_usuario.xml").getFile())) {
            DbxEntry.File downloadedFile = getClient().getFile("/mensagem_usuario.xml", null,
                    outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
        }
        return new File(this.getClass().getResource("/downloads/mensagem_usuario.xml").getFile());
    }

}
