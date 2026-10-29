import javax.swing.*;                                     // import Swing (JFrame, JPanel, SwingUtilities)
import java.awt.*;                                        // import CardLayout

public class App {                                        // class utama program
    public static void main(String[] args) {              // titik awal program dijalankan
        JFrame frame = new JFrame("FlappyBird");         // buat jendela berjudul "FlappyBird"
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // program berhenti saat jendela ditutup
        frame.setResizable(false);                        // ukuran jendela tidak bisa diubah

        // CardLayout: berpindah antara Main Menu dan layar game
        CardLayout cards = new CardLayout();              // layout bertumpuk (satu layar tampil sekali)
        JPanel root = new JPanel(cards);                  // panel induk memakai CardLayout

        Logic logika = new Logic();                       // buat otak game
        View tampilan = new View(logika);                 // buat tampilan; butuh Logic agar bisa berkomunikasi
        logika.setView(tampilan);                         // sambungkan Logic ke View (komunikasi dua arah)

        MainMenu menu = new MainMenu(                     // buat Main Menu dengan dua aksi tombol
                () -> {                                   // aksi tombol MULAI
                    cards.show(root, "game");       // tampilkan layar game
                    logika.newGame();                     // siapkan permainan baru
                    SwingUtilities.invokeLater(tampilan::requestFocusInWindow); // minta fokus keyboard ke layar game
                },                                        // akhir aksi MULAI
                () -> System.exit(0)                // aksi tombol KELUAR: tutup program
        );

        // dipanggil Logic saat pemain menekan M setelah game over
        logika.setOnMenu(() -> cards.show(root, "menu")); // aksi kembali menampilkan layar menu

        root.add(menu, "menu");                 // daftarkan layar menu dengan nama "menu"
        root.add(tampilan, "game");             // daftarkan layar game dengan nama "game"
        cards.show(root, "menu");                   // layar pertama yang tampil adalah menu

        frame.add(root);                                  // masukkan panel induk ke jendela
        frame.pack();                                     // sesuaikan ukuran jendela dengan isinya (360x640)
        frame.setLocationRelativeTo(null);                // tampilkan jendela di tengah layar monitor
        frame.setVisible(true);                           // tampilkan jendela (dipanggil paling akhir)
    }
}