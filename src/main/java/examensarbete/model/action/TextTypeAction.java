package examensarbete.model.action;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;

public class TextTypeAction extends ActionRobotBase implements IAction {

	private String text;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	
	
	public TextTypeAction() throws AWTException {
		super(EActionType.TYPE);
	}

	@Override
	public void actionSetup() {
		createTextInputDialog();
	}

	@Override
	public boolean performAction() {
		
		char charArray[] = text.toCharArray();
		for(char c : charArray) {
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
			robot.keyPress(keyCode);
		}
		return true;
	}
	
	
	private void createTextInputDialog(){
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Text Input Dialog");
		dialog.setHeaderText("Please enter text");
		dialog.setContentText("");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> text = name);
	}

}
