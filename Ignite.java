import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author Coders For Causes 
 */
public class Ignite extends Application 
{		
	Running Students = new Running();
	public String logString;
	public boolean error   ; 
	public void setLogString(String logString)
	{
		this.logString = logString;
		error = true;
	}

	ImageView imageView = new ImageView(new Image(this.getClass().getResource("h.png").toExternalForm(), 25, 25, true, true));	

	//Input screen variables
	/* ***************************************************************************************************************************************************************************** */
	FileChooser fileChooser = new FileChooser();
	Button inBrowseButton = new Button("Browse");
	Button startButton = new Button("Start");    
	Button helpButtonInput = new Button(null,imageView);
	Label Desc = new Label("Location Of Excel File:");
	Label Warn = new Label("( Files must end in .xls or .xlsx )");
	TextField Direc = new TextField();
	/* ***************************************************************************************************************************************************************************** */

	//Output screen variables
	/* ***************************************************************************************************************************************************************************** */
	Button outBrowseButton = new Button("Browse");
	Button saveButton = new Button("Save");  
	Button helpButtonOutput = new Button(null,imageView);
	Button logButton = new Button("Log");         
	Label Dsc = new Label("Description:");
	Label Save = new Label("Save File As:");
	Label Loc = new Label("Save File In:");
	TextField exfile = new TextField();
	TextField Output = new TextField();
	ScrollPane Answer = new ScrollPane();
	/* ***************************************************************************************************************************************************************************** */

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
	
	public void saveBox(String f)
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Ignite Mentoring");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(this.getClass().getResource("icon.jpeg").toString()));
		alert.setHeaderText("File has been saved. Do you wish to open it?");
		alert.setContentText("Choose your option");

		ButtonType buttonYes = new ButtonType("Yes");
		ButtonType buttonNo = new ButtonType("No");

		alert.getButtonTypes().setAll(buttonYes, buttonNo);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == buttonYes)
		{
			Desktop desktop = Desktop.getDesktop();
			File file = new File(f);
			if(!Desktop.isDesktopSupported())
			{
	           errorBox("The operating system you are using is not supported not supported", "ERROR", "Unsupported desktop");
	           
	           alert.close();
			}
			try {
	            desktop.open(file);
	        } catch (IOException ex) {
	            Logger.getLogger(Ignite.class.getName()).log(
	                Level.SEVERE, null, ex
	            );
	        }
		}
		
		else 
		{
		    alert.close();
		    System.exit(0);
		}
	}

	private static void configureFileChooser(FileChooser fileChooser) 
	{  
		fileChooser.setInitialDirectory( new File(System.getProperty("user.home")) );                 
		fileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter("Excel", "*.xls*") );
	}

	@Override
	public void start(final Stage inputStage) 
	{      	
		
		ScrollPane helppane = new ScrollPane();
		Scene help = new Scene(helppane, 576, 250);
		TextArea helpInfo = new TextArea("");
		helpInfo.setEditable(false);
		helppane.setFitToWidth(true);
		helppane.setFitToHeight(true);
		helppane.setContent(helpInfo);
		Stage helpStage = new Stage();
		helpStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpeg")));
		helpStage.setTitle("Ignite Mentoring");      	
		help.getStylesheets().add("IgniteGUI.css");
		helpStage.setScene(help);

//Output screen variables
/* ********************************************************************************************************************************************************************************* */
		final Stage outputStage = new Stage();

		outputStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpeg")));
		outputStage.setTitle("Ignite Mentoring");       

		saveButton.setDisable(true);   

		helpButtonOutput.setContentDisplay(ContentDisplay.LEFT);
		helpButtonOutput.getStyleClass().add("myHelpButton");

		helpButtonOutput.setOnAction((final ActionEvent e) -> 
		{
			helpStage.show();
		});
		
		Stage logStage = new Stage();
		logStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpeg")));
		logStage.setTitle("Ignite Mentoring log");     
		
		
		TextArea logInfo = new TextArea("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logInfo.setEditable(false);
		//logInfo.setWrapText(true);
		ScrollPane logpane = new ScrollPane(); 
		logpane.setFitToWidth(true);
		logpane.setFitToHeight(true);
		logpane.setContent(logInfo);
		Scene logScene = new Scene(logpane, 576, 250);
		logScene.getStylesheets().add("IgniteGUI.css");
		logStage.setScene(logScene);		

		logButton.setOnAction((final ActionEvent e) -> 
		{
			//System.out.println(Students.returnLog());
			
			logInfo.setText(Students.returnLog()
					+ "================================================================================================================================================================================================================================================\n"
					+ "  ERRORS:"
					+ "\n================================================================================================================================================================================================================================================\n"
					+(logString==null ? "No errors" : "\n"+logString));				
			logStage.show();
		});

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
				if(exfile.getText()!= null && !"".equals(exfile.getText()) && Output.getText()!= null && !"".equals(Output.getText()))
					{
						saveButton.setDisable(false);
						saveButton.requestFocus();
					}
					else
						outBrowseButton.requestFocus();
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
				String save = savePath+"\\"+saveName+".xls";
				outputStage.close();
				//save here tag
				try {
					if (filePath.endsWith(".xlsx"))
						save += "x";
					Students.save(save);
				} catch (Exception e1) {
					
					String temp = "";
					StackTraceElement a[] = e1.getStackTrace();
					for (int i = 0; i < a.length; i++) {
						temp = temp+"\nat "+a[i].getMethodName()+" line "+a[i].getLineNumber()+" "+a[i].getFileName();
					}
					setLogString(temp);

					e1.printStackTrace();
				}
				saveBox(save);

			}
		});

		AnchorPane OutputAnchorPane = new AnchorPane();         

		AnchorPane.setTopAnchor(Dsc, 12.0);
		AnchorPane.setLeftAnchor(Dsc, 12.0);
		//OutputAnchorPane.setRightAnchor(Desc, 230.0);
		//OutputAnchorPane.setBottomAnchor(Desc, 90.0);

		AnchorPane.setTopAnchor(helpButtonOutput, 5.0);
		AnchorPane.setRightAnchor(helpButtonOutput, 15.0);
		//OutputAnchorPane.setLeftAnchor(helpButton, 230.0);
		//OutputAnchorPane.setBottomAnchor(helpButton, 90.0);

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

		//OutputAnchorPane.setTopAnchor(logButton, 91.0);
		AnchorPane.setLeftAnchor(logButton, 12.0);
		//AnchorPane.setRightAnchor(logButton, 87.0);
		AnchorPane.setBottomAnchor(logButton, 12.0);

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

		OutputAnchorPane.getChildren().addAll(Dsc, Answer, helpButtonOutput, exfile, Loc, Save, Output, logButton, saveButton, outBrowseButton);

		Scene outputScene = new Scene(OutputAnchorPane, 600, 397);
		outputScene.getStylesheets().add("IgniteGUI.css");
		outputStage.setScene(outputScene);
