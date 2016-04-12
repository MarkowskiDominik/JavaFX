package javafx.bookmanager.dataprovider;

import java.io.IOException;
import java.util.ArrayList;
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

import com.google.gson.JsonObject;

import javafx.bookmanager.data.BookVO;

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
	
	private enum Type {
		ADD, FIND
	}
	
	JsonObject json;

	public Collection<BookVO> findBooks(String title, String authors) {
		json = new JsonObject();		
		json.addProperty("title", title);
		json.addProperty("authors", authors);

		System.out.println(httpRequest(json, Type.FIND));
		return new ArrayList<>();
	}
	
	public BookVO addBook(String title, String authors) {
		json = new JsonObject();		
		json.addProperty("title", title);
		json.addProperty("authors", authors);

		System.out.println(httpRequest(json, Type.ADD));
		return new BookVO(1L,title,authors);
	}

	private String httpRequest(JsonObject jsonObject, Type type) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpPost request;
			if (Type.ADD.equals(type)) {
				request = new HttpPost("http://localhost:9721/workshop/book");
			}
			else {
				request = new HttpPost("http://localhost:9721/workshop/books");
			}
			request.addHeader("content-type", "application/json");
			System.out.println(jsonObject.toString());
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

}
