import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

class Trans {

    static Node activeUser;
    static Node mobileCompany, fasTagCompany, broadbandCompany, DTHCompany,
            electricityCompany, waterCompany;
    static HashSet<Node> users = new HashSet<>();
    static Scanner sc = new Scanner(System.in);
    static String dburl = "jdbc:mysql://localhost:3306/upi users";
    static String dbuser = "root";
    static String dbpass = "";

    // Get Data From Database --------------------------------------------------

    static void getUsers() throws SQLException {

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/upi users", "root", "");
        Statement getUsers = con.createStatement();
        ResultSet allUsers = getUsers.executeQuery("SELECT * FROM USERS;");

        while (allUsers.next()) {

            Node n = new Node(allUsers.getString(1), allUsers.getString(2),
                    allUsers.getLong(3),
                    allUsers.getLong(4), allUsers.getString(5), allUsers.getString(6),
                    allUsers.getString(7), allUsers.getString(8), allUsers.getDouble(10));
            users.add(n);

            if (activeUser != null && n.upiId.equals(activeUser.upiId)) {
                activeUser = n;
            } else if ("mobileCompany@oksbi".equals(n.upiId)) {
                mobileCompany = n;
            } else if ("fasTagCompany@oksbi".equals(n.upiId)) {
                fasTagCompany = n;
            } else if ("broadbandCompany@oksbi".equals(n.upiId)) {
                broadbandCompany = n;
            } else if ("DTHCompany@oksbi".equals(n.upiId)) {
                DTHCompany = n;
            } else if ("electricityCompany@oksbi".equals(n.upiId)) {
                electricityCompany = n;
            } else if ("waterCompany@oksbi".equals(n.upiId)) {
                waterCompany = n;
            }
        }

    }

