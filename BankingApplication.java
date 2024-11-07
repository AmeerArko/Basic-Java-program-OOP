import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class BankingApplication {
    private JFrame frame;
    private JTextField accountNumberField;
    private JPasswordField passwordField;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton checkBalanceButton;
    private JButton loginButton;
    private JTextArea transactionHistoryArea;
    private JLabel balanceLabel;
    private JLabel transactionHistoryLabel;

    private BankAccount[] accounts = new BankAccount[3];
    private int currentAccountId = -90;

    public BankingApplication() {
        frame = new JFrame("Banking Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Create account array with initial values
        accounts[0] = new BankAccount("Account 1", 0, 1000);
        accounts[1] = new BankAccount("Account 2", 1, 2000);
        accounts[2] = new BankAccount("Account 3", 2, 3000);

        // Create GUI components
        JLabel accountNumberLabel = new JLabel("Account Number:");
        frame.add(accountNumberLabel);
        accountNumberField = new JTextField(10);
        frame.add(accountNumberField);

        JLabel passwordLabel = new JLabel("Password:");
        frame.add(passwordLabel);
        passwordField = new JPasswordField(10);
        frame.add(passwordField);

        JLabel amountLabel = new JLabel("Amount:");
        frame.add(amountLabel);
        amountField = new JTextField(10);
        frame.add(amountField);

        depositButton = new JButton("Deposit");
        depositButton.addActionListener(new DepositActionListener());
        frame.add(depositButton);

        withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(new WithdrawActionListener());
        frame.add(withdrawButton);

        checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.addActionListener(new CheckBalanceActionListener());
        frame.add(checkBalanceButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        frame.add(loginButton);

        transactionHistoryArea = new JTextArea(10, 20);
        transactionHistoryArea.setBackground(Color.WHITE);
        transactionHistoryArea.setForeground(Color.BLACK);
        frame.add(new JScrollPane(transactionHistoryArea));

        balanceLabel = new JLabel("Balance: 0.00 TAKA");
        frame.add(balanceLabel);

        transactionHistoryLabel = new JLabel("Transaction History:");
        frame.add(transactionHistoryLabel);

        frame.getContentPane().setBackground(Color.ORANGE);

        frame.pack();
        frame.setVisible(true);
    }

    private class DepositActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentAccountId != -90) {
                double amount = Double.parseDouble(amountField.getText());
                try {
                    accounts[currentAccountId].addTransaction("Deposited TAKA" + String.format("%.2f", amount));
                    accounts[currentAccountId].setBalance(accounts[currentAccountId].getBalance() + amount);
                    updateTransactionHistory();
                    balanceLabel.setText("Balance: " + String.format("%.2f", accounts[currentAccountId].getBalance())+"TAKA");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please login first");
            }
        }
    }

    private class WithdrawActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentAccountId != -90) {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > accounts[currentAccountId].getBalance()) {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance");
                } else {
                    try {
                        accounts[currentAccountId].addTransaction("Withdrew " + String.format("%.2f", amount)+"TAKA");
                        accounts[currentAccountId].setBalance(accounts[currentAccountId].getBalance() - amount);
                        updateTransactionHistory();
                        balanceLabel.setText("Balance: " + String.format("%.2f", accounts[currentAccountId].getBalance())+"TAKA");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please login first");
            }
        }
    }

    private class CheckBalanceActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentAccountId != -90) {
                balanceLabel.setText("Balance: " + String.format("%.2f", accounts[currentAccountId].getBalance())+"TAKA");
            } else {
                JOptionPane.showMessageDialog(frame, "Please login first");
            }
        }
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String password = new String(passwordField.getPassword());
                int accountId = Integer.parseInt(accountNumberField.getText());
                if (accounts[accountId].getPassword().equals(password)) {
                    currentAccountId = accountId;
                    updateTransactionHistory();
                    balanceLabel.setText("Balance: " + String.format("%.2f", accounts[currentAccountId].getBalance())+"TAKA");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid account number or password");
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid account number");
            }
        }
    }

    private void updateTransactionHistory() {
        try {
            transactionHistoryArea.setText(accounts[currentAccountId].getTransactionHistory());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        new BankingApplication();
    }
}

class BankAccount {
    private String accountNumber;
    private String password;
    private double balance;
    private List<String> transactions;

    public BankAccount(String accountNumber, int id, double balance) {
        this.accountNumber = accountNumber;
        this.password = String.valueOf(id);
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTransaction(String transaction) throws IOException {
        transactions.add(transaction);
        // Write transaction to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\arkoa\\OneDrive\\Desktop\\FILE\\transactions.txt", true))) {
            writer.write(transaction + "\n");
        }
    }

    public String getTransactionHistory() throws IOException {

        StringBuilder history = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\arkoa\\OneDrive\\Desktop\\FILE\\transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        }
        return history.toString();
    }

    public int getId() {
        return Integer.parseInt(password);
    }
}