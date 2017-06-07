import event.type.ApplicationStartEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.application.ApplicationStatus;
import org.apache.log4j.Logger;
import util.EventBusProvider;

public class Main extends Application {

    final private  Logger logger = Logger.getLogger(getClass());

    @Override
    public void start(Stage primaryStage) throws Exception{
        registerEventBus();

        logger.debug("The application started.");

        Parent root = FXMLLoader.load(getClass().getResource("/view/main_pane.fxml"));
        primaryStage.setTitle("Pdf NC");
        primaryStage.setScene(new Scene(root,900,600));
        primaryStage.show();

        EventBusProvider.getInstance().post(new ApplicationStartEvent(primaryStage));
        EventBusProvider.getInstance().post(ApplicationStatus.IDLE);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        logger.info("The application stopped");
    }

    private void registerEventBus(){
        EventBusProvider.getInstance().register(this);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
