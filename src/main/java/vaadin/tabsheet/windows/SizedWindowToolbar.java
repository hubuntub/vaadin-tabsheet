package vaadin.tabsheet.windows;


import vaadin.tabsheet.windows.Coordinates;

import com.vaadin.ui.Window;


/**
 * @author BEN OTHMENE HOUBEB
 * 
 */
public class SizedWindowToolbar extends Window {

    private static final long serialVersionUID = 1L;
    
    public static Coordinates[] POSITIONS_X_Y = new Coordinates[] {
        new Coordinates(600, 150), new Coordinates(650, 200),
        new Coordinates(700, 250), new Coordinates(750, 300),
        new Coordinates(800, 250), new Coordinates(850, 300),
        new Coordinates(900, 350), new Coordinates(950, 400),
        new Coordinates(1000, 450) };
    
    protected float lastHeight;
    protected int lastX;
    protected int lastY;

    public SizedWindowToolbar(String value) {
        super(value);
    }

    public SizedWindowToolbar() {
        this(null);

    }


   
}
