package textEditor;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/* @author silviafuen */
public class FormatFont {

    public FormatFont(){
        
    }
    Dialog dialog = new Dialog();
    HBox hbox = new HBox();
    DialogPane dp = new DialogPane();
    ListView<String> familyView = new ListView<>();
    ListView<String> styleView = new ListView<>();
    ListView<Double> sizeView = new ListView<>();
    String family;
    String style;
    Double size;

    public List<String> getFamily() {   
        List<String> familiesList = Font.getFamilies();
        ObservableList<String> familiesObservableList = FXCollections.observableArrayList(familiesList);
        familyView.setItems(familiesObservableList);
        family = familyView.getSelectionModel().getSelectedItem();
        return (List<String>) familyView;
    }

    public String getStyle() {
        List<String> styles = Font.getFontNames(family);
        ObservableList<String> styleObservableList = FXCollections.observableArrayList(styles);
        styleView.setItems(styleObservableList);
        style = styleView.getSelectionModel().getSelectedItem();
        return style;
    }
    
    public double getSize(){
        
        return 0;
    }

    public Dialog getDialog(){
        dialog.setTitle("Font");
        hbox.setSpacing(10);
        hbox.getChildren().add(familyView);
        hbox.getChildren().add(styleView);
        hbox.getChildren().add(sizeView);
        dp.setContent(hbox);
        ButtonType select = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.setHeaderText(null);

        dialog.setDialogPane(dp);
        dp.autosize();
        dialog.getDialogPane().getButtonTypes().addAll(select, ButtonType.CANCEL);
        dialog.showAndWait();
        return dialog;
    }
}
