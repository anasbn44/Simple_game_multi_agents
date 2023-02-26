package view;

import containers.ClientContainer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConnectController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private Button connect;

    public void onConnect() throws IOException {
        Stage next = (Stage) connect.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat_1.fxml"));
        System.out.println(fxmlLoader);
        ClientContainer clientContainer = new ClientContainer();
        clientContainer.setNickName(username.getText());
        fxmlLoader.setController(clientContainer);
        next.setScene(new Scene(fxmlLoader.load()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER){
                try {
                    onConnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
