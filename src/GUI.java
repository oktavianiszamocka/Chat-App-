
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    Client client;
    JPanel join;
    JPanel chatting;
    JTextArea messageList;
    JTextArea message;
    JTextArea nick;
    JButton joinButton;
    JButton send;
    JButton quit;

    public GUI(Client client){
        super();
        setTitle("TPO3");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        joinPanel();

        setLocationRelativeTo(null);
        setVisible(true);
        this.client = client;

    }


    public void joinPanel(){
        join = new JPanel();
       // join.setVisible(true);
        join.setBackground(Color.CYAN);
        join.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        JLabel label = new JLabel("Enter your nickname");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new Insets(10,10,10,10);
        join.add(label, gridBagConstraints);

        nick = new JTextArea();
        nick.setPreferredSize(new Dimension(100, 20));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(10,10,10,10);
        join.add(nick, gridBagConstraints);

        joinButton = new JButton("JOIN");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(10,10,10,10);;
        join.add(joinButton, gridBagConstraints);

        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                join.setVisible(false);
                chattingPanel();
                client.connectServer();
                new Thread(client).start();

            }
        });
        this.add(join);
    }

    public void chattingPanel(){
        chatting = new JPanel();
        chatting.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        chatting.setBackground(Color.lightGray);

        messageList = new JTextArea();
        messageList.setPreferredSize(new Dimension(400, 400));
        messageList.setEditable(false);
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new Insets(10,10,2,10);
        chatting.add(messageList, gridBagConstraints );

        JScrollPane scrollPane = new JScrollPane(messageList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        chatting.add(scrollPane);
        JLabel label = new JLabel("Write a message");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        chatting.add(label, gridBagConstraints);


        message = new JTextArea();
        message.setPreferredSize(new Dimension(400, 30));
     //   gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        chatting.add(message, gridBagConstraints );


        send = new JButton("Send");
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.LINE_END;
        chatting.add(send, gridBagConstraints );
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessage(message.getText());
                message.setText("");
            }
        });

        quit = new JButton("QUIT");
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        chatting.add(quit, gridBagConstraints );
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.quit();
                GUI.this.setVisible(false);
            }
        });

        this.add(chatting );

    }

}
