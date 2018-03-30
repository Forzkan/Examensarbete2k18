package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Rectangle;

public class ClickWithinBoundsAction extends ActionRobotBase implements IAction {

	private Rectangle bounds;
	private int x, y;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	
	public ClickWithinBoundsAction(Rectangle bounds) throws AWTException {
		super(EActionType.AUTOCLICK);
		this.bounds = bounds;
	}
	
	public void setClickPosition() {
		x = (int) (bounds.getX() + (bounds.getWidth() / 2));
		y = (int) (bounds.getY() + (bounds.getHeight() / 2));
	}
	
	// Could just as well do this in the constructor... but I figured since all other "Actions" do it this way..
	@Override
	public void actionSetup() {
		setClickPosition();
	}

	@Override
	public boolean performAction() {
		this.performClick(x, y);
		//Return true if OK.
		return true;
	}

}
