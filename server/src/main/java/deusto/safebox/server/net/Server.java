package deusto.safebox.server.net;

import deusto.safebox.common.net.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public class Server extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;
    private final String keyPath;
    private final String keyPassword;

    private SSLServerSocket serverSocket;

    public Server(int port, String keyPath, String keyPassword) {
        this.port = port;
        this.keyPath = keyPath;
        this.keyPassword = keyPassword;
    }

    @Override
    public void run() {
        SSLServerSocketFactory ssf;
        try {
            ssf = createServerSocketFactory();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException |
                IOException | UnrecoverableKeyException | KeyManagementException e) {
            logger.error("Could not create a secure socket factory. ", e);
            return;
        }

        try {
            serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        } catch (IOException e) {
            logger.error("Could not create a server socket.", e);
            return;
        }

        logger.trace("Server started listening on port {}.", port);
        listen();
    }

    private SSLServerSocketFactory createServerSocketFactory()
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
            IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(getClass().getResourceAsStream(keyPath), keyPassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyPassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        return sc.getServerSocketFactory();
    }

    @Override
    protected ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    protected ClientConnection getClientHandler(Socket socket) {
        return new ClientHandler((SSLSocket) socket);
    }

    private static class ClientHandler extends ClientConnection {

        private SSLSocket socket;

        ClientHandler(SSLSocket socket) {
            logger.trace("Creating new client handler.");
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                socket.startHandshake();
            } catch (IOException e) {
                e.printStackTrace();
            }

            listen();
        }

        @Override
        public Socket getSocket() {
            return socket;
        }

        @Override
        protected void connectionEstablished() {
            logger.trace("New client connection established.");
        }

        @Override
        protected void receivePacket(byte[] packet) {
            logger.trace("Received a packet: {}", packet);
        }
    }
}
