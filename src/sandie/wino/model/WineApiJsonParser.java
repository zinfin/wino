package sandie.wino.model;

import java.util.ArrayList;

public interface WineApiJsonParser {
	ArrayList<Category> parseCategories();
	ArrayList<sandie.wino.model.List> parseProducts();
}
