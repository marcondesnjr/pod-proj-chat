package ifpb.pod.proj.appdata.gerenciador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class TXTNotificacao {

    public String criarNotificacao(String content) throws IOException  {

        File filetxt = File.createTempFile("not", ".txt", new File(""));
        FileUtils.write(filetxt, content);
        
        String fileName = filetxt.getName();
        String token = fileName.substring(0, fileName.length()-5);
        
        return token;

    }

    public String recuperarByToken(String token) throws IOException {
        return FileUtils.readFileToString(new File(token+".txt"));
    }

}
