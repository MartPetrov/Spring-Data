package IntroductionToDBApps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class P05ChangeTownNamesCasing {
    public static void main(String[] args) throws SQLException {
        final Connection connection = Utils.getSQLConnection();

        Scanner scanner = new Scanner(System.in);

        String countryName = scanner.nextLine();

        PreparedStatement updateTownName = connection.prepareStatement(

                "UPDATE towns SET name  = UPPER(name) where country = ?"
        );

        updateTownName.setString(1,countryName);
        int updatedCount = updateTownName.executeUpdate();

        if (updatedCount == 0) {
            System.out.println("No town names were affected.");
            connection.close();
            return;
        }

        System.out.println(updatedCount + " town names were affected.");

        PreparedStatement selectAllTowns = connection.prepareStatement(
                "SELECT name FROM towns WHERE country = ?"
        );

        selectAllTowns.setString(1,countryName);
        ResultSet townSet = selectAllTowns.executeQuery();
        System.out.print("[");
        List<String> towns = new ArrayList<>();
        while (townSet.next()) {
            String townName = townSet.getString("name");
            towns.add(townName);
        }
        System.out.print(String.join(",", towns));
        System.out.print("]");
        connection.close();
    }
}
