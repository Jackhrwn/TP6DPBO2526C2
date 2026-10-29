import javax.swing.*;                                     // import Swing (Timer, ImageIcon)
import java.awt.*;                                        // import Image
import java.awt.event.ActionEvent;                        // import event dari Timer
import java.awt.event.ActionListener;                     // import interface pendengar Timer
import java.awt.event.KeyEvent;                           // import event keyboard
import java.awt.event.KeyListener;                        // import interface pendengar keyboard
import java.util.ArrayList;                               // import ArrayList untuk daftar pipa

public class Logic implements ActionListener, KeyListener { // pusat aturan game; mendengar Timer dan keyboard
    // ukuran layar & tanah
    int frameWidth = 360;                                 // lebar layar game
    int frameHeight = 640;                                // tinggi layar game
    int groundHeight = 64;                                // tinggi tanah di bagian bawah layar

    // player
    int playerStartPosX = frameWidth / 8;                 // posisi X awal burung (sepertiga kiri layar)
    int playerStartPosY = frameHeight / 2;                // posisi Y awal burung (tengah layar)
    int playerWidth = 34;                                 // lebar burung
    int playerHeight = 24;                                // tinggi burung

    // pipa
    int pipeStartPosX = frameWidth;                       // pipa muncul dari tepi kanan layar
    int pipeWidth = 64;                                   // lebar pipa
    int pipeHeight = 512;                                 // tinggi pipa
    int pipeVelocityX = -4;                               // kecepatan pipa ke kiri

    // fisika
    int gravity = 1;                                      // percepatan jatuh per frame
    int jumpPower = -9;                                   // kecepatan awal saat melompat (negatif = naik)

    // status game
    boolean started = false;                              // true jika game sudah dimulai
    boolean gameOver = false;                             // true jika game sudah berakhir
    boolean keyHeld = false;                              // true jika tombol lompat sedang ditahan
    int score = 0;                                        // skor sekarang
    int highScore = 0;                                    // skor terbaik

    Timer gameLoop;                                       // Timer utama (game loop)
    Timer pipesCooldown;                                  // Timer penentu jeda munculnya pipa

    View view;                                            // referensi ke tampilan
    Runnable onMenu;                                      // aksi untuk kembali ke Main Menu
    SoundManager sound;                                   // pengelola suara

    Image birdImage;                                      // gambar burung
    Image upperPipeImage;                                 // gambar pipa atas
    Image lowerPipeImage;                                 // gambar pipa bawah
    Image backgroundImage;                                // gambar latar

    Player player;                                        // objek burung
    ArrayList<Pipe> pipes = new ArrayList<>();            // daftar semua pipa yang ada di layar

    public Logic() {                                           // constructor Logic
        backgroundImage = load("assets/background.png");  // muat gambar latar
        birdImage = load("assets/bird.png");              // muat gambar burung
        upperPipeImage = load("assets/upperPipe.png");    // muat gambar pipa atas
        lowerPipeImage = load("assets/lowerPipe.png");    // muat gambar pipa bawah

        sound = new SoundManager();                            // siapkan pengelola suara
        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage); // buat burung

        // pipa baru muncul tiap 1,5 detik (baru jalan setelah game dimulai)
        pipesCooldown = new Timer(1500, e -> placePipes()); // tiap 1500 ms panggil placePipes()

