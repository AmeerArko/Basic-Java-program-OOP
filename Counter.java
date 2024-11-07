import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

 public class Counter implements ActionListener {
    JFrame frame;
    JLabel label;
    JTextField t1;
    JButton b1,b2,b3;


    public Counter() {
        frame = new JFrame("Counter");
        frame.setSize(400, 300);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());




        label = new JLabel("Enter a number");
        frame.add(label);
        t1 = new JTextField(10);
        frame.add(t1);
        b1=new JButton("Count Up");
        frame.add(b1);
        b1.addActionListener(this);
        b2=new JButton("Count Down");
        frame.add(b2);
        b2.addActionListener(this);
        b3=new JButton("Reset");
        frame.add(b3);
        b3.addActionListener(this);



        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==b1) {
            t1.setText(String.valueOf(Integer.parseInt(t1.getText()) + 1));

        }
        if(e.getSource()==b2) {
            t1.setText(String.valueOf(Integer.parseInt(t1.getText()) - 1));
        }
        if(e.getSource()==b3) {
            t1.setText ("0");
        }
    }
    public static void main(String[] args) {
        new Counter();
    }


}


