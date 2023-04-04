package client.Services;

import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.user.UserData;
import client.utils.ServerUtils;
import org.springframework.messaging.simp.stomp.StompSession;

public class ConnectionService {

    private final UserData userData;
    private final ServerUtils serverUtils;

    public ConnectionService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
    }

    public void setUrl(String url) {
        serverUtils.setUrl(url);
    }

    public void setWsConfig(StompSession session) {
        userData.setWsConfig(new MessageAdmin(session), new MessageSender(session));
    }
}
