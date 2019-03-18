package decryptagemodel;

import rsa.RsaEvent;

public interface ModelEvents extends Events,RsaEvent {
	public void userListChanged();
	public void shutdownRequested();
}
