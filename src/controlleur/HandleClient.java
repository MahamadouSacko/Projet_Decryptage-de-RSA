package controlleur;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

import decryptagemodel.DecryptModel;
import decryptagemodel.ModelEvents;
import entrées.Input;
import journal.ILogger;
import rsa.Bloc;
import sortie.Output;
import sortie.Protocol;

public class HandleClient implements  Runnable, Protocol, ModelEvents{
	private final Socket s;
	private Output cho;
	private Input chi;
	private String name = "";
	private ILogger logger = null;
	private BigInteger debut;
	private BigInteger fin;
	private BigInteger KeyN;
	private Bloc bloc;
	private Thread tache ;
	private enum ClientState { ST_INIT, ST_NORMAL };

	private ClientState state = ClientState.ST_INIT;
	private boolean stop = false;
	private boolean stopcalcule = false;
	public HandleClient(Socket s, ILogger logger) throws IOException{
		this.s = s;
		this.logger = logger;
	}

	@Override
	public void run(){
		try (Socket s1 = s) {
			cho = new Output(s1.getOutputStream());
			chi = new Input(s1.getInputStream(), this);
			chi.doRun();
		} catch (IOException ex) {
			if (!stop) {
				finish();
			}
		}
	}

	public void sendName(String name){
		String newName = name;
		if (DecryptModel.existUserName(newName)) {
			cho.sendNameBad();
		} else {
			if (state == ClientState.ST_INIT) {
				DecryptModel.registerUser(newName, this);
				state = ClientState.ST_NORMAL;
				this.name = newName;
				cho.sendNameOK();
				logger.clientGotName(s.toString(), name);
			} 			
		}
	}

	public void askUList(){
		if (state == ClientState.ST_INIT) return;
		cho.sendUserList(DecryptModel.getUserNames());
	}
	public synchronized void finish(){
		if (!stop) {
			stop = true;
			try {
				s.close();
			} catch (IOException ex) { ex.printStackTrace();}
			if (name != null)
				DecryptModel.unregisterUser(name);
			if(DecryptModel.rsaSize()>=1) {
				DecryptModel.userForRSa(name,bloc);
             
			}
			logger.clientDisconnected(s.toString(), name);
		}
	}
	@Override
	public void shutdownRequested() {
		finish();
	}
	@Override
	public void sendForDcryptRsa(String lengthOfKey, String KeyE,String KeyN){
		if (state == ClientState.ST_INIT) return;
		if(DecryptModel.ASKDcryptRsa(name,lengthOfKey,KeyE,KeyN)){
		}else {
			cho.sendRSABad();
		}

	}

	@Override
	public void rsaUserListChanged(String room){
		cho.sendRSAUsersList(room, DecryptModel.RsaGetUserList());
	}
	@Override
	public void startRsaCalcule(String from,Bloc bloc, BigInteger KeyN){
		if(from.equals(name)) {
			this.KeyN=KeyN;
			this.debut=bloc.getDebut();
			this.fin=bloc.getFin();
			this.bloc=bloc;
			stopcalcule=false;
			tache=new Thread(new calcule());
			tache.start();
		}
	}
	@Override
	public void sendJoinRSA(){
		if (state == ClientState.ST_INIT) {
			return;
		}
		if(DecryptModel.rsaSize()>=1) {
			DecryptModel.requestForRsa(name);

		} else {
			cho.sendEror( "RSA NOT initialized");
		}
	}

	@Override
	public void sendRequestRSA() {
		if (state == ClientState.ST_INIT) {
			return;
		}
		if(DecryptModel.rsaSize()>=1) {
			if(DecryptModel.requestForRSA(name)) {
				cho.sendCalculEncour();
			}else {
				cho.sendEror("calcul deja en cour ou vous n'etais pas n'inscrie");
			}
		} else {
			cho.sendEror( "RSA NOT initialized");
		}

	}

	@Override
	public void stopCalcule(boolean stats) {
		stopcalcule=stats;
		cho.sendRSAmessage("p et q sont trouves merci de votre participation");
	}
	@Override
	public void rsaListLoad() {
		cho.sendCreateRSA();
	}
	@Override
	public void userListChanged(){
		cho.sendUserList(DecryptModel.getUserNames());
	}

	@Override
	public void sendAskUserList(){
		askUList();
	}
	@Override
	public void sendQuit(){
		cho.sendQuit();
		shutdownRequested();
	}
	@Override
	public void endRsacalcul(){
		if (state == ClientState.ST_INIT) {
			return;
		}
		if(DecryptModel.rsaSize()>=1) {
			DecryptModel.endCalcul(name,bloc);
			cho.sendFinduCacule();
		} else {
			cho.sendEror( "RSA NOT initialized");
		}
	}
	@Override
	public void finDucalcule(String from, BigInteger KeyE, BigInteger D) {
		if(name.equals(from)) {
			cho.sendRSAmessage("cle trouve D:"+D+" E:"+KeyE);
		}


	}
	@Override
	public void sendvaleurTrouve(BigInteger p,BigInteger q){
		if (state == ClientState.ST_INIT) {
			return;
		}
		if(DecryptModel.rsaSize()>=1) {
			cho.sendFinduCacule();
			DecryptModel.pAndqFind(name,p,q);

		} else {
			cho.sendEror("RSA NOT initialized");
		}
	}
	private class calcule implements Runnable{
		public calcule() {
		}
		@Override
		public void run() {
			if(debut.equals(BigInteger.ONE)) {
				debut=new BigInteger("2");
			}
			boolean find=false;
			while(debut.compareTo(fin)<=0) {
				if(stopcalcule)break;
				if(estDiviseur(KeyN,debut)){
					sendvaleurTrouve(debut,KeyN.divide(debut));
					find=true;
					break;
				}
				debut=debut.add(BigInteger.ONE);
			}
			if(!find)endRsacalcul();
		}

		public boolean estDiviseur( BigInteger p, BigInteger q){
			if(p.mod(q).equals(BigInteger.ZERO)) {
				return true;
			}
			else return false;
		}
	}

}

