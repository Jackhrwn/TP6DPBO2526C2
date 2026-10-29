import javax.sound.sampled.*;                             // import API audio Java (Clip, AudioSystem, dll.)
import java.net.URL;                                      // import URL untuk lokasi file audio

public class SoundManager {                               // class pengelola semua suara game
    private Clip bgm;                                     // klip musik latar (diulang terus)
    private Clip jump;                                    // klip efek suara lompat
    private Clip point;                                   // klip efek suara dapat skor
    private Clip hit;                                     // klip efek suara tabrakan

    public SoundManager() {                               // constructor: memuat semua audio
        bgm = load("assets/sounds/bgm.wav");         // muat musik latar
        jump = load("assets/sounds/jump.wav");       // muat efek lompat
        point = load("assets/sounds/point.wav");     // muat efek skor
        hit = load("assets/sounds/hit.wav");         // muat efek tabrakan
        setVolume(bgm, -8f);                          // BGM dibuat lebih pelan 8 dB dari efek suara
    }

    // kalau audio gagal dimuat, game tetap jalan tanpa suara
    private Clip load(String path) {                      // memuat satu file audio menjadi Clip
        try {                                             // coba muat file
            URL url = getClass().getResource(path);       // cari lokasi file di dalam project
            AudioInputStream stream = AudioSystem.getAudioInputStream(url); // buka file sebagai aliran audio
            Clip clip = AudioSystem.getClip();            // buat Clip kosong
            clip.open(stream);                            // isi Clip dengan data audio
            return clip;                                  // kembalikan Clip yang siap diputar
        } catch (Exception e) {                           // jika terjadi error (file tidak ada, format salah, dll.)
            System.out.println("Audio tidak dapat dimuat: " + path); // tampilkan pesan di console
            return null;                                  // kembalikan null (tanpa suara)
        }
    }

    private void setVolume(Clip clip, float db) {         // mengatur volume klip dalam desibel
        if (clip == null) return;                         // jika klip tidak ada, tidak melakukan apa-apa
        try {                                             // coba atur volume
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // ambil kontrol volume
            gain.setValue(db);                            // set nilai volume
        } catch (Exception ignored) {}                    // abaikan jika perangkat tidak mendukung
    }

    private void play(Clip clip) {                        // memutar klip satu kali dari awal
        if (clip == null) return;                         // jika klip tidak ada, keluar
        if (clip.isRunning()) clip.stop();                // jika sedang diputar, hentikan dulu
        clip.setFramePosition(0);                         // kembalikan posisi ke awal klip
        clip.start();                                     // mulai putar
    }

    public void playJump() { play(jump); }                // putar efek lompat
    public void playPoint() { play(point); }              // putar efek skor
    public void playHit() { play(hit); }                  // putar efek tabrakan

    public void startBgm() {                              // memulai musik latar
        if (bgm == null) return;                          // jika BGM tidak ada, keluar
        bgm.setFramePosition(0);                          // mulai dari awal lagu
        bgm.loop(Clip.LOOP_CONTINUOUSLY);                 // putar berulang tanpa henti
    }

    public void stopBgm() {                               // menghentikan musik latar
        if (bgm != null) bgm.stop();                      // hentikan jika BGM ada
    }
}