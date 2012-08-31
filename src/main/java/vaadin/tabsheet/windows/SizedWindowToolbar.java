package vaadin.tabsheet.windows;
/**
 * 
 */


import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author BEN OTHMENE HOUBEB
 * 
 */
public class SizedWindowToolbar extends Window {

    private static final long serialVersionUID = 1L;
    private float lastWidth;
    protected float lastHeight;
    protected int lastX;
    protected int lastY;
    private HorizontalLayout tools;
    private Button buttonMax;

    public SizedWindowToolbar(String value) {
        super(value);
        buildTools();
    }

    public SizedWindowToolbar() {
        this(null);

    }

    private void buildTools() {
        tools = new HorizontalLayout();
        tools.addStyleName(Reindeer.LAYOUT_BLACK);
        tools.setSpacing(true);
        buttonMax = new Button(null, (Button.ClickListener) this);
        buttonMax.setDescription("Maximize");
        buttonMax.setIcon(new ThemeResource("window/grey/maximize.png"));
        buttonMax.addStyleName(Reindeer.BUTTON_LINK);

        tools.addComponent(buttonMax);

    }

   
}
