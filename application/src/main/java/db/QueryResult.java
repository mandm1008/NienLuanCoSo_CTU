package db;

import java.sql.ResultSet;

public class QueryResult {
  protected ConnectDB connectDB;
  protected ResultSet resultSet;

  public QueryResult(ConnectDB connectDB, ResultSet resultSet) {
    this.connectDB = connectDB;
    this.resultSet = resultSet;
  }

  public ResultSet getResultSet() {
    return resultSet;
  }

  public void close() {
    connectDB.closeConnect();
  }
}
