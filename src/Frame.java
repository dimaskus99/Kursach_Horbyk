import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Frame extends JFrame{
    private JMenuBar menuBar;
    private JMenu menuGame;
    private JMenu menuGameStart;
    private JMenuItem itemStartAuto;
    private JMenuItem itemStartRast;
    private JMenuItem itemExit;
    Frame() {
        super("Морський бій");
        Panel pole=new Panel();
        menuBar=new JMenuBar();
        menuGame = new JMenu("Гра");
        menuGameStart = new JMenu("Почати гру");
        itemStartAuto =new JMenuItem("Рандомна розстановка");
        itemStartAuto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pole.start();
            }
        });
        itemStartRast = new JMenuItem("Вручна розстановка");
        itemStartRast.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pole.startRasstanovka();
            }
        });
        itemExit=new JMenuItem("Вийти");
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pole.exit();
            }
        });
        menuGameStart.add(itemStartAuto);
        menuGameStart.add(itemStartRast);
        menuGame.add(menuGameStart);
        menuGame.add(itemExit);
        menuBar.add(menuGame);
        setJMenuBar(menuBar);
        Container container=getContentPane();
        container.add(pole);
        setSize(pole.getSize());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


    }
}
