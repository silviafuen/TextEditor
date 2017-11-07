
package textEditor;

import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
 
public class TextPaste {
    
    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();
    String text;

    public TextPaste() {
    }

    public String getText() {
        text =  (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String textPaste(Clipboard clipboard,ClipboardContent content ) {
        if (content.hasString()) {
            clipboard.getContent(DataFormat.PLAIN_TEXT);
            String text = content.getString();
            System.out.println("textPaste " + text);
        } else {
            System.out.println("nothing was copied to begin with");
        }
        return text;
    }
}
