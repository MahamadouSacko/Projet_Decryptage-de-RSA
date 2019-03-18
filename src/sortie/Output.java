package sortie;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;


public class Output implements Protocol {
	PrintWriter os;
	OutputStream oso;
	public Output(OutputStream out) throws IOException {
		this.os = new PrintWriter(out, true);
		this.oso=out;
	}
	public synchronized void sendName(String name) {
		os.println("NAME");
		os.println(name);
	}
	public synchronized void sendUserList(Collection<String> ulist) {
		os.println("ULIST");
		ulist.forEach(os::println);
		os.println(".");

	}
	public synchronized void sendAskUserList() {
		os.println("AULIST");
	}
	public synchronized void sendQuit() {
		os.println("QUIT");
	}
	public synchronized void sendNameOK() {
		os.println("NAME OK");

	}
	public synchronized void sendNameBad() {
		os.println("NAME BAD");

	}
	@Override
	public void sendCreateRSA() {
		os.println("RSA loaded");
	}

	@Override
	public void sendRSAOK() {
		os.println("RSA OK");
	}

	@Override
	public void sendRSABad() {
		os.println("RSA BAD");
	}
	@Override
	public void sendRSAUsersList(String name, Collection<String> ulist) {
		os.println("RSA_ULIST");
		os.println(name);
		ulist.forEach(os::println);
		os.println(".");
	}
	@Override
	public void sendCalculEncour() {
		os.println("calcule en cours");
	}
	@Override
	public void sendFinduCacule() {
		os.println("calcule termine");
	}
	@Override
	public void sendEror(String msg) {
		os.println("ERR");
		os.println(msg);
	}
	@Override
	public void sendRSAmessage(String msg) {
		os.println("RSAmsg: "+msg);
	}
	@Override
	public void sendRequestRSA() {
		// TODO Auto-generated method stub
		
	}
}
