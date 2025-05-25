package Oyun;

import javax.swing.*;
import java.awt.*;

public class Karakter {
    public String ad;
    public int x, y;
    public int can = 3;
    public int mermi = 5;
    public int genislik = 90, yukseklik = 90;
    public boolean sagaBakiyor = true;
    private Image resimSag;
    private Image resimSol;

    public Karakter(String ad, int x, int y, String resimSagYolu, String resimSolYolu) {
        this.ad = ad;
        this.x = x;
        this.y = y;
        this.resimSag = new ImageIcon(resimSagYolu).getImage();
        this.resimSol = new ImageIcon(resimSolYolu).getImage();
    }

    public void ciz(Graphics g) {
        if (sagaBakiyor) {
            g.drawImage(resimSag, x, y, genislik, yukseklik, null);
        } else {
            g.drawImage(resimSol, x, y, genislik, yukseklik, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, genislik, yukseklik);
    }
    
    
}