    // SignUp or Log In --------------------------------------------------

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println();
        getUsers();
        for (;;) {
            System.out.println("\u001B[34m        GREETINGS\u001B[0m");
            System.out.println();
            System.out.println("1.Open A New Account");
            System.out.println("2.Already Have An Account");
            System.out.println("3.Exit");

            System.out.print("Enter Your Choice : ");
            int choice = sc.nextInt();
            try {
                switch (choice) {
                    case 1:
                        System.out.println();
                        System.out.println("-----ENTER YOUR CREDENTIALS----- \n");

                        try {
                            signUp();
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Input Type.");
                            signUp();
                        }

                        System.out.println(
                                "\u001B[32mAccount Created Successfully.\u001B[32m\nLoggedIn As " + activeUser.name
                                        + "\u001B[0m\n");

                        display();
                        System.out.println();

                        break;

                    case 2:
                        System.out.println();
                        System.out.println("------ENTER YOUR CREDENTIALS------ \n");

                        if (!logIn()) {
                            System.out.println("Log In Failed.");
                            System.out.print("\n\n");
                            main(args);
                        }

                        break;
                    case 3:
                        System.out.println("\u001B[34m        THANK YOU\u001B[0m");

                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid Choice,Try Again");
                        main(args);
                }
            } catch (InputMismatchException e) {
                System.out.println("Ivalid Input Type.");
            }
            // Code For All Functionalities
            insideTheApp();

        }
    }
    // User Authentication --------------------------------------------------

    static void signUp() throws SQLException {
        System.out.print("Enter Name : ");
        String name = sc.nextLine();

        for (;;) {

            System.out.print("\nEnter Mobile Number : ");
            long mobileNumber = sc.nextLong();

            if (validateMobileNumber(mobileNumber)) {

                for (;;) {

                    System.out.println("\nOnly Gmail Accounts are accepted.");
                    System.out.print("Enter Mail Id : ");
                    String mailId = sc.next();

                    if (validateMailId(mailId)) {
                        System.out.print("\nEnter Bank Name : ");
                        String bank = sc.next();
                        System.out.print("\nEnter Initial Balance : ");
                        long balance = sc.nextLong();
                        System.out.print("\nEnter Initial Wallet Balance : ");
                        Double wallet1 = sc.nextDouble();
                        for (;;) {

                            System.out.println(
                                    "\nPassword must be of 6 characters only.\nNo spaces are allowed in the password.\nThis Password Will Be Asked Before Each Transaction Occurs.\n");
                            System.out.print("Enter password : ");
                            String password21 = sc.next();
                            System.out.println();

                            if (validatePassword(password21, 6)) {
                                System.out.print("Confirm Password : ");
                                String password22 = sc.next();

                                if (password22.equals(password21)) {
                                    for (;;) {
                                        System.out.println("Password Set Successfully.");
                                        System.out.println(
                                                "\n" + "Password must be of 4 characters only.\nNo spaces are allowed in the password.\nThis Password Will Be Asked For Logging In.\n");

                                        System.out.print("Create Log In Password : ");
                                        String password11 = sc.next();

                                        if (validatePassword(password11, 4)) {
                                            System.out.print("Confirm Password : ");
                                            String password12 = sc.next();
                                            System.out.println("\n\n");

                                            if (password12.equals(password11)) {

                                                Node n = new Node(generateUpiId(mailId, bank), name, mobileNumber,
                                                        balance, mailId, bank, password11, password21, wallet1);
                                                users.add(n);// So the newly added user also gets added in users hashSet
                                                activeUser = n;
                                                Connection con = DriverManager
                                                        .getConnection("jdbc:mysql://localhost:3306/upi users",
                                                                "root",
                                                                "");
                                                Statement insertNewUser = con.createStatement();
                                                insertNewUser
                                                        .execute(
                                                                "INSERT INTO USERS(upiId, name, mobileNumber, balance, mail_Id, bank,password1, password2,wallet) VALUES('"
                                                                        + generateUpiId(mailId, bank)
                                                                        + "','"
                                                                        + name
                                                                        + "',"
                                                                        + mobileNumber
                                                                        + "," + balance + ",'" + mailId + "','" + bank
                                                                        + "','" + password11 + "','" + password22
                                                                        + "'," + wallet1 + ");");
                                                // Node inserted into users table
                                                return;

                                            } else {
                                                System.out.println("password 1 and 2 do not match.\n");
                                            }
                                        } else {
                                            System.out.println("Invalid password type.\nTry again\n");

                                        }
                                    }
                                } else {
                                    System.out.println("password 1 and 2 do not match.\n");
                                }
                            }

                            else {
                                System.out.println("Invalid password type.\nTry again\n");
                            }
                        }

                    } else {
                        System.out.println("Invalid Mail Id.");
                        System.out.println("Try Entering With some Other Id.");
                    }
                }

            } else {
                System.out.println("Enter A Valid mobile number.");
            }
        }
    }

    static boolean validateMobileNumber(long mobileNumber) {// To check if mobile number entered is valid or not.
        if (mobileNumber >= 1000000000l && mobileNumber <= 9999999999l) {
            return true;
        }
        return false;
    }

    static boolean validatePassword(String password, int length) {
        if (password.toCharArray().length != length)
            return false;
        else if (password.contains(" "))
            return false;

        return true;
    }

    static boolean validateMailId(String mailId) {
        if (mailId.contains("@gmail.com"))
            return true;
        return false;
    }

    static String generateUpiId(String mailId, String bank) {
        return mailId.replace("gmail.com", "ok" + bank);
    }

    // Log In

    static boolean logIn() throws SQLException {
        if (validateAccount()) {
            System.out.println("\u001B[32mLogged in successfully As " + activeUser.name +
                    "\u001B[0m");
            return true;
        } else {

            System.out.println("Invalid Account");
            return false;
        }
    }

    static boolean validateAccount() throws SQLException {

        for (int i = 1; i <= 3; i++) {

            System.out.print("Enter 4-digit password : ");
            String password1 = sc.next();

            System.out.print("Enter UPI Id : ");
            String upiId = sc.next();
            for (Node user : users) {
                if (user.upiId.equals(upiId)) {
                    if (password1.equals(user.password1)) {

                        activeUser = user;
                        return true;
                    }

                    else {

                        System.out.println("1.Forgot Password\n2.Try again");
                        switch (sc.nextInt()) {
                            case 1:
                                System.out.print("Enter Mobile No : ");
                                try {
                                    long mobileNumber = sc.nextLong();
                                    if (user.mobileNumber == mobileNumber) {
                                        if (forgotPassword(mobileNumber, user)) {
                                            logIn();
                                        }
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid Input Type.");
                                }
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Enter a valid choice.");
                        }
                    }
                }
            }
        }
        System.out.println("Invalid User Id or password.");
        System.out.println();
        return false;
    }

    static boolean forgotPassword(long mobileNumber, Node user) throws SQLException {
        System.out.println("Password must be of 6 characters only.\nNo spaces are allowed in the password.");
        System.out.println("Enter New Password : ");
        String password2 = sc.next();
        if (validatePassword(password2, 6)) {
            System.out.print("Confirm Password : ");
            String password22 = sc.next();

            System.out.println();
            if (password2.equals(password22)) {

                user.password2 = password22;// Change password
                System.out.println("Password changed succesfully.");
                Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
                Statement updatePassword = con.createStatement();
                updatePassword.execute(
                        "UPDATE USERS SET PASSWORD2 = '" + password2 + "' WHERE MOBILE_NUMBER = " +
                                mobileNumber);

                users.clear();
                getUsers();
                return true;

            } else {
                System.out.println("Password 1 and 2 do not match.");
            }

        } else {
            System.out.println("Password criteria is not fulfilled");
        }

        System.out.println("Password updation failed.");
        return false;

    }
    // Inside The App --------------------------------------------------

    static void insideTheApp() throws Exception {
        for (;;) {
            System.out.println();
            String password2;
            System.out.print("\u001B[32mWELCOME , " + activeUser.name + "\u001B[0m\n");
            System.out.println("1.Pay Someone");
            System.out.println("2.Display Account Information");
            System.out.println("3.View Balance");
            System.out.println("4.Delete Account");
            System.out.println("5.View Transactions History");
            System.out.println("6.Pay Bills");
            System.out.println("7.Notifications");
            System.out.println("8.Add To Wallet");
            System.out.println("9.Exit");

            System.out.print("Enter Your Choice : ");

            int choice = sc.nextInt();
            System.out.println();

            switch (choice) {

                case 1:// code to pay someone
                    users.clear();
                    getUsers();
                    System.out.print("Enter Profile UpiId To Be Paid To : ");
                    String upiId1 = sc.next();

                    for (Node user : users) {
                        if (upiId1.equals(user.upiId)) {
                            System.out.print("Enter 6-Digit Password : ");
                            password2 = sc.next();
                            System.out.println();
                            System.out.print("Enter Amount To Be Sent To " + user.name + " : ");
                            double amount = sc.nextInt();

                            if (activeUser.password2.equals(password2)) {
                                paySomeone(user, amount);
                            } else {
                                System.out.println("\u001B[31mInvalid Password.\u001B[0m");
                            }
                            break;
                        }
                    }
                    break;

                case 2:// code to display acc info
                    users.clear();
                    getUsers();
                    System.out.print("Enter 6-Digit Password : ");
                    password2 = sc.next();
                    System.out.println();
                    if (activeUser.password2.equals(password2)) {
                        display();
                    }
                    break;

                case 3:
                    users.clear();
                    getUsers();
                    System.out.print("Enter 6-Digit Password : ");
                    password2 = sc.next();
                    System.out.println();
                    if (activeUser.password2.equals(password2)) {
                        System.out.println("Balance : " + activeUser.balance);
                        System.out.println("wallet Balance : " + activeUser.wallet);

                    }
                    break;
                case 4:
                    System.out.print("""
                            Once You Delete This Account, All It's Related data Will Also BE Deleted.
                            Enter 6-Digit Password : """);
                    password2 = sc.next();
                    System.out.println();
                    if (activeUser.password2.equals(password2)) {
                        deleteAccount();
                    }

                    main(null);
                    break;
                case 5:

                    System.out.print("Enter 6-Digit Password : ");
                    password2 = sc.next();
                    System.out.println();
                    if (activeUser.password2.equals(password2)) {
                        viewTransactionHistory();
                    }

                    users.clear();
                    getUsers();
                    break;
                case 6:
                    payBills();
                    users.clear();
                    getUsers();
                    break;
                case 7:
                    notifications();
                    break;
                case 8:
                    addToWallet();
                    break;
                case 9:
                    System.out.println("\u001B[34m        THANK YOU\u001B[0m\n\n");
                    System.exit(0);
                default:
                    System.out.println("Invalid Choice, Try Again.");
            }

        }
    }

    // Pay Someone--------------------------------------------------

    static void addToWallet() throws SQLException, IOException {
        System.out.print("\nEnter Initial Wallet Balance : ");
        Double wallet1 = sc.nextDouble();
        System.out.print("Enter 6-Digit Password : ");
        String password2 = sc.next();
        System.out.println();
        long setBalance = (long) (activeUser.balance - wallet1);
        if (activeUser.password2.equals(password2)) {
            if (activeUser.balance >= wallet1) {
                Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
                PreparedStatement addWallet = con
                        .prepareStatement("UPDATE USERS SET wallet = ? , balance = ? WHERE UPIID = ?;");
                addWallet.setDouble(1, activeUser.wallet + wallet1);
                addWallet.setLong(2, setBalance);
                addWallet.setString(3, activeUser.upiId);
                addWallet.execute();

                messageFromWallet(activeUser, wallet1);
                saveWalletUpdateToFile(activeUser, wallet1);
                System.out.println("\n\u001B[32m" + wallet1 + " Added Successfully\u001B[0m");
            } else {
                System.out.println(
                        "\n\u001B[31mNo Sufficient Balance In Wallet For The Transaction.\nPaymet Was NotSuccessfull.\u001B[0m");

            }
        }
    }

    static void paySomeone(Node profile, double amount) throws SQLException,
            IOException {
        System.out.println("1.Pay From Account");
        System.out.println("2.Pay From wallet");
        System.out.println("3.Exit");
        System.out.print("Enter Your Choice : ");
        switch (sc.nextInt()) {
            case 1:
                payfromAccount(profile, amount);
                return;
            case 2:
                payfromWallet(profile, amount);
                return;
            case 3:
                return;

            default:
                System.out.println("Invalid Choice.");
        }

    }

    static void payfromWallet(Node profile, double amount) throws SQLException, IOException {
        if (activeUser.wallet >= amount) {
            Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
            PreparedStatement updatewallet = con.prepareStatement("UPDATE USERS SET balance = ? WHERE UPIID = ?;");

            updatewallet.setDouble(1, profile.balance + amount);
            updatewallet.setString(2, profile.upiId);
            updatewallet.executeUpdate();
            saveTransactionToFile(activeUser, profile, amount);
            System.out.println("\u001B[32m\nAmount " + amount + " Sent Successfully To "
                    + profile.name + ".\u001B[0m");
            PreparedStatement updatewallet1 = con.prepareStatement("UPDATE USERS SET wallet = ? WHERE UPIID = ?;");

            updatewallet1.setDouble(1, activeUser.wallet - amount);
            updatewallet1.setString(2, activeUser.upiId);
            updatewallet1.execute();
            message(activeUser, profile, amount);
            users.clear();
            getUsers();
        } else {
            System.out.println(
                    "\n\u001B[31mNo Sufficient Balance In Wallet For The Transaction.\nPaymet Was NotSuccessfull.\u001B[0m");
        }
    }

    static void payfromAccount(Node profile, double amount) throws SQLException, IOException {
        if (activeUser.balance >= amount) {
            Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
            PreparedStatement updateBalance = con.prepareStatement("UPDATE USERS SET BALANCE = ? WHERE UPIID = ?;");

            updateBalance.setDouble(1, profile.balance + amount);
            updateBalance.setString(2, profile.upiId);
            updateBalance.executeUpdate();
            saveTransactionToFile(activeUser, profile, amount);
            System.out.println("\u001B[32m\nAmount " + amount + " Sent Successfully To "
                    + profile.name + ".\u001B[0m");

            updateBalance.setDouble(1, activeUser.balance - amount);
            updateBalance.setString(2, activeUser.upiId);
            updateBalance.execute();
            message(activeUser, profile, amount);
            users.clear();
            getUsers();
        } else {
            System.out.println(
                    "\n\u001B[31mNo Sufficient Balance For The Transaction.\nPaymet Was NotSuccessfull.\u001B[0m");
        }
    }
    // Display --------------------------------------------------

    static void display() {
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();

        for (int i = 0; i <= 14; i++) {
            System.out.print(" ");
        }

        System.out.print("YOUR ACCOUNT");

        for (int i = 0; i <= 14; i++) {
            System.out.print(" ");
        }

        System.out.println();

        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();
        System.out.println("UPI Id : " + activeUser.upiId);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();

        System.out.println("Name : " + activeUser.name);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();

        System.out.println("Mobile Number : " + activeUser.mobileNumber);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();

        System.out.println("Mail ID : " + activeUser.mailId);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();

        System.out.println("Bank : " + activeUser.bank);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();

        System.out.println("Balance : " + activeUser.balance);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }

        System.out.println();
        System.out.println("Wallet Balance : " + activeUser.wallet);
        for (int i = 0; i <= 40; i++) {
            System.out.print("-");
        }
        System.out.println();

    }

    
    
    
    
    
    
    
    
    
    
    
    
    // Delete Account --------------------------------------------------

    static void deleteAccount() throws SQLException {
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        PreparedStatement delete = con.prepareStatement("DELETE FROM USERS WHERE UPIID = ?;");

        delete.setString(1, activeUser.upiId);
        File fileToDelete = new File(activeUser.upiId + ".txt");
        fileToDelete.delete();
        delete.executeUpdate();
        System.out.println("Account And All It's Data Deleted Successfully.");
    }

    // Save Transaction History To File ------------------------------------

    static void saveTransactionToFile(Node sender, Node reciever, double amount)
            throws IOException {

        BufferedWriter send = new BufferedWriter(new FileWriter(new File(sender.upiId
                + ".txt"), true));
        send.write("[" + new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()) + "]" + amount
                + " Paid To " + reciever.upiId + " (" + reciever.name + ")");
        send.newLine();
        send.close();
        BufferedWriter rec = new BufferedWriter(new FileWriter(new File(reciever.upiId + ".txt"), true));
        rec.write("[" + new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()) + "] " + amount
                + " Recieved From " + sender.upiId + " (" + sender.name + ")");
        rec.newLine();
        rec.close();

    }

    static void saveWalletUpdateToFile(Node sender, double amount) throws IOException {
        BufferedWriter send = new BufferedWriter(new FileWriter(new File(sender.upiId + ".txt"), true));
        send.write("[" + new java.sql.Timestamp(new Date(System.currentTimeMillis()).getTime()) + "]" + amount
                + " Added To Wallet.");
        send.newLine();
        send.close();
    }

    static void viewTransactionHistory() throws IOException {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(activeUser.upiId + ".txt")));
            String data = br.readLine();
            for (int i = 0; i <= 100; i++) {
                System.out.print("-");
            }
            System.out.println();
            for (int i = 1; i <= 40; i++) {
                System.out.print(" ");
            }
            System.out.print("TRANSACTIONS HISTORY");
            for (int i = 1; i <= 41; i++) {
                System.out.print(" ");
            }
            System.out.println();
            for (int i = 0; i <= 100; i++) {
                System.out.print("-");
            }

            while (data != null) {
                if (data.contains("Recieved") || data.contains("Added")) {
                    System.out.print("\n\u001B[32m" + data + "\u001B[0m\n");
                } else {
                    System.out.print("\n\u001B[31m" + data + "\u001B[0m\n");
                }
                data = br.readLine();
                for (int i = 0; i <= 100; i++) {
                    System.out.print("-");
                }
            }
            System.out.println();
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("\u001B[31mNo Transactions Made Yet.\u001B[0m");
        }
    }

    // Pay Bills --------------------------------------------------

    static void payBills() throws SQLException, IOException {
        for (;;) {
            System.out.println("-----Pay Bills-----");
            System.out
                    .println(
                            "1.Mobile Recharge\n2.FasTag\n3.BroadBand\n4.DTH Services\n5.Electricity\n6.Water\n7.Return");
            System.out.print("Enter Your Choice : ");
            switch (sc.nextInt()) {
                case 1:

                    mobileRecharge();
                    users.clear();
                    getUsers();
                    break;
                case 2:
                    fasTag();
                    users.clear();
                    getUsers();
                    break;
                case 3:
                    broadband();
                    users.clear();
                    getUsers();
                    break;
                case 4:
                    DTH();
                    users.clear();
                    getUsers();
                    break;
                case 5:
                    electricity();
                    users.clear();
                    getUsers();
                    break;
                case 6:
                    water();
                    users.clear();
                    getUsers();
                    break;
                case 7:
                    return;

            }
        }
    }

    static void mobileRecharge() throws SQLException, IOException {

        System.out.print("Enter Mobile Number : ");
        long mobileNumber = sc.nextLong();
        System.out.println();
        if (validateMobileNumber(mobileNumber)) {

            System.out.print("Enter Recharge Amount : ");
            double amount = sc.nextDouble();

            System.out.print("Enter 6-Digit Password : ");
            String password2 = sc.next();
            System.out.println();
            if (activeUser.password2.equals(password2)) {
                paySomeone(mobileCompany, amount);

                System.out.print("\u001B[32mRecharge of " + amount + "rs For " + mobileNumber
                        + " Is Done Successfully.\u001B[0m \n\n");
            }
        } else {
            System.out.println("Invalid Mobile Number\n\n");
        }
    }

    static void fasTag() throws SQLException, IOException {
        System.out.print("Enter Vehicle Number (As AB01AB1234) : ");
        String vehicle = sc.next();

        if (validateVehicle(vehicle)) {

            System.out.print("Enter Recharge Amount : ");
            double amount = sc.nextDouble();

            System.out.print("Enter 6-Digit Password : ");
            String password2 = sc.next();
            System.out.println();
            if (activeUser.password2.equals(password2)) {

                paySomeone(fasTagCompany, amount);

                System.out.print(
                        "\u001B[32mRecharge of " + amount + "rs For " + vehicle
                                + " Is Done Successfully.\u001B[0m \n\n");
            }
        } else {
            System.out.println("Invalid Vehicle Number\n\n");
        }
    }

    static boolean validateVehicle(String vehicle) {

        return true;
    }

    static void broadband() throws IOException, SQLException {// Cannot Validate Account No
        System.out.print("Enter UserID : ");
        String accNo = sc.next();
        System.out.print("Enter Recharge Amount : ");
        double amount = sc.nextDouble();

        System.out.print("Enter 6-Digit Password : ");
        String password2 = sc.next();
        System.out.println();
        if (activeUser.password2.equals(password2)) {
            paySomeone(broadbandCompany, amount);

            System.out
                    .print("\u001B[32mRecharge of " + amount + "rs For " + accNo
                            + " Is Done Successfully.\u001B[0m \n\n");
        }
    }

    static void DTH() throws IOException, SQLException {// Cannot Validate Account No
        System.out.print("Enter UserID : ");
        String accNo = sc.next();
        System.out.print("Enter Recharge Amount : ");
        double amount = sc.nextDouble();

        System.out.print("Enter 6-Digit Password : ");
        String password2 = sc.next();
        System.out.println();
        if (activeUser.password2.equals(password2)) {
            paySomeone(DTHCompany, amount);

            System.out
                    .print("\u001B[32mRecharge of " + amount + "rs For " + accNo
                            + " Is Done Successfully.\u001B[0m \n\n");
        }
    }

    static void electricity() throws IOException, SQLException {
        System.out.print("Enter UserID : ");
        String accNo = sc.next();
        System.out.print("Enter Recharge Amount : ");
        double amount = sc.nextDouble();

        System.out.print("Enter 6-Digit Password : ");
        String password2 = sc.next();
        System.out.println();
        if (activeUser.password2.equals(password2)) {
            paySomeone(electricityCompany, amount);

            System.out
                    .print("\u001B[32mRecharge of " + amount + "rs For " + accNo
                            + " Is Done Successfully.\u001B[0m \n\n");
        }
    }

    static void water() throws IOException, SQLException {
        System.out.print("Enter Customer Number : ");
        String accNo = sc.next();
        System.out.print("Enter Recharge Amount : ");
        double amount = sc.nextDouble();
        System.out.print("Enter 6-Digit Password : ");
        String password2 = sc.next();
        System.out.println();
        if (activeUser.password2.equals(password2)) {
            paySomeone(waterCompany, amount);

            System.out
                    .print("\u001B[32mRecharge of " + amount + "rs For " + accNo
                            + " Is Done Successfully. \u001B[0m\n\n");
        }
    }

    // Meassages And Notifications ------------------------------------------

    static void message(Node sender, Node reciever, double amount) throws IOException, SQLException {// As New Message
        // is Recieved,Old
        // Gets Removed
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);

        PreparedStatement message = con.prepareStatement("UPDATE USERS SET Notifications = ? WHERE UPIID = ?");
        message.setString(1, amount + " Recieved From " + sender.upiId + "(" +
                sender.name + ")");
        message.setString(2, reciever.upiId);
        message.executeUpdate();
    }

    static void messageFromWallet(Node activeUser, Double amount) throws SQLException {
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);

        PreparedStatement message = con.prepareStatement("UPDATE USERS SET Notifications = ? WHERE UPIID = ?");
        message.setString(1, amount + " Added To Wallet.");
        message.setString(2, activeUser.upiId);
        message.executeUpdate();
    }

    static void notifications() throws Exception {
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);

        PreparedStatement notification = con.prepareStatement("SELECT NOTIFICATIONs FROM USERS WHERE UPIID = ?");
        notification.setString(1, activeUser.upiId);
        ResultSet message1 = notification.executeQuery();

        while (message1.next() && message1 != null) {

            try {
                if (message1.getString(1).contains("Recieved")) {
                    System.out.print("\n\u001B[32m" + message1.getString(1) + "\u001B[0m\n");
                } else {
                    System.out.print("\n\u001B[31m" + message1.getString(1) + "\u001B[0m\n");
                }
            } catch (NullPointerException e) {
                System.out.println("\u001B[31mHave'nt Recieved Any Notification Yet.\u001B[0m");
            }
        }
    }
}
// User Node --------------------------------------------------

class Node {

    String upiId;
    String name;
    long mobileNumber;
    double balance;
    String mailId;
    String bank;
    String password1;// 4-Digit
    String password2;// 6_Digit
    double wallet;

    Node(String upiId, String name, long mobileNumber, long balance, String mailId, String bank, String password1,
            String password2, double wallet) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.upiId = upiId;// generate as dumbo@bankName
        this.mailId = mailId;
        this.balance = balance;
        this.bank = bank;
        this.password1 = password1;
        this.password2 = password2;
        this.wallet = wallet;
    }
}