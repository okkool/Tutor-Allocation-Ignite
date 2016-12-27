import java.io.File;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Coders For Causes
 * 
 */
public class Ignite extends Application {
    
//Input screen variables
/* **************************************************************************************************************************** */
    FileChooser fileChooser = new FileChooser();
    Button inBrowseButton = new Button("Browse");
    Button startButton = new Button("Start");         
    Label Desc = new Label("Location Of Excel File:");
    Label Warn = new Label("( Files must end in .xls or .xlsx )");
    TextField Direc = new TextField();
/* **************************************************************************************************************************** */
    
//Output screen variables
/* **************************************************************************************************************************** */
    Button outBrowseButton = new Button("Browse");
    Button saveButton = new Button("Save");         
    Label Dsc = new Label("Description:");
    Label Save = new Label("Save File As:");
    Label Loc = new Label("Save File In:");
    TextField exfile = new TextField();
    TextField Output = new TextField();
    ScrollPane Answer = new ScrollPane();
/* **************************************************************************************************************************** */
    
    static String filePath;
    static String saveName;
    static String savePath;
    
    public static String retFile()
    {
        return filePath;
    }
    
    public static String retName()
    {
        return saveName;
    }
    
    public static String retFolder()
    {
        return savePath;
    }
    
    public void infoBox(String infoMessage, String titleBar)
    {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        infoBox(infoMessage, titleBar, null);
    }

