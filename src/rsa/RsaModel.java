package rsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import decryptagemodel.DecryptModel;

public class RsaModel {
	private String master;
	//private BigInteger lengthOfKey;
	private BigInteger KeyE;
	private BigInteger KeyN;
	private BigInteger P;
	private BigInteger Q;
	private int taille=10;
	private ArrayList<Bloc> elem=new ArrayList<>();
	final private Map<String, RsaEvent> rsaObserverList = new TreeMap<>();
	final private  TreeMap<String,Bloc> usersThatRequestedComputations=new TreeMap<>();
	public RsaModel(String master, String lengthOfKey, String keyE, String keyN, RsaEvent handler) {
		this.master = master;
		//this.lengthOfKey = new BigInteger(lengthOfKey);
		this.KeyE = new BigInteger(keyE);
		this.KeyN = new BigInteger(keyN);
		rsaObserverList.put(master, handler);
		spliteBloc(sqrt(KeyN));
	}

	public void spliteBloc(BigInteger n){
		BigInteger x=n.divide(new BigInteger(""+taille));
		BigInteger elemBloc=new BigInteger("1");
		BigInteger prec;
		for(int i=0;i<taille;i++) {
			prec=elemBloc;
			elemBloc=elemBloc.add(x);
			Bloc a=new Bloc(prec, elemBloc,false,""+i,false);
			elem.add(a);
		}
	}
	public BigInteger sqrt(BigInteger n) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
		while (b.compareTo(a) >= 0) {
			BigInteger mid = a.add(b).shiftRight(1);
			if (mid.multiply(mid).compareTo(n) > 0) {
				b = mid.subtract(BigInteger.ONE);
			} 
			else {
				a = mid.add(BigInteger.ONE);
			}
		}
		return a.subtract(BigInteger.ONE);
	}
	private void notifyUserListChanged() {
		rsaObserverList.values().forEach(c -> c.rsaUserListChanged("RSA des "+master));
		
	}
	public synchronized void registerUser(String user, RsaEvent handler) {
		rsaObserverList.put(user, handler);
		notifyUserListChanged();
	}
	public synchronized void demandeDeCalcule(String user) {
		for (int i = 0; i <elem.size(); i++) {
			if(elem.get(i).getTreat()==false) {
				if(elem.get(i).getAllocate()==false) {
					elem.get(i).setAllocate(true);
					usersThatRequestedComputations.put(user,elem.get(i));
					calcule(user, elem.get(i), KeyN);
					i=elem.size();
				}
			}
		}
	}
	public synchronized void requestTreated(String user,Bloc b) {
		Bloc a=usersThatRequestedComputations.get(user);
		if(a.getNum().equals(b.getNum())){
			for (int i = 0; i <elem.size(); i++) {
				if(elem.get(i).getNum().equals(b.getNum())) {
					elem.get(i).setTreat(true);
					i=elem.size();
				}
			}
			usersThatRequestedComputations.remove(user);
		}
	}
	public void calcule(String from,Bloc b, BigInteger KeyN) {
		rsaObserverList.values().forEach(c -> c.startRsaCalcule(from,b,KeyN));
	}
	public synchronized void QetPtrauve(BigInteger p,BigInteger q) {
		this.Q=q;
		this.P=p;
		BigInteger phN=Q.subtract(BigInteger.ONE).multiply(P.subtract(BigInteger.ONE));
		BigInteger d=KeyE.modInverse(phN);
		rsaObserverList.values().forEach(c->c.stopCalcule(true));
		if(rsaObserverList.containsKey(master))rsaObserverList.values().forEach(c -> c.finDucalcule(master,KeyE, d));
		DecryptModel.deletRSA();
		
	}
	public synchronized Collection<String> userList() {
		return rsaObserverList.keySet();
	}
	public synchronized int userCount() {
		return rsaObserverList.size();
	}
	public synchronized boolean hasUserCalculate(String user) {
		if(usersThatRequestedComputations.containsKey(user))return true;
		return false;
	}
	public synchronized boolean hasUser(String user) {
		if(rsaObserverList.containsKey(user))return true;
		return false;
	}
	public synchronized boolean unregisterUser(String user) {
		if(rsaObserverList.containsKey(user)) {
			rsaObserverList.remove(user);
			notifyUserListChanged();
			return true;
		}
		return false;
	}
	public synchronized boolean unregisterUser(String user,Bloc b) {
		if(rsaObserverList.containsKey(user)) {
			for (int i = 0; i <elem.size(); i++) {
				if(elem.get(i).getNum().equals(b.getNum())) {
					elem.get(i).setTreat(false);
					i=elem.size();
				}
			}
			usersThatRequestedComputations.remove(user);
			rsaObserverList.remove(user);
			notifyUserListChanged();
			return true;
		}
		return false;
	}
	
}
