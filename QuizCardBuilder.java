import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Create and save a set of Flashcards
public class QuizCardBuilder {
    private JFrame frame;
    private JMenuBar menuBar;
    private JPanel panel;
    private JButton next;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private ArrayList<QuizCard> QuizCardSet;

    public static void main(String[] Args) {
        QuizCardBuilder builder = new QuizCardBuilder();
        builder.go();
    }

    public void go() {
        frame = new JFrame("Quiz Card Builder");
        panel = new JPanel();
        menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        next = new JButton("Next Card");
        next.addActionListener(new NextCardListener());
        QuizCardSet = new ArrayList<>();

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new SaveMenuListener());
        file.add(save);
        JMenuItem newCard = new JMenuItem("New");
        newCard.addActionListener(new NewMenuListener());
        file.add(newCard);

        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        JLabel answer = new JLabel("Answer:");
        answerArea = new JTextArea(10, 20);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setFont(bigFont);
        JScrollPane answerScroller = new JScrollPane(answerArea);
        answerScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        answerScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JLabel question = new JLabel("Question:");
        questionArea = new JTextArea(10, 20);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(bigFont);
        JScrollPane questionScroller = new JScrollPane(questionArea);
        questionScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        questionScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(question);
        panel.add(questionScroller);
        panel.add(answer);
        panel.add(answerScroller);
        panel.add(next);

        menuBar.add(file);
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setSize(500, 500);
    }

    // Empty the current quiz card set and clear the text fields
    public class NewMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // empty the current set of quiz cards and clear the text fields
            QuizCardSet.clear();
            clearCard();
        }
    }

    // SaveMenuListener the current quiz card set into a text file
    public class SaveMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // add the current card into the quiz card set
            QuizCard quizCard = new QuizCard(questionArea.getText(), answerArea.getText());
            QuizCardSet.add(quizCard);

            // save the quiz card set into a text file
            JFileChooser fileSave = new JFileChooser();
            fileSave.showSaveDialog(frame);
            saveFile(fileSave.getSelectedFile());
        }
    }

    // SaveMenuListener the current card to quiz card set and display a new quiz card
    public class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // add the current quiz card into the set of quiz cards
            QuizCard quizCard = new QuizCard(questionArea.getText(), answerArea.getText());
            QuizCardSet.add(quizCard);
            clearCard();
        }
    }

    // Empty the text area and focus on the question area
    private void clearCard() {
        answerArea.setText("");
        questionArea.setText("");
        questionArea.requestFocus();
    }

    private void saveFile(File file) {
        // save the current set of quiz cards into a text file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (QuizCard card : QuizCardSet) {
                writer.write(card.getQuestion() + "/"); // separate question and answer with a slash
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
