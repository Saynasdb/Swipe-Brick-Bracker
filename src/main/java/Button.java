import javax.swing.*;

public
class Button extends JButton {
    Button(String text){
        this.setFocusable(false);
        this.setText(text);
    }
}
