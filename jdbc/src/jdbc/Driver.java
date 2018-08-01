package jdbc;

import java.sql.*;
import com.opencsv.*;
import java.io.*;
public class Driver {

	
   
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");  
			Connection my_conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","newrootpassword");
			Statement my_stat = my_conn.createStatement();
			ResultSet my_res;
			BufferedReader inp = new BufferedReader (new InputStreamReader(System.in));
			String anum,aname,acity;
			int ch,bal = 0;
			while(true){
				
                System.out.println("Choose Option");
                System.out.println("1. CREATE ACCOUNT");
                System.out.println("2. BALANCE CHECK");
                System.out.println("3. TRANSACT");
                System.out.println("4. VIEW TRANSACTIONS");
                System.out.println("0. EXIT");
                ch = Integer.parseInt(inp.readLine());
                switch (ch) {
                
    			case 1 : {
    					
    					bal = 0;
    					System.out.println("Enter 8 digit Acc. Num");
                    anum = inp.readLine();
                    if(anum.length()!=8)
                    {
                    	System.out.println("Wrong length entered, try again!");
                    	break;
                    }
                    System.out.println("Enter Customer Name");
                    aname = inp.readLine();
                    System.out.println("Enter City Name");
                    acity = inp.readLine();
                   
                    String accountopen1 =  "INSERT INTO user (account_num,name, balance, city) VALUES(?, ?, 0, ? )";
                    String accountopen2 =  "INSERT IGNORE INTO timestamps (account_num,transaction_type, open_bal, close_bal,update_time) VALUES(?,\"NIL\",0,0,0)";
                    String updatetime = "UPDATE timestamps SET update_time = CURRENT_TIMESTAMP WHERE account_num = ?";
                    PreparedStatement pre_stmtA = my_conn.prepareStatement(accountopen1);
                    PreparedStatement pre_stmtB = my_conn.prepareStatement(accountopen2);
                    PreparedStatement pre_stmtC = my_conn.prepareStatement(updatetime);
                    pre_stmtA.setString(1, anum);
                    pre_stmtA.setString(2, aname);
                    pre_stmtA.setString(3, acity);
                    pre_stmtA.executeUpdate();
                    pre_stmtB.setString(1, anum);
                    pre_stmtB.executeUpdate();
                    pre_stmtC.setString(1, anum);
                    pre_stmtC.execute();
                    System.out.println("");
                    System.out.println("");
                    break;
                    }
    			case 2 :{
    				System.out.println("Enter 8 digit Acc. Num");
    				anum = inp.readLine();
    				if(anum.length()!=8)
                    {
                    	System.out.println("Wrong length entered, try again!");
                    	break;
                    }
    				String balchk = String.format("SELECT * FROM user WHERE account_num =%s", anum);
    				my_res = my_stat.executeQuery(balchk);
    				
    				ResultSetMetaData my_meta = my_res.getMetaData();
    				int columnsNumber = my_meta.getColumnCount();
    				while (my_res.next()) {
    				    for (int i = 1; i <= columnsNumber; i++) {
    				        if (i > 1) System.out.print(",  ");
    				        String columnValue = my_res.getString(i);
    				        
    				        System.out.print("|"+ my_meta.getColumnName(i)+"-"+columnValue + " " );
    				    }
    				    System.out.println("");
    				}
    				System.out.println("");
    				System.out.println("");
    				break;
    			}
    			case 3 :{
    				
    				System.out.println("Enter 8 digit Acc. Num");
    				anum = inp.readLine();
    				if(anum.length()!=8)
                    {
                    	System.out.println("Wrong length entered, try again!");
                    	break;
                    }
    				String balchk = String.format("SELECT * FROM user WHERE account_num =%s", anum);
    				ResultSet my_res1 = my_stat.executeQuery(balchk);
    				while(my_res1.next()){
    			        bal = my_res1.getInt("balance"); //IDTable
    					}
    				System.out.print("Current bal  :  ");
    				System.out.println(bal);

    				System.out.println("Enter transaction type(D/W) :");
    				String trans_type;
    				trans_type = inp.readLine();
    				if(trans_type.equalsIgnoreCase("D"))
    				{
    					int trans_amt;
    				System.out.println("Enter transaction amount :");
    				trans_amt = Integer.parseInt(inp.readLine());

    				String dep = "INSERT INTO timestamps SET open_bal=?,close_bal=?,account_num=?,transaction_type=?,update_time = CURRENT_TIMESTAMP";
    				PreparedStatement pre_stmt = my_conn.prepareStatement(dep);
    				pre_stmt.setString(1,String.valueOf(bal));
    				pre_stmt.setString(2,String.valueOf(bal+trans_amt));
    				pre_stmt.setString(3, anum);
    				pre_stmt.setString(4, trans_type);
    				pre_stmt.executeUpdate();
    				String up = "UPDATE user SET balance = ? WHERE account_num = ?";
    				PreparedStatement pre_stmt1 = my_conn.prepareStatement(up);
    				pre_stmt1.setString(1,String.valueOf(bal+trans_amt));
    				pre_stmt1.setString(2, anum);
    				pre_stmt1.executeUpdate();
    				System.out.println("");
    				System.out.println("");


    				break;
    				}
    				else if(trans_type.equalsIgnoreCase("W"))
    				{
    					int trans_amt;
    				System.out.println("Enter transaction amount :");
    				trans_amt = Integer.parseInt(inp.readLine());
    				String with = "INSERT INTO timestamps SET open_bal=?,close_bal=?,account_num=?,transaction_type=?,update_time = CURRENT_TIMESTAMP";
    				PreparedStatement pre_stmt2 = my_conn.prepareStatement(with);
    				pre_stmt2.setString(1,String.valueOf(bal));
    				pre_stmt2.setString(2,String.valueOf(bal-trans_amt));
    				pre_stmt2.setString(3,anum);
    				pre_stmt2.setString(4,trans_type);
    				pre_stmt2.executeUpdate();
    				String up = "UPDATE user SET balance = ? WHERE account_num = ?";
    				PreparedStatement pre_stmt3 = my_conn.prepareStatement(up);
    				pre_stmt3.setString(1,String.valueOf(bal-trans_amt));
    				pre_stmt3.setString(2, anum);
    				pre_stmt3.executeUpdate();
    				System.out.println("");
    				System.out.println("");
    				break;
    				
    				}
    				else if(!trans_type.equalsIgnoreCase("W")&&!trans_type.equalsIgnoreCase("D")) {
    				System.out.println("Wrong Input detected. Try Again!");	
    				} break;
    			}
    			case 4 :{
    				System.out.println("Enter 8 digit Acc. Num");
    				anum = inp.readLine();
    				String balchk = String.format("SELECT * FROM timestamps WHERE account_num =%s", anum);
    				my_res = my_stat.executeQuery(balchk);
    				ResultSetMetaData my_meta = my_res.getMetaData();
    				int columnsNumber = my_meta.getColumnCount();
    				while (my_res.next()) {
    				    for (int i = 1; i <= columnsNumber; i++) {
    				        if (i > 1) System.out.print(",  ");
    				        String columnValue = my_res.getString(i);
    				        
    				        System.out.print("|"+ my_meta.getColumnName(i)+"-"+columnValue + " " );
    				    }
    				    System.out.println("");
    				}
    				System.out.println("Generate report? [Y/N]");
    				String report = inp.readLine();
    				if(report.equalsIgnoreCase("Y")) {
    				@SuppressWarnings("deprecation")
					CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\Sanjay\\Desktop\\result.csv"), '\t','|');
    				Boolean includeHeaders = true;

    				java.sql.ResultSet myResultSet = my_stat.executeQuery(balchk);
    				
    				writer.writeAll(myResultSet, includeHeaders);

    				writer.close();
    				System.out.println("Report saved to desktop on results.csv");
    				System.out.println("");
    				System.out.println("");
    				break;
    				}
    				else break;
    				
    			}
    			
    			case 0:{
    				inp.close();
    				
    				System.exit(0);
    			}
    			default :{
    				break;
    			}
    			}
                
			}
			
		}
		catch(Exception exc){
			exc.printStackTrace();
		}
	}

}
