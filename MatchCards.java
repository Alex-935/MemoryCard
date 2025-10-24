import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MatchCards {

    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    String[] cardList = {//card names
        "darkness", "double", "fairy", "fighting", "fire", 
        "grass", "lightning", "metal", "psychic", "water"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 180;
    int cardHeight = 256;

    ArrayList<Card> cardSet; //deck of cards with cardNames and cardImageIcons
    ImageIcon cardBackImageIcon;

    int boardWidth =  columns * cardWidth;
    int boardHeight = rows * cardHeight;

    JFrame frame =  new JFrame("Card Match");
    JPanel textPanel = new JPanel();
    JLabel textLabel = new JLabel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0;
    ArrayList<JButton> board;
    Timer hideCardTimer;
    boolean gameReady = false;
    JButton card1Selected;
    JButton card2Selected;

    MatchCards() {
        setupCards();
        shuffleCards();

        frame.setLayout(new BorderLayout());
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel.setFont(new Font("Ariel", Font.PLAIN, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: 0");

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        //card game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {

            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        }
                        else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                errorCount++;
                                textLabel.setText("Errors: " + errorCount);
                                hideCardTimer.start();
                            }
                            else {
                                card1Selected = null;
                                card2Selected = null;
                            }                 
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        //restart game button
        restartButton.setFont(new Font("Arial", Font.PLAIN,16));
        restartButton.setText("Restart");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.setBackground(Color.lightGray);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }
                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                errorCount = 0;
                textLabel.setText("Errors: 0");
                shuffleCards();
                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }
                hideCardTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        //start dark
        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();
    }

    void hideCards() {
        if (gameReady && card1Selected != null && card2Selected != null)   {
            card1Selected.setIcon(cardBackImageIcon);
            card2Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected = null;
        }
        else {
            for (int i = 0; i < board.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }
    }

    void setupCards() {

        cardSet = new ArrayList<Card>();

        for (String cardName : cardList) {
            Image cardImg = new ImageIcon(getClass().getResource("./img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
            cardSet.add(new Card(cardName, cardImageIcon));
        }
        cardSet.addAll(cardSet);//doubles whats in the array list as we need pairs

        Image cardBackImage = new ImageIcon(getClass().getResource("./img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(cardBackImage.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }   

    void shuffleCards() {
        for (int i = 0; i < cardSet.size(); i++) {
            int j = (int) (Math.random() * cardSet.size());//random number between 0 and 19
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);
        }
    }
}
