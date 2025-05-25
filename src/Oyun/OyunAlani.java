package Oyun;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OyunAlani extends JPanel implements ActionListener {

    private Karakter oyuncu1, oyuncu2;
    private java.util.List<FireBall> atesler1 = new ArrayList<>();
    private java.util.List<FireBall> atesler2 = new ArrayList<>();
    private Harita harita;
    private Image arkaPlan;
    private Timer timer;
    private String oyuncu1Adi, oyuncu2Adi;
    private long sonAtisZamani1 = 0;
    private long sonAtisZamani2 = 0;
    private final int atisBeklemeSuresi = 1000;
    private Heal heal;
    private Timer healRespawn, healStay;
    private Timer mermiTimer;
    

    private final Set<Integer> aktifTuslar = new HashSet<>();

    public OyunAlani(String oyuncu1Adi, String oyuncu2Adi) {
        this.oyuncu1Adi = oyuncu1Adi;
        this.oyuncu2Adi = oyuncu2Adi;

        setPreferredSize(new Dimension(900, 700));
        setFocusable(true);

        arkaPlan = new ImageIcon("Resimler/ArkaPlan.png").getImage();

        oyuncu1 = new Karakter(oyuncu1Adi, 50, 350, "Resimler/Buyucu1Sag.png", "Resimler/Buyucu1Sol.png");
        oyuncu2 = new Karakter(oyuncu2Adi, 800, 350, "Resimler/Buyucu2Sag.png", "Resimler/Buyucu2Sol.png");
        oyuncu2.sagaBakiyor = false;

        harita = new Harita();

        timer = new Timer(20, this);
        timer.start();
        // HEAL ZAMANLAYICILARI
        Random rand = new Random();
        healRespawn = new Timer(10000, e -> {
            int x = rand.nextInt(getWidth() - Heal.WIDTH);
            int y = rand.nextInt(getHeight() - Heal.HEIGHT);
            heal = new Heal(x, y);
            healStay.restart();
        });
        healRespawn.setInitialDelay(1000);
        healRespawn.start();

        healStay = new Timer(10000, e -> {
            if (heal != null) heal.setVisible(false);
        });
        // Mermi dolum sistemi
        mermiTimer = new Timer(5000, e -> {
            if (oyuncu1.mermi < 5) oyuncu1.mermi++;
            if (oyuncu2.mermi < 5) oyuncu2.mermi++;
        });
        mermiTimer.start();
        
        

        // Tuş kontrolü için
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        for (int i = 0; i < 256; i++) {
            final int key = i;
            im.put(KeyStroke.getKeyStroke(i, 0, false), "pressed" + i);
            im.put(KeyStroke.getKeyStroke(i, 0, true), "released" + i);
            am.put("pressed" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    aktifTuslar.add(key);
                }
            });
            am.put("released" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    aktifTuslar.remove(key);
                }
            });
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(arkaPlan, 0, 0, 900, 700, null);

        for (Engel engel : harita.engeller) {
            engel.ciz(g);
        }

        oyuncu1.ciz(g);
        oyuncu2.ciz(g);

        for (FireBall a : atesler1) a.ciz(g);
        for (FireBall a : atesler2) a.ciz(g);
        if (heal != null && heal.isVisible()) {
            heal.draw(g);
        }

        // Oyuncu 1 - İsim ve Can
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(oyuncu1.ad, 20, 30);

        // Canları Kalp ile gösterme
        Image kalpResmi = new ImageIcon("Resimler/kalp.png").getImage();
        for (int i = 0; i < oyuncu1.can; i++) {
            g.drawImage(kalpResmi, 20 + i * 35, 40, 32, 32, null);
        }
        // Şarjörleri ekranda gösterme
        int maxMermi = 5; // Maksimum mermi

        Image fireBallResmi = new ImageIcon("Resimler/FireBall.png").getImage(); // Fireball görseli

        g.drawImage(fireBallResmi, 20, 80, 25, 25, null); // Fireball resmi
        g.drawString(oyuncu1.mermi + "/" + maxMermi, 55, 100); // Oyuncu1 mermi sayısı

        g.drawImage(fireBallResmi, 700, 80, 25, 25, null); // Oyuncu2 Fireball resmi
        g.drawString(oyuncu2.mermi + "/" + maxMermi, 735, 100); // Oyuncu2 mermi sayısı

        // Oyuncu 2 - İsim ve Can
        g.setColor(Color.WHITE);
        g.drawString(oyuncu2.ad, 700, 30);
        for (int i = 0; i < oyuncu2.can; i++) {
            g.drawImage(kalpResmi, 700 + i * 35, 40, 32, 32, null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        hareketEttir();
        fireballKontrol();
        healKontrol();
        repaint();
        oyunKontrol();
    }
    
   

    private void hareketEttir() {
        hareketKontrol(oyuncu1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D);
        hareketKontrol(oyuncu2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

        
        if (aktifTuslar.contains(KeyEvent.VK_F)) atesEt(oyuncu1, atesler1, 1);
        if (aktifTuslar.contains(KeyEvent.VK_I)) atesEt(oyuncu2, atesler2, 2);
    }

    private void hareketKontrol(Karakter k, int yukari, int asagi, int sol, int sag) {
        int yeniX = k.x;
        int yeniY = k.y;

        if (aktifTuslar.contains(yukari)) yeniY -= 5;
        if (aktifTuslar.contains(asagi)) yeniY += 5;
        if (aktifTuslar.contains(sol)) {
            yeniX -= 5;
            k.sagaBakiyor = false;
        }
        if (aktifTuslar.contains(sag)) {
            yeniX += 5;
            k.sagaBakiyor = true;
        }

        Rectangle yeniKonum = new Rectangle(yeniX, yeniY, k.genislik, k.yukseklik);
        boolean carpisma = false;

        for (Engel engel : harita.engeller) {
            if (yeniKonum.intersects(engel.getBounds())) {
                carpisma = true;
                break;
            }
        }

        if (!carpisma && yeniX >= 0 && yeniX + k.genislik <= getWidth() && yeniY >= 0 && yeniY + k.yukseklik <= getHeight()) {
            k.x = yeniX;
            k.y = yeniY;
        }
    }

    private void atesEt(Karakter oyuncu, java.util.List<FireBall> atesListesi, int oyuncuNumarasi) {
        long simdi = System.currentTimeMillis();
        if (oyuncu.mermi > 0) {
        // Atış bekleme süresi dolmuş mu kontrol et
            if ((oyuncuNumarasi == 1 && simdi - sonAtisZamani1 >= atisBeklemeSuresi) ||
                (oyuncuNumarasi == 2 && simdi - sonAtisZamani2 >= atisBeklemeSuresi)) {

                    int topX = oyuncu.sagaBakiyor ? oyuncu.x + oyuncu.genislik : oyuncu.x - 32;
                    int topY = oyuncu.y + oyuncu.yukseklik / 2 - 16;
                    atesListesi.add(new FireBall(topX, topY, oyuncu.sagaBakiyor));

                    // Mermiyi azalt ve son atış zamanını güncelle
                    oyuncu.mermi--;
                if (oyuncuNumarasi == 1) {
                    sonAtisZamani1 = simdi;
                } else {
                    sonAtisZamani2 = simdi;
                }
            }
        }
    }

    private void fireballKontrol() {
        Iterator<FireBall> it1 = atesler1.iterator();
        while (it1.hasNext()) {
            FireBall a = it1.next();
            a.hareketEt();
            if (carpisti(a, oyuncu2)) {
                oyuncu2.can--;
                it1.remove();
            } else if (engelCarpisti(a) || a.x < 0 || a.x > 900) {
                it1.remove();
            }
        }

        Iterator<FireBall> it2 = atesler2.iterator();
        while (it2.hasNext()) {
            FireBall a = it2.next();
            a.hareketEt();
            if (carpisti(a, oyuncu1)) {
                oyuncu1.can--;
                it2.remove();
            } else if (engelCarpisti(a) || a.x < 0 || a.x > 900) {
                it2.remove();
            }
        }
    }

    private boolean carpisti(FireBall top, Karakter hedef) {
        return top.getBounds().intersects(new Rectangle(hedef.x, hedef.y, hedef.genislik, hedef.yukseklik));
    }

    private boolean engelCarpisti(FireBall top) {
        for (Engel engel : harita.engeller) {
            if (top.getBounds().intersects(engel.getBounds())) return true;
        }
        return false;
    }

    private void oyunKontrol() {
        if (oyuncu1.can <= 0 || oyuncu2.can <= 0) {
            timer.stop();
            String kazanan = oyuncu1.can > 0 ? oyuncu1.ad : oyuncu2.ad;
            boolean yeniden = OyunSonuEkrani.sor(kazanan);
            if (yeniden) yenidenBaslat();
            else System.exit(0);
        }
    }
    
    private void healKontrol() {
    if (heal == null || !heal.isVisible()) return;

    Rectangle healBounds = heal.getBounds();

    if (oyuncu1.can < 3 && healBounds.intersects(new Rectangle(oyuncu1.x, oyuncu1.y, oyuncu1.genislik, oyuncu1.yukseklik))) {
        oyuncu1.can++;
        heal.setVisible(false);
    } else if (oyuncu2.can < 3 && healBounds.intersects(new Rectangle(oyuncu2.x, oyuncu2.y, oyuncu2.genislik, oyuncu2.yukseklik))) {
        oyuncu2.can++;
        heal.setVisible(false);
    }
}

    private void yenidenBaslat() {
        oyuncu1.can = 3;
        oyuncu2.can = 3;
        oyuncu1.mermi = 5;
        oyuncu2.mermi = 5;
        oyuncu1.x = 50;
        oyuncu1.y = 350;
        oyuncu2.x = 800;
        oyuncu2.y = 350;
        oyuncu1.sagaBakiyor = true;
        oyuncu2.sagaBakiyor = false;
        atesler1.clear();
        atesler2.clear();
        aktifTuslar.clear();
        timer.start();
    }
}