import java.awt.*;                                        // import Image dan Rectangle dari AWT

public class Player {                                     // class yang mewakili burung (player)
    private int posX;                                     // posisi horizontal (sumbu X) burung
    private int posY;                                     // posisi vertikal (sumbu Y) burung
    private int width;                                    // lebar burung
    private int height;                                   // tinggi burung
    private Image image;                                  // gambar burung
    private int velocityY;                                // kecepatan vertikal (negatif = naik, positif = turun)

    public Player(int posX, int posY, int width, int height, Image image) { // constructor player
        this.posX = posX;                                 // simpan posisi X awal
        this.posY = posY;                                 // simpan posisi Y awal
        this.width = width;                               // simpan lebar
        this.height = height;                             // simpan tinggi
        this.image = image;                               // simpan gambar

        this.velocityY = 0;                               // awalnya burung diam (kecepatan 0)
    }

    // hitbox sedikit lebih kecil dari gambar supaya tabrakan terasa adil
    public Rectangle getBounds() {                        // kotak tabrakan (hitbox) burung
        return new Rectangle(posX + 3, posY + 3, width - 6, height - 6); // kotak dikecilkan 3 px tiap sisi
    }

    public int getPosX() { return posX; }                 // getter posisi X
    public void setPosX(int posX) { this.posX = posX; }   // setter posisi X

    public int getPosY() { return posY; }                 // getter posisi Y
    public void setPosY(int posY) { this.posY = posY; }   // setter posisi Y

    public int getWidth() { return width; }               // getter lebar
    public void setWidth(int width) { this.width = width; } // setter lebar

    public int getHeight() { return height; }             // getter tinggi
    public void setHeight(int height) { this.height = height; } // setter tinggi

    public Image getImage() { return image; }             // getter gambar
    public void setImage(Image image) { this.image = image; } // setter gambar

    public int getVelocityY() { return velocityY; }       // getter kecepatan vertikal
    public void setVelocityY(int velocityY) { this.velocityY = velocityY; } // setter kecepatan vertikal
}