/* ********************************************************************************************************************************************************************************* */   

//Input screen variables
/* ********************************************************************************************************************************************************************************* */ 
		inputStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.jpeg")));
		inputStage.setTitle("Ignite Mentoring");       
		Warn.getStyleClass().add("myWarnLabel");

		inBrowseButton.requestFocus();
		startButton.setDisable(true);
		helpButtonInput.setContentDisplay(ContentDisplay.LEFT);
		helpButtonInput.getStyleClass().add("myHelpButton");

		helpButtonInput.setOnAction((final ActionEvent e) -> 
		{
			helpStage.show();
		});

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

				try {
					String discription = Students.pairingSystem(filePath);
					if(discription.charAt(0)=='0'){
						text.setText(discription.substring(1));
					}else{
						text.setText("There was a fatal error.\nAn error log was created in this directory.\n\nThe error was of type:\n   "+discription.substring(1, discription.indexOf("\n")));
						exfile.setDisable(true);
						Output.setDisable(true);
						outBrowseButton.setDisable(true);
						setLogString(discription.substring(1));
					}
					
				} catch (Exception e1) {
					//todo
					StackTraceElement[] ee = e1.getStackTrace();
					text.setText("There was a fatal error.\nAn error log was created in this directory.\n\nThe error was of type:\n   "+ee[1].getMethodName()+" on line "+ee[1].getLineNumber());
					exfile.setDisable(true);
					Output.setDisable(true);
					outBrowseButton.setDisable(true);
					String temp = e1.getClass().getSimpleName();
					StackTraceElement a[] = e1.getStackTrace();
					for (int i = 0; i < a.length; i++) {
						temp = temp+"\n\tat "+a[i].getMethodName()+(a[i].getLineNumber()>-1?" line "+a[i].getLineNumber():"")+(a[i].getFileName()!=null?" "+a[i].getFileName():"");
					}
					setLogString(temp);

					e1.printStackTrace();
				}


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

		AnchorPane.setTopAnchor(helpButtonInput, 15.0);
		//AnchorPane.setLeftAnchor(helpButton, 17.0);
		AnchorPane.setRightAnchor(helpButtonInput, 15.0);
		//AnchorPane.setBottomAnchor(helpButton, 20.0);

		//inputAnchorPane.setTopAnchor(startButton, 91.0);
		AnchorPane.setLeftAnchor(startButton, 333.0);
		AnchorPane.setRightAnchor(startButton, 15.0);
		AnchorPane.setBottomAnchor(startButton, 20.0);

		inputAnchorPane.getChildren().addAll(Desc, Warn, inBrowseButton, helpButtonInput, Direc, startButton);

		Scene inputScene = new Scene(inputAnchorPane, 411, 128);
		inputScene.getStylesheets().add("IgniteGUI.css");

		inputStage.setScene(inputScene);
		inputStage.setResizable(false);
		inputStage.show();
/* ********************************************************************************************************************************************************************************* */          
	}           

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

}
