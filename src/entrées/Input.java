package entrées;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import buffere.LineBufferedInputStream;
import sortie.Protocol;

public class Input {
	Protocol handler;
	boolean stop=false;//attention
	InputStream in;
	public Input(InputStream in, Protocol handler) throws IOException {
		this.in = in;
		this.handler = handler;
	}
	public void doRun() throws IOException {
		String  strName,lengthOfKey,KeyE,KeyN;
		ArrayList <String> userList;
		try (LineBufferedInputStream is = new LineBufferedInputStream(in)) {
			while (!stop) {
				String line = is.readLine();
				switch (line) {
				case "NAME":
					strName = is.readLine();
					handler.sendName(strName);
					break;
				case "ULIST":
					userList = new ArrayList<>();
					String x;
					while (!(x = is.readLine()).equals(".")) {
						userList.add(x);
					}
					handler.sendUserList(userList);
					break;
				case "AULIST":
					handler.sendAskUserList();
					break;
				case "QUIT":
					handler.sendQuit();
					break;
				case "KEY RSA":
					lengthOfKey = is.readLine();
					KeyE = is.readLine();
					KeyN=is.readLine();
					handler.sendForDcryptRsa(lengthOfKey,KeyE,KeyN);
					break;
				case "JOIN RSA":
					handler.sendJoinRSA();
					break;
				case "REQUEST RSA":
					handler.sendRequestRSA();
					break;
				default:
					throw new ProtocolException("Invalid input");
				}
			}
		}
	}


}
