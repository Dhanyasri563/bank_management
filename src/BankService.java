import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class BankService {
    Scanner sc = new Scanner(System.in);

    // ---------------- Create Account ----------------
    public void createAccount() {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Your Name: ");
            String name = sc.nextLine();

            System.out.print("Set a 4-digit PIN: ");
            int pin = sc.nextInt();
            sc.nextLine(); // consume newline

            // Insert account into users table
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(name, pin, balance) VALUES(?,?,?)");
            ps.setString(1, name);
            ps.setInt(2, pin);
            ps.setDouble(3, 0.0); // initial balance

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Account Created Successfully!");
            }

        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }

    // ---------------- Login ----------------
    public int login() {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Account Number: ");
            int acc = sc.nextInt();
            System.out.print("Enter PIN: ");
            int pin = sc.nextInt();
            sc.nextLine(); // consume newline

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE account_no=? AND pin=?");
            ps.setInt(1, acc);
            ps.setInt(2, pin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful! Welcome " + rs.getString("name"));
                return acc;
            } else {
                System.out.println("Invalid Account Number or PIN!");
            }

        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return -1; // login failed
    }

    // ---------------- Deposit ----------------
    public void deposit(int acc) {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Amount to Deposit: ");
            double amt = sc.nextDouble();
            sc.nextLine(); // consume newline

            // Update balance
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET balance = balance + ? WHERE account_no=?");
            ps.setDouble(1, amt);
            ps.setInt(2, acc);
            ps.executeUpdate();

            // Add transaction record
            PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO transactions(account_no, trans_type, amount) VALUES(?,?,?)");
            ps2.setInt(1, acc);
            ps2.setString(2, "Deposit");
            ps2.setDouble(3, amt);
            ps2.executeUpdate();

            System.out.println("Deposit Successful! Amount: " + amt);

        } catch (Exception e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    // ---------------- Withdraw ----------------
    public void withdraw(int acc) {
        try {
            Connection con = DBConnection.getConnection();

            System.out.print("Enter Amount to Withdraw: ");
            double amt = sc.nextDouble();
            sc.nextLine(); // consume newline

            // Check current balance
            PreparedStatement ps = con.prepareStatement(
                    "SELECT balance FROM users WHERE account_no=?");
            ps.setInt(1, acc);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amt) {
                    // Update balance
                    PreparedStatement ps2 = con.prepareStatement(
                            "UPDATE users SET balance = balance - ? WHERE account_no=?");
                    ps2.setDouble(1, amt);
                    ps2.setInt(2, acc);
                    ps2.executeUpdate();

                    // Add transaction record
                    PreparedStatement ps3 = con.prepareStatement(
                            "INSERT INTO transactions(account_no, trans_type, amount) VALUES(?,?,?)");
                    ps3.setInt(1, acc);
                    ps3.setString(2, "Withdraw");
                    ps3.setDouble(3, amt);
                    ps3.executeUpdate();

                    System.out.println("Withdrawal Successful! Amount: " + amt);
                } else {
                    System.out.println("Insufficient Balance!");
                }
            }

        } catch (Exception e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }

    // ---------------- Check Balance ----------------
    public void checkBalance(int acc) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT balance FROM users WHERE account_no=?");
            ps.setInt(1, acc);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Your Current Balance: " + balance);
            }

        } catch (Exception e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
    }

    // ---------------- View Transaction History ----------------
    public void transactionHistory(int acc) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM transactions WHERE account_no=? ORDER BY trans_date DESC");
            ps.setInt(1, acc);
            ResultSet rs = ps.executeQuery();

            System.out.println("Transaction History:");
            System.out.println("ID | Type | Amount | Date");
            while (rs.next()) {
                System.out.println(rs.getInt("trans_id") + " | " +
                        rs.getString("trans_type") + " | " +
                        rs.getDouble("amount") + " | " +
                        rs.getTimestamp("trans_date"));
            }

        } catch (Exception e) {
            System.out.println("Error fetching transaction history: " + e.getMessage());
        }
    }
}
