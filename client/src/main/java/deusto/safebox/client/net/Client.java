package deusto.safebox.client.net;

import deusto.safebox.common.net.ClientConnection;
import deusto.safebox.common.net.Packet;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client extends ClientConnection {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    // Trust manager that does not validate certificate chains.
    private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] {
        new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(X509Certificate[] certs, String authType) {}

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }
    };

    private final int port;
    private final String hostname;

    private SSLSocket socket;

    /**
     * Creates a {@link Client} with that will connect to the specified server.
     *
     * @param hostname server hostname.
     * @param port server port.
     */
    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /** Connect to the server. */
    @Override
    public void run() {
        logger.info("Connecting to {}:{}", hostname, port);

        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, TRUST_ALL_CERTS, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("Could not create a secure socket context.", e);
            return;
        }

        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        try {
            socket = (SSLSocket) socketFactory.createSocket(hostname, port);
            socket.addHandshakeCompletedListener(e -> logger.trace("Handshake completed."));
            socket.startHandshake();
            listen();
        } catch (IOException e) {
            logger.error("Could not start a socket.", e);
        }
    }

    @Override
    protected Socket getSocket() {
        return socket;
    }

    @Override
    protected void connectionEstablished() {
        logger.trace("Socket connection established.");
    }

    @Override
    protected void receivePacket(Packet packet) {
        logger.trace("Received a packet: {}", packet);
    }
}
