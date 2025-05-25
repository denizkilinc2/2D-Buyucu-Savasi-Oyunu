package Oyun;

import java.awt.*;
import javax.swing.*;

public class FireBall {
    public int x, y; // ateş topunun konumu
    public int hiz = 8; // ateş topunun hızı
    public boolean sagaGidiyor; // sağ tarafa mı gidiyor kontrolü için
    public boolean aktif = true; // ateş topunun kullanılıp kullanılmadığı kontrolü
    private Image atesTopu; // ateş topunun görüntüsü

    public FireBall(int x, int y, boolean sagaGidiyor) {
        this.x = x;
        this.y = y;
        this.sagaGidiyor = sagaGidiyor;
        this.atesTopu = new ImageIcon("Resimler/FireBall.png").getImage();
    }

    public void hareketEt() {
        if (sagaGidiyor){
            x += hiz; // sağ tarafa gitme kontrolü
        } 
        else{
            x -= hiz; // sağ tarafa gitmiyor ise zaten sola gidecektir.
        }
    }

    public void ciz(Graphics g) {
        g.drawImage(atesTopu, x, y, 32, 32, null); // ateş topunun boyutu
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32); // çarpışma boyutu
    }
}
