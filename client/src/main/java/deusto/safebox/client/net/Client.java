package deusto.safebox.client.net;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import deusto.safebox.common.ItemData;
import deusto.safebox.common.ItemType;
import deusto.safebox.common.net.SocketHandler;
import deusto.safebox.common.net.packet.Packet;
import deusto.safebox.common.net.packet.ReceiveDataPacket;
import deusto.safebox.common.util.BoundClassConsumerMap;
import deusto.safebox.common.util.IBoundClassConsumerMap;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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
    private final IBoundClassConsumerMap<Packet> packetAction = new BoundClassConsumerMap<>();

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

        packetAction.put(ReceiveDataPacket.class, packet -> {
            Map<ItemType, List<ItemData>> rawItems = classifyByType(packet.getItems());
            // TODO
        });
    }

    private Map<ItemType, List<ItemData>> classifyByType(Collection<ItemData> items) {
        return items.parallelStream()
                .collect(groupingBy(
                    ItemData::getType,
                    () -> new EnumMap<>(ItemType.class),
                    toList())
                );
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
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
        } catch (IOException e) {
            logger.error("Could not start a socket.", e);
        }

        listen();
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
        packetAction.of(packet).ifPresentOrElse(
            action -> action.accept(packet),
            () -> logger.error("There is no action defined for the received packet ({})", packet)
        );
    }
}
