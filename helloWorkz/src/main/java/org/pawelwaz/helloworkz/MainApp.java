package org.pawelwaz.helloworkz;

import java.io.File;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pawelwaz.helloworkz.util.JpaUtil;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        JpaUtil.buildFactory();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("logowanie");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() {
        JpaUtil.shutdown();
        File tmp = new File("tmp");
        File[] files = tmp.listFiles();
        for(int i = 0; i < files.length; i++) files[i].delete();
        tmp.delete();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
