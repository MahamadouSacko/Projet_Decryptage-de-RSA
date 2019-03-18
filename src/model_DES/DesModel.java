package model_DES;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import rsa.Bloc;
import rsa.RsaEvent;

@SuppressWarnings("unused")
public class DesModel {
	@SuppressWarnings("unused")
	private String message;
	private String master;
	final private Map<String, DesEvent> desObserverList = new TreeMap<>();
	@SuppressWarnings("unused")
	final private  TreeMap<String,Bloc> usersThatRequestedComputations=new TreeMap<>();
	public DesModel(String message, String master) {
		super();
		this.message = message;
		this.master = master;
	}
	public synchronized void registerUser(String user, DesEvent handler) {
		desObserverList.put(user, handler);
		notifyUserListChanged();
	}
	private void notifyUserListChanged() {
		desObserverList.values().forEach(c -> c.dESUserListChanged("DES des "+master));
		
	}
	public synchronized Collection<String> userList() {
		return desObserverList.keySet();
	}
	public synchronized int userCount() {
		return desObserverList.size();
	}
	public synchronized boolean hasUserCalculate(String user) {
		if(desObserverList.containsKey(user))return true;
		return false;
	}
	public synchronized boolean hasUser(String user) {
		if(desObserverList.containsKey(user))return true;
		return false;
	}
}
