package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;
	
public class BrowserAction extends ActionBase{
	
	private String browserPath = "";
	
	// OBSERVE : THIS IS NOT REALLY USED ANYLONGER..
	public BrowserAction() throws AWTException {
		super();
		this.actionType = EActionType.BROWSER;
	}
	
	
	
	@Override
	public EActionType getActionType() {
		return this.actionType;
	}

	@Override
	public void actionSetup() {
		createTextInputDialog();
	}

	@Override
	public boolean performAction() {
		if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    try {
				Desktop.getDesktop().browse(new URI(browserPath));
				return true;
			} catch (IOException | URISyntaxException e) {
				System.out.println(e.getMessage());
				System.out.println("Could not open browser...");
			}
		}
		return false;
	}

	private void createTextInputDialog(){
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Text Input Dialog");
		dialog.setHeaderText("Please enter web page");
		dialog.setContentText("");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> browserPath = name);
	}
}
