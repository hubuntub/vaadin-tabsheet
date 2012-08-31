/**
 * 
 */
package vaadin.tabsheet.windows;

import com.vaadin.Application;
import com.vaadin.ui.Window;

/**
 * @author houbeb
 *
 */
public class ApplicationExample extends Application {

    private static final long serialVersionUID = 6062445632287310643L;

    @Override
    public void init() {
        Window mainWindow = new Window("Tabsheet <-> Windows Application");
        
        MultiMode multiMode = new MultiMode();
        for (int i=0; i<10; i++){
        	String caption = "composant " + String.valueOf(i);
        	multiMode.addTab(DummyComponents.getComponent(caption), caption);
        }
        multiMode.setSizeFull();
        
        mainWindow.addComponent(multiMode);
        setMainWindow(mainWindow);
    }
    
}
