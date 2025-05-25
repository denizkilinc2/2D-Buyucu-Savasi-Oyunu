package Oyun;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String oyuncu1 = JOptionPane.showInputDialog("Oyuncu 1 adını girin:");
        String oyuncu2 = JOptionPane.showInputDialog("Oyuncu 2 adını girin:");

        SwingUtilities.invokeLater(() -> {
            new OyunPenceresi(oyuncu1, oyuncu2);
        });
    }
}
