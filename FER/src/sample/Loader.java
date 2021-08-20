package sample;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;



public class Loader {

    private AnchorPane rootPane;
    private User currentUser;

    public Loader(User currentUser, AnchorPane rootPane) {
        this.rootPane = rootPane;
        this.currentUser = currentUser;
    }

    public void load(String filename) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(filename + ".fxml"));
        AnchorPane parent = loader.load();

        Controller controller = loader.getController();
        controller.initData(this.currentUser);
        this.rootPane.getChildren().setAll(parent);

    }
}
