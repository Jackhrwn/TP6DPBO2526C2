import javax.swing.*;                                           // import komponen Swing (JPanel, JButton, ImageIcon, dll.)
import java.awt.*;                                              // import Graphics, Color, Font, Dimension, dll.

public class MainMenu extends JPanel {                          // layar Main Menu (turunan JPanel)
    private Image background;                                   // gambar latar menu
    private Image bird;                                         // gambar burung hiasan menu

    public MainMenu(Runnable onStart, Runnable onExit) {        // constructor menerima aksi untuk tombol MULAI & KELUAR
        setPreferredSize(new Dimension(360, 640)); // ukuran panel sama dengan layar game
        setLayout(null);                                        // posisi komponen diatur manual dengan setBounds

        background = new ImageIcon(getClass().getResource("assets/background.png")).getImage(); // muat gambar latar
        bird = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();             // muat gambar burung

        JButton startButton = createButton("MULAI", 340);     // buat tombol MULAI di y = 340
        startButton.addActionListener(e -> onStart.run()); // saat diklik, jalankan aksi mulai

        JButton exitButton = createButton("KELUAR", 410);    // buat tombol KELUAR di y = 410
        exitButton.addActionListener(e -> onExit.run());  // saat diklik, jalankan aksi keluar
    }

    private JButton createButton(String text, int y) {               // membuat tombol bergaya seragam
        JButton button = new JButton(text);                          // buat tombol dengan teks
        button.setBounds(80, y, 200, 52);            // posisi x=80, y sesuai parameter, ukuran 200x52
        button.setFont(new Font("Arial", Font.BOLD, 20)); // huruf tebal ukuran 20
        button.setBackground(new Color(247, 200, 40));      // warna latar tombol kuning
        button.setForeground(new Color(84, 56, 71));        // warna teks cokelat tua
        button.setOpaque(true);                                      // memastikan warna latar terlihat
        button.setFocusPainted(false);                               // hilangkan garis fokus pada teks
        button.setBorder(BorderFactory.createLineBorder(new Color(84, 56, 71), 3)); // garis tepi cokelat 3 px
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // kursor berubah jadi tangan
        add(button);                                                // tambahkan tombol ke panel
        return button;                                              // kembalikan tombol agar bisa diberi aksi
    }

    @Override                                                       // menimpa method bawaan JPanel
    protected void paintComponent(Graphics g) {                     // menggambar isi menu
        super.paintComponent(g);                                    // bersihkan panel dulu
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null); // gambar latar memenuhi panel

        g.setFont(new Font("Arial", Font.BOLD, 44));     // huruf judul tebal ukuran 44
        String title = "FLAPPY BIRD";                               // teks judul
        int x = (getWidth() - g.getFontMetrics().stringWidth(title)) / 2; // hitung X agar judul rata tengah
        g.setColor(Color.BLACK);                                    // warna bayangan hitam
        g.drawString(title, x + 3, 163);                      // gambar bayangan judul (geser 3 px)
        g.setColor(Color.WHITE);                                    // warna judul putih
        g.drawString(title, x, 160);                             // gambar judul di atas bayangan

        g.drawImage(bird, (getWidth() - 68) / 2, 210, 68, 48, null); // gambar burung rata tengah, diperbesar 2x
    }
}