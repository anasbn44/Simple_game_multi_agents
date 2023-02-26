package containers;

import agents.ClientAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Stage;
import view.Application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientContainer implements Initializable {
//    @FXML
//    private ListView<String> listView;
//    @FXML
//    private TextField message;
//    @FXML
//    private Button send;
    @FXML
    private Label name;
    @FXML
    private Button send;
    @FXML
    private Button disconnect;
    @FXML
    private TextField message;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox vBox;

    private ClientAgent clientAgent;
    private String nickName;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setClientAgent(ClientAgent clientAgent) {
        this.clientAgent = clientAgent;
    }

    public Label getName() {
        return name;
    }

    public void createClientAgent(String nickName) throws Exception {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        AgentContainer clientContainer = runtime.createAgentContainer(profile);
        AgentController agentController = clientContainer.createNewAgent(nickName, "agents.ClientAgent", new Object[]{this});
        agentController.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            createClientAgent(nickName);
            name.setText(nickName);
            vBox.heightProperty().addListener((observableValue, number, t1) -> {
                scrollPane.setVvalue((Double) t1);
            });
            send.setOnAction(actionEvent -> sendMessage(message.getText()));
            message.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    sendMessage(message.getText());
                }
            });
            disconnect.setOnAction(actionEvent -> {
                try {
                    sendMessage("giveup");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String messageToSend){
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);

            hBox.setPadding(new Insets(5, 5, 5, 10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle(
                    "-fx-color: rgb(239, 242, 255);" +
                            "-fx-background-color: rgb(15, 125, 242);" +
                            "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, 0.925, 0.996));

            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);
            GuiEvent guiEvent = new GuiEvent(this, 1);
            guiEvent.addParameter(messageToSend);
            clientAgent.onGuiEvent(guiEvent);
            message.clear();
        }
    }

    private void addMessage (String messageFromServer){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

    public void recieveMessage(String message) throws Exception {
        if(message.equals("win")){
            back("you have won");
        } else if (message.contains("lose")){
            System.out.println("lose");
            String[] split = message.split("=>");

            back(split[1]);
        } else {
            addMessage(message);
        }
    }

    private void back(String str) throws Exception {
        addMessage(str);
        clientAgent.getContainerController().getAgent(nickName).kill();

        message.setDisable(true);
        send.setDisable(true);
        disconnect.setDisable(true);
    }
}
