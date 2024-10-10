package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;

public abstract class Model {
  protected abstract String getInsertString();

  protected abstract String getTableName();

  protected abstract String getIdName();

  protected abstract int getId();

  protected abstract void setValueInsert(PreparedStatement pstmt);

  protected abstract boolean checkAccess();

  public QueryResult query(String queryString, Consumer<PreparedStatement> setParams) {
    ConnectDB connectDB = new ConnectDB();

    try {
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(queryString);

      setParams.accept(pstmt);

      return new QueryResult(connectDB, pstmt.executeQuery());
    } catch (SQLException e) {
      System.out.println("Failed to query data from table: " + getTableName());
      e.printStackTrace();

      return null;
    }
  }

  public boolean update(String updateString, Consumer<PreparedStatement> setParams) {
    ConnectDB connectDB = new ConnectDB();

    try {
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(updateString);

      setParams.accept(pstmt);

      pstmt.executeUpdate();

      return true;
    } catch (SQLException e) {
      System.out.println("Failed to update data in table: " + getTableName());
      e.printStackTrace();

      return false;
    } finally {
      connectDB.closeConnect();
    }
  }

  public boolean insert() {
    if (!checkAccess())
      return false;

    return update(getInsertString(), (pstmt) -> setValueInsert(pstmt));
  }

  public QueryResult findById() {
    return query("SELECT * FROM " + getTableName() + " WHERE " + getIdName() + " = ?", (pstmt) -> {
      try {
        pstmt.setInt(1, getId());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }

  public boolean delete() {
    return update("DELETE FROM " + getTableName() + " WHERE " + getIdName() + " = ?", (pstmt) -> {
      try {
        pstmt.setInt(1, getId());
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        new ConnectDB().closeConnect();
      }
    });
  }

}
