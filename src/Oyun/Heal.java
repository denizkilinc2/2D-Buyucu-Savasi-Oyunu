package Oyun;

import java.awt.*;
import javax.swing.*;

public class Heal {
    public static final int WIDTH = 64; // can iksirinin boyutu
    public static final int HEIGHT = 64; // can iksirinin boyutu

    private int x, y; // can iksirinin konumu
    private boolean visible = true; // can iksirinin gözüküp gözükmeyeceğini sorgulamak için
    private Image healPotion;

    public Heal(int x, int y) {
        this.x = x;
        this.y = y;
        this.healPotion = new ImageIcon("Resimler/heal.png").getImage();
    }

    public void draw(Graphics g) {
        if (visible) {
            g.drawImage(healPotion, x, y, WIDTH, HEIGHT, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH - 24, HEIGHT - 24); // can iksiri çarpışma olayı için boyut belirler bunu yine doğallık açısından boyutunu küçülttük.
    }

    public boolean isVisible() { // can iksirinin görünüp görünmeme durumunu kontrol eder.
        return visible;
    }

    public void setVisible(boolean visible) { // can iksirinin görünürlük durumunu atamak için
        this.visible = visible; 
    }
}
