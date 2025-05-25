package Oyun;

import javax.swing.*;

public class OyunPenceresi extends JFrame {
    public OyunPenceresi(String oyuncu1, String oyuncu2) {
        setTitle("2D Savaş Oyunu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        OyunAlani oyunAlani = new OyunAlani(oyuncu1, oyuncu2);
        add(oyunAlani);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
