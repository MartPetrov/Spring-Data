package IntroductionToDBApps;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class P06RemoveVillain {
    public static void main(String[] args) throws SQLException {

        Connection connection = Utils.getSQLConnection();

        Scanner scanner = new Scanner(System.in);
        int villainId = Integer.parseInt(scanner.nextLine());
        PreparedStatement selectVillain = connection.prepareStatement(
                "SELECT name FROM villains where id = ?"
        );
        selectVillain.setInt(1, villainId);
        ResultSet villainSet = selectVillain.executeQuery();
        if (!villainSet.next()) {
            System.out.println("No such villain was found");
            connection.close();
            return;
        }

        String villainName = villainSet.getString(Constants.COLUMN_LABEL_NAME);


        List<Integer> minionsIds = new ArrayList<>();
        PreparedStatement selectAllVillainMinions = connection.prepareStatement(
                "SELECT count(minion_id) as m_count FROM minions_villains WHERE villain_id = ?"
        );
        selectAllVillainMinions.setInt(1, villainId);
        ResultSet minionsSet = selectAllVillainMinions.executeQuery();
        minionsSet.next();
        int countMinionsReleased = minionsSet.getInt("m_count");
        while (minionsSet.next()) {
            minionsIds.add(minionsSet.getInt("minion_id"));
        }
        connection.setAutoCommit(false);
        try {
            PreparedStatement deleteMinionsVillains = connection.prepareStatement(
                    "DELETE  FROM minions_villains WHERE villain_id = ?"
            );
            deleteMinionsVillains.setInt(1, villainId);
            deleteMinionsVillains.executeUpdate();

            PreparedStatement deleteVillain = connection.prepareStatement(
                    "DELETE  FROM villains WHERE id = ?"
            );
            deleteVillain.setInt(1, villainId);
            deleteVillain.executeUpdate();


            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
        }
        System.out.println(villainName + " was deleted");
        System.out.println(countMinionsReleased + " minions released");
    }
}
