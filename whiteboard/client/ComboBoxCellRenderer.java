
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

class ComboBoxCellRenderer extends JLabel implements ListCellRenderer {
    
    public ComboBoxCellRenderer(JComboBox comboBox) {
        this.comboBox = comboBox;
        setOpaque(true);
    }
    
    public Component getListCellRendererComponent(JList listbox, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
            if (index == -1) {
                setOpaque(false);
            }
            else {
                setOpaque(true);
            }
        }
        else {
            setOpaque(true);
        }

        if (value == null) {
            setText("");
            setIcon(null);
        }
        
        setIcon(((ComboBoxItem)value).getImageIcon());
        //setIcon((ImageIcon)h.get("image"));
        setText(value.toString());
        setBackground(UIManager.getColor("ComboBox.background"));
        setForeground(UIManager.getColor("ComboBox.foreground"));
        
        //}
        return this;
      }
    
    JComboBox comboBox = null;
    
}