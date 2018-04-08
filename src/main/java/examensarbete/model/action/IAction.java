package examensarbete.model.action;

public interface IAction {
	public String toString();

	public void actionSetup();
	public boolean performAction();
	public EActionType getActionType();
}
