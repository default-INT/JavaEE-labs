package by.gstu.models.dao.mysql;

import by.gstu.models.dao.ReturnRequestDAO;
import by.gstu.models.entities.ReturnRequest;
import by.gstu.models.utils.ConfigurationManager;
import by.gstu.models.utils.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * Implements ReturnRequestDAO.
 *
 * @author Evgeniy Trofimov
 * @version 1.1
 */
class MySqlReturnRequestDAO implements ReturnRequestDAO {

    private static final Logger logger = Logger.getLogger(MySqlOrderDAO.class);

    private static final String DEFAULT_CREATE = "{CALL add_return_request(?, ?, ?, ?)}";
    private static final String DEFAULT_READ = "{CALL read_return_request(?)}";
    private static final String DEFAULT_READ_ALL = "{CALL read_all_return_requests()}";
    private static final String DEFAULT_UPDATE = "{CALL edit_return_request(?, ?, ?, ?, ?)}";
    private static final String DEFAULT_DELETE = "{CALL delete_return_request(?)}";

    private static final String CREATE;
    private static final String READ;
    private static final String READ_ALL;
    private static final String UPDATE;
    private static final String DELETE;

    static {
        ConfigurationManager configuratorManager = ConfigurationManager.getInstance();

        CREATE = configuratorManager.getProperty("sql.ReturnRequests.create", DEFAULT_CREATE, "mysql");
        READ = configuratorManager.getProperty("sql.ReturnRequests.read", DEFAULT_READ, "mysql");
        READ_ALL = configuratorManager.getProperty("sql.ReturnRequests.readAll", DEFAULT_READ_ALL,
                "mysql");
        UPDATE = configuratorManager.getProperty("sql.ReturnRequests.update", DEFAULT_UPDATE, "mysql");
        DELETE = configuratorManager.getProperty("sql.ReturnRequests.delete", DEFAULT_DELETE, "mysql");
    }

    @Override
    public boolean create(ReturnRequest returnRequest) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(CREATE);

            callStatement.setDate("var_return_date", new Date(returnRequest.getReturnDate().getTimeInMillis()),
                    returnRequest.getReturnDate());
            callStatement.setInt("var_order_id", returnRequest.getOrderId());
            callStatement.setString("var_description", returnRequest.getDescription());
            callStatement.setBoolean("var_return_mark", returnRequest.isReturnMark());
            callStatement.setDouble("var_repair_cost", returnRequest.getRepairCost());

            int k = callStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public Collection<ReturnRequest> readAll() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ_ALL);
            ResultSet resultSet = statement.executeQuery();
            Collection<ReturnRequest> returnRequests = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date returnDate = resultSet.getDate("return_date");
                int orderId = resultSet.getInt("order_id");
                String description = resultSet.getString("description");
                boolean returnMark = resultSet.getBoolean("return_mark");
                double repairCost = resultSet.getDouble("repair_cost");

                Calendar returnDateCal = new GregorianCalendar();
                returnDateCal.setTime(returnDate);

                returnRequests.add(new ReturnRequest(id, returnDateCal, orderId, description, returnMark, repairCost));
            }
            return returnRequests;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public ReturnRequest read(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement statement;
        try {
            connection = connectionPool.getConnection();
            statement = connection.prepareCall(READ);
            statement.setInt("var_return_req_id", id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Date returnDate = resultSet.getDate("return_date");
                int orderId = resultSet.getInt("order_id");
                String description = resultSet.getString("description");
                boolean returnMark = resultSet.getBoolean("return_mark");
                double repairCost = resultSet.getDouble("repair_cost");

                Calendar returnDateCal = new GregorianCalendar();
                returnDateCal.setTime(returnDate);

                return new ReturnRequest(id, returnDateCal, orderId, description, returnMark, repairCost);
            }
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(ReturnRequest returnRequest) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(UPDATE);

            callStatement.setInt("var_return_req_id", returnRequest.getId());
            callStatement.setDate("var_return_date", new Date(returnRequest.getReturnDate().getTimeInMillis()),
                    returnRequest.getReturnDate());
            callStatement.setInt("var_order_id", returnRequest.getOrderId());
            callStatement.setString("var_description", returnRequest.getDescription());
            callStatement.setBoolean("var_return_mark", returnRequest.isReturnMark());
            callStatement.setDouble("var_repair_cost", returnRequest.getRepairCost());

            int k = callStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = null;
        CallableStatement callStatement;
        try {
            connection = connectionPool.getConnection();

            callStatement = connection.prepareCall(DELETE);

            callStatement.setInt("var_return_req_id", id);

            int k = callStatement.executeUpdate();
            return k > 0;
        } catch (SQLException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            connectionPool.closeConnection(connection);
        }
        return false;
    }
}
