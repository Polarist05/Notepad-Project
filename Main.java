import java.beans.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

enum FileMenu{
    New,
    Open,
    Save,
    SaveAs,
    Exit
};

public class Main extends Application{
    private static Alert alert;

    public static void main(String[] args) {
        launch();
    }
    public void start(Stage stage) throws Exception {
        
        //Menu
        EnumMenu fileMenu = new EnumMenu(FileMenu.class,"File");
        MenuBar menuBar = new MenuBar(fileMenu.getMenu());
        alert = new Alert(AlertType.WARNING,"Read-Only File!", ButtonType.CLOSE);
        
        //set Event
        fileMenu.getMenuItems()[FileMenu.New.ordinal() ].setOnAction(this::onClickNew);
        fileMenu.getMenuItems()[FileMenu.Open.ordinal()].setOnAction(this::onClickOpen);
        fileMenu.getMenuItems()[FileMenu.Save.ordinal()].setOnAction(this::onClickSave);
        fileMenu.getMenuItems()[FileMenu.SaveAs.ordinal()].setOnAction(this::onClickSaveAs);
        fileMenu.getMenuItems()[FileMenu.Exit.ordinal()].setOnAction(this::onClickExit);

        //BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(new TextPane());
        borderPane.setBottom(new Label("Read only"));

        stage.setTitle("Super Very NotePad :)");//getFileName
        stage.setScene(new Scene(borderPane,500,500));
        stage.show();
    }
    
    //Event Action
    public void onClickNew(ActionEvent event){
        //alert.show();
    }

    public void onClickOpen(ActionEvent event){
        
    }
    
    public void onClickSave(ActionEvent event){
        
    }
    
    public void onClickSaveAs(ActionEvent event){
        
    }
    
    public void onClickExit(ActionEvent event){
        
    }

}
