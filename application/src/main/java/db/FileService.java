// package db;

// import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
// import com.google.api.client.json.gson.GsonFactory;
// import com.google.api.services.drive.Drive;
// import com.google.api.services.drive.DriveScopes;
// import com.google.api.services.drive.model.File;
// import com.google.api.services.drive.model.Permission;
// import com.google.auth.http.HttpCredentialsAdapter;
// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.auth.oauth2.ServiceAccountCredentials;
// // import com.google.api.client.http.FileContent;

// import java.io.FileInputStream;
// import java.io.IOException;
// import java.security.GeneralSecurityException;
// import java.util.Collections;

// public class FileService {
// private static final String APPLICATION_NAME = "Your Music API Drive";
// private static final GsonFactory JSON_FACTORY =
// GsonFactory.getDefaultInstance();
// private static final String CREDENTIALS_FILE_PATH =
// "/access/yourmusicapidrive-a411b50ba969.json";
// private static final String ADMIN_EMAIL = "musicplayersource@gmail.com";

// // Connect GG Drive API
// public static Drive getDriveService() throws GeneralSecurityException,
// IOException {
// // Load JSON file credentials
// FileInputStream serviceAccountStream = new FileInputStream(
// FileDBService.class.getResource(CREDENTIALS_FILE_PATH).getPath());

// GoogleCredentials credentials =
// ServiceAccountCredentials.fromStream(serviceAccountStream)
// .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));

// return new Drive.Builder(
// GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new
// HttpCredentialsAdapter(credentials))
// .setApplicationName(APPLICATION_NAME)
// .build();
// }

// // Upload file to Google Drive
// public static String uploadFile(String filePath, String mimeType) throws
// Exception {
// // Set metadata
// File fileMetadata = new File();
// fileMetadata.setName("My Uploaded File");

// // // Load File
// // java.io.File filePathToUpload = new java.io.File(filePath);
// // FileContent mediaContent = new FileContent(mimeType, filePathToUpload);

// Drive service = getDriveService();

// // Upload file
// File file = service.files().create(fileMetadata)
// .setFields("id")
// .execute();

// // Make file public
// makeFilePublic(service, file.getId());

// // Share to admin
// shareToAdmin(service, file.getId());

// return "https://drive.google.com/uc?export=download&id=" + file.getId();
// }

// private static void makeFilePublic(Drive service, String fileId) throws
// IOException {
// Permission permission = new Permission();
// permission.setType("anyone"); // anyone can access
// permission.setRole("reader"); // Only read

// // add public permission to file
// service.permissions().create(fileId, permission)
// .setFields("id")
// .execute();
// }

// private static void shareToAdmin(Drive service, String fileId) throws
// IOException {
// Permission permission = new Permission();
// permission.setType("user"); // anyone can access
// permission.setRole("writer"); // Only read
// permission.setEmailAddress(ADMIN_EMAIL);

// // add admin permission to file
// service.permissions().create(fileId, permission)
// .execute();
// }

// public static void main(String[] args) {
// try {
// System.out.println(
// uploadFile(
// "D:\\CTU\\NienLuanCoSo\\application\\src\\main\\resources\\testing\\anh_dech_can_gi_nhieu_ngoai_em_den.mp3",
// "audio/mpeg"));
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// }
