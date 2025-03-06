import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

public class ConverterFinal {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Currency Converter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(20, 20, 80, 30);
        JTextField amountTextField = new JTextField("0");
        amountTextField.setBounds(100, 20, 100, 30);

        JLabel fromCurrencyLabel = new JLabel("From Currency:");
        fromCurrencyLabel.setBounds(20, 60, 100, 30);
        String[] currencies = {"USD", "INR", "EUR", "GBP", "JPY"};
        JComboBox<String> fromCurrencyComboBox = new JComboBox<>(currencies);
        fromCurrencyComboBox.setBounds(130, 60, 100, 30);

        JLabel toCurrencyLabel = new JLabel("To Currency:");
        toCurrencyLabel.setBounds(20, 100, 100, 30);
        JComboBox<String> toCurrencyComboBox = new JComboBox<>(currencies);
        toCurrencyComboBox.setBounds(130, 100, 100, 30);

        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(240, 60, 100, 30);

        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setBounds(20, 140, 60, 30);
        JLabel resultValueLabel = new JLabel("0.00");
        resultValueLabel.setBounds(80, 140, 100, 30);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double amount = Double.parseDouble(amountTextField.getText());
                    String fromCurrency = fromCurrencyComboBox.getSelectedItem().toString();
                    String toCurrency = toCurrencyComboBox.getSelectedItem().toString();

                    double conversionRate = getConversionRate(fromCurrency, toCurrency);
                    double result = amount * conversionRate;
                    insertConversionRecord(fromCurrency, toCurrency, amount, result);

                    DecimalFormat df = new DecimalFormat("#0.00");
                    resultValueLabel.setText(df.format(result));
                } catch (NumberFormatException ex) {
                    resultValueLabel.setText("Invalid input");
                }
            }
        });

        frame.setLayout(null);
        frame.add(amountLabel);
        frame.add(amountTextField);
        frame.add(fromCurrencyLabel);
        frame.add(fromCurrencyComboBox);
        frame.add(toCurrencyLabel);
        frame.add(toCurrencyComboBox);
        frame.add(convertButton);
        frame.add(resultLabel);
        frame.add(resultValueLabel);

        frame.setVisible(true);
        ConverterFinal currencyConverter = new ConverterFinal();
        currencyConverter.display();
    }
    

    private static double getConversionRate(String fromCurrency, String toCurrency) {
        // Replace this with your currency conversion logic
        // For simplicity, we're using hardcoded rates
        //double conversionRate = 1.0;
        if (fromCurrency.equals("USD") && toCurrency.equals("EUR")) {
          return 0.85;
        } else if (fromCurrency.equals("USD") && toCurrency.equals("GBP")) {
           return  0.73;
        } else if (fromCurrency.equals("USD") && toCurrency.equals("JPY")) {
          return 110.0;
        }
            else if (fromCurrency.equals("USD") && toCurrency.equals("INR")) {
              return  83.26;
      
        }
        if (fromCurrency.equals("EUR") && toCurrency.equals("USD")) {
            return 1.05;
        } else if (fromCurrency.equals("EUR") && toCurrency.equals("GBP")) {
            return 0.87;
        } else if (fromCurrency.equals("EUR") && toCurrency.equals("JPY")) {
            return 157.41;
        }
        else if (fromCurrency.equals("EUR") && toCurrency.equals("INR")) {
            return 88.27;
        }
        
        if (fromCurrency.equals("GBP") && toCurrency.equals("EUR")) {
            return 1.15;
        } else if (fromCurrency.equals("GBP") && toCurrency.equals("USD")) {
            return 1.21;
        } else if (fromCurrency.equals("GBP") && toCurrency.equals("JPY")) {
            return 181.59;
        }
        else if (fromCurrency.equals("GBP") && toCurrency.equals("INR")) {
          return 101.57;
        }
        
        if (fromCurrency.equals("JPY")&& toCurrency.equals("EUR")) {
            return 0.0064;
        } else if (fromCurrency.equals("JPY") && toCurrency.equals("GBP")) {
            return 0.0055;
        } else if (fromCurrency.equals("JPY") && toCurrency.equals("USD")) {
            return 0.0067;
        }
        else if (fromCurrency.equals("JPY") && toCurrency.equals("INR")) {
          return 0.56;
          }
        
        if (fromCurrency.equals("INR") && toCurrency.equals("USD")) {
            return 0.012;
        } else if (fromCurrency.equals("INR") && toCurrency.equals("GBP")) {
            return 0.0098;
        } else if (fromCurrency.equals("INR") && toCurrency.equals("JPY")) {
            return 1.81;
        }
        else if (fromCurrency.equals("INR") && toCurrency.equals("EUR")) {
            return 0.011;
        }
        return 1.0;
    }
    private static void insertConversionRecord(String fromCurrency, String toCurrency, double amount, double result) {
        // Insert the conversion record into the database
        String url = "jdbc:mysql://localhost:3306/converter";
        String userName = "root";
        String password = "";

        try (Connection con = DriverManager.getConnection(url, userName, password)) {
            String sql = "INSERT INTO currency_converter (From_currency, To_currency, Amount, Result) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, fromCurrency);
                preparedStatement.setString(2, toCurrency);
                preparedStatement.setDouble(3, amount);
                preparedStatement.setDouble(4, result);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        
    }
    private void display() {
        try {
        	 Connection con = null;
              String url = "jdbc:mysql://localhost:3306/converter";
              String dbName = "converter";
              String userName = "root";
              String password = "";

            con = DriverManager.getConnection(url, userName, password);
            Statement st;

            st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM conversion_rates");

            while (rs.next()) {
                String fromCurrency = rs.getString("from_currency");
                String toCurrency = rs.getString("to_currency");
                double rate = rs.getDouble("rate");
                System.out.println(fromCurrency + " to " + toCurrency + " rate: " + rate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}