package javafx.bookmanager.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.bookmanager.data.BookVO;
import javafx.collections.FXCollections;

public class BookManagerModel {

	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty authors = new SimpleStringProperty();
	private final ListProperty<BookVO> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public String getTitle() {
		return title.get();
	}

	public final void setTitle(String title) {
		this.title.set(title);
	}

	public StringProperty titleProperty() {
		return title;
	}

	public String getAuthors() {
		return authors.get();
	}

	public final void setAuthors(String authors) {
		this.authors.set(authors);
	}

	public StringProperty authorsProperty() {
		return authors;
	}

	public List<BookVO> getResult() {
		return result.get();
	}

	public final void setResult(List<BookVO> result) {
		this.result.setAll(result);
	}

	public ListProperty<BookVO> resultProperty() {
		return result;
	}
}
