package IntroductionToDBApps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class P02WritingYourOwnDataRetrievalApplication {
    public static void main(String[] args) throws SQLException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your Username:");
        String name_player = sc.nextLine();

        final Connection connection = Utils.getSQLConnection_Diablo();

        PreparedStatement selectUserGames = connection.prepareStatement(
                "SELECT count(game_id) as games, concat_ws(' ',u.first_name,u.last_name) as full_name " +
                        " from users_games" +
                        " JOIN users u on u.id = users_games.user_id" +
                        " where u.user_name = ?" +
                        " group by user_id"
        );


        selectUserGames.setString(1, name_player);
        ResultSet countGames = selectUserGames.executeQuery();
        if (!countGames.next()) {
            System.out.println("No such user exists");
            connection.close();
            return;
        }

        int games = countGames.getInt("games");
        String full_name = countGames.getString("full_name");
        String result = String.format("User: %s%n" + "%s has played %d games", name_player, full_name, games);

        System.out.println(result);
    }
}
