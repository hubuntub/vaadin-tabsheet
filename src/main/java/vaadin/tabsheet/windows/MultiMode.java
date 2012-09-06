package vaadin.tabsheet.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vaadin.tabsheet.windows.SizedWindowToolbar;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
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
 * MultiMode is a component that contains a tabsheet with multiple tabs we can
 * switch the tabs to windows
 * 
 * @author houbeb
 * 
 */
public class MultiMode extends VerticalLayout implements ClickListener{

	private static final long serialVersionUID = -3997898242138065868L;
	protected TabSheet tabsheet;
	protected TabSheet tabsheetFromWindows;

	private Map<String, List<Component>> mapComponentsByIcon;
	private Map<String, Component> mapCaptionComponents;

	private Map<Component, String> mapCaptionByComponent;
	private Map<Component, String> mapIconByComponent;

	private List<Component> listComponents;
	private int nbreWindows;
	private Button buttonTabs;
	private Button buttonWindows;

	/**
	 * build a verticalLayout containing a tabsheet with margins
	 */
	public MultiMode() {
		this(true);
	}

	/**
	 * build a verticalLayout containing a tabsheet
	 * 
	 * @param margin
	 */
	public MultiMode(boolean margin) {
		super();
		buildAndAddToolButtons();
		buildAndAddTabsheet();

		if (margin) {
			setMargin(true, true, true, true);
		}
		
		initMaps();
	}

	/**
	 * initialize the tabsheet
	 */
	private void buildAndAddTabsheet() {
		tabsheet = new TabSheet();
		tabsheet.setSizeFull();
		addComponent(tabsheet);
		setExpandRatio(tabsheet, 1);

	}

	/**
	 * build buttons windows, tabs to switch from one mode to another
	 */
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

	/**
	 * initialize differents maps
	 */
	private void initMaps() {
		listComponents = new ArrayList<Component>();
		mapComponentsByIcon = new LinkedHashMap<String, List<Component>>();
		mapCaptionComponents = new LinkedHashMap<String, Component>();

		mapCaptionByComponent = new LinkedHashMap<Component, String>();
		mapIconByComponent = new LinkedHashMap<Component, String>();
	}

	/**
	 * feed the map components by caption
	 * 
	 * @param caption
	 * @param c
	 */
	private void alimenteSuperMap(String caption, Component c) {
		if (!mapComponentsByIcon.containsKey(caption)) {
			mapComponentsByIcon.put(caption, new ArrayList<Component>());
		}
		mapComponentsByIcon.get(caption).add(c);
	}

	/**
	 * add tabs to the tabsheet icon must be not null
	 * 
	 * @param c
	 * @param caption
	 * @param icon
	 * @return
	 */
	public Tab addTab(Component c, String caption, Resource icon) {
		Tab t = tabsheet.addTab(c, caption, icon);
		listComponents.add(c);
		mapCaptionComponents.put(caption, c);

		alimenteSuperMap(icon.toString(), c);
		mapCaptionByComponent.put(c, caption);
		mapIconByComponent.put(c, icon.toString());

		return t;
	}

	/**
	 * remove the components from the tabsheet
	 */
	public void removeAllTabsContent() {
		initMaps();
		tabsheet.removeAllComponents();
	}

	/**
	 * remove the tabsheet and the components
	 */
	public void removeAllTabs() {
		tabsheet.removeAllComponents();
		this.removeComponent(tabsheet);
	}

	/**
	 * Build windows regrouped by the iconPath of different tabs
	 */
	public void switchToWindows() {

		nbreWindows = 0;
		// Move components in the right window
		for (Component c : listComponents) {
			// final Component c = i.next();
			String caption = null;
			Resource icon = null;
			if (TabSheet.class.isAssignableFrom(tabsheet.getClass())) {
				caption = ((TabSheet) tabsheet).getTabCaption(c);
				icon = ((TabSheet) tabsheet).getTabIcon(c);
			}

			SizedWindowToolbar w = new SizedWindowToolbar(caption);
			w.setIcon(icon);
			nbreWindows++;
			// set the positions of windows so that they can be visble
			w.setPositionX(SizedWindowToolbar.POSITIONS_X_Y[nbreWindows].getX());
			w.setPositionY(SizedWindowToolbar.POSITIONS_X_Y[nbreWindows].getY());
			w.setWidth("400px");
			w.setHeight("300px");

			VerticalLayout vl = new VerticalLayout();
			vl.setSizeFull();
			vl.addComponent(c);
			w.setContent(vl);
			getApplication().getMainWindow().addWindow(w);
		}
		this.replaceComponent(tabsheetFromWindows, tabsheet);
		tabsheet.setVisible(false);
	}

	/**
	 * remove all windows
	 */
	public void removeAllWindows() {
		for (Window w : new ArrayList<Window>(getWindow().getChildWindows())) {
			getWindow().removeWindow(w);
		}

	}

	/**
	 * rebuild the tabsheet from tha listComponents
	 */
	public void switchToTabs() {
		tabsheetFromWindows = new TabSheet();
		tabsheetFromWindows.setSizeFull();
		for (Component c : listComponents) {
			String caption = mapCaptionByComponent.get(c);
			Resource icon = new ThemeResource(mapIconByComponent.get(c));
			c.setParent(null);
			tabsheetFromWindows.addTab(c, caption, icon);
		}
		this.replaceComponent(tabsheet, tabsheetFromWindows);
		tabsheetFromWindows.setVisible(true);
		this.setExpandRatio(tabsheetFromWindows, 1);
		
	}

	/**
	 * used to notify the view that a tab has been selected
	 * 
	 * @param caption
	 */
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
