package IntroductionToDBApps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class P02Get_Villains_Names {
    private static final String getVillains_Names = "SELECT " +
            "v.name, " +
            "count( distinct mv.minion_id) as minions_count " +
            " FROM villains v\n" +
            " JOIN minions_villains mv on v.id = mv.villain_id\n" +
            " group by mv.villain_id\n" +
            " HAVING minions_count > ?\n" +
            " ORDER BY minions_count DESC";

    private static final String COLUMN_LABEL_NAME_MINIONS_COUNT = "minions_count";
    private static final String PRINT_FORMAT = "%s %d";

    public static void main(String[] args) throws SQLException {

        final Connection connection = Utils.getSQLConnection();
        final PreparedStatement statement = connection.prepareStatement(getVillains_Names);

        statement.setInt(1, 15);

        final ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            final String villainName = resultSet.getString(Constants.COLUMN_LABEL_NAME);
            final int minions_count = resultSet.getInt(COLUMN_LABEL_NAME_MINIONS_COUNT);

            System.out.printf(PRINT_FORMAT, villainName, minions_count);

        }
        connection.close();
    }
}

