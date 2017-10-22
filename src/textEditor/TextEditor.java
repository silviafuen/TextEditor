/* @author 
Silvia Fuentes
10/16/17
Lab - 11 - 3330
 */
package textEditor;

import com.google.gson.Gson;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TextEditor extends Application {

    public Settings set;
    private Desktop desktop = Desktop.getDesktop();
    final FileChooser chooseFile = new FileChooser();
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();
    MenuBar menuBar = new MenuBar();
    ToolBar toolBar = new ToolBar();
    TextArea text = new TextArea();
    File file;
    private String filename = "";
    String intro = "Type here... ";
    int width = 400;
    int height = 350;
    Stage primaryStage;
    Scene scene;

    @Override
    public void start(Stage primaryStage) {
        onOpen(); //opening the document
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Menu fileMenu = new Menu("_File");
        fileMenu.setMnemonicParsing(true);
        fileMenu.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+F")
        );
        MenuItem f0 = new MenuItem("_New");
        f0.setMnemonicParsing(true);
        f0.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+N")
        );
        MenuItem f1 = new MenuItem("_Open");
        f1.setMnemonicParsing(true);
        f1.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+O")
        );
        f1.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                file = chooseFile.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile();
                }
            }
        });
        MenuItem f2 = new MenuItem("_Save");
        f2.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+S"));
        f2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) { //Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt", "*.json", "*.rtf");
                chooseFile.getExtensionFilters().add(extFilter);
                saveFile(scene.getWidth(), scene.getHeight(), text.getText());
            }
        });
        MenuItem f3 = new MenuItem("Save As...");
        f3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                file = chooseFile.showSaveDialog(primaryStage);
            }
        });
        MenuItem f4 = new MenuItem("Page _Setup");
        MenuItem f5 = new MenuItem("_Print");
        f5.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+P")
        );
        MenuItem f6 = new MenuItem("Exit");
        f6.setOnAction(actionEvent -> primaryStage.close());
        fileMenu.getItems().add(f0);
        fileMenu.getItems().add(f1);
        fileMenu.getItems().add(f2);
        fileMenu.getItems().add(f3);
        fileMenu.getItems().add(f4);
        fileMenu.getItems().add(f5);
        fileMenu.getItems().add(f6);
        /**
         * ******MENU BARS********
         */
        menuBar.getMenus().addAll(fileMenu);
        fileMenu.setMnemonicParsing(true);
        editMenu();         //Edit Menu
        formatMenu();       //format Menu
        viewMenu();          //View Menu
        helpMenu();         //Help Menu
        toolMenu();

        // Text Editor
        text.wrapTextProperty();
        text.setText(set.getInput());
        text.setAccessibleText("Begin typing here.");
        text.positionCaret(intro.length());

        //SCENE + PANE
        BorderPane root = new BorderPane();
        VBox vbox = new VBox();
        vbox.getChildren().add(menuBar);
        vbox.getChildren().add(toolBar);
        root.setTop(vbox);
        root.setCenter(text);

        double getWidth = set.getWidth();
        double getHeight = set.getHeight();
        scene = new Scene(root, getWidth, getHeight);
        //String getText = text.getText();
        // STAGE
        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent ev) {
                saveFile(scene.getWidth(), scene.getHeight(), text.getText());
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * *****FILE ACTIONS*********
     */
    public void onOpen() {
        if(!filename.isEmpty()){
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            set = new Gson().fromJson(br.readLine(), Settings.class);
            br.close();
        } catch (IOException ex) {
            System.out.println("IOException - 177");
        }} else {
           set = new Settings(width, height, intro);      
        }
    }

    private void openFile() {
        try {
            filename = file.getName();
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            set = new Gson().fromJson(br.readLine(), Settings.class); 
            scene.getHeight();
            scene.getWidth();
            text.setText(set.input);  
            //set.setWidth(set.getWidth());
            br.close();
            System.out.println("read file");
        } catch (IOException ex) {
            System.out.println("IOException ex");
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveFile(double width, double height, String text) {
        set = new Settings(width, height, text);
        String input = new Gson().toJson(set);
        if (filename.isEmpty()) {
            file = chooseFile.showSaveDialog(primaryStage);
            saveAsFile(text, file);
        } else {
            try (FileWriter fw = new FileWriter(filename)) {
                fw.write(input);
                fw.close();
                System.out.println(width);
                System.out.println(height);
                System.out.println(text);
                System.out.println("saved");
            } catch (IOException e) {
                System.out.println("IOException - 163");
            }
        }
    }

    private void saveAsFile(String content, File file) {
        try {
            filename = file.getName();
            FileWriter fileWriter;
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void textCopy() {
        content.clear();
        clipboard.clear();
        if (content.isEmpty()) {
            content.putString(text.getText());
            clipboard.setContent(content);
            System.out.println("textCopy " + content);
        } else {
            System.out.println("nothing selected");
        }
    }

    private void textCut() {
        clipboard.clear();
        content.clear();
        content.putString(text.getText());
        text.clear();
        clipboard.setContent(content);
        System.out.println("textCut " + content);
    }

    private void textPaste() {
        if (content.hasString()) {
            clipboard.getContent(DataFormat.PLAIN_TEXT);
            //String newText = content.getString();
            //text.setText(content.getString());
            text.appendText(" " + content.getString());
            //text.appendText(newText);
            System.out.println("textPaste " + clipboard.getContent(DataFormat.PLAIN_TEXT));
        } else {
            System.out.println("nothing was copied to begin with");
        }
    }

    /**
     * *****SUB MENUS******
     */
    private void editMenu() {
        String[] eOption = {"_Undo", "_Cut", "C_opy", "_Paste", "_Delete", "Find...",
            "Find _Next", "_Replace...", "Go _To...", "Select _All", "Time/_Date"};
        Menu eMenu = new Menu("Edit");
        eMenu.setMnemonicParsing(true);
        MenuItem e0 = new MenuItem(eOption[0]);
        e0.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+Z")
        );  
        MenuItem e1 = new MenuItem(eOption[1]);
        e1.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+X")
        );
        e1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                textCut();
            }
        });
        MenuItem e2 = new MenuItem(eOption[2]);
        e2.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+C")
        );
        e2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                textCopy();
            }
        });

        MenuItem e3 = new MenuItem(eOption[3]);
        e3.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+V")
        );
        e3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                textPaste();
            }
        });
        MenuItem e4 = new MenuItem(eOption[4]);
        e4.setAccelerator(
                KeyCombination.keyCombination("Del")
        );
        e4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                text.clear();
            }
        });
        MenuItem e5 = new MenuItem(eOption[5]);
        e5.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+F")
        );
        MenuItem e6 = new MenuItem(eOption[6]);
        e6.setAccelerator(
                KeyCombination.keyCombination("F3")
        );
        MenuItem e7 = new MenuItem(eOption[7]);
        e7.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+H")
        );
        MenuItem e8 = new MenuItem(eOption[8]);
        e8.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+G")
        );
        MenuItem e9 = new MenuItem(eOption[9]);
        e9.setAccelerator(
                KeyCombination.keyCombination("SHORTCUT+A")
        );
        MenuItem e10 = new MenuItem(eOption[10]);
        e10.setAccelerator(
                KeyCombination.keyCombination("F5")
        );
        eMenu.getItems().add(e0);
        eMenu.getItems().add(e1);
        eMenu.getItems().add(e2);
        eMenu.getItems().add(e3);
        eMenu.getItems().add(e4);
        eMenu.getItems().add(e5);
        eMenu.getItems().add(e6);
        eMenu.getItems().add(e7);
        eMenu.getItems().add(e8);
        eMenu.getItems().add(e9);
        eMenu.getItems().add(e10);
        menuBar.getMenus().addAll(eMenu);
    }

    private void formatMenu() {
        Menu fMenu = new Menu("_Format");
        fMenu.setMnemonicParsing(true);
        MenuItem f1 = new MenuItem("Word _Wrap");
        MenuItem f2 = new MenuItem("_Font..");
        fMenu.getItems().add(f1);
        fMenu.getItems().add(f2);
        menuBar.getMenus().addAll(fMenu);
    }

    private void viewMenu() {
        Menu vMenu = new Menu("_View");
        vMenu.setMnemonicParsing(true);
        MenuItem v1 = new MenuItem("_Status Bar");
        vMenu.getItems().add(v1);
        menuBar.getMenus().addAll(vMenu);
    }

    private void helpMenu() {
        Menu hMenu = new Menu("_Help");
        hMenu.setMnemonicParsing(true);
        MenuItem h1 = new MenuItem("View _Help");
        MenuItem h2 = new MenuItem("_About Notepad");
        hMenu.getItems().add(h1);
        hMenu.getItems().add(h2);
        menuBar.getMenus().addAll(hMenu);
    }

    public void toolMenu() {
        Separator separate = new Separator();
        /* ****SAVE BUTTON*******/
        Button save = new Button();
        Image saveIcon = new Image(getClass().getResourceAsStream("Assets/file-save.png"));
        ImageView saveGraphic = new ImageView(saveIcon);
        save.setGraphic(saveGraphic);
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                saveFile(scene.getWidth(), scene.getHeight(), text.getText());
            }
        });
        setShadow(save);
        /**
         * *****OPEN BUTTON******
         */
        Button open = new Button();
        Image openIcon = new Image(getClass().getResourceAsStream("Assets/file-open.png"));
        ImageView openGraphic = new ImageView(openIcon);
        open.setGraphic(openGraphic);
        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                file = chooseFile.showOpenDialog(primaryStage);
                if (file != null) {
                    openFile();
                }
            }
        });
        setShadow(open);
        /**
         * *****CUT BUTTON******
         */
        Button cut = new Button();
        Image cutIcon = new Image(getClass().getResourceAsStream("Assets/file-cut.png"));
        ImageView cutGraphic = new ImageView(cutIcon);
        cut.setGraphic(cutGraphic);
        cut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textCut();
            }
        });
        setShadow(cut);
        /**
         * ****COPY BUTTON*****
         */
        Button copy = new Button();
        Image copyIcon = new Image(getClass().getResourceAsStream("Assets/file-copy.png"));
        ImageView copyGraphic = new ImageView(copyIcon);
        copy.setGraphic(copyGraphic);
        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textCopy();
            }
        });
        setShadow(copy);
        /**
         * ****PASTE BUTTON*****
         */
        Button paste = new Button();
        Image pasteIcon = new Image(getClass().getResourceAsStream("Assets/file-paste.png"));
        ImageView pasteGraphic = new ImageView(pasteIcon);
        paste.setGraphic(pasteGraphic);
        paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                textPaste();
            }
        });
        setShadow(paste);
        toolBar = new ToolBar(
                save, open, separate, cut, copy, paste
        );
    }
  
    public void setShadow(Button button){
        DropShadow shadow = new DropShadow();
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button.setEffect(shadow);
            }
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                button.setEffect(null);
            }
        });
    }
}
