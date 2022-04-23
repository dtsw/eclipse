package company.eventprocessor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class HttpConnection {

	/**
	 * LOGGER is an instance of the Logger class so that we can do proper logging
	 */
	static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
	private static final Logger LOGGER = Logger.getLogger(HttpConnection.class.getName());

	private final String USER_AGENT = "Mozilla/5.0";

	public boolean sendGet(String message, String oid, String number) throws Exception {

		Properties props = Configuration.getInstance().getProperties();

		LOGGER.fine("message: " + message);
		LOGGER.fine("oid: " + oid);
		LOGGER.fine("number: " + number);

		String url = props.getProperty("sms_url");
		url = url + number;
		url = url + "&oid=";
		url = url + URLEncoder.encode(oid, java.nio.charset.StandardCharsets.UTF_8.toString());
		url = url + "&message=";
		// url = url + URLEncoder.encode(message,
		// java.nio.charset.StandardCharsets.ISO_8859_1.toString());
		url = url + URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8.toString());

		LOGGER.fine("URL: " + url);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		LOGGER.fine("\nSending 'GET' request to URL : " + url);
		LOGGER.info("Response Code : " + responseCode);

		return (responseCode == 200);
	}

	public boolean sendGet(String message, String number) throws Exception {
		return sendGet(message, "not_set", number);
	}

	@SuppressWarnings("unused")
	private void printResponse(HttpURLConnection con) throws Exception {

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		LOGGER.fine(response.toString());

	}

	@SuppressWarnings("unused")
	private void sendPost() throws Exception {

		Properties params = new Properties();
		params.setProperty("event_class", "");
		params.setProperty("mc_host", "");
		params.setProperty("mc_object", "");
		params.setProperty("mc_object_class", "");
		params.setProperty("severity", "");
		params.setProperty("mc_host_class", "");
		params.setProperty("mc_parameter", "");
		params.setProperty("mc_parameter_value", "");
		params.setProperty("event_message", "");
		params.setProperty("mc_priority", "");
		params.setProperty("hel_app_level_1", "");

		String postParamString = getQuery(params);
		System.out.println(postParamString);

		Properties props = Configuration.getInstance().getProperties();
		String url = props.getProperty("sms_url");
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream((con.getOutputStream()));
		dos.writeBytes(postParamString);
		dos.flush();
		dos.close();
		int responseCode = con.getResponseCode();
		System.out.println("Response code : " + responseCode);

	}

	private String getQuery(Properties params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		Enumeration<?> e = params.propertyNames();
		while (e.hasMoreElements()) {
			if (first)
				first = false;
			else
				result.append("&");
			String key = (String) e.nextElement();
			result.append(URLEncoder.encode(key, "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(params.getProperty(key), "UTF-8"));
		}

		return result.toString();
	}
}
