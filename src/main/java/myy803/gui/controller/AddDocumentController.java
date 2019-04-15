package myy803.gui.controller;

import myy803.gui.Controller;
import myy803.gui.views.AddDocumentView;
import myy803.model.DocumentType;

public interface AddDocumentController extends Controller<AddDocumentView> {

	@Override
	void initialize();

	void chooseAndLoadDocument();

	void loadDocument(String path);

	void createDocument();

	void onChangeDocTypeSelection(DocumentType docType);

	@Override
	AddDocumentView getView();

	@Override
	void setView(AddDocumentView view);
}
