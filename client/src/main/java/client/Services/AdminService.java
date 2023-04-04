package client.Services;

import client.utils.ServerUtils;

public class AdminService {
    private final ServerUtils serverUtils;

    public AdminService(ServerUtils serverUtils){
        this.serverUtils = serverUtils;
    }

    public boolean verifyAdmin(String password) {
        return this.serverUtils.verifyAdmin(password);
    }
}
