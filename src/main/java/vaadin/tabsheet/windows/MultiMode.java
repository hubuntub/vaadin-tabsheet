package vaadin.tabsheet.windows;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 */

/**
 * @author houbeb
 *
 */
public class MultiMode extends VerticalLayout implements ClickListener{

	private static final long serialVersionUID = -3997898242138065868L;
	protected TabSheet tabsheet;

    private Map<String, List<Component>> mapComponentsByIcon;
    private Map<String, Component> mapCaptionComponents;

    private Map<Component, String> mapCaptionByComponent;
    private Map<Component, String> mapIconByComponent;

    private List<Component> listComponents;
    private int nbreWindows;
	private Button buttonTabs;
	private Button buttonWindows;
	
	public MultiMode(){
		this(true);
	}
	public MultiMode(boolean margin){
		super();
		buildAndAddToolButtons();
		buildAndAddTabsheet();
        
        if (margin) {
            setMargin(true, true, true, true);
        }
        
        initMaps();
	}
	private void buildAndAddTabsheet() {
		tabsheet = new TabSheet();
        tabsheet.setSizeFull();
        addComponent(tabsheet);
        setExpandRatio(tabsheet, 1);
		
	}
	private void buildAndAddToolButtons() {
		HorizontalLayout toolButton = new HorizontalLayout();
		toolButton.setHeight("50px");
		toolButton.setWidth("100%");
		
		buttonTabs = new Button("Tabs");
		buttonWindows = new Button("Windows");
		buttonTabs.addListener((ClickListener) this);
		buttonWindows.addListener((ClickListener) this);
		
		toolButton.addComponent(buttonTabs);
		toolButton.addComponent(buttonWindows);
		
		addComponent(toolButton);
		
	}
	private void initMaps() {
        listComponents = new ArrayList<Component>();
        mapComponentsByIcon = new LinkedHashMap<String, List<Component>>();
        mapCaptionComponents = new LinkedHashMap<String, Component>();

        mapCaptionByComponent = new LinkedHashMap<Component, String>();
        mapIconByComponent = new LinkedHashMap<Component, String>();
    }

    private void alimenteSuperMap(String caption, Component c) {
        if (!mapComponentsByIcon.containsKey(caption)) {
            mapComponentsByIcon.put(caption, new ArrayList<Component>());
        }
        mapComponentsByIcon.get(caption).add(c);
    }

    public Tab addTab(Component c, String caption, Resource icon) {
        Tab t = tabsheet.addTab(c, caption, icon);
        listComponents.add(c);
        mapCaptionComponents.put(caption, c);

        alimenteSuperMap(icon.toString(), c);
        mapCaptionByComponent.put(c, caption);
        mapIconByComponent.put(c, icon.toString());

        return t;
    }
    public Tab addTab(Component c, String caption) {
        Tab t = tabsheet.addTab(c, caption);
        listComponents.add(c);
        mapCaptionComponents.put(caption, c);
        
        mapCaptionByComponent.put(c, caption);
        
        return t;
    }

    public void removeAllTabsContent() {
        initMaps();
        tabsheet.removeAllComponents();
    }

    public void removeAllTabs() {
        tabsheet.removeAllComponents();
        this.removeComponent(tabsheet);
    }

    public void switchToWindows() {
       
        nbreWindows = 0;
        Map<String, TabSheet> tabSheets = new HashMap<String, TabSheet>();
        // Init tabsheets
        for (String iconPath : mapComponentsByIcon.keySet()) {
            TabSheet newTabsheet = new TabSheet();
            newTabsheet.addStyleName(Reindeer.TABSHEET_SMALL);
            newTabsheet.setSizeFull();
            tabSheets.put(iconPath, newTabsheet);
            newTabsheet.requestRepaintAll();
        }
        
        // Move components in the right tabsheet
        for (Component c : listComponents) {
            // final Component c = i.next();
            String caption = null;
            Resource icon = null;
            if (TabSheet.class.isAssignableFrom(tabsheet.getClass())) {
                caption = ((TabSheet) tabsheet).getTabCaption(c);
                icon = ((TabSheet) tabsheet).getTabIcon(c);
            }
                tabsheet.removeComponent(c);
                // unless it is a table, tabsheet shows nothing so we add a notifiqueComponent
//                if (!(c instanceof Table)){
//                	c = new NotifiqueComponent();
//					((NotifiqueComponent) c).build("Pas de données");
//                }
                tabSheets.get(icon.toString()).addTab(c, caption, icon);
        }
        // Create windows
        for (String iconPath : tabSheets.keySet()) {
            SizedWindowToolbar w = new SizedWindowToolbar("Regroupement de pdls par énergie");
            Resource iconEnergy = new ThemeResource(iconPath);
            w.setIcon(iconEnergy);
            w.setStyleName("sizedWindowToolbar");
//            // pour avoir des fenetres en cascade
//            // FIXME trouver un autre moyen de faire
//            w.setPositionX(SizedWindowToolbar.POSITIONS_X_Y[nbreWindows].getX());
//            w.setPositionY(SizedWindowToolbar.POSITIONS_X_Y[nbreWindows].getY());
            w.setWidth("400px");
            w.setHeight("300px");
            // Add tabsheet to windows
            w.setContent(tabSheets.get(iconPath));
            getApplication().getMainWindow().addWindow(w);
        }
        this.removeComponent(tabsheet);
    }

    public void removeAllWindows() {
        for (Window w : new ArrayList<Window>(getWindow().getChildWindows())) {
            getWindow().removeWindow(w);
        }

    }

    public void switchToTabs() {
        for (Component c : listComponents) {
            String caption = mapCaptionByComponent.get(c);
            Resource icon = new ThemeResource(mapIconByComponent.get(c));
            c.setParent(null);
            tabsheet.addTab(c, caption, icon);
        }
        tabsheet.requestRepaint();
        this.addComponent(tabsheet);
        this.setExpandRatio(tabsheet, 1);
    }

    public void selectTabByCaption(String caption) {
        tabsheet.setSelectedTab(mapCaptionComponents.get(caption));
    }
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == buttonWindows) {
			switchToWindows();
		} else if (event.getButton() == buttonTabs) {
			removeAllWindows();
			switchToTabs();
		}
	}
}
