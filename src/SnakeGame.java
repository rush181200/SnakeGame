import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {


    private class
    Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x =x;
            this.y=y;
        }

    }


    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    Tile food;
    Random random;

    ArrayList<Tile> snakeBody;

    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardHeight,int boardWidth){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        addKeyListener(this);
        setFocusable(true);

        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.BLACK);
        snakeHead=new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();
        velocityX=0;
        velocityY=0;
        gameLoop = new Timer(100,this);
        gameLoop.start();
    }

    public boolean collision(Tile t1, Tile t2){
        return t1.x == t2.x && t1.y==t2.y;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
//        for grid
        for(int i=0;i<boardWidth/tileSize;i++){
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
        }
//food
        g.setColor(Color.red);
//        g.fillRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize);
        g.fill3DRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize,true);

//        for snake
        g.setColor(Color.GREEN);
//        g.fillRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize);
        g.fill3DRect(snakeHead.x*tileSize,snakeHead.y*tileSize,tileSize,tileSize,true);

        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
//            g.fillRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize);
            g.fill3DRect(snakePart.x*tileSize,snakePart.y*tileSize,tileSize,tileSize,true);
        }

//        score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over : " + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }else{
            g.drawString("Score : " + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }

    }
    private void placeFood() {
       food.x = random.nextInt(boardWidth/tileSize);
       food.y = random.nextInt(boardHeight/tileSize);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        movee();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }

    private void movee() {
        if(collision(snakeHead,food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for(int i = snakeBody.size()-1;i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x= snakeHead.x;
                snakePart.y = snakeHead.y;
            }else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }


        snakeHead.x+=velocityX;
        snakeHead.y+=velocityY;

        for(int i=0;i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead,snakePart)){
                gameOver=true;
            }
        }

        if(snakeHead.x*tileSize<0 || snakeHead.x*tileSize > boardWidth || snakeHead.y*tileSize<0 || snakeHead.y*tileSize>boardHeight){
            gameOver=true;
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getKeyCode() == KeyEvent.VK_UP && velocityY!=1){
            velocityX=0;
            velocityY=-1;
        }else if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN&& velocityY!=-1){
            velocityX=0;
            velocityY=1;
        }else if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT&&velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }else if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT&&velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }
    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}
