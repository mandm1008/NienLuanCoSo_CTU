package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Schema {
  protected abstract String getSQLString();

  protected abstract String getTableName();

  protected abstract int getId();

  protected abstract void setValues(PreparedStatement pstmt);

  public boolean insert() {
    ConnectDB connectDB = new ConnectDB();
    try (PreparedStatement pstmt = connectDB.getConnect().prepareStatement(getSQLString())) {
      setValues(pstmt);
      pstmt.executeUpdate();
      System.out.println("Inserted data successfully into table: " + getTableName());
      return true;
    } catch (SQLException e) {
      System.out.println("Failed to insert data into table: " + getTableName());
      e.printStackTrace();
      return false;
    }
  }

  public boolean delete() {
    ConnectDB connectDB = new ConnectDB();
    String deleteSQL = "DELETE FROM " + getTableName() + " WHERE id = ?";
    try (PreparedStatement pstmt = connectDB.getConnect().prepareStatement(deleteSQL)) {
      pstmt.setInt(1, getId());
      pstmt.executeUpdate();
      System.out.println("Deleted data successfully from table: " + getTableName());
      return true;
    } catch (SQLException e) {
      System.out.println("Failed to delete data from table: " + getTableName());
      e.printStackTrace();
      return false;
    }
  }

}
