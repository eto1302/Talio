package client.Services;

import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.user.UserData;
import client.utils.ServerUtils;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConnectionService {

    private final UserData userData;
    private final ServerUtils serverUtils;
    private File serverFilePath;

    public ConnectionService(UserData userData, ServerUtils serverUtils) {
        this.userData = userData;
        this.serverUtils = serverUtils;
        this.serverFilePath = new File(System.getProperty("user.dir") + "/serverData.txt");
    }

    public void setUrl(String url) {
        serverUtils.setUrl(url);
    }

    public void setWsConfig(StompSession session) {
        userData.setWsConfig(new MessageAdmin(session), new MessageSender(session));
    }

    public void disconnect() {
        this.userData.disconnect();
    }

    public void setFilePath(File file) {
        this.serverFilePath = file;
    }

    public List<String> getServerUrls(){
        try{
            return Files.readAllLines(serverFilePath.toPath()).stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
        catch(Exception e){
            System.out.println("File not found");
            return new ArrayList<>();
        }
    }
}