    public void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("icon.jpeg").toString()));
        alert.showAndWait();
    }
    
    public void warnBox(String warnMessage, String titleBar)
    {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        warnBox(warnMessage, titleBar, null);
    }

    public void warnBox(String warnMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(warnMessage);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("icon.jpeg").toString()));
        alert.showAndWait();
    }
    
    public void errorBox(String errorMessage, String titleBar)
    {
        /* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
        errorBox(errorMessage, titleBar, null);
    }

    public void errorBox(String errorMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(errorMessage);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("icon.jpeg").toString()));
        alert.showAndWait();
    }
    
    private static void configureFileChooser(FileChooser fileChooser) 
    {  
        fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) );                 
        fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Excel", "*.xls*") );
    }
    
    @Override
    public void start(final Stage inputStage) 
    {
        
//Output screen variables
/* **************************************************************************************************************************** */
        final Stage outputStage = new Stage();
        
        outputStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpeg")));
        outputStage.setTitle("Ignite Mentoring");       
           
        saveButton.setDisable(true);   
        
        
        exfile.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> 
        {
            if (newValue) 
            {
                exfile.setOnKeyPressed((KeyEvent ke) ->
                {
                    if(ke.getCode().equals(KeyCode.ENTER))
                    {
                        if(exfile.getText()!= null && !"".equals(exfile.getText()) && Output.getText()!= null && !"".equals(Output.getText()))
                        {
                            saveButton.setDisable(false);
                            saveButton.requestFocus();
                        }
                        else
                            outBrowseButton.requestFocus();                            
                    }
                });
            }
            else
            {
                if(exfile.getText()== null || "".equals(exfile.getText()))
                {
                    warnBox("File name cannot be left blank", "WARNING");
                    exfile.requestFocus();
                }
                else            
                {
                    if(exfile.getText()!= null && !"".equals(exfile.getText()) && Output.getText()!= null && !"".equals(Output.getText()))
                    {
                        saveButton.setDisable(false);
                        saveButton.requestFocus();
                    }
                    else
                        outBrowseButton.requestFocus();
                }
            }
        });
        
        Output.setOnAction((final ActionEvent e) -> 
        {
            //setOnKeyPressed((KeyEvent ke) -> {
            if(Output.getText()!= null && !"".equals(Output.getText()))
                saveButton.setDisable(false);
            else
                errorBox("Unable to access file", "ERROR");  
        });
         
        outBrowseButton.setOnAction((final ActionEvent e) -> 
        {
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            final File selectedDirectory = directoryChooser.showDialog(outputStage);
            if (selectedDirectory != null)
            {
                String temp = selectedDirectory.getAbsolutePath();
                Output.setText(temp);
                if(exfile.getText()!= null && !"".equals(exfile.getText()) && Output.getText()!= null && !"".equals(Output.getText()))
                {
                    saveButton.setDisable(false);
                    saveButton.requestFocus();
                }
                else if(exfile.getText()== null || "".equals(exfile.getText()))
                {
                    exfile.requestFocus();
                }
            } 
        });  
 
        saveButton.setOnAction((ActionEvent e) -> 
        {
            saveName = exfile.getText();
            savePath = Output.getText();
            File file = new File(savePath);
            boolean checkOld = new File(savePath, saveName+".xls").exists();
            boolean checkNew = new File(savePath, saveName+".xlsx").exists();
            if(!file.isDirectory())
            {
                saveButton.setDisable(true);
                errorBox("Folder does not exist", "ERROR");
                Output.setText("");
                Output.requestFocus();
            }
            else if(checkOld != checkNew)
            {
                saveButton.setDisable(true);
                errorBox("File already exists in current folder", "ERROR");
                exfile.setText("");
                exfile.requestFocus();
            }
            else
            {
                System.out.println(filePath + "\n" + saveName + "\n" + savePath);
                outputStage.close();
                infoBox("File has been saved", "Ignite Mentoring");
                
            }
        });
 
        AnchorPane OutputAnchorPane = new AnchorPane();         
 
        AnchorPane.setTopAnchor(Dsc, 12.0);
        AnchorPane.setLeftAnchor(Dsc, 12.0);
        //OutputAnchorPane.setRightAnchor(Desc, 230.0);
        //OutputAnchorPane.setBottomAnchor(Desc, 90.0);
        
        AnchorPane.setTopAnchor(Answer, 35.0);
        AnchorPane.setLeftAnchor(Answer, 12.0);
        AnchorPane.setRightAnchor(Answer, 12.0);
        AnchorPane.setBottomAnchor(Answer, 154.0);
        
        //OutputAnchorPane.setTopAnchor(Save, 12.0);
        AnchorPane.setLeftAnchor(Save, 12.0);
        //OutputAnchorPane.setRightAnchor(Warn, 205.0);
        AnchorPane.setBottomAnchor(Save, 124.0);
        
        //OutputAnchorPane.setTopAnchor(Direc, 53.0);
        AnchorPane.setLeftAnchor(exfile, 12.0);
        AnchorPane.setRightAnchor(exfile, 12.0);
        AnchorPane.setBottomAnchor(exfile, 97.0);
        
        //OutputAnchorPane.setTopAnchor(Save, 12.0);
        AnchorPane.setLeftAnchor(Loc, 12.0);
        //OutputAnchorPane.setRightAnchor(Warn, 205.0);
        AnchorPane.setBottomAnchor(Loc, 71.0);
        
        //OutputAnchorPane.setTopAnchor(Direc, 53.0);
        AnchorPane.setLeftAnchor(Output, 12.0);
        AnchorPane.setRightAnchor(Output, 12.0);
        AnchorPane.setBottomAnchor(Output, 46.0);
        
        //OutputAnchorPane.setTopAnchor(browseButton, 91.0);
        //OutputAnchorPane.setLeftAnchor(browseButton, 250.0);
        AnchorPane.setRightAnchor(outBrowseButton, 87.0);
        AnchorPane.setBottomAnchor(outBrowseButton, 12.0);
        
        //OutputAnchorPane.setTopAnchor(startButton, 91.0);
        //OutputAnchorPane.setLeftAnchor(startButton, 333.0);
        AnchorPane.setRightAnchor(saveButton, 12.0);
        AnchorPane.setBottomAnchor(saveButton, 12.0);
        
        Scene root = new Scene(Answer, 576, 250);
        TextArea text = new TextArea("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        text.setEditable(false);
        text.setWrapText(true);
        Answer.setFitToWidth(true);
        Answer.setFitToHeight(true);
        Answer.setContent(text);
        
        OutputAnchorPane.getChildren().addAll(Dsc, Answer,  exfile, Loc, Save, Output, saveButton, outBrowseButton);
        
        Scene outputScene = new Scene(OutputAnchorPane, 600, 397);
        outputScene.getStylesheets().add("IgniteGUI.css");
        outputStage.setScene(outputScene);
/* **************************************************************************************************************************** */   

//Input screen variables
/* **************************************************************************************************************************** */ 
        inputStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpeg")));
        inputStage.setTitle("Ignite Mentoring");       
        Warn.getStyleClass().add("myWarnLabel");
       
        inBrowseButton.requestFocus();
        startButton.setDisable(true);
         
        Direc.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> 
        {
            if (newValue) 
            {
                Direc.setOnKeyPressed((KeyEvent ke) -> 
                {
                    if(Direc.getText()!= null && !"".equals(Direc.getText()))
                        startButton.setDisable(false);
                });
            }
        });        
 
        inBrowseButton.setOnAction((ActionEvent e) -> 
        {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(inputStage);
            if(file != null)
            {
                String t = file.getAbsolutePath();
                Direc.setText(t);
                startButton.setDisable(false);
                startButton.requestFocus();
            }
            
        });     
        
        startButton.setOnAction((ActionEvent e) -> 
        {
            if(!Direc.getText().contains(".xls"))
            {
                startButton.setDisable(true);
                errorBox("Unable to access selected file", "ERROR");
                Direc.setText("");
                Direc.requestFocus();
            }
            else
            {
                filePath = Direc.getText();
                inputStage.close();
                outputStage.show();
            }
        });
 
        AnchorPane inputAnchorPane = new AnchorPane();
 
        AnchorPane.setTopAnchor(Desc, 30.0);
        AnchorPane.setLeftAnchor(Desc, 17.0);
        AnchorPane.setRightAnchor(Desc, 230.0);
        //inputAnchorPane.setBottomAnchor(Desc, 90.0);
        
        //inputAnchorPane.setTopAnchor(Warn, 95.0);
        AnchorPane.setLeftAnchor(Warn, 45.0);
        //inputAnchorPane.setRightAnchor(Warn, 205.0);
        AnchorPane.setBottomAnchor(Warn, 24.0);
        
        AnchorPane.setTopAnchor(Direc, 53.0);
        AnchorPane.setLeftAnchor(Direc, 17.0);
        AnchorPane.setRightAnchor(Direc, 15.0);
        AnchorPane.setBottomAnchor(Direc, 60.0);
        
        //inputAnchorPane.setTopAnchor(browseButton, 91.0);
        AnchorPane.setLeftAnchor(inBrowseButton, 250.0);
        AnchorPane.setRightAnchor(inBrowseButton, 97.0);
        AnchorPane.setBottomAnchor(inBrowseButton, 20.0);
        
        //inputAnchorPane.setTopAnchor(startButton, 91.0);
        AnchorPane.setLeftAnchor(startButton, 333.0);
        AnchorPane.setRightAnchor(startButton, 15.0);
        AnchorPane.setBottomAnchor(startButton, 20.0);
        
        inputAnchorPane.getChildren().addAll(Desc, Warn, inBrowseButton, Direc, startButton);
        
        Scene inputScene = new Scene(inputAnchorPane, 411, 128);
        inputScene.getStylesheets().add("IgniteGUI.css");
        
        inputStage.setScene(inputScene);
        inputStage.setResizable(false);
        inputStage.show();
/* **************************************************************************************************************************** */          
    }           

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
