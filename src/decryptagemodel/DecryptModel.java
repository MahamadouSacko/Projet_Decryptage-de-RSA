package decryptagemodel;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import rsa.Bloc;
import rsa.RsaModel;

public class DecryptModel {
	private static final TreeMap<String, ModelEvents> clientList = new TreeMap<>();
	private static final TreeMap<String,RsaModel> requestForRsa=new TreeMap<>();
	public static synchronized boolean registerUser(String name, ModelEvents client) {
		if (!existUserName(name) && !name.equals("")) {
			clientList.put(name, client);
			notifyNewName();
			return true;
		}
		return false;
	}
	public static synchronized void unregisterUser(String name) {
		if (existUserName(name)) {
			clientList.remove(name);
			notifyNewName();
		}

	}
	public static synchronized boolean existUserName(String name) {
		return clientList.containsKey(name);

	}
	public static synchronized Set<String> getUserNames(){
		return clientList.keySet();
	}
	public static synchronized boolean ASKDcryptRsa(String master, String lengthOfKey, String KeyE,String KeyN) {
		if ((rsaSize()<=0) && existUserName(master)) {
			requestForRsa.put("RSA",new RsaModel (master,lengthOfKey,KeyE,KeyN,clientList.get(master)));
			notifyRSAloading();
			return true;
		}
		return false;
	}
	public static synchronized int rsaSize(){
		return requestForRsa.size();
	}
	public static synchronized void requestForRsa(String user){
		if (!existUserName(user)) {
			return;
		}
		requestForRsa.get("RSA").registerUser(user, clientList.get(user));
	}
	public static synchronized boolean requestForRSA(String user){
		if (!existUserName(user)||requestForRsa.get("RSA").hasUserCalculate(user)) {
			return false;
		}
		if(requestForRsa.get("RSA").hasUser(user)) {
			requestForRsa.get("RSA").demandeDeCalcule(user);
			return true;
		}
		return false;
	}
	public static synchronized boolean userForRSa(String user,Bloc b){
		if(requestForRsa.get("RSA").hasUser(user)) {
			if(requestForRsa.get("RSA").hasUserCalculate(user)) {
				requestForRsa.get("RSA").unregisterUser(user,b);
				return true;
			}else {
				requestForRsa.get("RSA").unregisterUser(user);
				return false;
			}
			
		}else {
			return false;
		}
		
	}

	public static synchronized void endCalcul(String user,Bloc b){
		if (!existUserName(user)) {
			return;
		}
		if(requestForRsa.get("RSA").hasUser(user))requestForRsa.get("RSA").requestTreated(user, b);
	}
	public static synchronized void pAndqFind(String user,BigInteger p,BigInteger q){
		if (!existUserName(user)) {
			return;
		}
		if(requestForRsa.get("RSA").hasUser(user))requestForRsa.get("RSA").QetPtrauve(p,q);
	}
	private static void notifyNewName() {
		clientList.values().forEach(ModelEvents::userListChanged);
	}
	public static synchronized Collection<String> RsaGetUserList(){
		if(rsaSize()>=1) {
			return requestForRsa.get("RSA").userList();
		}
		return null;
	}
	public static void clearAll() {
		clientList.values().forEach(c->c.shutdownRequested());
		clientList.clear();
	}


	private static synchronized void notifyRSAloading(){
		clientList.values().forEach(ModelEvents::rsaListLoad);
	}
	public static synchronized void deletRSA(){
		requestForRsa.remove("RSA");
	}
}
