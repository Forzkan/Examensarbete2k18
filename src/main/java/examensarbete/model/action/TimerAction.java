package examensarbete.model.action;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import examensarbete.model.utility.WaitHandler;
import javafx.scene.control.TextInputDialog;

public class TimerAction extends ActionBase {

	private int seconds = 0;
	
	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	
	@JsonIgnore
	private String input = "";

	

	@Override
	public void actionSetup() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Wait Timer Input Dialog");
		dialog.setHeaderText("Please enter Wait Time in seconds.");
		dialog.setContentText("");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> input = name);
		
		try {
			int converted = Integer.parseInt(input);
			setSeconds(converted);
		}catch(NumberFormatException e) {
			// leave the default value of seconds, i.e 0. 
		}
	}

	@Override
	public boolean performAction() {
		WaitHandler.waitForMilliseconds(seconds * 1000); // Convert to miliseconds.
		return true;
	}



	
	public TimerAction() {
		super(EActionType.TIMER);
	}

}
