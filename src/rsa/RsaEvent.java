package rsa;

import java.math.BigInteger;

public interface RsaEvent {
	public void stopCalcule(boolean stats);
	public void rsaUserListChanged(String msg);
	public void startRsaCalcule(String from,Bloc bloc, BigInteger KeyN);
	public void finDucalcule(String from,BigInteger KeyE,BigInteger D);
	
}
