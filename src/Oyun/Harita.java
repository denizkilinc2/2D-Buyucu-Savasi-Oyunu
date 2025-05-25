package Oyun;

import java.util.*;

public class Harita {
    public List<Engel> engeller = new ArrayList<>(); // engeller için bir dizi

    public Harita() {
        // engellerin görselleri diziye eklendi.
        engeller.add(new Engel(500, 100, "Resimler/rock.png"));
        engeller.add(new Engel(250, 500, "Resimler/rock.png"));
        engeller.add(new Engel(300, 200, "Resimler/rock.png"));
        engeller.add(new Engel(550, 400, "Resimler/rock.png"));
    }
}