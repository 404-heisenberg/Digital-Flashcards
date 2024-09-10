import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// Load Flashcard set and play it for the user
public class QuizCardPlayer {
    JFrame frame = new JFrame("Quiz Card Player");
    JPanel panel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JTextArea textArea = new JTextArea(10, 20);
    JButton next = new JButton("Show Answer");
    JButton unknownButton = new JButton("Unknown");
    JButton repeatSet = new JButton("Review Cards Again");
    JButton revUnkButton = new JButton("Review Unknown Cards");
    ArrayList<QuizCard> cardSet = new ArrayList<>();
    ArrayList<QuizCard> unknownCardSet = new ArrayList<>();
    ArrayList<QuizCard> tempSet = new ArrayList<>();
    int currCard = 0;

    public static void main(String[] Args) {
        QuizCardPlayer player = new QuizCardPlayer();
        player.go();
    }

    public void go() {
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        file.add(open);
        mb.add(file);

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(next);
        buttonPanel.add(unknownButton);
        buttonPanel.add(revUnkButton);
        buttonPanel.add(repeatSet);
        unknownButton.addActionListener(new UnknownButtonListener());
        revUnkButton.addActionListener(new reviewUnknownCardsListener());
        repeatSet.addActionListener(new repeatCardSetListener());
        unknownButton.setVisible(false);
        revUnkButton.setVisible(false);
        repeatSet.setVisible(false);

        Font bigFont = new Font("sanserif", Font.BOLD, 24);
        textArea.setFont(bigFont);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        open.addActionListener(new OpenMenuListener());
        next.addActionListener(new NextCardListener());

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(scroller);
        panel.add(buttonPanel);
        frame.setJMenuBar(mb);
        frame.getContentPane().add(panel);
    }


    // Show the next answer or question in the card set
    class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            revUnkButton.setVisible(false);
            repeatSet.setVisible(false);

            boolean isQuestion = false;
            // Check if the text area has the same question as the current card
            if (textArea.getText().equals(cardSet.get(currCard).getQuestion())) {
                isQuestion = true;
            }
            // If the tex area is displaying the question, show the answer & set the button text to "Show Question"
            if (isQuestion) {
                textArea.setText(cardSet.get(currCard).getAnswer());
                next.setText("Show Next Question");

                // Hide the "unknown" button
                unknownButton.setVisible(true);
            } else {
                // If the text area is displaying an answer, increment the current card and show the next question
                if (currCard < cardSet.size() - 1) {
                    currCard++;
                    textArea.setText(cardSet.get(currCard).getQuestion());
                    next.setText("Show Answer");

                    // Show a button that tags the card as "unknown"
                    unknownButton.setVisible(false);

                } else {
                    // If we are at the end of the card set, show a message and disable the button
                    textArea.setText("That was the last card!");
                    next.setVisible(false);
                    unknownButton.setVisible(false);
                    repeatSet.setVisible(true);

                    // Show the "review unknown cards" button, if there are any unknown cards
                    if (!unknownCardSet.isEmpty()) {
                        revUnkButton.setVisible(true);
                    } else {
                        unknownButton.setVisible(false);
                    }
                }
            }
        }
    }

    // Open the text file containing the saved que cards
    class OpenMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Empty the previous card set and reset the current card
            if (!cardSet.isEmpty()){
                cardSet.clear();
                currCard = 0;
                next.setEnabled(true);
                next.setText("Show Answer");
            }
            // Bring up a file dialog box
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(frame);
            // Let the user navigate to and choose a card set to open
            loadFile(fileChooser.getSelectedFile());
            // Display the question of the first card
            textArea.setText(cardSet.get(0).getQuestion());
            // Set the current card to 0, because we have just shown the first card
        }
    }

    private void loadFile(File file) {
        // Must build an ArrayList of cards, by reading them from a file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            // Read the file one line at a time
            while ((line = reader.readLine()) != null){
                makeCard(line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Couldn't read the card file");
            e.printStackTrace();
        }

    }

    private void makeCard(String line) {
        // Separate the line into two parts: question and answer
        String[] result = line.split("/");
        // Create a new QuizCard object and add it to the ArrayList
        QuizCard card = new QuizCard(result[0], result[1]);
        cardSet.add(card);
        // Randomize the card set
        randomCardSet();
    }

    // Randomize the card set
    private void randomCardSet() {
        ArrayList<QuizCard> randomCardSet = new ArrayList<>();
        while (!cardSet.isEmpty()) {
            Random rand = new Random();
            int randCard = rand.nextInt(cardSet.size());
            randomCardSet.add(cardSet.get(randCard));
            cardSet.remove(randCard);
        }
        cardSet = randomCardSet;
        tempSet = cardSet;
    }

    // Tag the current card as "unknown"
    class UnknownButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Add the current card to the unknown card set if it is not already there
            if (!unknownCardSet.contains(cardSet.get(currCard))){
                unknownCardSet.add(cardSet.get(currCard));
            }
        }
    }
 
    class reviewUnknownCardsListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // Review the unknown cards set
            randUnkCardSet();
            cardSet = unknownCardSet;
            newForm();
        }
    }

    class repeatCardSetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            cardSet = tempSet;
            newForm();
        }
    }

    // Create a new form with the question of the first quiz card in the card set
    private void newForm() {
        currCard = 0;
        textArea.setText(cardSet.get(currCard).getQuestion());
        // Enable the "next" button
        next.setVisible(true);
        next.setEnabled(true);
        next.setText("Show Answer");
        // Hide the "unknown" button and override its action listener to tag the card as "unknown" again
        unknownButton.setVisible(false);
        revUnkButton.setVisible(false);
        repeatSet.setVisible(false);
    }
    
    // Randomize the unknown card set
    private void randUnkCardSet() {
        ArrayList<QuizCard> randomCardSet = new ArrayList<>();
        while (!unknownCardSet.isEmpty()) {
            Random rand = new Random();
            int randCard = rand.nextInt(unknownCardSet.size());
            randomCardSet.add(unknownCardSet.get(randCard));
            unknownCardSet.remove(randCard);
        }
        unknownCardSet = randomCardSet;
    }
}
