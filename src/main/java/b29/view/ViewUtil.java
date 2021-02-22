package b29.view;

import javax.swing.*;

public class ViewUtil {
    public static void popupNotify(String message){
        JOptionPane.showMessageDialog(null, message);
    }

    public static boolean popupConfirm(String title, String message){
        // 0=yes, 1=no, 2=cancel
        int ret = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        return ret == 0;
    }

    public static String popupInput(String title, String message){
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    private ViewUtil(){}
}
