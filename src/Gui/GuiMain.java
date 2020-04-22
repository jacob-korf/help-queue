package Gui;

import javax.swing.*;

public class GuiMain {
    public static void main(String[] args) {
        try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ClientPanel().setVisible(true);

                    new DisplayPanel().setVisible(true);
                }
            });
        }
    }


