/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.gerenciador;

import ifpb.pod.proj.appdata.repositorio.BibliotecaArquivos;
import ifpb.pod.proj.appdata.repositorio.GDriveRepositorio;
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
import java.util.function.Predicate;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Serializer;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class GrupoGerenciador {

    public List<Map<String, String>> listarGrupos() throws Exception {
        Builder builder = new Builder();
        Repositorio repositorio = new GDriveRepositorio();
        InputStream is = new FileInputStream(repositorio.downloadFile(BibliotecaArquivos.GRUPO));
        Document doc = builder.build(is);
        Element root = doc.getRootElement();
        Elements childs = root.getChildElements("grupo");
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++) {
            Element atual = childs.get(i);
            HashMap<String, String> map = new HashMap<>();
            String idValue = atual.getChild(1).getValue();
            String nomeValue = atual.getChild(3).getValue();
            map.put("id", idValue);
            map.put("nome", nomeValue);
            list.add(map);
        }
        return list;
    }

    public List<Map<String, String>> listarUsuarioGrupo() throws Exception {
        Builder builder = new Builder();
        Repositorio repositorio = new GDriveRepositorio();
        InputStream is = new FileInputStream(repositorio.downloadFile(BibliotecaArquivos.USUARIO_GRUPO));
        Document doc = builder.build(is);
        Element root = doc.getRootElement();
        Elements childs = root.getChildElements("usuarioGrupo");
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++) {
            Element atual = childs.get(i);
            HashMap<String, String> map = new HashMap<>();
            String idUsr = atual.getChild(1).getValue();
            String idGrp = atual.getChild(3).getValue();
            map.put("idUsr", idUsr);
            map.put("idGrp", idGrp);
            list.add(map);
        }
        return list;
    }
    
    public void entrarGrupo(String usrEmail, String grpId) throws Exception {
        Repositorio repositorio = new GDriveRepositorio();
        Builder builder = new Builder();
        InputStream is = new FileInputStream(repositorio.downloadFile(BibliotecaArquivos.USUARIO_GRUPO));

        Document doc = builder.build(is);

        List<Map<String, String>> list = listarUsuarioGrupo();

        HashMap<String, String> map = new HashMap();
        map.put("idGrp", grpId);
        map.put("idUsr", usrEmail);

        list.add(map);

        escreverEntrarGrupo(list);

        System.out.println(list);
    }
    
    public void escreverEntrarGrupo(List<Map<String, String>> list) throws Exception {
        Repositorio repositorio = new GDriveRepositorio();
        Element root = new Element("participantes");
        for (Map<String, String> map : list) {
            
            Element usuarioGrupoEl = new Element("usuarioGrupo");
            
            Element idUsrEl = new Element("idUsr");        
            idUsrEl.appendChild(map.get("idUsr"));
            
            Element idGrpIdEl = new Element("idGrp");          
            idGrpIdEl.appendChild(map.get("idGrp"));
            
            usuarioGrupoEl.appendChild(idUsrEl);
            usuarioGrupoEl.appendChild(idGrpIdEl);
            
            root.appendChild(usuarioGrupoEl);
        }

        Document doc = new Document(root);

        File usrFile = new File(this.getClass().getResource("/uploads/mensagens.xml").getFile());

        try {
            Serializer serializer = new Serializer(new FileOutputStream(usrFile), "UTF-8");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(doc);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        repositorio.updateFile(usrFile, BibliotecaArquivos.USUARIO_GRUPO);
    }
    
    

    public Map<String, String> grupoById(String id) throws Exception {
        List<Map<String, String>> all = listarGrupos();
        all.removeIf((Map<String, String> t) -> {
            return !t.get("id").equals(id);
        });
        return all.size() >= 1 ? all.get(0) : null;
    }

}
