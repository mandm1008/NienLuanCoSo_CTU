package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Schema {
  protected abstract String getSQLString();

  protected abstract String getTableName();

  protected abstract void setValues(PreparedStatement pstmt);

  public void insert() {
    ConnectDB connectDB = new ConnectDB();
    try (PreparedStatement pstmt = connectDB.getConnect().prepareStatement(getSQLString())) {
      setValues(pstmt);
      pstmt.executeUpdate();
      System.out.println("Inserted data successfully into table: " + getTableName());
    } catch (SQLException e) {
      System.out.println("Failed to insert data into table: " + getTableName());
      e.printStackTrace();
    }
  }

}
