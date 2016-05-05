/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.gerenciador;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import ifpb.pod.proj.appdata.googledrive.GDriveRepositorio;
import ifpb.pod.proj.appdata.repositorio.BibliotecaArquivos;
import ifpb.pod.proj.appdata.repositorio.Repositorio;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Serializer;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class UsuarioGerenciador {

    private Repositorio repositorio;

    public UsuarioGerenciador(Repositorio repositorio) {
        this.repositorio = repositorio;
    }

    public String cadastrarUsuario(String nome, String email, String senha) throws Exception {

        UUID id = UUID.randomUUID();

        Builder builder = new Builder();
        InputStream is = new FileInputStream(repositorio.downloadFile(BibliotecaArquivos.USUARIOS));

        Document doc = builder.build(is);

        List<Map<String, String>> list = listarUsuarios(doc);

        HashMap<String, String> usr = new HashMap();
        usr.put("id", id.toString());
        usr.put("nome", nome);
        usr.put("email", email);
        usr.put("senha", senha);

        list.add(usr);

        escreverUsuario(list);

        System.out.println(list);
        return id.toString();
    }

    public void escreverUsuario(List<Map<String, String>> list) throws Exception {
        Element root = new Element("usuarios");
        for (Map<String, String> map : list) {
            Element usrEl = new Element("usuario");
            Element idEl = new Element("id");
            idEl.appendChild(map.get("id"));
            Element nameEl = new Element("nome");
            nameEl.appendChild(map.get("nome"));
            Element emailEl = new Element("email");
            emailEl.appendChild(map.get("email"));
            Element senhaEl = new Element("senha");
            senhaEl.appendChild(map.get("senha"));
            usrEl.appendChild(idEl);
            usrEl.appendChild(nameEl);
            usrEl.appendChild(emailEl);
            usrEl.appendChild(senhaEl);
            root.appendChild(usrEl);
        }
        Document doc = new Document(root);

        File usrFile = new File(this.getClass().getResource("/uploads/usuarios.xml").getFile());

        try {
            Serializer serializer = new Serializer(new FileOutputStream(usrFile), "UTF-8");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(doc);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        repositorio.updateFile(usrFile, BibliotecaArquivos.USUARIOS);
    }

    public List<Map<String, String>> listarUsuarios(Document doc) {

        Element root = doc.getRootElement();
        Elements childs = root.getChildElements();
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++) {
            Element atual = childs.get(i);
            HashMap<String, String> map = new HashMap<>();
            String idValue = atual.getChild(1).getValue();
            String nameValue = atual.getChild(3).getValue();
            String emailValue = atual.getChild(5).getValue();
            String senhaValue = atual.getChild(7).getValue();
            map.put("id", idValue);
            map.put("email", emailValue);
            map.put("senha", senhaValue);
            map.put("nome", nameValue);
            list.add(map);
        }
        return list;
    }
}
