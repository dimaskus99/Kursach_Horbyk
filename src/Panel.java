import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
public class Panel extends JPanel{
    private final int DXY = 60;
    private final int H = 23;
    private String number[] = {"Р", "Е", "С", "П", "У", "Б", "Л", "І", "К", "А"};
    private Game game;
    private int mX, mY;
    private Timer timer;
    private BufferedImage  ranen, boom,killed,paluba;
    private Rectangle2D line4,line3,line2,line1;
    private boolean isSelectP4=false;
    private boolean isSelectP3=false;
    private boolean isSelectP2=false;
    private boolean isSelectP1=false;
    private int p4,p3,p2,p1;
    public boolean vert=true;
    private JButton checkNapr;
    public static boolean rasstanovka;


    public Panel() {
        addMouseListener(new Mouse());
        addMouseMotionListener(new Mouse());
        setFocusable(true);
        game = new Game();
        setSize(800,370);
        try {
            ranen = ImageIO.read(getClass().getResource("image/injur.png"));
            boom = ImageIO.read(getClass().getResource("image/bomb.jpg"));
            killed = ImageIO.read(getClass().getResource("image/kill.png"));
            paluba = ImageIO.read(getClass().getResource("image/ship.png"));
        } catch (IOException e) {e.printStackTrace();}
        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        setLayout(null);
        checkNapr=new JButton("Повернути");
        checkNapr.setBackground(new Color(248, 248, 255));
        checkNapr.setBounds(DXY+24*H,DXY+8*H,7*H,H);
        checkNapr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vert)vert=false;
                else vert=true;
            }
        });
        add(checkNapr);
        checkNapr.setVisible(false);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2= (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        g.setColor(new Color(248, 248, 255));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("Times New Roman", 0, H - 5));
        g.setColor(new Color(330099));
        if (rasstanovka) {
            g2.setStroke(new BasicStroke(2));
            if (vert) {
                line4 = new Rectangle2D.Double(DXY + 24 * H, DXY, 4 * H, H);
                line3 = new Rectangle2D.Double(DXY + 24 * H, DXY + 2 * H, 3 * H, H);
                line2 = new Rectangle2D.Double(DXY + 24 * H, DXY + 4 * H, 2 * H, H);
                line1 = new Rectangle2D.Double(DXY + 24 * H, DXY + 6 * H, 1 * H, H);
            } else {
                line4 = new Rectangle2D.Double(DXY + 24 * H, DXY, H, 4 * H);
                line3 = new Rectangle2D.Double(DXY + 26 * H, DXY, H, 3 * H);
                line2 = new Rectangle2D.Double(DXY + 28 * H, DXY, H, 2 * H);
                line1 = new Rectangle2D.Double(DXY + 30 * H, DXY, H, 1 * H);
            }

            if (p4 != 0) ((Graphics2D) g).draw(line4);
            if (p3 != 0) ((Graphics2D) g).draw(line3);
            if (p2 != 0) ((Graphics2D) g).draw(line2);
            if (p1 != 0) ((Graphics2D) g).draw(line1);
            if ((p1+p2+p3+p4)!=0) {
                g.drawString("Розставте кораблі", DXY + 24 * H, DXY-H);
                checkNapr.setVisible(true);
            }
            else {
                checkNapr.setVisible(false);
            }
        }
        g.drawString("Наш флот", DXY + 4 * H, DXY - H);
        g.drawString("Ворожий флот", DXY + 16 * H, DXY - H);

        for (int i = 1; i <= 10; i++) {
            g.drawString(String.valueOf(i), DXY - H, DXY + i * H - (H / 4));
            g.drawString(String.valueOf(i), DXY + 12 * H, DXY + i * H - (H / 4));
            g.drawString(number[i-1], DXY + (i-1) * H + (H / 4), DXY - 3);
            g.drawString(number[i-1], 13 * H + DXY + (i-1) * H + (H / 4), DXY - 3);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (game.masComp[i][j]!=0) {
                    if ((game.masComp[i][j] >= 1) && (game.masComp[i][j] <= 4 && Game.gamePkVsPk)) {
                        g.drawImage(paluba, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                    }
                    else if ((game.masComp[i][j] >= 8) && (game.masComp[i][j] <= 11)) {
                        g.drawImage(ranen, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                    }
                    else if ((game.masComp[i][j] >= 15)) {
                        g.drawImage(killed, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                    }
                    else if ((game.masComp[i][j] >= 5 && game.masComp[i][j]<8 || game.masComp[i][j]==-2)) {
                        g.drawImage(boom, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                    }
                    else if (Game.endGame!=0 && (game.masComp[i][j] >= 1 && game.masComp[i][j] <= 4)) {
                        g.drawImage(paluba, DXY + 13 * H + H * i, DXY + H * j, H, H, null);
                        g.setColor(new Color(0x8B0000));
                        g.drawRect(DXY + 13 * H + H * i,DXY + H * j,H,H);
                    }
                }

                if (game.masPlay[i][j]!=0){
                    if ((game.masPlay[i][j] >= 1) && (game.masPlay[i][j] <= 4)) {
                        g.drawImage(paluba, DXY + H * i, DXY + H * j, H, H, null);
                    }else if ((game.masPlay[i][j] >= 8) && (game.masPlay[i][j] <= 11)) {
                        g.drawImage(ranen, DXY + + H * i, DXY + H * j, H, H, null);
                    }else if ((game.masPlay[i][j] >= 15)) {
                        g.drawImage(killed, DXY + H * i, DXY + H * j, H, H, null);
                    }else if ((game.masPlay[i][j] >= 5) && game.masPlay[i][j]<8) {
                        g.drawImage(boom, DXY +  + H * i, DXY + H * j, H, H, null);
                    }else if (Game.gamePkVsPk && game.masPlay[i][j] ==-2){
                        g.drawImage(boom, DXY +  + H * i, DXY + H * j, H, H, null);
                    }
                }
            }
        }


        for (int i = DXY; i <= DXY + 10 * H; i += H) {
            g2.setStroke(new BasicStroke(1));
            g.setColor(new Color(202,202,255));
            g.drawLine(DXY, i, DXY + 10 * H, i); // ----
            g.drawLine(i, DXY, i, DXY + 10 * H);
            g.drawLine(DXY + 13 * H, i, DXY + 23 * H, i); //бот ---
            g.drawLine(i + 13 * H, DXY, i + 13 * H, DXY + 10 * H);

            g2.setStroke(new BasicStroke(2));
            g.setColor(new Color(330099));
            g.drawRect(DXY, DXY, 10 * H, 10 * H);
            g.drawRect(DXY+13*H,DXY,10 * H,10 * H);
        }

        g.setFont(new Font("Times New Roman", 0, H));
        g.setColor(new Color(330099));


        if (Game.endGame==0 && (p1+p2+p3+p4)==0 && rasstanovka || Game.endGame==0 && !rasstanovka){
            g.setFont(new Font("Times New Roman", 0, H - 5));
            if (game.myHod) {
                g.setColor(Color.green);
                g.drawString("Вогонь по ворогу!", DXY + 24 * H, DXY + 12 * H - (H / 4));
            }
            else {
                g.setColor(Color.red);
                g.drawString("Ворожий вогонь!", DXY + 24 * H, DXY + 12 * H - (H / 4));
            }
        }if (Game.endGame == 1) {
            timer.stop();

        }if (Game.endGame == 2) {
            timer.stop();
        }
    }

    public void start() {
        rasstanovka = false;
        Game.gamePkVsPk = false;
        checkNapr.setVisible(false);
        timer.start();
        game.start();
    }

    public void startRasstanovka(){
        rasstanovka = true;
        timer.start();
        game.start();
        p1 = 4;
        p2 = 3;
        p3 = 2;
        p4 = 1;
    }

    public void startAutoGame(){
        rasstanovka = false;
        checkNapr.setVisible(false);
        timer.start();
        Game.gamePkVsPk = true;
        game.autoGame();

    }

    public void exit() {
        System.exit(0);
    }

    public class Mouse implements MouseListener,MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if ((e.getButton() == 1) && (e.getClickCount() == 1)) {
                mX = e.getX();
                mY = e.getY();
                if ((rasstanovka && p1+p2+p3+p4==0) || !rasstanovka && !Game.gamePkVsPk
                        && mX > (DXY + 13 * H) && mY > (DXY) && mX < (DXY + 23 * H) && mY < DXY + 10 * H) {
                    if (game.myHod && Game.endGame ==0 && !game.compHod){
                        int i=(mX-(DXY+13*H))/H;
                        int j=(mY-DXY)/H;
                        if ((i>=0 && i<=9) && (j>=0 && j<=9)) {
                            if (game.masComp[i][j] <= 4 && game.masComp[i][j] >= -1) {
                                game.attack(game.masComp, i, j);
                            }
                        }
                    }

                }
            }
            if (rasstanovka){
                if (line4.contains(e.getPoint())){
                    isSelectP4 =true;isSelectP3 =false;isSelectP2 =false;isSelectP1 =false;
                }if (line3.contains(e.getPoint())){
                    isSelectP4 =false;isSelectP3 =true;isSelectP2 =false;isSelectP1 =false;
                }if (line2.contains(e.getPoint())){
                    isSelectP4 =false;isSelectP3 =false;isSelectP2 =true;isSelectP1 =false;
                }if (line1.contains(e.getPoint())){
                    isSelectP4 =false;isSelectP3 =false;isSelectP2 =false;isSelectP1 =true;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (rasstanovka) {
                mX = e.getX();
                mY = e.getY();
                int i = (mX - (DXY)) / H;
                int j = (mY - DXY) / H;
                if (p4 != 0 && isSelectP4 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP4 = false;
                    if (game.setPaluba(i, j, 4, vert)) {
                        p4--;
                    }

                } else if (p3 != 0 && isSelectP3 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP3 = false;
                    if (game.setPaluba(i, j, 3, vert)) {
                        p3--;
                    }

                } else if (p2 != 0 && isSelectP2 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP2 = false;
                    if (game.setPaluba(i, j, 2, vert)) {
                        p2--;
                    }

                } else if (p1 != 0 && isSelectP1 && mX > (DXY) && mY > (DXY) && mX < (DXY + 10 * H) && mY < DXY + 10 * H) {
                    isSelectP1 = false;
                    if (game.setPaluba(i, j, 1, vert)) {
                        p1--;
                    }
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (rasstanovka) {
                mX = e.getX();
                mY = e.getY();
                int i = (mX - (DXY)) / H;
                int j = (mY - DXY) / H;
                Graphics g = getGraphics();
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(2));
                g.setColor(new Color(330099));
                if (isSelectP4) {
                    if(vert) g.drawRect(DXY + H * i, DXY + H * j, H*4, H);
                    else g.drawRect(DXY + H * i, DXY + H * j, H, H*4);
                }
                if (isSelectP3) {
                    if(vert) g.drawRect(DXY + H * i, DXY + H * j, H*3, H);
                    else g.drawRect(DXY + H * i, DXY + H * j, H, H*3);
                }
                if (isSelectP2) {
                    if(vert) g.drawRect(DXY + H * i, DXY + H * j, H*2, H);
                    else g.drawRect(DXY + H * i, DXY + H * j, H, H*2);
                }
                if (isSelectP1) {
                    g.drawRect(DXY + H * i, DXY + H * j, H, H);
                }

            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {


        }
    }
}
