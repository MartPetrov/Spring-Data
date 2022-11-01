package IntroductionToDBApps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class P03GetMinionNames {

    private static final String GET_MINIONS_NAME_AND_AGE_BY_VILLAIN_ID =
            "SELECT m.name, m.age" +
                    " from minions as m" +
                    " JOIN minions_villains mv on m.id = mv.minion_id" +
                    " WHERE mv.villain_id = ?";

    private static final String COLUMN_LABEL_AGE = "age";
    private static final String GET_VILLAIN_NAME_BY_ID = "SELECT * from villains v where  v.id = ?";

    private static String NO_VILLAIN_FORMAT = "No villain with ID %d exists in the database.";

    private static String VILLAIN_FORMAT = "Villain: %s%n";
    private static String MINION_FORMAT = "%d. %s %d%n";

    public static void main(String[] args) throws SQLException {

        final Connection connection = Utils.getSQLConnection();
        final int villainId = new Scanner(System.in).nextInt();

        final PreparedStatement villianStatement = connection.prepareStatement(GET_VILLAIN_NAME_BY_ID);
        villianStatement.setInt(1, villainId);

        final ResultSet villainSet = villianStatement.executeQuery();

        if (!villainSet.next()) {
            System.out.printf(NO_VILLAIN_FORMAT, villainId);
            connection.close();
            return;
        }

        final String villainName = villainSet.getString(Constants.COLUMN_LABEL_NAME);

        System.out.printf(VILLAIN_FORMAT, villainName);

        final PreparedStatement minionsStatements = connection.prepareStatement(GET_MINIONS_NAME_AND_AGE_BY_VILLAIN_ID);

        minionsStatements.setInt(1, villainId);

        final ResultSet minionsSet = minionsStatements.executeQuery();

        for (int index = 1; minionsSet.next(); index++) {
            final String minionName = minionsSet.getString(Constants.COLUMN_LABEL_NAME);
            final int age = minionsSet.getInt(COLUMN_LABEL_AGE);

            System.out.printf(MINION_FORMAT, index, minionName, age);
        }

        connection.close();
    }
}
