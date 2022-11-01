package IntroductionToDBApps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class P01AccessingDatabaseViaSimpleJavaApplication {
    public static void main(String[] args) throws SQLException {

        Scanner sc = new Scanner(System.in);
        double salaryLimit = Double.parseDouble(sc.nextLine());

        final Connection connection = Utils.getSQLConnection_Soft_uni();

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT concat_ws(' ', e.first_name,e.last_name) as full_name FROM soft_uni.employees AS e" +
                        " WHERE salary >= ?"
        );
        stmt.setDouble(1, salaryLimit);
        ResultSet resultSetSalary = stmt.executeQuery();
        while (resultSetSalary.next()) {
            System.out.println(resultSetSalary.getString("full_name"));
        }
    }
}
