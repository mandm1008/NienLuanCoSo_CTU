package modules;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class KeyStoreManager {

  private static final String KEYSTORE_FILE = "account.jks";
  private static final String KEYSTORE_PASSWORD = "keystorepassword";

  public KeyStore loadKeyStore() throws Exception {
    KeyStore keyStore = KeyStore.getInstance("JCEKS");
    FileInputStream fis = null;

    try {
      fis = new FileInputStream(KEYSTORE_FILE);
      keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
    } catch (Exception e) {
      keyStore.load(null, null);
      FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE);

      keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
      fos.close();
    } finally {
      if (fis != null) {
        fis.close();
      }
    }

    return keyStore;
  }

  public void saveUserInfo(KeyStore keyStore, String alias, String username, String password) throws Exception {
    String combinedCredentials = username + ":" + password;

    SecretKey secretKey = new SecretKeySpec(combinedCredentials.getBytes(), "AES");

    KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());
    KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(secretKey);

    keyStore.setEntry(alias, entry, param);

    try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
      keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
    }
  }

  public String loadCredentials(KeyStore keyStore, String alias) throws Exception {
    KeyStore.ProtectionParameter param = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());

    KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, param);
    SecretKey secretKey = entry.getSecretKey();

    String credentials = new String(secretKey.getEncoded());

    // credentials[0] là username và credentials[1] là password
    return credentials;
  }

  public String[] loadUserInfo(KeyStore keyStore, String alias) throws Exception {
    String credentials = loadCredentials(keyStore, alias);
    return credentials.split(":");
  }

  public void deleteUserInfo(KeyStore keyStore, String alias) throws Exception {
    keyStore.deleteEntry(alias);

    try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
      keyStore.store(fos, KEYSTORE_PASSWORD.toCharArray());
    }
  }
}
