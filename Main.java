import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

enum FileMenu{
    New,
    Open,
    Save,
    SaveAs,
    Exit
};

public class Main extends Application{
    private static Alert alert;
    static Stage stage;
    Stage saveChangeStage = new Stage();
    TextArea textArea = new TextArea();
    FileChooser fileChooser = new FileChooser();
    PrintStream printS;
    boolean saveChange=false;

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        //Menu
        EnumMenu fileMenu = new EnumMenu(FileMenu.class,"File");
        MenuBar menuBar = new MenuBar(fileMenu.getMenu());
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        alert = new Alert(AlertType.WARNING,"Read-Only File!", ButtonType.CLOSE);
        
        //set Event
        fileMenu.getMenuItems()[FileMenu.New.ordinal() ].setOnAction(this::onClickNew);
        fileMenu.getMenuItems()[FileMenu.Open.ordinal()].setOnAction(this::onClickOpen);
        fileMenu.getMenuItems()[FileMenu.Save.ordinal()].setOnAction(this::onClickSave);
        fileMenu.getMenuItems()[FileMenu.SaveAs.ordinal()].setOnAction(this::onClickSaveAs);
        fileMenu.getMenuItems()[FileMenu.Exit.ordinal()].setOnAction(this::onClickExit);

        //BorderPane
        BorderPane borderPane = new BorderPane();
        textArea.setWrapText(true);
        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);
        borderPane.setBottom(new Label("Read only"));
        stage.setTitle("*.txt");
        stage.setScene(new Scene(borderPane,500,500));
        stage.show();

    }
    
    //Event Action
    public void onClickNew(ActionEvent event){
        BorderPane saveChangePane = new BorderPane();
        HBox btnBox = new HBox();
        Scene saveChangeScene = new Scene(saveChangePane,300,60);
        Button save = new Button("Save");
        save.setOnAction((e) -> onClickSave(event));
        Button dontSave = new Button("Don't Save");
        dontSave.setOnAction((e) -> saveChangeStage.close());
        btnBox.getChildren().addAll(save,dontSave);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setSpacing(5);
        Text text = new Text("Do you want to save change?");
        text.setFont(new Font(16));
        saveChangePane.setPadding(new Insets(5));
        saveChangePane.setTop(text);
        saveChangePane.setBottom(btnBox);
        saveChangeStage.setTitle("Save Change?");
        saveChangeStage.setScene(saveChangeScene);

        if(textArea.getText().hashCode()!=0&&!saveChange){
            saveChangeStage.show();
            saveChange=true;
        }
    }

    public void onClickOpen(ActionEvent event){
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("txt File",".txt"));
        File file = fileChooser.showOpenDialog(stage);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null){
                textArea.appendText(line);
                textArea.appendText("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(file);

    }

    public void onClickSave(ActionEvent event){
        fileChooser.setTitle("Save File");
        fileChooser.setInitialFileName("*.txt");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text file", ".txt"));
        saveChangeStage.close();
        try {
            File file = fileChooser.showSaveDialog(stage);
            fileChooser.setInitialDirectory(file.getParentFile());
            if(file != null){
                try{
                    printS = new PrintStream(file);
                    printS.print(textArea.getText());
                    stage.setTitle(file.getName());
                    //System.out.print(file.getName());
                    printS.flush();
                }catch(FileNotFoundException e){
                System.out.println("File can't save");
            }
        }

        } catch (Exception ex) {
            //TODO: handle exception
        }
        if(saveChange){
            stage.setTitle("*.txt");
            textArea.setText("");
            saveChange=false;
        }
    }
    
    public void onClickSaveAs(ActionEvent event){
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Add All", "*"));
        fileChooser.setTitle("Save File");
        try {
            File file = fileChooser.showSaveDialog(stage);
            fileChooser.setInitialDirectory(file.getParentFile());
            if(file != null){
                try{
                    printS = new PrintStream(file);
                    printS.print(textArea.getText());
                    stage.setTitle(file.getName());
                    //System.out.print(file.getName());
                    printS.flush();
                }catch(FileNotFoundException e){
                System.out.println("File can't save");
            }
        }

        } catch (Exception ex) {
            //TODO: handle exception
        }
    }

    public void onClickExit(ActionEvent event){
        stage.close();
    }
}