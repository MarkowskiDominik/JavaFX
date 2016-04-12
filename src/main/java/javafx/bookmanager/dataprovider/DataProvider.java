package javafx.bookmanager.dataprovider;

import java.io.IOException;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import javafx.bookmanager.data.BookVO;
import javafx.bookmanager.jsonmapper.JsonMapper;

public class DataProvider {
	/*
	 * Find books by criteria
	 * http://localhost:9721/workshop/books POST
	 * JSON: { "title": "", "authors": "" }
	 * 
	 * Add new book
	 * http://localhost:9721/workshop/book POST
	 * JSON: { "title": "", "authors": "" }
	 */

	private static final Logger LOG = Logger.getLogger(DataProvider.class);
	JsonMapper jsonMapper = new JsonMapper();
	
	private enum Type {
		ADD, FIND
	}
	
	public Collection<BookVO> findBooks(String title, String authors) {
		LOG.debug("findBooks()");
		
		return jsonMapper.parseResponse(httpRequest(jsonMapper.map2Json(title, authors), Type.FIND));
	}
	
	public BookVO addBook(String title, String authors) {
		LOG.debug("addBooks()");
		
		return jsonMapper.map2BookVO(httpRequest(jsonMapper.map2Json(title, authors), Type.ADD));
	}

	private String httpRequest(JsonObject jsonObject, Type type) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			LOG.debug("httpRequest()");			
			
			HttpPost request = new HttpPost(getAddress(type));
			request.addHeader("content-type", "application/json");
			request.setEntity(new StringEntity(jsonObject.toString()));

			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			return httpClient.execute(request, responseHandler);
		} catch (Exception ex) {
			
		}
		return null;
	}
	
	private String getAddress(Type type) {
		String address;
		switch (type) {
		case ADD:
			address = "http://localhost:9721/workshop/book";
			break;
		case FIND:
			address = "http://localhost:9721/workshop/books";
			break;
		default:
			address = "http://localhost:9721/workshop/book";
		}
		return address;
	}

}
