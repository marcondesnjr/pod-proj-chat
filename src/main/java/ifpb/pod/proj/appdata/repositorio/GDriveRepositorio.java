/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.pod.proj.appdata.repositorio;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import ifpb.pod.proj.appdata.repositorio.BibliotecaArquivos;
import ifpb.pod.proj.appdata.repositorio.Repositorio;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author José Marcondes do Nascimento Junior
 */
public class GDriveRepositorio implements Repositorio {

    private static final String USRFILE = "0B9cCXHDSR9etVEFyTUdwQW9PM0E";
    private static final String MSGFILE = "0B9cCXHDSR9etZHdpMVlaS3Y1VzA";
    private static final String USRGRP = "0B9cCXHDSR9etb1BtWlBpUUpOZFk";
    private static final String GRUPOFILE = "0B9cCXHDSR9etTXJKcUZTZHNoQnM";

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "Drive API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/pod-proj-chat.json");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private Credential authorize() throws Exception {
        // Load client secrets.
        InputStream in = this.getClass().getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    private Drive getDriveService() throws IOException, Exception {
        Credential credential = authorize();
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

//    public static void main(String[] args) throws IOException, Exception {
//        // Build a new authorized API client service.
//        Drive service = new GDriveRepositorio().getDriveService();
//
//        // Print the names and IDs for up to 10 files.
//        FileList result = service.files().list()
//                .setPageSize(100)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        List<File> files = result.getFiles();
//        if (files == null || files.size() == 0) {
//            System.out.println("No files found.");
//        } else {
//            System.out.println("Files:");
//            for (File file : files) {
//                System.out.printf("%s (%s)\n", file.getName(), file.getId());
//            }
//        }
//    }

    @Override
    public void updateFile(java.io.File fl, BibliotecaArquivos b) throws FileNotFoundException, Exception {
        if (b == b.USUARIOS) {
            updateUsrFile(fl);
        } else if (b == b.MENSAGENS) {
            updateMsgFile(fl);
        }
    }

    private void updateUsrFile(java.io.File fl) throws IOException, Exception, FileNotFoundException {
        File body = new File();
        body.setTitle("usuarios.xml");
        body.setMimeType("text/xml");
        InputStreamContent mediaContent = new InputStreamContent("text/xml", new BufferedInputStream(new FileInputStream(fl)));
        Drive.Files.Update request = getDriveService().files().update(USRFILE, body, mediaContent);
        request.execute();
    }

    private void updateMsgFile(java.io.File fl) throws IOException, Exception, FileNotFoundException {
        File body = new File();
        body.setTitle("mensagens.xml");
        body.setMimeType("text/xml");
        InputStreamContent mediaContent = new InputStreamContent("text/xml", new BufferedInputStream(new FileInputStream(fl)));
        Drive.Files.Update request = getDriveService().files().update(MSGFILE, body, mediaContent);
        request.execute();
    }

    @Override
    public java.io.File downloadFile(BibliotecaArquivos b) throws Exception {
        switch (b) {
            case USUARIOS:
                return dowloadUsersFile();
            case MENSAGENS:
                return dowloadMsgFile();
            case USUARIO_GRUPO:
                return dowloadUsrGpFile();
            default:
                return null;
        }

    }

    private java.io.File dowloadUsersFile() throws IOException, Exception, FileNotFoundException {
        File userFilesMeta = getDriveService().files().get(USRFILE).execute();
        OutputStream out = new FileOutputStream(this.getClass().getResource("/downloads/usuarios.xml").getFile());
        MediaHttpDownloader downloader = new MediaHttpDownloader(HTTP_TRANSPORT, getDriveService().getRequestFactory().getInitializer());
        downloader.download(new GenericUrl(userFilesMeta.getDownloadUrl()), out);
        return new java.io.File(this.getClass().getResource("/downloads/usuarios.xml").getFile());
    }

    private java.io.File dowloadMsgFile() throws IOException, Exception, FileNotFoundException {
        File userFilesMeta = getDriveService().files().get(USRFILE).execute();
        OutputStream out = new FileOutputStream(this.getClass().getResource("/downloads/mensagens.xml").getFile());
        MediaHttpDownloader downloader = new MediaHttpDownloader(HTTP_TRANSPORT, getDriveService().getRequestFactory().getInitializer());
        downloader.download(new GenericUrl(userFilesMeta.getDownloadUrl()), out);
        return new java.io.File(this.getClass().getResource("/downloads/mensagens.xml").getFile());
    }

    private void updateUsrGpFile(java.io.File fl) throws IOException, Exception, FileNotFoundException {
        File body = new File();
        body.setTitle("usuario_grupo.xml");
        body.setMimeType("text/xml");
        InputStreamContent mediaContent = new InputStreamContent("text/xml", new BufferedInputStream(new FileInputStream(fl)));
        Drive.Files.Update request = getDriveService().files().update(MSGFILE, body, mediaContent);
        request.execute();
    }

    private java.io.File dowloadUsrGpFile() throws IOException, Exception, FileNotFoundException {
        File userFilesMeta = getDriveService().files().get(USRFILE).execute();
        OutputStream out = new FileOutputStream(this.getClass().getResource("/downloads/usuario_grupo.xml").getFile());
        MediaHttpDownloader downloader = new MediaHttpDownloader(HTTP_TRANSPORT, getDriveService().getRequestFactory().getInitializer());
        downloader.download(new GenericUrl(userFilesMeta.getDownloadUrl()), out);
        return new java.io.File(this.getClass().getResource("/downloads/usuario_grupo.xml").getFile());
    }

}
