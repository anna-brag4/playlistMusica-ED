package com.mycompany.playlistmusica.ed;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

/**
 * PlaylistMusicaED — Interface grafica principal.
 *
 * Audio: player nativo do sistema operacional via ProcessBuilder.
 *        Suporta MP3, WAV e qualquer formato que o SO reproduza.
 *        Windows: wmplayer | macOS: afplay | Linux: mpg123/ffplay/cvlc
 *
 * Integracao com Playlist.java:
 *   - sortBy()       -> playlist.ordenarTitulo() / playlist.ordenarArtista()
 *   - shuffleList()  -> playlist.embaralharPlaylist()
 *   - nextTrack()    -> playlist.proximaMusica()
 *   - prevTrack()    -> playlist.musicaAnterior()
 *   - removeTrack()  -> playlist.remover()
 *   - addTrack()     -> playlist.adicionar()
 *   Apos qualquer operacao, rebuildOrdem() sincroniza a lista auxiliar
 *   com a lista encadeada da Playlist.
 */
public class PlaylistMusicaED extends JFrame {

    // ── Cores ──────────────────────────────────────────────────────────────
    private static final Color BG_DARK    = new Color(14, 14, 14);
    private static final Color BG_CARD    = new Color(20, 20, 20);
    private static final Color BG_SIDEBAR = new Color(17, 17, 17);
    private static final Color BG_ROW_ALT = new Color(18, 18, 18);
    private static final Color BG_PLAYING = new Color(15, 31, 15);
    private static final Color BG_INPUT   = new Color(10, 10, 10);
    private static final Color BG_BTN     = new Color(24, 24, 24);
    private static final Color BG_BTN_HOV = new Color(34, 34, 34);
    private static final Color BG_BAR     = new Color(12, 12, 12);
    private static final Color BG_MODAL   = new Color(22, 22, 22);

    private static final Color GREEN      = new Color(74, 222, 128);
    private static final Color GREEN_HOV  = new Color(134, 239, 172);
    private static final Color GREEN_DIM  = new Color(45, 71, 45);
    private static final Color TEXT_PRI   = new Color(224, 224, 224);
    private static final Color TEXT_SEC   = new Color(100, 100, 100);
    private static final Color TEXT_HINT  = new Color(50, 50, 50);
    private static final Color BORDER_CLR = new Color(30, 30, 30);
    private static final Color BORDER_MED = new Color(42, 42, 42);

