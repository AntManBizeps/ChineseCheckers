package org.AAKB.client.client;



import static org.AAKB.constants.ConstantProperties.*;

public class Client {

    private CommunicationManager communicationManager;

    public Client() throws Exception {
        this.communicationManager = new CommunicationManager(SERVER_ADDRESS, SERVER_PORT);
    }
}
