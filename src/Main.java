import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankService();

        while (true) {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    bankService.createAccount();
                    break;

                case 2:
                    int acc = bankService.login();
                    if (acc != -1) {
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("\n--- Account Menu ---");
                            System.out.println("1. Deposit");
                            System.out.println("2. Withdraw");
                            System.out.println("3. Check Balance");
                            System.out.println("4. Transaction History");
                            System.out.println("5. Logout");
                            System.out.print("Enter your choice: ");
                            int loginChoice = sc.nextInt();
                            sc.nextLine(); // consume newline

                            switch (loginChoice) {
                                case 1:
                                    bankService.deposit(acc);
                                    break;
                                case 2:
                                    bankService.withdraw(acc);
                                    break;
                                case 3:
                                    bankService.checkBalance(acc);
                                    break;
                                case 4:
                                    bankService.transactionHistory(acc);
                                    break;
                                case 5:
                                    System.out.println("Logged out successfully!");
                                    loggedIn = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice! Try again.");
                            }
                        }
                    }
                    break;

                case 3:
                    System.out.println("Thank you for using Bank Management System!");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