    // ── Fontes ─────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE   = new Font("SansSerif", Font.PLAIN,  18);
    private static final Font FONT_TRACK   = new Font("Serif",     Font.BOLD,   26);
    private static final Font FONT_SUB     = new Font("SansSerif", Font.PLAIN,  13);
    private static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN,  12);
    private static final Font FONT_SMALL   = new Font("SansSerif", Font.PLAIN,  11);
    private static final Font FONT_BRAND   = new Font("Serif",     Font.BOLD,   20);
    private static final Font FONT_STAT    = new Font("SansSerif", Font.BOLD,   12);
    private static final Font FONT_LABEL   = new Font("SansSerif", Font.PLAIN,   9);
    private static final Font FONT_BTN     = new Font("SansSerif", Font.BOLD,   11);
    private static final Font FONT_COL_HDR = new Font("SansSerif", Font.PLAIN,  10);

    // ── Tipos de icone ─────────────────────────────────────────────────────
    enum BtnIcon { PLAY, PAUSE, PREV, NEXT, REPEAT, SHUFFLE }

    // ══════════════════════════════════════════════════════════════════════
    //  IconButton — icone desenhado com Graphics2D, sem unicode
    // ══════════════════════════════════════════════════════════════════════
    static class IconButton extends JButton {
        BtnIcon icon;
        boolean mainStyle;
        boolean active = false;
        Color   fgColor;

        IconButton(BtnIcon icon, boolean mainStyle) {
            this.icon      = icon;
            this.mainStyle = mainStyle;
            this.fgColor   = mainStyle ? Color.BLACK : TEXT_SEC;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(mainStyle ? new Dimension(48, 48) : new Dimension(40, 40));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!mainStyle && !active) fgColor = TEXT_PRI;
                    repaint();
                }
                public void mouseExited(MouseEvent e) {
                    if (!mainStyle && !active) fgColor = TEXT_SEC;
                    repaint();
                }
            });
        }

        void setIcon(BtnIcon i)   { this.icon = i; repaint(); }
        void setActive(boolean v) {
            this.active  = v;
            this.fgColor = v ? GREEN : TEXT_SEC;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int cx = w / 2,     cy = h / 2;

            if (mainStyle) {
                Color bg = getModel().isRollover() ? GREEN_HOV : GREEN;
                g2.setColor(bg);
                g2.fillOval(0, 0, w - 1, h - 1);
                g2.setColor(Color.BLACK);
            } else {
                g2.setColor(fgColor);
            }

            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (icon) {
                case PLAY: {
                    int s = mainStyle ? 9 : 7;
                    int ox = mainStyle ? 2 : 1;
                    int[] xs = { cx - s/2 + ox, cx - s/2 + ox, cx + s + ox };
                    int[] ys = { cy - s,        cy + s,        cy          };
                    g2.fillPolygon(xs, ys, 3);
                    break;
                }
                case PAUSE: {
                    int bw = mainStyle ? 4 : 3;
                    int bh = mainStyle ? 13 : 9;
                    int gap = mainStyle ? 3 : 2;
                    g2.fillRoundRect(cx - gap - bw, cy - bh/2, bw, bh, 2, 2);
                    g2.fillRoundRect(cx + gap,       cy - bh/2, bw, bh, 2, 2);
                    break;
                }
                case PREV: {
                    int s = 7;
                    g2.fillRect(cx - s - 1, cy - s, 3, s * 2);
                    int[] xs = { cx + s - 2, cx + s - 2, cx - s + 3 };
                    int[] ys = { cy - s,     cy + s,     cy          };
                    g2.fillPolygon(xs, ys, 3);
                    break;
                }
                case NEXT: {
                    int s = 7;
                    int[] xs = { cx - s + 2, cx - s + 2, cx + s - 3 };
                    int[] ys = { cy - s,     cy + s,     cy          };
                    g2.fillPolygon(xs, ys, 3);
                    g2.fillRect(cx + s - 2, cy - s, 3, s * 2);
                    break;
                }
                case REPEAT: {
                    int r = 8;
                    g2.drawArc(cx - r, cy - r, r * 2, r * 2, 40, 280);
                    int ax = cx + r - 1, ay = cy - 3;
                    int[] xs = { ax - 4, ax + 1, ax + 1 };
                    int[] ys = { ay,     ay - 4, ay + 2 };
                    g2.fillPolygon(xs, ys, 3);
                    break;
                }
                case SHUFFLE: {
                    int x1 = cx - 9, x2 = cx + 9;
                    int y1 = cy - 6, y2 = cy + 6;
                    g2.drawLine(x1, y1, x2, y2);
                    g2.drawLine(x1, y2, x2, y1);
                    g2.drawLine(x2, y1, x2 - 3, y1 + 1);
                    g2.drawLine(x2, y1, x2 - 1, y1 + 3);
                    g2.drawLine(x2, y2, x2 - 3, y2 - 1);
                    g2.drawLine(x2, y2, x2 - 1, y2 - 3);
                    break;
                }
            }
            g2.dispose();
        }
    }

    // ── MiniIconButton ─────────────────────────────────────────────────────
    static class MiniIconButton extends JButton {
        BtnIcon icon;
        Color   fgColor = TEXT_SEC;

        MiniIconButton(BtnIcon icon) {
            this.icon = icon;
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(30, 28));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { fgColor = TEXT_PRI; repaint(); }
                public void mouseExited(MouseEvent e)  { fgColor = TEXT_SEC; repaint(); }
            });
        }

        void setIcon(BtnIcon i) { this.icon = i; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(fgColor);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = getWidth() / 2, cy = getHeight() / 2;
            switch (icon) {
                case PLAY:  { int[] xs={cx-4,cx-4,cx+5}; int[] ys={cy-6,cy+6,cy}; g2.fillPolygon(xs,ys,3); break; }
                case PAUSE: { g2.fillRoundRect(cx-5,cy-5,3,10,2,2); g2.fillRoundRect(cx+2,cy-5,3,10,2,2); break; }
                case PREV:  { g2.fillRect(cx-6,cy-5,2,10); int[] xs={cx+5,cx+5,cx-4}; int[] ys={cy-5,cy+5,cy}; g2.fillPolygon(xs,ys,3); break; }
                case NEXT:  { int[] xs={cx-5,cx-5,cx+4}; int[] ys={cy-5,cy+5,cy}; g2.fillPolygon(xs,ys,3); g2.fillRect(cx+4,cy-5,2,10); break; }
                default: break;
            }
            g2.dispose();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  AUDIO — player nativo do SO via ProcessBuilder
    // ══════════════════════════════════════════════════════════════════════
    //  AUDIO — JLayer (javazoom.jl.player.advanced.AdvancedPlayer)
    //
    //  Dependencia: jlayer-1.0.1.jar  (unico jar necessario, ~100 KB)
    //  Download: https://www.javazoom.net/javalayer/sources.html
    //            ou Maven: javazoom:jlayer:1.0.1
    //
    //  Adicione o jar ao classpath do projeto no NetBeans:
    //    Propriedades do Projeto -> Bibliotecas -> Adicionar JAR/Pasta
    // ══════════════════════════════════════════════════════════════════════
    private javazoom.jl.player.advanced.AdvancedPlayer mp3Player  = null;
    private Thread   playerThread  = null;   // thread dedicada ao decode MP3
    private boolean  audioReady    = false;
    private File     audioFile     = null;
    private int      pauseElapsed  = 0;      // segundos salvos ao pausar
    private java.io.FileInputStream  audioFis    = null;  // stream aberta
    private long     audioFileSize  = 0;
    private int      audioDuration  = 0;     // duracao total em seg

    /**
     * Inicia reproducao do arquivo f a partir de startSec segundos.
     * Usa AdvancedPlayer do JLayer — decode e saida de audio 100% dentro da JVM.
     */
    private boolean startAudioFrom(File f, int startSec) {
        stopAudioInternal();
        if (f == null || !f.exists()) return false;

        try {
            audioFis      = new java.io.FileInputStream(f);
            audioFile     = f;
            audioFileSize = f.length();

            // Calcula bytes a pular para chegar em startSec
            // (estimativa por proporcao; precisa da duracao real)
            if (startSec > 0 && audioDuration > 0) {
                long skipBytes = (long)(audioFileSize * startSec / (double) audioDuration);
                audioFis.skip(Math.max(0, skipBytes));
            }

            mp3Player = new javazoom.jl.player.advanced.AdvancedPlayer(audioFis);

            // Callback quando a musica termina naturalmente
            mp3Player.setPlayBackListener(new javazoom.jl.player.advanced.PlaybackListener() {
                @Override
                public void playbackFinished(javazoom.jl.player.advanced.PlaybackEvent evt) {
                    if (audioReady && playing) {
                        SwingUtilities.invokeLater(() -> {
                            if (repeat) {
                                elapsed = 0; pauseElapsed = 0;
                                startAudioFrom(audioFile, 0);
                            } else {
                                nextTrack();
                            }
                        });
                    }
                }
            });

            audioReady = true;

            // Toca em thread separada para nao travar a EDT
            playerThread = new Thread(() -> {
                try {
                    mp3Player.play();
                } catch (javazoom.jl.decoder.JavaLayerException e) {
                    System.err.println("[Audio] Erro de decodificacao: " + e.getMessage());
                }
            }, "mp3-player-thread");
            playerThread.setDaemon(true);
            playerThread.start();

            return true;

        } catch (Exception e) {
            System.err.println("[Audio] Erro ao iniciar audio: " + e.getMessage());
            audioReady = false;
            return false;
        }
    }

    /** Para o player imediatamente sem limpar audioFile (usado no pause). */
    private void stopAudioInternal() {
        audioReady = false;
        if (mp3Player != null) {
            try { mp3Player.stop(); } catch (Exception ignored) {}
            mp3Player = null;
        }
        if (playerThread != null) {
            playerThread.interrupt();
            playerThread = null;
        }
        if (audioFis != null) {
            try { audioFis.close(); } catch (Exception ignored) {}
            audioFis = null;
        }
    }

    /** Pausa: para o decode e salva posicao em segundos. */
    private void pauseAudio() {
        pauseElapsed = elapsed;
        stopAudioInternal();
    }

    /** Resume: reinicia a partir de pauseElapsed. */
    private void resumeAudio() {
        if (audioFile != null && audioFile.exists()) {
            startAudioFrom(audioFile, pauseElapsed);
        }
    }

    /** Para completamente e limpa todo o estado de audio. */
    private void stopAudio() {
        stopAudioInternal();
        audioFile     = null;
        audioFileSize = 0;
        audioDuration = 0;
        pauseElapsed  = 0;
        elapsed       = 0;
    }

    /**
     * Le a duracao de um MP3 lendo o header do primeiro frame MPEG.
     * Nao requer nenhuma biblioteca — usa apenas java.io.
     * Retorna -1 se nao conseguir determinar.
     */
    private static int readFileDuration(File f) {
        // WAV: tenta via javax.sound.sampled (nativo)
        try {
            javax.sound.sampled.AudioInputStream ais =
                javax.sound.sampled.AudioSystem.getAudioInputStream(f);
            javax.sound.sampled.AudioFormat fmt = ais.getFormat();
            long frames = ais.getFrameLength();
            ais.close();
            if (frames > 0 && fmt.getFrameRate() > 0)
                return (int)(frames / fmt.getFrameRate());
        } catch (Exception ignored) {}

        // MP3: le bitrate do header do primeiro frame MPEG
        try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
            long fileSize = f.length();
            long tagSize  = 0;

            // Pula tag ID3v2
            byte[] hdr = new byte[10];
            if (fis.read(hdr, 0, 10) == 10
                    && hdr[0]=='I' && hdr[1]=='D' && hdr[2]=='3') {
                tagSize = ((hdr[6] & 0x7F) << 21) | ((hdr[7] & 0x7F) << 14)
                        | ((hdr[8] & 0x7F) <<  7) |  (hdr[9] & 0x7F);
                tagSize += 10;
                long skipped = 10;
                while (skipped < tagSize) skipped += fis.skip(tagSize - skipped);
            } else {
                // nao ha ID3v2 — le direto do inicio
                fis.getChannel().position(0);
            }

            // Procura sync word do frame MPEG (0xFF seguido de 0xEx ou 0xFx)
            int[] br1 = {0,32,40,48,56,64,80,96,112,128,160,192,224,256,320,0};
            int[] br2 = {0, 8,16,24,32,40,48,56, 64, 80, 96,112,128,144,160,0};
            int[] sr1 = {44100,48000,32000,0};
            int[] sr2 = {22050,24000,16000,0};

            int b, prev = -1, searched = 0;
            while ((b = fis.read()) != -1 && searched++ < 131072) {
                if (prev == 0xFF && (b & 0xE0) == 0xE0) {
                    int b2 = fis.read(), b3 = fis.read();
                    if (b2 < 0 || b3 < 0) break;
                    int version    = (b  >> 3) & 0x03;
                    int bitrateIdx = (b2 >> 4) & 0x0F;
                    int sampleIdx  = (b2 >> 2) & 0x03;
                    int bitrate    = (version == 3 ? br1[bitrateIdx] : br2[bitrateIdx]) * 1000;
                    int sampleRate = (version == 3 ? sr1[sampleIdx]  : sr2[sampleIdx]);
                    if (bitrate > 0 && sampleRate > 0)
                        return (int)((fileSize - tagSize) * 8 / bitrate);
                    break;
                }
                prev = b;
            }
        } catch (Exception ignored) {}
        return -1;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ESTADO DO PLAYER
    // ══════════════════════════════════════════════════════════════════════
    private Playlist  playlist   = new Playlist();
    private List<No>  ordem      = new ArrayList<>();   // espelho da lista encadeada
    private No        noAtual    = null;                 // No corrente (ponteiro da Playlist)
    private int       curIdx     = -1;
    private boolean   playing    = false;
    private boolean   repeat     = false;
    private boolean   shuffle    = false;
    private int       elapsed    = 0;
    private Timer     ticker;
    private boolean   blinkState = true;
    private Timer     blinkTimer;

    // ── Componentes globais ────────────────────────────────────────────────
    private JPanel     cardLayout;
    private CardLayout cards;

    private JLabel lblTracks, lblDurTotal;

    private DefaultTableModel tableModel;
    private JTable            table;

    private JLabel       lblNowTitle, lblNowArtist, lblCurTime, lblTotTime;
    private JLabel       lblNowDot;
    private JProgressBar progBar;
    private IconButton   btnPlay, btnRepeat, btnShuffle;
    private JPanel       pnlNextUp;

    private JLabel       lblBarTitle, lblBarArtist, lblBarCur, lblBarTot;
    private JProgressBar barProg;
    private IconButton   btnBarPlay;

    private JPanel filaPanel;

    // ── Construtor ─────────────────────────────────────────────────────────
    public PlaylistMusicaED() {
        setTitle("PlaylistED");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920, 620);
        setMinimumSize(new Dimension(780, 540));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);

        buildUI();
        loadSampleData();
        renderAll();

        ticker = new Timer(1000, e -> tickPlayer());

        blinkTimer = new Timer(900, e -> {
            blinkState = !blinkState;
            if (lblNowDot != null)
                lblNowDot.setForeground(blinkState ? GREEN : BG_CARD);
        });
        blinkTimer.start();

        // Garante que o audio para quando a janela fechar
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { stopAudio(); }
        });

        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BUILD UI
    // ══════════════════════════════════════════════════════════════════════
    private void buildUI() {
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildCenter(),  BorderLayout.CENTER);
    }

    // ── SIDEBAR ────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_SIDEBAR);
        p.setPreferredSize(new Dimension(190, 0));
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_CLR));

        JLabel brand = new JLabel("<html><span style='color:#e8e8e8'>Playlist</span>"
                                + "<span style='color:#4ade80'>ED</span></html>");
        brand.setFont(FONT_BRAND);
        brand.setBorder(new EmptyBorder(20, 18, 14, 18));
        brand.setAlignmentX(LEFT_ALIGNMENT);
        p.add(brand);

        JPanel stats = new JPanel(new GridLayout(2, 2, 0, 6));
        stats.setBackground(BG_SIDEBAR);
        stats.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 1, 0, BORDER_CLR),
            new EmptyBorder(10, 18, 10, 18)
        ));
        stats.setMaximumSize(new Dimension(190, 72));
        stats.setAlignmentX(LEFT_ALIGNMENT);
        stats.add(mkStatLabel("FAIXAS"));
        lblTracks = mkStatVal("0");
        stats.add(lblTracks);
        stats.add(mkStatLabel("DURACAO"));
        lblDurTotal = mkStatVal("0s");
        stats.add(lblDurTotal);
        p.add(stats);

        p.add(Box.createVerticalStrut(10));

        String[][] navItems = {
            {"  Player",   "player"},
            {"  Playlist", "lista"},
            {"  Fila",     "fila"},
        };
        for (String[] item : navItems) {
            JButton btn = navBtn(item[0], item[1]);
            btn.setAlignmentX(LEFT_ALIGNMENT);
            p.add(btn);
            p.add(Box.createVerticalStrut(1));
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JLabel mkStatLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(FONT_LABEL); l.setForeground(TEXT_HINT); return l;
    }
    private JLabel mkStatVal(String t) {
        JLabel l = new JLabel(t); l.setFont(FONT_STAT);  l.setForeground(GREEN);     return l;
    }

    private JButton navBtn(String label, String view) {
        JButton b = new JButton(label);
        b.setFont(FONT_BODY); b.setForeground(TEXT_SEC); b.setBackground(BG_SIDEBAR);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(190, 38));
        b.setBorder(new EmptyBorder(9, 18, 9, 18));
        b.addActionListener(e -> switchView(view));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!b.getForeground().equals(GREEN)) { b.setForeground(TEXT_PRI); b.setBackground(new Color(26,26,26)); }
            }
            public void mouseExited(MouseEvent e) {
                if (!b.getForeground().equals(GREEN)) { b.setForeground(TEXT_SEC); b.setBackground(BG_SIDEBAR); }
            }
        });
        return b;
    }

    // ── CENTER ─────────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        cards      = new CardLayout();
        cardLayout = new JPanel(cards);
        cardLayout.setBackground(BG_DARK);
        cardLayout.add(buildPlayerView(), "player");
        cardLayout.add(buildListaView(),  "lista");
        cardLayout.add(buildFilaView(),   "fila");
        p.add(cardLayout,       BorderLayout.CENTER);
        p.add(buildPlayerBar(), BorderLayout.SOUTH);
        return p;
    }

    // ── PLAYER VIEW ────────────────────────────────────────────────────────
    private JPanel buildPlayerView() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(22, 22, 16, 22));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(22, 26, 20, 26)
        ));

        JPanel nowRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        nowRow.setBackground(BG_CARD);
        nowRow.setAlignmentX(LEFT_ALIGNMENT);
        lblNowDot = new JLabel("* ");
        lblNowDot.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblNowDot.setForeground(GREEN);
        JLabel nowLbl = new JLabel("TOCANDO AGORA");
        nowLbl.setFont(FONT_LABEL); nowLbl.setForeground(GREEN);
        nowRow.add(lblNowDot); nowRow.add(nowLbl);
        nowRow.setBorder(new EmptyBorder(0, 0, 10, 0));

        lblNowTitle = new JLabel("--");
        lblNowTitle.setFont(FONT_TRACK);
        lblNowTitle.setForeground(Color.WHITE);
        lblNowTitle.setAlignmentX(LEFT_ALIGNMENT);

        lblNowArtist = new JLabel("--");
        lblNowArtist.setFont(FONT_SUB); lblNowArtist.setForeground(TEXT_SEC);
        lblNowArtist.setBorder(new EmptyBorder(2, 0, 18, 0));
        lblNowArtist.setAlignmentX(LEFT_ALIGNMENT);

        progBar = new JProgressBar(0, 1000);
        progBar.setValue(0);
        progBar.setBackground(new Color(38, 38, 38)); progBar.setForeground(GREEN);
        progBar.setBorderPainted(false);
        progBar.setPreferredSize(new Dimension(0, 4));
        progBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
        progBar.setAlignmentX(LEFT_ALIGNMENT);

        JPanel timesRow = new JPanel(new BorderLayout());
        timesRow.setBackground(BG_CARD); timesRow.setAlignmentX(LEFT_ALIGNMENT);
        lblCurTime = mkTimeLabel("0:00"); lblTotTime = mkTimeLabel("0:00");
        timesRow.add(lblCurTime, BorderLayout.WEST);
        timesRow.add(lblTotTime, BorderLayout.EAST);
        timesRow.setBorder(new EmptyBorder(5, 0, 16, 0));
        timesRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        controls.setBackground(BG_CARD); controls.setAlignmentX(LEFT_ALIGNMENT);

        btnRepeat          = new IconButton(BtnIcon.REPEAT,  false);
        IconButton btnPrev = new IconButton(BtnIcon.PREV,    false);
        btnPlay            = new IconButton(BtnIcon.PAUSE,   true);
        IconButton btnNext = new IconButton(BtnIcon.NEXT,    false);
        btnShuffle         = new IconButton(BtnIcon.SHUFFLE, false);

        btnRepeat.addActionListener(e  -> toggleRepeat());
        btnPrev.addActionListener(e    -> prevTrack());
        btnPlay.addActionListener(e    -> togglePlay());
        btnNext.addActionListener(e    -> nextTrack());
        btnShuffle.addActionListener(e -> toggleShuffle());

        controls.add(btnRepeat); controls.add(btnPrev); controls.add(btnPlay);
        controls.add(btnNext);   controls.add(btnShuffle);
        controls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        card.add(nowRow); card.add(lblNowTitle); card.add(lblNowArtist);
        card.add(progBar); card.add(timesRow); card.add(controls);

        JPanel nextSection = new JPanel();
        nextSection.setLayout(new BoxLayout(nextSection, BoxLayout.Y_AXIS));
        nextSection.setBackground(BG_DARK);
        nextSection.setBorder(new EmptyBorder(16, 0, 0, 0));

        JLabel nextLbl = new JLabel("A SEGUIR");
        nextLbl.setFont(FONT_LABEL); nextLbl.setForeground(TEXT_HINT);
        nextLbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        nextLbl.setAlignmentX(LEFT_ALIGNMENT);

        pnlNextUp = new JPanel(new BorderLayout());
        pnlNextUp.setBackground(BG_DARK); pnlNextUp.setAlignmentX(LEFT_ALIGNMENT);

        nextSection.add(nextLbl); nextSection.add(pnlNextUp);

        root.add(card,        BorderLayout.NORTH);
        root.add(nextSection, BorderLayout.CENTER);
        return root;
    }

    private JLabel mkTimeLabel(String t) {
        JLabel l = new JLabel(t); l.setFont(FONT_SMALL); l.setForeground(TEXT_SEC); return l;
    }

    // ── PLAYLIST VIEW ──────────────────────────────────────────────────────
    private JPanel buildListaView() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(22, 22, 0, 22));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(0, 0, 14, 0));
        JLabel title = new JLabel("Playlist");
        title.setFont(FONT_TITLE); title.setForeground(TEXT_PRI);
        header.add(title, BorderLayout.WEST);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btns.setBackground(BG_DARK);
        JButton bTit  = flatBtn("Titulo");
        JButton bArt  = flatBtn("Artista");
        JButton bShuf = flatBtn("Embaralhar");
        JButton bAdd  = greenBtn("+ Adicionar");
        bTit.addActionListener(e  -> sortBy("titulo"));
        bArt.addActionListener(e  -> sortBy("artista"));
        bShuf.addActionListener(e -> shuffleList());
        bAdd.addActionListener(e  -> showAddDialog());
        btns.add(bTit); btns.add(bArt); btns.add(bShuf); btns.add(bAdd);
        header.add(btns, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        String[] cols = {"#", "Titulo", "Artista", "Duracao", "Arquivo", ""};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setBackground(BG_DARK); table.setForeground(TEXT_PRI);
        table.setGridColor(new Color(22, 22, 22));
        table.setRowHeight(44); table.setFont(FONT_BODY);
        table.setSelectionBackground(BG_PLAYING); table.setSelectionForeground(GREEN);
        table.setShowHorizontalLines(true); table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);

        JTableHeader th = table.getTableHeader();
        th.setBackground(new Color(16, 16, 16)); th.setForeground(TEXT_HINT);
        th.setFont(FONT_COL_HDR);
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));
        th.setPreferredSize(new Dimension(0, 32));

        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(32); cm.getColumn(0).setMaxWidth(40);
        cm.getColumn(1).setPreferredWidth(180);
        cm.getColumn(2).setPreferredWidth(130);
        cm.getColumn(3).setPreferredWidth(75);  cm.getColumn(3).setMaxWidth(90);
        cm.getColumn(4).setPreferredWidth(140);
        cm.getColumn(5).setPreferredWidth(44);  cm.getColumn(5).setMaxWidth(52);

        table.setDefaultRenderer(Object.class, new TrackCellRenderer());
        cm.getColumn(5).setCellRenderer(new DeleteBtnRenderer());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row < 0) return;
                if (col == 5) removeTrack(row);
                else          playAt(row);
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(BG_DARK); scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    // ── FILA VIEW ──────────────────────────────────────────────────────────
    private JPanel buildFilaView() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(22, 22, 0, 22));
        JLabel title = new JLabel("Fila de reproducao");
        title.setFont(FONT_TITLE); title.setForeground(TEXT_PRI);
        title.setBorder(new EmptyBorder(0, 0, 14, 0));
        root.add(title, BorderLayout.NORTH);
        filaPanel = new JPanel();
        filaPanel.setLayout(new BoxLayout(filaPanel, BoxLayout.Y_AXIS));
        filaPanel.setBackground(BG_DARK);
        JScrollPane scroll = new JScrollPane(filaPanel);
        scroll.setBackground(BG_DARK); scroll.getViewport().setBackground(BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        root.add(scroll, BorderLayout.CENTER);
        return root;
    }

    // ── PLAYER BAR ─────────────────────────────────────────────────────────
    private JPanel buildPlayerBar() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(BG_BAR);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(10, 16, 10, 16)
        ));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(BG_BAR);
        info.setPreferredSize(new Dimension(150, 0));
        lblBarTitle  = new JLabel("--"); lblBarTitle.setFont(FONT_BTN);   lblBarTitle.setForeground(TEXT_PRI);
        lblBarArtist = new JLabel("--"); lblBarArtist.setFont(FONT_SMALL); lblBarArtist.setForeground(TEXT_SEC);
        info.add(lblBarTitle); info.add(Box.createVerticalStrut(2)); info.add(lblBarArtist);

        JPanel center = new JPanel(new BorderLayout(0, 5));
        center.setBackground(BG_BAR);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        ctrl.setBackground(BG_BAR);
        MiniIconButton bPrev = new MiniIconButton(BtnIcon.PREV);
        btnBarPlay           = new IconButton(BtnIcon.PAUSE, false);
        btnBarPlay.setPreferredSize(new Dimension(28, 28));
        MiniIconButton bNext = new MiniIconButton(BtnIcon.NEXT);
        bPrev.addActionListener(e      -> prevTrack());
        btnBarPlay.addActionListener(e -> togglePlay());
        bNext.addActionListener(e      -> nextTrack());
        ctrl.add(bPrev); ctrl.add(btnBarPlay); ctrl.add(bNext);

        JPanel progRow = new JPanel(new BorderLayout(6, 0));
        progRow.setBackground(BG_BAR);
        lblBarCur = new JLabel("0:00"); lblBarCur.setFont(FONT_SMALL); lblBarCur.setForeground(TEXT_SEC);
        lblBarTot = new JLabel("0:00"); lblBarTot.setFont(FONT_SMALL); lblBarTot.setForeground(TEXT_SEC);
        barProg = new JProgressBar(0, 1000);
        barProg.setBackground(new Color(38, 38, 38)); barProg.setForeground(GREEN);
        barProg.setBorderPainted(false); barProg.setPreferredSize(new Dimension(0, 3));
        progRow.add(lblBarCur, BorderLayout.WEST);
        progRow.add(barProg,   BorderLayout.CENTER);
        progRow.add(lblBarTot, BorderLayout.EAST);

        center.add(ctrl, BorderLayout.CENTER); center.add(progRow, BorderLayout.SOUTH);
        bar.add(info, BorderLayout.WEST); bar.add(center, BorderLayout.CENTER);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DADOS DE EXEMPLO
    // ══════════════════════════════════════════════════════════════════════
    private void loadSampleData() {
        // Musicas de exemplo sem arquivo local — so simulacao de timer
        playlist.adicionar("Blinding Lights", "The Weeknd",   "-", 200);
        playlist.adicionar("As It Was",       "Harry Styles", "-", 167);
        playlist.adicionar("Flowers",         "Miley Cyrus",  "-", 200);
        playlist.adicionar("Cruel Summer",    "Taylor Swift", "-", 178);
        playlist.adicionar("Levitating",      "Dua Lipa",     "-", 203);
        rebuildOrdem();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SINCRONIZACAO ordem <-> Playlist encadeada
    // ══════════════════════════════════════════════════════════════════════
    /**
     * Reconstroi a lista auxiliar `ordem` percorrendo a lista encadeada
     * da Playlist a partir da cabeca. Deve ser chamado apos qualquer
     * operacao que altere a Playlist (adicionar, remover, ordenar, embaralhar).
     * Tambem atualiza curIdx para apontar para noAtual.
     */
    private void rebuildOrdem() {
        ordem.clear();
        No cur = playlist.getCabeca();
        while (cur != null) { ordem.add(cur); cur = cur.getProximo(); }

        // Reajusta curIdx para o noAtual (pode ter mudado de posicao apos ordenacao)
        curIdx = -1;
        if (noAtual != null) {
            for (int i = 0; i < ordem.size(); i++) {
                if (ordem.get(i) == noAtual) { curIdx = i; break; }
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RENDER
    // ══════════════════════════════════════════════════════════════════════
    private void renderAll() {
        renderStats(); renderTable(); renderPlayer(); renderFila();
    }

    private void renderStats() {
        lblTracks.setText(String.valueOf(playlist.getTamanho()));
        lblDurTotal.setText(fmtDur(playlist.getDuracaoTotal()));
    }

    private void renderTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < ordem.size(); i++) {
            No t = ordem.get(i);
            String arq = t.getCaminhoOuUrl();
            String arqLabel = (arq == null || arq.equals("-") || arq.isBlank())
                ? "sem arquivo"
                : new File(arq).getName();
            tableModel.addRow(new Object[]{
                i + 1, t.getTitulo(), t.getArtista(),
                fmtTime(t.getDuracao()), arqLabel, "X"
            });
        }
    }

    private void renderPlayer() {
        if (curIdx < 0 || ordem.isEmpty()) {
            lblNowTitle.setText("--");    lblNowArtist.setText("--");
            lblCurTime.setText("0:00");   lblTotTime.setText("0:00");
            progBar.setValue(0);
            btnPlay.setIcon(BtnIcon.PLAY);
            pnlNextUp.removeAll(); pnlNextUp.revalidate(); pnlNextUp.repaint();
            lblBarTitle.setText("--");    lblBarArtist.setText("--");
            lblBarCur.setText("0:00");    lblBarTot.setText("0:00");
            barProg.setValue(0);
            btnBarPlay.setIcon(BtnIcon.PLAY);
            return;
        }

        No t = ordem.get(curIdx);
        lblNowTitle.setText(t.getTitulo());
        lblNowArtist.setText(t.getArtista());
        lblTotTime.setText(fmtTime(t.getDuracao()));
        lblCurTime.setText(fmtTime(elapsed));

        int pct = t.getDuracao() > 0 ? (int)(elapsed * 1000.0 / t.getDuracao()) : 0;
        progBar.setValue(pct); barProg.setValue(pct);

        BtnIcon playIcon = playing ? BtnIcon.PAUSE : BtnIcon.PLAY;
        btnPlay.setIcon(playIcon); btnBarPlay.setIcon(playIcon);
        btnRepeat.setActive(repeat); btnShuffle.setActive(shuffle);

        lblBarTitle.setText(t.getTitulo());
        lblBarArtist.setText(t.getArtista());
        lblBarTot.setText(fmtTime(t.getDuracao()));
        lblBarCur.setText(fmtTime(elapsed));

        // A seguir — usa proximo da lista encadeada
        pnlNextUp.removeAll();
        No proxNo = t.getProximo();
        if (proxNo != null) {
            pnlNextUp.add(buildQueueRow(curIdx + 2, proxNo, false), BorderLayout.CENTER);
        } else {
            JLabel end = new JLabel("Fim da playlist");
            end.setFont(FONT_SMALL); end.setForeground(TEXT_HINT);
            pnlNextUp.add(end, BorderLayout.CENTER);
        }
        pnlNextUp.revalidate(); pnlNextUp.repaint();
    }

    private void renderFila() {
        filaPanel.removeAll();
        if (ordem.isEmpty()) {
            filaPanel.add(emptyHint("Playlist vazia"));
        } else if (curIdx < 0) {
            filaPanel.add(emptyHint("Nenhuma musica tocando -- selecione uma faixa"));
        } else {
            for (int i = 0; i < ordem.size(); i++) {
                filaPanel.add(buildQueueRow(i + 1, ordem.get(i), i == curIdx));
                filaPanel.add(Box.createVerticalStrut(3));
            }
        }
        filaPanel.revalidate(); filaPanel.repaint();
    }

    private JLabel emptyHint(String msg) {
        JLabel l = new JLabel(msg);
        l.setFont(FONT_BODY); l.setForeground(TEXT_HINT);
        l.setBorder(new EmptyBorder(20, 0, 0, 0));
        return l;
    }

    private JPanel buildQueueRow(int pos, No t, boolean current) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(current ? BG_PLAYING : BG_CARD);
        row.setBorder(new CompoundBorder(
            new LineBorder(current ? GREEN_DIM : BORDER_CLR, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JLabel lPos = new JLabel(current ? ">" : String.valueOf(pos));
        lPos.setFont(FONT_SMALL);
        lPos.setForeground(current ? GREEN : TEXT_HINT);
        lPos.setPreferredSize(new Dimension(20, 0));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(row.getBackground());
        JLabel lTit = new JLabel(t.getTitulo()); lTit.setFont(FONT_BTN);
        JLabel lArt = new JLabel(t.getArtista()); lArt.setFont(FONT_SMALL);
        lTit.setForeground(current ? GREEN : TEXT_PRI);
        lArt.setForeground(TEXT_SEC);
        info.add(lTit); info.add(lArt);

        JPanel right = new JPanel(new BorderLayout(8, 0));
        right.setBackground(row.getBackground());
        JLabel lDur = new JLabel(fmtTime(t.getDuracao()));
        lDur.setFont(FONT_SMALL); lDur.setForeground(TEXT_HINT);
        if (current) {
            JLabel pill = new JLabel("agora");
            pill.setFont(FONT_LABEL); pill.setForeground(GREEN);
            pill.setBorder(new CompoundBorder(
                new LineBorder(GREEN_DIM, 1, true), new EmptyBorder(2, 7, 2, 7)
            ));
            right.add(pill, BorderLayout.WEST);
        }
        right.add(lDur, BorderLayout.EAST);

        row.add(lPos, BorderLayout.WEST);
        row.add(info, BorderLayout.CENTER);
        row.add(right, BorderLayout.EAST);
        return row;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ACOES DO PLAYER
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Inicia reproducao do indice idx da lista ordem.
     * Usa playlist.proximaMusica() / playlist.musicaAnterior() internamente
     * para manter o ponteiro noAtual sincronizado com a lista encadeada.
     */
    private void playAt(int idx) {
        if (idx < 0 || idx >= ordem.size()) return;

        stopAudio();
        ticker.stop();

        noAtual = ordem.get(idx);
        curIdx  = idx;
        elapsed = 0;
        pauseElapsed = 0;
        audioDuration = noAtual.getDuracao();

        String path = noAtual.getCaminhoOuUrl();
        if (path != null && !path.equals("-") && !path.isBlank()) {
            startAudioFrom(new File(path), 0);
        }

        playing = true;
        ticker = new Timer(1000, e -> tickPlayer());
        ticker.start();

        renderPlayer(); renderTable(); renderFila();
        switchView("player");
    }

    private void togglePlay() {
        if (curIdx < 0) return;
        playing = !playing;
        if (playing) {
            resumeAudio();
            ticker.start();
        } else {
            pauseAudio();
            ticker.stop();
        }
        renderPlayer();
    }

    /** Usa playlist.proximaMusica() para navegar na lista encadeada. */
    private void nextTrack() {
        if (ordem.isEmpty()) return;

        int ni;
        if (shuffle) {
            ni = (int)(Math.random() * ordem.size());
        } else {
            // usa o ponteiro da lista encadeada
            No prox = (noAtual != null) ? playlist.proximaMusica(noAtual) : playlist.getCabeca();
            ni = indexOf(prox);
            if (ni < 0) ni = 0;
        }
        playAt(ni);
    }

    /** Usa playlist.musicaAnterior() para navegar na lista encadeada. */
    private void prevTrack() {
        if (ordem.isEmpty()) return;
        if (elapsed > 4) { elapsed = 0; pauseElapsed = 0; renderPlayer(); return; }

        No ant = (noAtual != null) ? playlist.musicaAnterior(noAtual) : playlist.getCauda();
        int pi = indexOf(ant);
        if (pi < 0) pi = 0;
        playAt(pi);
    }

    private int indexOf(No no) {
        for (int i = 0; i < ordem.size(); i++)
            if (ordem.get(i) == no) return i;
        return -1;
    }

    private void toggleRepeat()  { repeat  = !repeat;  renderPlayer(); }
    private void toggleShuffle() { shuffle = !shuffle; renderPlayer(); }

    /** Tick — incrementa elapsed e detecta fim da musica simulada (sem arquivo). */
    private void tickPlayer() {
        if (!playing || curIdx < 0) return;
        elapsed++;
        No t = ordem.get(curIdx);
        if (elapsed >= t.getDuracao()) {
            if (repeat) elapsed = 0;
            else { nextTrack(); return; }
        }
        renderPlayer();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  ACOES DA PLAYLIST
    // ══════════════════════════════════════════════════════════════════════

    private void showAddDialog() {
        JDialog dlg = new JDialog(this, "Adicionar musica", true);
        dlg.setResizable(false);
        dlg.getContentPane().setBackground(BG_MODAL);
        dlg.setLayout(new BorderLayout());

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(BG_MODAL);
        body.setBorder(new EmptyBorder(18, 22, 8, 22));

        JTextField fTit = darkField("Nome da musica");
        JTextField fArt = darkField("Nome do artista");
        JTextField fDur = darkField("ex: 210 (preenchido automaticamente para WAV)");

        // ── Campo de arquivo com botao Procurar ──────────────────────────
        JTextField fArq = darkField("Clique em Procurar para selecionar o arquivo");
        fArq.setEditable(false);
        fArq.setBackground(new Color(16, 16, 16));

        // Guarda o File selecionado para usar depois
        final File[] arquivoSelecionado = { null };

        JButton btnProcurar = flatBtn("Procurar...");
        btnProcurar.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Selecionar arquivo de musica");
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Arquivos de audio (*.wav, *.mp3, *.aif, *.au)",
                "wav", "mp3", "aif", "aiff", "au"
            ));
            int res = fc.showOpenDialog(dlg);
            if (res == JFileChooser.APPROVE_OPTION) {
                arquivoSelecionado[0] = fc.getSelectedFile();
                fArq.setText(arquivoSelecionado[0].getAbsolutePath());

                // Preenche titulo automaticamente se vazio
                if (fTit.getText().isBlank()) {
                    String nome = arquivoSelecionado[0].getName();
                    int dot = nome.lastIndexOf('.');
                    fTit.setText(dot > 0 ? nome.substring(0, dot) : nome);
                }

                // Tenta ler duracao real do arquivo
                int durReal = readFileDuration(arquivoSelecionado[0]);
                if (durReal > 0) {
                    fDur.setText(String.valueOf(durReal));
                }
            }
        });

        // Painel para o campo arquivo + botao lado a lado
        JPanel arqRow = new JPanel(new BorderLayout(6, 0));
        arqRow.setBackground(BG_MODAL);
        arqRow.setAlignmentX(LEFT_ALIGNMENT);
        fArq.setMaximumSize(new Dimension(Integer.MAX_VALUE, fArq.getPreferredSize().height));
        arqRow.add(fArq, BorderLayout.CENTER);
        arqRow.add(btnProcurar, BorderLayout.EAST);

        body.add(mkFieldBlock("Titulo",        fTit));
        body.add(Box.createVerticalStrut(10));
        body.add(mkFieldBlock("Artista",        fArt));
        body.add(Box.createVerticalStrut(10));
        // Arquivo com label manual (arqRow nao e JTextField, e um JPanel)
        JPanel arqBlock = new JPanel();
        arqBlock.setLayout(new BoxLayout(arqBlock, BoxLayout.Y_AXIS));
        arqBlock.setBackground(BG_MODAL);
        arqBlock.setAlignmentX(LEFT_ALIGNMENT);
        JLabel arqLbl = new JLabel("Arquivo de audio");
        arqLbl.setFont(FONT_SMALL); arqLbl.setForeground(TEXT_SEC);
        arqLbl.setAlignmentX(LEFT_ALIGNMENT);
        arqLbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        arqRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        arqBlock.add(arqLbl);
        arqBlock.add(arqRow);
        body.add(arqBlock);
        body.add(Box.createVerticalStrut(10));
        body.add(mkFieldBlock("Duracao (seg)", fDur));

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        footer.setBackground(BG_MODAL);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(10, 20, 14, 20)
        ));
        JButton cancel = flatBtn("Cancelar");
        JButton ok     = greenBtn("Confirmar");
        cancel.addActionListener(e -> dlg.dispose());
        ok.addActionListener(e -> {
            String tit = fTit.getText().trim();
            String art = fArt.getText().trim();
            int dur;
            try { dur = Integer.parseInt(fDur.getText().trim()); }
            catch (NumberFormatException ex) { dur = 0; }

            if (tit.isEmpty() || art.isEmpty() || dur <= 0) {
                JOptionPane.showMessageDialog(dlg,
                    "Preencha titulo, artista e duracao valida.",
                    "Campos invalidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String caminho = arquivoSelecionado[0] != null
                ? arquivoSelecionado[0].getAbsolutePath()
                : "-";

            // Usa playlist.adicionar() da Playlist.java
            playlist.adicionar(tit, art, caminho, dur);
            rebuildOrdem();
            renderAll();
            dlg.dispose();
        });
        footer.add(cancel); footer.add(ok);

        dlg.add(body,   BorderLayout.CENTER);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.pack();
        dlg.setMinimumSize(new Dimension(460, dlg.getHeight()));
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private JPanel mkFieldBlock(String labelText, JTextField field) {
        JPanel block = new JPanel();
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        block.setBackground(BG_MODAL);
        block.setAlignmentX(LEFT_ALIGNMENT);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_SMALL); lbl.setForeground(TEXT_SEC);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        field.setAlignmentX(LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        block.add(lbl); block.add(field);
        return block;
    }

    private void removeTrack(int row) {
        if (row < 0 || row >= ordem.size()) return;
        No no = ordem.get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remover \"" + no.getTitulo() + "\"?",
            "Confirmar remocao", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        if (noAtual == no) {
            stopAudio();
            ticker.stop();
            noAtual = null; curIdx = -1; playing = false; elapsed = 0;
        }

        // Usa playlist.remover() da Playlist.java
        playlist.remover(no);
        rebuildOrdem();
        renderAll();
    }

    /**
     * Ordena usando os metodos da Playlist.java, depois sincroniza.
     * Se havia uma musica tocando, ela continua tocando (curIdx reajustado
     * em rebuildOrdem via noAtual).
     */
    private void sortBy(String campo) {
        boolean estavaTocando = playing;
        if (estavaTocando) { ticker.stop(); pauseAudio(); }

        if (campo.equals("titulo"))
            playlist.ordenarTitulo();
        else
            playlist.ordenarArtista();

        rebuildOrdem();

        if (estavaTocando && curIdx >= 0) {
            resumeAudio();
            ticker.start();
        }
        renderAll();
    }

    private void shuffleList() {
        boolean estavaTocando = playing;
        if (estavaTocando) { ticker.stop(); pauseAudio(); }

        playlist.embaralharPlaylist();
        rebuildOrdem();

        if (estavaTocando && curIdx >= 0) {
            resumeAudio();
            ticker.start();
        }
        renderAll();
    }

    private void switchView(String v) {
        cards.show(cardLayout, v);
        if (v.equals("fila")) renderFila();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HELPERS — formatacao
    // ══════════════════════════════════════════════════════════════════════
    private String fmtTime(int s) {
        int m = s / 60, sec = s % 60;
        return m + ":" + (sec < 10 ? "0" : "") + sec;
    }

    private String fmtDur(int s) {
        int h = s / 3600, m = (s % 3600) / 60, sec = s % 60;
        if (h == 0 && m == 0) return sec + "s";
        if (h == 0)           return m + "min " + sec + "s";
        return h + "h " + m + "min";
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HELPERS — componentes
    // ══════════════════════════════════════════════════════════════════════
    private JButton flatBtn(String txt) {
        JButton b = new JButton(txt);
        b.setFont(FONT_BTN); b.setForeground(TEXT_SEC); b.setBackground(BG_BTN);
        b.setBorder(new CompoundBorder(new LineBorder(BORDER_MED, 1, true), new EmptyBorder(5, 12, 5, 12)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(BG_BTN_HOV); b.setForeground(TEXT_PRI); }
            public void mouseExited(MouseEvent e)  { b.setBackground(BG_BTN);     b.setForeground(TEXT_SEC); }
        });
        return b;
    }

    private JButton greenBtn(String txt) {
        JButton b = new JButton(txt);
        b.setFont(FONT_BTN); b.setForeground(Color.BLACK); b.setBackground(GREEN);
        b.setBorder(new CompoundBorder(new LineBorder(GREEN, 1, true), new EmptyBorder(5, 14, 5, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(GREEN_HOV); }
            public void mouseExited(MouseEvent e)  { b.setBackground(GREEN); }
        });
        return b;
    }

    private JTextField darkField(String placeholder) {
        JTextField f = new JTextField(20);
        f.setBackground(BG_INPUT); f.setForeground(TEXT_PRI); f.setCaretColor(GREEN);
        f.setFont(FONT_BODY);
        f.setBorder(new CompoundBorder(new LineBorder(BORDER_MED, 1, true), new EmptyBorder(8, 10, 8, 10)));
        f.setToolTipText(placeholder);
        return f;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RENDERERS DE TABELA
    // ══════════════════════════════════════════════════════════════════════
    private class TrackCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, foc, row, col);
            boolean isPlaying = (row == curIdx);
            setBackground(isPlaying ? BG_PLAYING : (row % 2 == 0 ? BG_DARK : BG_ROW_ALT));
            setForeground(isPlaying ? GREEN : (col == 0 ? TEXT_HINT : TEXT_PRI));
            setFont(col == 1 ? FONT_BTN : (col == 0 ? FONT_SMALL : FONT_BODY));
            // coluna arquivo em cinza mais escuro
            if (col == 4 && !isPlaying) setForeground(TEXT_SEC);
            setBorder(new EmptyBorder(0, 10, 0, 10));
            return this;
        }
    }

    private class DeleteBtnRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int row, int col) {
            JLabel l = new JLabel("X");
            l.setFont(new Font("SansSerif", Font.BOLD, 11));
            l.setForeground(new Color(60, 60, 60));
            l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setBackground(row == curIdx ? BG_PLAYING : (row % 2 == 0 ? BG_DARK : BG_ROW_ALT));
            l.setOpaque(true);
            return l;
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SCROLLBAR ESCURA
    // ══════════════════════════════════════════════════════════════════════
    private static class DarkScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        protected void configureScrollBarColors() {
            thumbColor = new Color(48, 48, 48);
            trackColor = new Color(18, 18, 18);
        }
        protected JButton createDecreaseButton(int o) { return zeroBtn(); }
        protected JButton createIncreaseButton(int o) { return zeroBtn(); }
        private JButton zeroBtn() {
            JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(PlaylistMusicaED::new);
    }
}