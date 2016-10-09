import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by luke on 10/8/2016.
 */
public class SQLLoader {
    private Connection con;
    private Statement stmt;
    public SQLLoader() throws Exception{
       // List<Account> accountList = new ArrayList<Account>();
        String url = "jdbc:mysql://kc-sce-appdb01.kc.umkc.edu/lsg72";
        String userID = "lsg72";
        String password = "bMNLwflRlmmHhi58CaVD";
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(java.lang.ClassNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }

        con = DriverManager.getConnection(url,userID,password);
        stmt = con.createStatement();
    }

    public void cleanup() throws SQLException {
        // Close connection and statement
        // Connections, statements, and result sets are
        // closed automatically when garbage collected
        // but it is a good idea to free them as soon
        // as possible.
        // Closing a statement closes its current result set.
        // Operations that cause a new result set to be
        // created for a statement automatically close
        // the old result set.
        stmt.close();
        con.close();
    }

    public void createTables() throws SQLException { }
    public void updateTable() throws SQLException { }
    public Object[][] queryTable() throws SQLException {
        Vector<Account> accountVector = new Vector<>();
        String query = "Select * FROM cs349as7";
        int index = 0;
        try {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

               Account account = new Account();
                account.setAccountBalance(rs.getInt("Account_Balance"));
                account.setAccountId(rs.getInt("Account_ID"));
                account.setAccountName(rs.getString("Account_Name"));
                accountVector.add(account);
//                objects[index][0] = rs.getInt("Account_ID");
//                objects[index][2]= rs.getInt("Account_Balance");
//                objects[index][1] = rs.getString("Account_Name");
//                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Object objects[][] = convertToArray(accountVector);
//            for (Account a : accountVector) {
//                System.out.println(a.accountId);
//                System.out.println(a.accountName);
//                System.out.println(a.accountBalance);
//                System.out.println(accountVector.size());
            return objects;


        }
    }
    public Object[][] convertToArray(Vector<Account> accounts){
        Object object[][] = new Object[accounts.size()][3];
        for(int x = 0; x < accounts.size(); x++){
            object[x][0]  = accounts.get(x).getAccountId();
            object[x][1] = accounts.get(x).getAccountName();
            object[x][2] = accounts.get(x).getAccountBalance();
        }
        return object;
    }
    public boolean transfer(int id1, int id2, int amount) {
        String query = "Select Account_Balance FROM cs349as7 Where Account_ID = " + id1;
        ResultSet rs;
        int from = -1;
        int to = 0;
        ResultSet rs2;
        String query2 = "Select Account_Balance FROM cs349as7 Where Account_ID = " + id2;
        try {
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                from = rs.getInt("Account_Balance");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            //stmt = con.createStatement();
            rs = stmt.executeQuery(query2);
            while (rs.next()) {
                to = rs.getInt("Account_Balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (from - amount >= 0) {
            String transfer1 = "UPDATE cs349as7 SET Account_Balance = " + (from - amount) + " WHERE Account_ID" +
                    " = " + id1 + "; ";
            String transfer2 = "UPDATE cs349as7 SET Account_Balance = " + (to + amount) + " WHERE Account_ID= " + id2 + ";";
            try {
                con.setAutoCommit(false);
                stmt.executeUpdate(transfer1);
                stmt.executeUpdate(transfer2);
                con.commit();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else return false;
    }

}
