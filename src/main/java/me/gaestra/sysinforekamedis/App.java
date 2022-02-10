package me.gaestra.sysinforekamedis;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import me.gaestra.sysinforekamedis.extras.MySQLDBConfig;
import me.gaestra.sysinforekamedis.helper.DialogAlertHelper;
import me.gaestra.sysinforekamedis.model.Pegawai;

/**
 * JavaFX App
 */
public class App extends Application {

    // Core Public Component
    public static Pegawai loggedUser;
    public static final String appName = "SIRKGAESTRA";
    public static final String appTitle = "Sistem Informasi Rekamedis - Gaestra";
    
    // Core Component
    private static Scene scene;
    private File file;
    private FileChannel channel;
    private FileLock lock;

    @Override
    public void start(Stage stage) throws IOException {
        if (this.isAppActive()) {
            DialogAlertHelper.showAlert(Alert.AlertType.ERROR, 
                    "Aplikasi telah berjalan!", "Aplikasi telah berjalan!", "Aplikasi sudah terbuka dan berjalan, mohon tutup aplikasi sebelumnya!");
            Platform.exit();
        }
        
        setupDatabase();
        showFirstForm(stage);
    }
    
    @Override
    public void stop() {
        DatabaseManager.getInstance().close();
    }
    
    public static void showFirstForm(Stage stage) throws IOException {
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = loadFXML("view/LoginForm");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static FXMLLoader getFXMLLoader(String fxml) {
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }
    
    public static Image getIconImage() {
        return new Image(App.class.getResourceAsStream("view/style/logo.png"));
    }

    public static void main(String[] args) {
        launch();
    }
    
    private static void setupDatabase() {
        DatabaseManager.getInstance().setDBConfig(new MySQLDBConfig());
    }
    
    public boolean isAppActive() {
        try {
            file = new File(System.getProperty("user.home"), appName + ".tmp");
            channel = new RandomAccessFile(file, "rw").getChannel();

            try {
                lock = channel.tryLock();
            }
            catch (OverlappingFileLockException e) {
                // Locked
                closeLock();
                return true;
            }

            if (lock == null) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    // Delete lock on exit
                    public void run() {
                        closeLock();
                        deleteFile();
                    }
                });
            return false;
        }
        catch (Exception e) {
            closeLock();
            return true;
        }
    }

    private void closeLock() {
        try { lock.release();  }
        catch (Exception e) {  }
        try { channel.close(); }
        catch (Exception e) {  }
    }

    private void deleteFile() {
        try { file.delete(); }
        catch (Exception e) { }
    }

}