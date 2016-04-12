package javafx.bookmanager.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.log4j.Logger;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.bookmanager.data.BookVO;
import javafx.bookmanager.dataprovider.DataProvider;
import javafx.bookmanager.model.BookManager;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class BookManagerController {

	private static final Logger LOG = Logger.getLogger(BookManagerController.class);
	private final DataProvider dataProvider = new DataProvider();

	@FXML
	private TextField titleField;
	@FXML
	private TextField authorField;
	@FXML
	private Button searchButton;
	@FXML
	private Button addButton;
	@FXML
	private TableView<BookVO> resultTable;
	@FXML
	private TableColumn<BookVO, Long> idColumn;
	@FXML
	private TableColumn<BookVO, String> titleColumn;
	@FXML
	private TableColumn<BookVO, String> authorColumn;

	private final BookManager model = new BookManager();

	public BookManagerController() {
		LOG.debug("Construct");
	}

	@FXML
	private void initialize() {
		LOG.debug("initialize()");

		initializeResultTable();

		titleField.textProperty().bindBidirectional(model.titleProperty());
		authorField.textProperty().bindBidirectional(model.authorProperty());
		resultTable.itemsProperty().bind(model.resultProperty());
	}

	private void initializeResultTable() {
		idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<Long>(cellData.getValue().getId()));
		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		authorColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthor()));
	}

	@FXML
	public void searchButtonAction() {
		LOG.debug("Search button");

		Task<Collection<BookVO>> backgroundTask = new Task<Collection<BookVO>>() {
			@Override
			protected Collection<BookVO> call() throws Exception {
				return dataProvider.findBooks(model.getTitle(), model.getAuthor());
			}

			@Override
			protected void succeeded() {
				model.setResult(new ArrayList<BookVO>(getValue()));
				resultTable.getSortOrder().clear();
			}
		};

		new Thread(backgroundTask).start();
	}

	@FXML
	public void addButtonAction() {
		LOG.debug("Add button");
		
		Task<BookVO> backgroundTask = new Task<BookVO>() {
			@Override
			protected BookVO call() throws Exception {
				return dataProvider.addBook(model.getTitle(), model.getAuthor());
			}

			@Override
			protected void succeeded() {
				model.setResult(Arrays.asList(getValue()));
				resultTable.getSortOrder().clear();
			}
		};

		new Thread(backgroundTask).start();
	}
}
