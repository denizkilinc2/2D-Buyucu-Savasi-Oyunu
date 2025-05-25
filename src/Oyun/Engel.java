package Oyun;

import java.awt.*;
import javax.swing.*;

public class Engel {
    public int x, y; // engellerin mapteki konumu
    private Image engel; // engellerin görseli

    public Engel(int x, int y, String resimYolu) {
        this.x = x;
        this.y = y;
        this.engel = new ImageIcon(resimYolu).getImage(); // dosyadan engel resmini çeker.
    }

    public void ciz(Graphics g) {
        g.drawImage(engel, x, y, 70, 60, null); // engellerin boyutunu 70*80 boyutunda çizmeye yarar.
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 45, 50); // oyuncuların engellerle çarpışma kontrolü için 60*60 bir alan oluşturur.
                                           // Burada engelden daha küçük çizdirmemizin sebebi oyunu oynarken daha doğal bir oynanış olması için.
    }
}
