import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
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
    Stage scStage = new Stage();
    TextArea textArea = new TextArea();
    FileChooser fileChooser = new FileChooser();
    PrintStream printS;
    boolean saveChange=false;
    boolean openEvent=false;
    boolean requestOpen=false;
    String openFile="";

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
        SaveChangePane scPane = new SaveChangePane();
        Scene scScene = new Scene(scPane,300,60);
        scPane.saveBtn.setOnAction((e) -> onClickSaveAs(event));
        scPane.dontSaveBtn.setOnAction((e) -> {
            stage.setTitle("*.txt");
            textArea.setText("");
            saveChange=false;
            scStage.close();
        });
        scPane.cancelBtn.setOnAction((e) -> {
            saveChange=false;
            scStage.close();
        });
        scStage.setTitle("Save Change?");
        scStage.setScene(scScene);

        if(textArea.getText().hashCode()!=0&&!saveChange){
            saveChange=true;
            scStage.show();
        }
    }

    public void onClickOpen(ActionEvent event){
        openEvent=true;
        if(textArea.getText().hashCode()!=0&&!requestOpen){
            SaveChangePane scPane = new SaveChangePane();
            Scene scScene = new Scene(scPane,300,60);
            scPane.saveBtn.setOnAction((e) ->{
                //System.out.println(openFile);
                if(openFile.length()==0){
                    onClickSaveAs(event);
                    scStage.close();
                }
                else{
                    onClickSave(event);
                    requestOpen=true;
                    scStage.close();
                    onClickOpen(event);
                }
            });
            scPane.dontSaveBtn.setOnAction((e) -> {
                requestOpen=true;
                scStage.close();
                onClickOpen(event);
            });
            scPane.cancelBtn.setOnAction((e) -> {
                saveChange=true;
                scStage.close();
            });
            scStage.setTitle("Save Change?");
            scStage.setScene(scScene);
            scStage.show();
        }
        else{
            saveChange=false;
            fileChooser.setTitle("Open File");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("txt File",".txt"));
            File file = fileChooser.showOpenDialog(stage);
            openFile = file.toString();
            //System.out.println(openFile);
            stage.setTitle(file.getName());
            textArea.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null){
                    textArea.appendText(line);
                    textArea.appendText("\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        //System.out.println(file);
        requestOpen=false;
        }
    }

    public void onClickSave(ActionEvent event){
        if(openEvent){
            try {
                File file = Path.of(openFile).toFile();
                fileChooser.setInitialDirectory(file.getParentFile());
                if(file != null){
                    try{
                        printS = new PrintStream(file);
                        printS.print(textArea.getText());
                        //printS.flush();
                    }catch(FileNotFoundException e){
                    System.out.println("File can't save");
                }
            }
            }catch (Exception ex) {
                //TODO: handle exception
                ex.printStackTrace();
            }
        }
        else{
            onClickSaveAs(event);
        }
    }
    
    public void onClickSaveAs(ActionEvent event){
        fileChooser.setTitle("Save As File");
        fileChooser.setInitialFileName("*.txt");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("text file", ".txt"));
        scStage.close();
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
            ex.printStackTrace();
        }
        if(saveChange){
            stage.setTitle("*.txt");
            textArea.setText("");
            saveChange=false;
        }
        if(openEvent){
            requestOpen=true;
            onClickOpen(event);
        }
        
    }

    public void onClickExit(ActionEvent event){
        if(textArea.getText().hashCode()!=0&&!saveChange){
            SaveChangePane scPane = new SaveChangePane();
            Scene scScene = new Scene(scPane,300,60);
            scPane.saveBtn.setOnAction((e) -> {
                if(openFile.length()==0){
                    onClickSaveAs(event);
                    scStage.close();
                    stage.close();
                }
                else{
                    onClickSave(event);
                    requestOpen=true;
                    scStage.close();
                    stage.close();
                }
            });
            scPane.dontSaveBtn.setOnAction((e) -> {
                saveChange=false;
                scStage.close();
                stage.close();
            });
            scPane.cancelBtn.setOnAction((e) -> {
                saveChange=false;
                scStage.close();
            });
            scStage.setTitle("Save Change?");
            scStage.setScene(scScene);
            scStage.show();
        }
        else{
            stage.close();
        }
    }
}