package Oyun;

import javax.swing.*;

public class OyunSonuEkrani {
    public static boolean sor(String kazanan) {
        int secim = JOptionPane.showConfirmDialog(null, kazanan + " kazandı! Yeniden oynamak ister misiniz?", "Oyun Bitti", JOptionPane.YES_NO_OPTION);
        return secim == JOptionPane.YES_OPTION;
    }
}
