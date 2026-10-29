import java.awt.*;                                        // import Image dan Rectangle dari AWT

public class Pipe {                                       // class yang mewakili satu pipa (obstacle)
    private int posX;                                     // posisi horizontal (sumbu X) pipa
    private int posY;                                     // posisi vertikal (sumbu Y) pipa
    private int width;                                    // lebar pipa
    private int height;                                   // tinggi pipa
    private Image image;                                  // gambar pipa
    private int velocityX;                                // kecepatan gerak pipa ke samping
    private boolean upper;                                // true = pipa atas, false = pipa bawah
    private boolean passed = false;                       // true jika pipa sudah dilewati player

    public Pipe(int posX, int posY, int width, int height, Image image, int velocityX, boolean upper) { // constructor pipa
        this.posX = posX;                                 // simpan posisi X awal
        this.posY = posY;                                 // simpan posisi Y awal
        this.width = width;                               // simpan lebar
        this.height = height;                             // simpan tinggi
        this.image = image;                               // simpan gambar
        this.velocityX = velocityX;                       // simpan kecepatan gerak
        this.upper = upper;                               // simpan jenis pipa (atas/bawah)
    }

    public void move() {                                  // menggerakkan pipa satu langkah
        posX += velocityX;                                // geser posisi X sebesar kecepatan (negatif = ke kiri)
    }

    public Rectangle getBounds() {                        // kotak tabrakan (hitbox) pipa
        return new Rectangle(posX, posY, width, height);  // kotak sesuai posisi dan ukuran pipa
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

    public int getVelocityX() { return velocityX; }       // getter kecepatan
    public void setVelocityX(int velocityX) { this.velocityX = velocityX; } // setter kecepatan

    public boolean isUpper() { return upper; }            // getter: apakah pipa atas
    public void setUpper(boolean upper) { this.upper = upper; } // setter jenis pipa (atas/bawah)

    public boolean isPassed() { return passed; }          // getter: apakah sudah dilewati
    public void setPassed(boolean passed) { this.passed = passed; } // setter status dilewati
}