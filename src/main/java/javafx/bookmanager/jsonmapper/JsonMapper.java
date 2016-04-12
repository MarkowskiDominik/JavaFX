package javafx.bookmanager.jsonmapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.bookmanager.data.BookVO;

public class JsonMapper {

	public JsonObject map2Json(String title, String authors) {
		JsonObject jsonObject = new JsonObject();		
		jsonObject.addProperty("title", title);
		jsonObject.addProperty("authors", authors);
		return jsonObject;
	}

	public BookVO map2BookVO(String jsonString) {
		JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();	

		return new BookVO(jsonObject.get("id").getAsLong(), jsonObject.get("title").getAsString(), jsonObject.get("authors").getAsString());
	}
	
	public Collection<BookVO> parseResponse(String response) {
		JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
		
		List<BookVO> list = new ArrayList<BookVO>();
		for (JsonElement jsonElement : jsonArray) {
			list.add(map2BookVO(jsonElement.toString()));
		}
		return list;
	}
}
