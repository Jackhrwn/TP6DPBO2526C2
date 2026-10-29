import javax.swing.*;                                     // import komponen Swing (JPanel, JLabel, dll.)
import java.awt.*;                                        // import Graphics, Color, Font, Dimension, dll.

public class View extends JPanel {                        // kanvas utama game (turunan JPanel)

    int width = 360;                                      // lebar kanvas
    int height = 640;                                     // tinggi kanvas

    private Logic logic;                                  // referensi ke Logic untuk membaca data game
    private JLabel scoreLabel;                            // JLabel untuk menampilkan skor

    public View(Logic logic) {                            // constructor menerima objek Logic
        this.logic = logic;                               // simpan referensi Logic
        setPreferredSize(new Dimension(width, height));   // atur ukuran kanvas
        setBackground(Color.cyan);                        // warna latar cadangan: cyan
        setLayout(null);                                  // layout manual agar JLabel bisa diposisikan bebas

        // JLabel skor
        scoreLabel = new JLabel("0", SwingConstants.CENTER);        // buat label berisi "0", teks rata tengah
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 48)); // huruf tebal ukuran 48
        scoreLabel.setForeground(Color.WHITE);                           // warna teks putih
        scoreLabel.setBounds(0, 30, width, 64);             // posisi di atas layar selebar kanvas
        scoreLabel.setVisible(false);                                    // disembunyikan sampai game dimulai
        add(scoreLabel);                                                 // tambahkan label ke panel

        // Tambahkan fokus dan Key Listener untuk menerima input
        setFocusable(true);                               // panel boleh menerima fokus keyboard
        addKeyListener(logic);                            // Logic menjadi pendengar tombol keyboard
    }                                                     // akhir constructor

    public void setScore(int score) {                     // memperbarui teks skor
        scoreLabel.setText(String.valueOf(score));        // ubah angka skor menjadi teks pada label
    }                                                     // akhir method setScore

    public void setScoreVisible(boolean visible) {        // menampilkan/menyembunyikan label skor
        scoreLabel.setVisible(visible);                   // atur visibilitas label
    }                                                     // akhir method setScoreVisible

    @Override                                             // menimpa method bawaan JPanel
    public void paintComponent(Graphics g) {              // dipanggil Swing setiap kali panel digambar ulang
        super.paintComponent(g);                          // bersihkan panel dulu
        draw(g);                                          // gambar semua elemen game
    }                                                     // akhir method paintComponent

    public void draw(Graphics g) {                        // menggambar seluruh isi game
        Graphics2D g2 = (Graphics2D) g;                   // ubah ke Graphics2D agar punya fitur lebih (rotasi, dll.)
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // teks lebih halus

        // background
        g.drawImage(logic.getBackgroundImage(), 0, 0, width, height, null); // gambar latar memenuhi kanvas

        // pipa
        for (Pipe pipe : logic.getPipes()) {              // ulangi untuk setiap pipa
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), // gambar pipa di koordinat X,Y
                    pipe.getWidth(), pipe.getHeight(), null); // dengan ukuran lebar dan tinggi pipa
        }                                                 // akhir for

        // player (miring mengikuti kecepatan jatuh)
        Player player = logic.getPlayer();                // ambil objek player dari Logic
        if (player != null) {                             // gambar hanya jika player ada
            Graphics2D gp = (Graphics2D) g2.create();     // salinan Graphics agar rotasi tidak memengaruhi gambar lain
            double angle = logic.isStarted()              // jika game sudah dimulai
                    ? Math.max(-0.5, Math.min(1.0, player.getVelocityY() * 0.06)) : 0;     // sudut miring dibatasi, selain itu 0
            gp.rotate(angle, player.getPosX() + player.getWidth() / 2.0,                // putar di sekitar titik tengah burung (X)
                    player.getPosY() + player.getHeight() / 2.0);                          // titik tengah burung (Y)
            gp.drawImage(player.getImage(), player.getPosX(), player.getPosY(),            // gambar burung di koordinat X,Y
                    player.getWidth(), player.getHeight(), null);                 // dengan ukuran lebar dan tinggi burung
            gp.dispose();                                                                  // buang salinan Graphics
        }

        // layar siap main
        if (!logic.isStarted() && !logic.isGameOver()) {                                   // jika game belum dimulai dan belum game over
            g.setFont(new Font("Arial", Font.BOLD, 18));                        // huruf tebal ukuran 18
            drawCentered(g, "Tekan SPASI untuk mulai", 400, Color.WHITE);          // tampilkan petunjuk mulai
        }

        // layar game over
        if (logic.isGameOver()) {                                                           // jika game over
            g.setColor(new Color(0, 0, 0, 110));                                // hitam semi transparan
            g.fillRect(0, 0, width, height);                                          // gelapkan seluruh layar
            g.setFont(new Font("Arial", Font.BOLD, 44));                         // huruf besar untuk judul
            drawCentered(g, "GAME OVER", 240, Color.WHITE);                         // tulisan GAME OVER
            g.setFont(new Font("Arial", Font.BOLD, 24));                         // huruf sedang untuk skor
            drawCentered(g, "Skor: " + logic.getScore(), 300, Color.WHITE);         // tampilkan skor sekarang
            drawCentered(g, "Terbaik: " + logic.getHighScore(), 335, Color.YELLOW); // tampilkan skor terbaik
            g.setFont(new Font("Arial", Font.BOLD, 16));                         // huruf kecil untuk petunjuk
            drawCentered(g, "Tekan R untuk restart", 400, Color.WHITE);             // petunjuk restart
            drawCentered(g, "Tekan M untuk kembali ke menu", 425, Color.WHITE);     // petunjuk kembali ke menu
        }
    }

    // teks rata tengah dengan bayangan hitam
    private void drawCentered(Graphics g, String text, int y, Color color) { // menulis teks rata tengah
        FontMetrics fm = g.getFontMetrics();              // alat ukur lebar teks
        int x = (width - fm.stringWidth(text)) / 2;       // hitung X agar teks berada di tengah
        g.setColor(Color.BLACK);                          // warna bayangan hitam
        g.drawString(text, x + 2, y + 2);           // gambar bayangan (geser 2 px)
        g.setColor(color);                                // warna teks sesuai parameter
        g.drawString(text, x, y);                         // gambar teks utama
    }
}