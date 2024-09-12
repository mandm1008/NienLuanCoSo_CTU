package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Model {
  protected abstract String getInsertString();

  protected abstract String getTableName();

  protected abstract int getId();

  protected abstract void setValueInsert(PreparedStatement pstmt);

  public boolean insert() {
    ConnectDB connectDB = new ConnectDB();

    try {
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(getInsertString());
      setValueInsert(pstmt);
      pstmt.executeUpdate();
      System.out.println("Inserted data successfully into table: " + getTableName());

      return true;
    } catch (SQLException e) {
      System.out.println("Failed to insert data into table: " + getTableName());
      e.printStackTrace();

      return false;
    } finally {
      connectDB.closeConnect();
    }
  }

  public ResultSet findById() {
    ConnectDB connectDB = new ConnectDB();
    String findString = "SELECT * FROM " + getTableName() + " WHERE id = ?";

    try {
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(findString);

      // id search
      pstmt.setInt(1, getId());

      return pstmt.executeQuery();
    } catch (SQLException e) {
      System.out.println("Failed to find data from table: " + getTableName());
      e.printStackTrace();
      return null;
    } finally {
      connectDB.closeConnect();
    }
  }

  public boolean delete() {
    ConnectDB connectDB = new ConnectDB();
    String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id = ?";

    try {
      PreparedStatement pstmt = connectDB.getConnect().prepareStatement(deleteSQL);

      pstmt.setInt(1, getId());
      pstmt.executeUpdate();
      System.out.println("Deleted data successfully from table: " + getTableName());

      return true;
    } catch (SQLException e) {
      System.out.println("Failed to delete data from table: " + getTableName());
      e.printStackTrace();

      return false;
    } finally {
      connectDB.closeConnect();
    }
  }

}
