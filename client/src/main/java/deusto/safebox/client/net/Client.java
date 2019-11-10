package deusto.safebox.client.net;

import deusto.safebox.common.net.SocketHandler;
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

public class Client extends SocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    // Trust manager that does not validate certificate chains.
    private static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[] {
        new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }
    };

    private final String hostname;
    private final int port;

    private SSLSocket socket;

    /**
     * Creates a client socket handler that connects to the specified server.
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

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, TRUST_ALL_CERTS, new SecureRandom());
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();

            socket = (SSLSocket) socketFactory.createSocket(hostname, port);
            socket.addHandshakeCompletedListener(e -> logger.trace("Handshake completed."));
            socket.startHandshake();
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            logger.error("Could not create a socket.", e);
            return;
        }

        listen();
    }

    @Override
    protected Socket getSocket() {
        return socket;
    }
}
