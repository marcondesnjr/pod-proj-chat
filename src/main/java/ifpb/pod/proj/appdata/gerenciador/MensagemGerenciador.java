/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.gerenciador;

import ifpb.pod.proj.appdata.repositorio.GDriveRepositorio;
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
public class MensagemGerenciador {

    public String cadastrarMensagem(String usrId, String dataTime, String grupoId, String conteudo) throws Exception {
        Repositorio repositorio = new GDriveRepositorio();
        UUID id = UUID.randomUUID();

        Builder builder = new Builder();
        InputStream is = new FileInputStream(repositorio.downloadFile(BibliotecaArquivos.MENSAGENS));

        Document doc = builder.build(is);

        List<Map<String, String>> list = listarMensagens();

        HashMap<String, String> map = new HashMap();
        map.put("id", id.toString());
        map.put("usuarioId", usrId);
        map.put("dataTime", dataTime);
        map.put("grupoId", grupoId);
        map.put("conteudo", conteudo);

        list.add(map);

        escreverMensagem(list);

        System.out.println(list);
        return id.toString();
    }

    public List<Map<String, String>> listarMensagens() throws Exception {
        Builder builder = new Builder();
        Repositorio repositorio = new GDriveRepositorio();
        InputStream is = new FileInputStream(repositorio.downloadFile(BibliotecaArquivos.MENSAGENS));
        Document doc = builder.build(is);
        Element root = doc.getRootElement();
        Elements childs = root.getChildElements("mensagem");
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < childs.size(); i++) {
            Element atual = childs.get(i);
            HashMap<String, String> map = new HashMap<>();
            String idValue = atual.getChild(1).getValue();
            String userId = atual.getChild(3).getValue();
            String dataTime = atual.getChild(5).getValue();
            String grupoId = atual.getChild(7).getValue();
            String conteudo = atual.getChild(9).getValue();
            map.put("id", idValue);
            map.put("usuarioId", userId);
            map.put("dataTime", dataTime);
            map.put("grupoId", grupoId);
            map.put("conteudo", conteudo);
            list.add(map);
        }
        return list;
    }

    public void escreverMensagem(List<Map<String, String>> list) throws Exception {
        Repositorio repositorio = new GDriveRepositorio();
        Element root = new Element("mensagens");
        for (Map<String, String> map : list) {
            Element mensagemEl = new Element("mensagem");
            Element idEl = new Element("id");
            idEl.appendChild(map.get("id"));
            Element usuarioIdEl = new Element("usuarioId");
            usuarioIdEl.appendChild(map.get("usuarioId"));
            Element dataTimeEl = new Element("dataTime");
            dataTimeEl.appendChild(map.get("dataTime"));
            Element grupoIdEl = new Element("grupoId");
            grupoIdEl.appendChild(map.get("grupoId"));
            Element contentEl = new Element("conteudo");
            contentEl.appendChild(map.get("conteudo"));
            mensagemEl.appendChild(idEl);
            mensagemEl.appendChild(usuarioIdEl);
            mensagemEl.appendChild(dataTimeEl);
            mensagemEl.appendChild(grupoIdEl);
            mensagemEl.appendChild(contentEl);
            root.appendChild(mensagemEl);
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

        repositorio.updateFile(usrFile, BibliotecaArquivos.MENSAGENS);
    }

}
