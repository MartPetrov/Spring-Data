package IntroductionToDBApps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class P04AddMinions {

    private static final String GET_TOWN_BY_NAME = "SELECT t.id from towns t where  t.name = ?";
    private static final String GET_VILLAIN_BY_NAME = "SELECT id from villains where  name = ?";
    private static final String INSERT_INTO_TOWNS = "INSERT INTO towns(name) values(?)";

    private static final String INSERT_INTO_VILLAINS = "INSERT INTO villains(name, evilness_factor) values(?, ?)";


    public static void main(String[] args) throws SQLException {

        final Connection connection = Utils.getSQLConnection();

        final Scanner scanner = new Scanner(System.in);

        final String[] minionInfo = scanner.nextLine().split(" ");
        final String minionName = minionInfo[1];
        final int minionAge = Integer.parseInt(minionInfo[2]);
        final String minionTown = minionInfo[3];


        final String villainName = scanner.nextLine().split(" ")[1];


        int townId = getOrInsertTown(connection, minionTown);
        int villainId = getOrInsertVillain(connection, villainName);

        PreparedStatement insertMinion = connection.prepareStatement(
                "INSERT INTO minions(name,age,town_id) values(?, ?, ?)"
        );
        insertMinion.setString(1, minionName);
        insertMinion.setInt(2, minionAge);
        insertMinion.setInt(3, townId);
        insertMinion.executeUpdate();

        PreparedStatement getLastMinion = connection.prepareStatement(
                "SELECT  id from minions ORDER BY id DESC LIMIT 1"
        );

        ResultSet lastMinionSet = getLastMinion.executeQuery();
        lastMinionSet.next();
        int lastMinionId = lastMinionSet.getInt("id");

        PreparedStatement insertMinionsVillains = connection.prepareStatement(
                "INSERT INTO minions_villains VALUES (?,?)"
        );
        insertMinionsVillains.setInt(1, lastMinionId);
        insertMinionsVillains.setInt(2, villainId);
        insertMinionsVillains.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s", minionName, villainName);

        connection.close();
    }

    private static int getOrInsertVillain(Connection connection, String villainName) throws SQLException {
        final PreparedStatement selectVillain = connection.prepareStatement(GET_VILLAIN_BY_NAME);
        selectVillain.setString(1, villainName);

        ResultSet villainSet = selectVillain.executeQuery();
        int villainId = 0;

        if (!villainSet.next()) {

            final PreparedStatement insertVillain = connection.prepareStatement(INSERT_INTO_VILLAINS);
            insertVillain.setString(1, villainName);
            insertVillain.setString(2, "evil");
            insertVillain.executeUpdate();

            ResultSet newVillainSet = selectVillain.executeQuery();
            newVillainSet.next();
            villainId = newVillainSet.getInt("id");
            System.out.printf("Villain %s was added to the database.%n", villainName);
        } else {
            villainId = villainSet.getInt("id");
        }
        return villainId;

    }

    private static int getOrInsertTown(Connection connection, String minionTown) throws SQLException {
        final PreparedStatement townStatement = connection.prepareStatement(GET_TOWN_BY_NAME);
        townStatement.setString(1, minionTown);

        final ResultSet townSet = townStatement.executeQuery();

        int townId = 0;
        if (!townSet.next()) {
            final PreparedStatement insertTowns = connection.prepareStatement(INSERT_INTO_TOWNS);
            insertTowns.setString(1, minionTown);
            insertTowns.executeUpdate();
            System.out.printf("Town %s was added to the database.%n", minionTown);

            final ResultSet newTownSet = townStatement.executeQuery();
            newTownSet.next();
            townId = newTownSet.getInt("id");
        } else {
            townId = townSet.getInt("id");
        }
        return townId;
    }
}
