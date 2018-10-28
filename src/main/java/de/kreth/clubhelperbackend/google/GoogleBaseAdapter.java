package de.kreth.clubhelperbackend.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar.Builder;
import com.google.api.services.calendar.CalendarScopes;

public abstract class GoogleBaseAdapter {

	private static final int GOOGLE_SECRET_PORT = 59431;

	/** Application name. */
	protected static final String APPLICATION_NAME = "ClubHelperVaadin";
	/** Directory to store user credentials for this application. */
	protected static final File DATA_STORE_DIR = new File(
			System.getProperty("catalina.base"), ".credentials");
	/** Global instance of the JSON factory. */
	protected static final JsonFactory JSON_FACTORY = JacksonFactory
			.getDefaultInstance();
	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 */
	static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);

	private static Credential credential;

	protected final Logger log = LoggerFactory
			.getLogger(getClass());
	/** Global instance of the {@link FileDataStoreFactory}. */
	protected final FileDataStoreFactory DATA_STORE_FACTORY;
	/** Global instance of the HTTP transport. */
	protected final HttpTransport HTTP_TRANSPORT;

	public GoogleBaseAdapter() throws GeneralSecurityException, IOException {
		super();
		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		if (DATA_STORE_DIR.mkdirs()) {
			log.info("created DATA_STORE_DIR: {}", DATA_STORE_DIR.getAbsolutePath());
		} else {
			log.trace("DATA_STORE_DIR already exists.");
		}
	}

	protected void checkRefreshToken(String serverName) throws IOException {
		synchronized (SCOPES) {
			if (credential == null) {
				credential = authorize(serverName);
			}
		}
		
		if ((credential.getExpiresInSeconds() != null
				&& credential.getExpiresInSeconds() < 3600)) {

			if (log.isDebugEnabled()) {
				log.debug("Security needs refresh, trying.");
			}
			boolean result = credential.refreshToken();
			if (log.isDebugEnabled()) {
				log.debug("Token refresh "
						+ (result ? "successfull." : "failed."));
			}
		}
	}

	public Builder createBuilder() {
		if (credential == null) {
			throw new IllegalStateException("credential is null, checkRefreshToken need to be called before.");
		}
		
		return new com.google.api.services.calendar.Calendar.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, credential);
	}
	
	public final boolean refreshToken() throws IOException {
		return credential.refreshToken();
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param request
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	private Credential authorize(String serverName)
			throws IOException {
		if (credential != null && (credential.getExpiresInSeconds() != null
				&& credential.getExpiresInSeconds() < 3600)) {
			credential.refreshToken();
			return credential;
		}
		// Load client secrets.
		InputStream in = getClass().getResourceAsStream("/client_secret.json");
		if (in == null) {
			File inHome = new File(System.getProperty("user.home"),
					"client_secret.json");
			if (inHome.exists()) {
				if (log.isInfoEnabled()) {
					log.info(
							"Google secret not found as Resource, using user Home file.");
				}
				in = new FileInputStream(inHome);
			} else {
				log.error(
						"Failed to load client_secret.json. Download from google console.");
				return null;
			}
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets
				.load(JSON_FACTORY, new InputStreamReader(in, Charset.defaultCharset()));
		if (log.isTraceEnabled()) {
			log.trace("client secret json resource loaded.");
		}

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
						.setDataStoreFactory(DATA_STORE_FACTORY)
						.setAccessType("offline").setApprovalPrompt("force")
						.build();

		if (log.isDebugEnabled()) {
			log.debug("Configuring google LocalServerReceiver on " + serverName
					+ ":" + GOOGLE_SECRET_PORT);
		}

		LocalServerReceiver localServerReceiver = new LocalServerReceiver.Builder()
				.setHost(serverName).setPort(GOOGLE_SECRET_PORT).build();

		Credential credential = new AuthorizationCodeInstalledApp(flow,
				localServerReceiver).authorize("user");
		if (log.isDebugEnabled()) {
			log.debug(
					"Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		}

		credential.setExpiresInSeconds(Long.valueOf(691200L));

		return credential;
	}

}