        gameLoop = new Timer(1000 / 60, this); // game loop 60 FPS, pendengarnya class ini
        gameLoop.start();                                   // jalankan game loop
    }                                                       // akhir constructor

    private Image load(String path) {                       // helper memuat gambar dari folder assets
        return new ImageIcon(getClass().getResource(path)).getImage(); // ambil file lalu ubah jadi Image
    }

    public void setView(View view) { this.view = view; }    // menyambungkan Logic dengan View
    public void setOnMenu(Runnable onMenu) { this.onMenu = onMenu; } // menyimpan aksi kembali ke menu

    public Player getPlayer() { return player; }            // getter player (dipakai View)
    public ArrayList<Pipe> getPipes() { return pipes; }     // getter daftar pipa (dipakai View)
    public Image getBackgroundImage() { return backgroundImage; } // getter gambar latar
    public int getScore() { return score; }                 // getter skor
    public int getHighScore() { return highScore; }         // getter skor terbaik
    public boolean isStarted() { return started; }          // getter status mulai
    public boolean isGameOver() { return gameOver; }        // getter status game over

    // membuat sepasang pipa dengan celah di posisi acak
    public void placePipes() {                              // menempatkan pipa atas dan bawah
        int randomY = (int) (-pipeHeight / 4 - Math.random() * (pipeHeight / 2)); // posisi Y pipa atas acak
        int openingSpace = frameHeight / 4;                 // lebar celah di antara dua pipa

        pipes.add(new Pipe(pipeStartPosX, randomY, pipeWidth, pipeHeight, // buat pipa atas
                upperPipeImage, pipeVelocityX, true));    // gambar pipa atas, kecepatan, penanda pipa atas

        pipes.add(new Pipe(pipeStartPosX, randomY + pipeHeight + openingSpace, pipeWidth, pipeHeight, // pipa bawah di bawah celah
                lowerPipeImage, pipeVelocityX, false));   // gambar pipa bawah, kecepatan, bukan pipa atas
    }

    public void move() {                                  // memperbarui semua pergerakan & aturan tiap frame
        if (!started || gameOver) return;                 // jangan bergerak jika belum mulai atau sudah game over

        // gravitasi
        player.setVelocityY(player.getVelocityY() + gravity);     // kecepatan jatuh bertambah akibat gravitasi
        player.setPosY(player.getPosY() + player.getVelocityY()); // geser posisi Y sesuai kecepatan
        player.setPosY(Math.max(player.getPosY(), 0));            // burung tidak boleh keluar dari batas atas

        // jatuh ke tanah / batas bawah -> game over
        int groundY = frameHeight - groundHeight;         // posisi Y permukaan tanah
        if (player.getPosY() + player.getHeight() >= groundY) { // jika kaki burung menyentuh tanah
            player.setPosY(groundY - player.getHeight()); // letakkan burung tepat di atas tanah
            endGame();                                    // akhiri game
            return;                                       // hentikan proses frame ini
        }

        // gerakkan pipa, hitung skor, cek tabrakan
        for (Pipe pipe : pipes) {                         // ulangi untuk setiap pipa
            pipe.move();                                  // geser pipa ke kiri

            // melewati pipa -> skor +1 (dihitung dari pipa atas saja)
            if (pipe.isUpper() && !pipe.isPassed()        // pipa atas yang belum dihitung
                    && player.getPosX() > pipe.getPosX() + pipe.getWidth()) { // dan burung sudah melewati sisi kanan pipa
                pipe.setPassed(true);                     // tandai pipa sudah dilewati
                score++;                                  // tambah skor 1
                if (view != null) view.setScore(score);   // perbarui JLabel skor
                sound.playPoint();                        // putar efek suara skor
            }

            // menyentuh pipa -> game over
            if (player.getBounds().intersects(pipe.getBounds())) { // jika hitbox burung bersinggungan dengan pipa
                endGame();                                // akhiri game
                break;                                    // keluar dari perulangan
            }
        }

        // buang pipa yang sudah keluar layar
        pipes.removeIf(p -> p.getPosX() + p.getWidth() < 0); // hapus pipa yang seluruhnya sudah di kiri layar
    }                                                     // akhir method move

    private void endGame() {                              // dipanggil saat game over
        gameOver = true;                                  // tandai game over
        highScore = Math.max(highScore, score);           // simpan skor terbaik
        pipesCooldown.stop();                             // hentikan pembuatan pipa baru
        sound.stopBgm();                                  // hentikan musik latar
        sound.playHit();                                  // putar efek tabrakan
    }

    // dipanggil dari tombol MULAI di Main Menu & tombol R
    public void newGame() {                               // menyiapkan permainan baru
        player.setPosX(playerStartPosX);                  // kembalikan burung ke X awal
        player.setPosY(playerStartPosY);                  // kembalikan burung ke Y awal
        player.setVelocityY(0);                           // reset kecepatan
        pipes.clear();                                    // hapus semua pipa
        pipesCooldown.stop();                             // pastikan timer pipa berhenti
        score = 0;                                        // reset skor
        gameOver = false;                                 // game belum berakhir
        started = false;                                  // menunggu pemain menekan spasi
        keyHeld = false;                                  // reset status tombol

        if (view != null) {                               // jika tampilan sudah tersambung
            view.setScore(0);                             // tampilkan skor 0
            view.setScoreVisible(false);                  // sembunyikan skor sampai game mulai
        }
        sound.startBgm();                                 // putar musik latar
    }

    private void startGame() {                            // dipanggil saat spasi pertama ditekan
        started = true;                                   // game dimulai
        placePipes();                                     // munculkan pipa pertama
        pipesCooldown.restart();                          // mulai timer pipa berikutnya
        if (view != null) view.setScoreVisible(true);     // tampilkan label skor
    }

    private void goToMenu() {                             // kembali ke Main Menu
        sound.stopBgm();                                  // hentikan musik
        started = false;                                  // reset status mulai
        gameOver = false;                                 // reset status game over
        if (onMenu != null) onMenu.run();                 // jalankan aksi pindah ke layar menu
    }

    @Override                                             // mengimplementasi ActionListener
    public void actionPerformed(ActionEvent e) {          // dipanggil Timer gameLoop tiap ~16 ms
        move();                                           // perbarui logika game
        if (view != null) {                               // jika tampilan sudah tersambung
            view.repaint();                               // gambar ulang tampilan
        }
    }

    @Override                                             // mengimplementasi KeyListener
    public void keyTyped(KeyEvent e) {}                   // tidak dipakai

    @Override                                             // mengimplementasi KeyListener
    public void keyPressed(KeyEvent e) {                  // dipanggil saat tombol ditekan
        int k = e.getKeyCode();                           // ambil kode tombol yang ditekan

        if (gameOver) {                                   // jika sedang game over
            if (k == KeyEvent.VK_R) newGame();            // tombol R: restart
            else if (k == KeyEvent.VK_M) goToMenu();      // tombol M: kembali ke menu
            return;                                       // abaikan tombol lain saat game over
        }

        if (k == KeyEvent.VK_SPACE || k == KeyEvent.VK_UP) { // tombol spasi atau panah atas
            if (keyHeld) return;                          // abaikan auto-repeat saat tombol ditahan
            keyHeld = true;                               // catat tombol sedang ditekan

            if (!started) startGame();                    // tekanan pertama memulai game
            player.setVelocityY(jumpPower);               // burung melompat
            sound.playJump();                             // putar efek suara lompat
        }
    }

    @Override                                             // mengimplementasi KeyListener
    public void keyReleased(KeyEvent e) {                 // dipanggil saat tombol dilepas
        keyHeld = false;                                  // tombol tidak lagi ditahan
    }
}