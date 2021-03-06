package services;

import model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserManager implements IUserManager{
    private String jdbcURL = "jdbc:mysql://localhost:3306/usermanager?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "123456";

    private static final String INSERT_USER_SQL = "insert into users" + "(name, email, country) VALUE" + "(?,?,?);";

    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id = ?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email = ?, country = ? where id = ?;";
    private static final String FIND_USER_BY_COUNTRY = "select * from users where country = ?;";
    private static final String SORT_USER_BY_NAME = "select * from users order by name asc";
    private static final String SQL_INSERT = "insert into employee(name, salary, created_date) values (?,?,?)";
    private static final String SQL_UPDATE = "update employee set salary = ? where name = ?";
    private static final String SQL_CREATE_TABLE = "create table employee "
            + "("
            + "id serial,"
            + "name varchar(100) not null,"
            + "salary numeric(15, 2) not null,"
            + "created_date timestamp,"
            + "primary key (id)"
            + ")";

    private static final String SQL_TABLE_DROP = "drop table if exists employee";

    public UserManager() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USER_SQL);
        try ( Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());

            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);

           ResultSet resultSet = preparedStatement.executeQuery();

           while (resultSet.next()) {
               String name = resultSet.getString("name");
               String email = resultSet.getString("email");
               String country = resultSet.getString("country");

               user = new User(id, name, email, country);
           }
        } catch (SQLException e) {
           printSQLException(e);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet. getString("country");

                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public List<User> findUser(String country) {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_COUNTRY);) {
            preparedStatement.setString(1, country);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");

                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public List<User> sortUserByName() {
        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(SORT_USER_BY_NAME);) {
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet. getString("country");

                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{call get_user_by_id(?)}";

        try (Connection connection = getConnection(); CallableStatement callableStatement = connection.prepareCall(query);) {
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");

                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public void insertUserStore(User user) throws SQLException {
        String query = "{call insert_user(?,?,?)}";

        try (Connection connection = getConnection(); CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getCountry());

            System.out.println(callableStatement);
            callableStatement.executeQuery();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void addUserTransaction(User user, int[] permissions) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementAssigment = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());

            int rowAffected = preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            int userId = 0;

            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }

            if (rowAffected == 1) {
                String sqlPivot = "insert into user_permission(user_id,permission_id) value(?,?)";
                preparedStatementAssigment = connection.prepareStatement(sqlPivot);

                for (int permissionId: permissions) {
                    preparedStatementAssigment.setInt(1, userId);
                    preparedStatementAssigment.setInt(2, permissionId);
                    preparedStatementAssigment.executeUpdate();
                }

                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
            }
            System.out.println(e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (preparedStatementAssigment != null) preparedStatementAssigment.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());;
            }
        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatementInsert = connection.prepareStatement(SQL_INSERT);
             PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE)) {

            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_CREATE_TABLE);

            preparedStatementInsert.setString(1, "Quynh");
            preparedStatementInsert.setBigDecimal(2, new BigDecimal(10));
            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatementInsert.execute();


            preparedStatementInsert.setString(1, "Ngan");
            preparedStatementInsert.setBigDecimal(2, new BigDecimal(20));
            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatementInsert.execute();

            preparedStatementUpdate.setBigDecimal(2, new BigDecimal(999.99));
            preparedStatementUpdate.setString(2, "Quynh");
            preparedStatementUpdate.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUpdateUseTransaction() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatementInsert = connection.prepareStatement(SQL_INSERT);
             PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE)) {

            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_CREATE_TABLE);

            connection.setAutoCommit(false);

            preparedStatementInsert.setString(1, "Quynh");
            preparedStatementInsert.setBigDecimal(2, new BigDecimal(10));
            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatementInsert.execute();


            preparedStatementInsert.setString(1, "Ngan");
            preparedStatementInsert.setBigDecimal(2, new BigDecimal(20));
            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatementInsert.execute();

            preparedStatementUpdate.setBigDecimal(1, new BigDecimal(999.99));
            preparedStatementUpdate.setString(2, "Quynh");
            preparedStatementUpdate.execute();

            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> selectAllUserStoreProcedure() {
        List<User> users = new ArrayList<>();
        String query = "{call select_all_user()}";

        try (Connection connection = getConnection(); CallableStatement callableStatement = connection.prepareCall(query)) {
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet. getString("country");

                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public boolean updateUserStoreProcedure(User user) throws SQLException {
        boolean rowUpdated = false;
        String query = "{call update_user(?,?,?,?)}";

        try (Connection connection = getConnection(); CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getCountry());
            callableStatement.setInt(4, user.getId());

            rowUpdated = callableStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            printSQLException(e);
        }
        return rowUpdated;
    }

    @Override
    public boolean deleteUserStoreProcedure(int id) throws SQLException {
        boolean rowDeleted;
        String query = "{call delete_user(?)}";
        try (Connection connection = getConnection(); CallableStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
