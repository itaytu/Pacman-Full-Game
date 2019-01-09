package GUI;

import Converters.Map;
import Elements.*;
import Geom.Point3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * The board UI. Paints the game Objects.
 */
public class Board extends JPanel implements MouseListener {

    private BufferedImage map, pacman, player, fruit, ghost;
    private Game game;
    private Map mapProperties;
    private NextPoint nextPoint;

    private boolean addPlayer, stepByStep, runAutoGame, loaded, firstClick = true, runAlgo = false;

    /** Constructs new Board.
     */
    public Board() {
        init();
    }

    /**
     * Inits the board with a new game (the models), and the observable object: NextPoint.
     * Loading object images, and inits the mouse click listener.
     */
    private void init() {
        game = new Game();
        nextPoint = new NextPoint();

        loadImages();
        addMouseListener(this);
    }

    /**
     * Paints the objects on screen.
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        int h = this.getHeight();
        int w = this.getWidth();

        mapProperties = new Map(map, w, h, 35.20238, 35.21236, 32.10190, 32.10569);

        g.drawImage(map, 0, 0, w, h, this);

        if (!game.getPacmanArrayList().isEmpty()) {
            for (int i = 0; i < game.getPacmanArrayList().size(); i++) {
                Pacman p = game.getPacmanArrayList().get(i);
                int[] arr = mapProperties.gps2Pixels(p.getPoint());
                g.drawImage(pacman, arr[0], arr[1], this);
            }
        }

        if (!game.getFruitArrayList().isEmpty()) {
            for (int i = 0; i < game.getFruitArrayList().size(); i++) {
                Fruit f = game.getFruitArrayList().get(i);
                int[] arr = mapProperties.gps2Pixels(f.getPoint());
                g.drawImage(fruit, arr[0], arr[1], this);
            }
        }

        if (!game.getGhostArrayList().isEmpty()) {
            for (int i = 0; i < game.getGhostArrayList().size(); i++) {
                Ghost gh = game.getGhostArrayList().get(i);
                int[] arr = mapProperties.gps2Pixels(gh.getPoint());
                g.drawImage(ghost, arr[0], arr[1], this);
            }
        }

        if (!game.getBlockArrayList().isEmpty()) {
            for(int i = 0; i < game.getBlockArrayList().size(); i ++) {
                Block b = game.getBlockArrayList().get(i);

                int[] startArr = mapProperties.gps2Pixels(b.getMaxLeft());
                int[] minArr = mapProperties.gps2Pixels(b.getMinLeft());
                int[] maxArr = mapProperties.gps2Pixels(b.getMaxRight());

                int width = maxArr[0] - startArr[0];
                int height = minArr[1] - startArr[1];

                g.fillRect(startArr[0], startArr[1], width, height);
            }
        }

        if (loaded) {
                int[] arr = mapProperties.gps2Pixels(game.getPlayer().getPoint());
                g.drawImage(player, arr[0], arr[1], this);
        }
    }

    /**
     * Handles mouse clicks.
     * When the click is targeted to a game mode, like Auto Game or
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        if (addPlayer || stepByStep || runAutoGame) {
            int x = e.getX();
            int y = e.getY();

            mapProperties = new Map(map, getWidth(), getHeight(), 35.20238, 35.21236, 32.10190, 32.10569);
            Point3D newPoint = mapProperties.toCoords(x, y);

            if (addPlayer) {
                nextPoint.setPoints(newPoint, newPoint);
            }

            else {
                // stepByStep || runAutoGame Option
                // Update observers with new points
                nextPoint.setPoints(newPoint, game.getPlayer().getPoint());
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void clearPlayer() {
        game.getPlayer().setPoint(null);
        repaint();
    }

    public void clearGame() {
        game.setPlayer(new Point3D(0,0,0));
        game.getBlockArrayList().clear();
        game.getGhostArrayList().clear();
        game.getFruitArrayList().clear();
        game.getPacmanArrayList().clear();

        repaint();
    }

    public void setRunAlgo(boolean flag) { runAlgo = flag; }

    public void setAddPlayer(boolean flag) {
        addPlayer = flag;
    }

    public void setStepByStep(boolean flag) { stepByStep = flag; }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setLoaded(boolean isLoaded) {
        loaded = isLoaded;
    }

    public void setRunAutoGame(boolean flag) {
        runAutoGame = flag;
    }

    public NextPoint getNextPoint() {
        return nextPoint;
    }

    public boolean isFirstClick() {
        return firstClick;
    }

    public void setFirstClick(boolean isClicked) {
        firstClick = isClicked;
    }

    private void loadImages() {
        try {
            File map1 = new File(getClass().getClassLoader().getResource("Ariel1.png").getFile());
            map = ImageIO.read(map1);

            File pacman1 = new File(getClass().getClassLoader().getResource("badPacman.png").getFile());
            pacman = ImageIO.read(pacman1);

            File fruit1 = new File(getClass().getClassLoader().getResource("fruit.png").getFile());
            fruit = ImageIO.read(fruit1);

            File ghost1 = new File(getClass().getClassLoader().getResource("ghost.png").getFile());
            ghost = ImageIO.read(ghost1);

            File player1 = new File(getClass().getClassLoader().getResource("pacman.png").getFile());
            player = ImageIO.read(player1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        repaint();
    }

    public void updateGUI() {
        repaint();
    }

    public boolean isRunStepByStep() {
        return stepByStep;
    }

    public boolean isRunAutoGame() {
        return runAutoGame;
    }

    public boolean isAddPlayer() {
        return addPlayer;
    }

    public boolean isRunAlgo() { return runAlgo; }

    public void showMessagePrompt(String msg) {
        JOptionPane.showMessageDialog(null, "Game ended.\n" + msg);
    }
}