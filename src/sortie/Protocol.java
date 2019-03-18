package sortie;


import java.math.BigInteger;
import java.util.Collection;



public  interface Protocol {
	default void sendName(String name){}
	default void sendNameOK(){}
	default void sendNameBad() {}
	default void sendForDcryptRsa(String lengthOfKey, String KeyE,String KeyN){}
	default void sendJoinRSA() {}
	default void sendRequestRSA() {}
	default void sendAskUserList() {}
	default void sendUserList(Collection<String> ulist) {}
	default void sendQuit(){}
	default void sendRSAOK(){}
	default void sendRSABad() {}
	default void endRsacalcul() {}
	default void sendEror(String msg) {}
	default void sendCreateRSA() {}
	default void sendRSAUsersList(String name, Collection<String> ulist) {}
	default void sendCalculEncour() {}
	default void sendFinduCacule() {}
	default void sendvaleurTrouve(BigInteger p,BigInteger q) {}
	default void sendRSAmessage(String msg) {}
	
}
