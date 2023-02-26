package agents;

import containers.ClientContainer;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.Application;

import java.io.IOException;

public class ClientAgent extends GuiAgent {
    private ClientContainer clientContainer;
    @Override
    protected void setup() {
        clientContainer = (ClientContainer) getArguments()[0];
        clientContainer.setClientAgent(this);
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage response = receive();
                if(response != null){
                    String content = response.getContent();
                    try {
                        clientContainer.recieveMessage(response.getContent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
    }


    @Override
    public void onGuiEvent(GuiEvent guiEvent) {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID("Server",AID.ISLOCALNAME));
        message.setContent(guiEvent.getParameter(0).toString());
        send(message);
    }

}
