package journal;

public class TextLogger implements ILogger {

	@Override
	public void clientConnected(String ip) {
		// TODO Auto-generated method stub
		System.out.println("clientConnected->:"+"ip :"+ip);
	}

	@Override
	public void clientDisconnected(String ip, String name) {
		// TODO Auto-generated method stub
		System.out.println("clientDisconnected->:"+"ip: "+ip+" name: "+name);
	}

	@Override
	public void clientGotName(String ip, String name) {
		// TODO Auto-generated method stub
		System.out.println("clientGotName->:"+"ip: "+ip+" name: "+name);
	}

	@Override
	public void clientGotCommand(String name, int command) {
		// TODO Auto-generated method stub
		System.out.println("clientGotCommand->: "+"name"+name+" command: "+command);
	}

	@Override
	public void systemMessage(String msg) {
		System.out.println("systemMessage->"+msg);
		// TODO Auto-generated method stub
	
	}
}
