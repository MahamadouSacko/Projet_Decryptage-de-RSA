package journal;

public interface ILogger {
	public void clientConnected(String ip);
	public void clientDisconnected(String ip, String name);
	public void clientGotName(String ip, String name);
	public void clientGotCommand(String name, int command);
	public void systemMessage(String msg);
}
