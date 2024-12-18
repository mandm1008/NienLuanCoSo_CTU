package modules;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.api.client.http.FileContent;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

public class FileService {
  private static final String APPLICATION_NAME = "Your Music API Drive";
  private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String CREDENTIALS_FILE_PATH = "/access/yourmusicapidrive.json";
  private static final String ADMIN_EMAIL = "musicplayersource@gmail.com";

  // Connect GG Drive API
  public static Drive getDriveService() throws GeneralSecurityException,
      IOException {
    // Load JSON file credentials
    GoogleCredentials credentials = ServiceAccountCredentials
        .fromStream(FileService.class.getResourceAsStream(CREDENTIALS_FILE_PATH))
        .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

    return new Drive.Builder(
        GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials))
        .setApplicationName(APPLICATION_NAME)
        .build();
  }

  // Upload file to Google Drive
  public static String uploadFile(String filePath, String mimeType, String fileName) throws Exception {
    // Set metadata
    File fileMetadata = new File();
    fileMetadata.setName(fileName);

    // Load File
    java.io.File filePathToUpload = new java.io.File(filePath);
    FileContent mediaContent = new FileContent(mimeType, filePathToUpload);

    Drive service = getDriveService();

    // Upload file
    File file = service.files().create(fileMetadata, mediaContent)
        .setFields("id")
        .execute();

    // Make file public
    makeFilePublic(service, file.getId());

    // Share to admin
    new Thread(() -> {
      try {
        shareToAdmin(service, file.getId());
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    return "https://drive.google.com/uc?export=download&id=" + file.getId();
  }

  private static void makeFilePublic(Drive service, String fileId) throws IOException {
    Permission permission = new Permission();
    permission.setType("anyone"); // anyone can access
    permission.setRole("reader"); // Only read

    // add public permission to file
    service.permissions().create(fileId, permission)
        .setFields("id")
        .execute();
  }

  private static void shareToAdmin(Drive service, String fileId) throws IOException {
    Permission permission = new Permission();
    permission.setType("user"); // anyone can access
    permission.setRole("writer"); // Only read
    permission.setEmailAddress(ADMIN_EMAIL);

    // add admin permission to file
    service.permissions().create(fileId, permission)
        .execute();
  }

  public static void delete(String fileId) throws Exception {
    Drive service = getDriveService();
    service.files().delete(fileId).execute();
  }

  public static String getFileName(String downloadUrl) throws Exception {
    String fileId = extractFileId(downloadUrl);

    if (fileId == null || fileId.isEmpty()) {
      throw new IllegalArgumentException("Invalid Google Drive URL");
    }

    Drive service = getDriveService();
    File file = service.files().get(fileId).setFields("name").execute();
    return file.getName();
  }

  private static String extractFileId(String url) throws Exception {
    URI uri = new URI(url);
    String query = uri.getQuery();

    return Arrays.stream(query.split("&"))
        .filter(param -> param.startsWith("id="))
        .map(param -> param.split("=")[1])
        .findFirst()
        .orElse(null);
  }

}
