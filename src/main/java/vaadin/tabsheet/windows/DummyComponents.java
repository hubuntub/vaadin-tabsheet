/**
 * 
 */
package vaadin.tabsheet.windows;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * @author houbeb
 *
 */
public class DummyComponents {

	public static Component getComponent(String data){
		VerticalLayout vl = new VerticalLayout();
		
		vl.addComponent(new Button(data));
		vl.addComponent(createTable(createContainer()));
		
		return vl;
	}
	  /* Build a model for the table with the data : Lots of ways of doing this;
	Just a quick-and-dirty example */
	    private static IndexedContainer createContainer() {
	      IndexedContainer container = new IndexedContainer();
	      container.addContainerProperty("title", String.class, null);
	      container.addContainerProperty("url", String.class, null);
	      container.addContainerProperty("height", Integer.class, null);
	      container.addContainerProperty("width", Integer.class, null);

	      addItem(container, "Sami Viitanen", "http://vaadin.com/vaadin-theme/images/company/personnel/alump.png", 121, 134);
	      addItem(container, "Anna Koskinen", "http://vaadin.com/vaadin-theme/images/company/personnel/anna.png", 121, 134);
	      addItem(container, "Artur Signell", "http://vaadin.com/vaadin-theme/images/company/personnel/artur.png", 121, 134);
	      return container;
	    }

	    private static void addItem(IndexedContainer container, String title, String url, int height, int width) {
	      Object itemId = container.addItem();
	      container.getItem(itemId).getItemProperty("title").setValue(title);
	      container.getItem(itemId).getItemProperty("url").setValue(url);
	      container.getItem(itemId).getItemProperty("height").setValue(height);
	      container.getItem(itemId).getItemProperty("width").setValue(width);
	    }

	    private static Table createTable(IndexedContainer container) {
	      Table table = new Table("Image Table", container);
	      table.setSizeFull();
	      table.addGeneratedColumn("image", new Table.ColumnGenerator() {
	        @Override
	        public Object generateCell(Table source, Object itemId, Object columnId) {
	          Item item = source.getItem(itemId);
	          String url = (String) item.getItemProperty("url").getValue();
	          int height = (Integer) item.getItemProperty("height").getValue();
	          int width = (Integer) item.getItemProperty("width").getValue();

	          return new Embedded("", new ExternalResource(url));

	        }

	      });
	      table.setVisibleColumns(new Object[]{"image", "title"});
	      table.setColumnExpandRatio("title", 1f);
	      return table;
	    }
}
