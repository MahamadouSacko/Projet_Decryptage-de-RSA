package rsa;

import java.math.BigInteger;

public class Bloc {
	private BigInteger debut;
	private BigInteger fin;
    private Boolean treat;
    private Boolean allocate;
    private String num;
	public Bloc(BigInteger debut, BigInteger fin, Boolean treat,String num,Boolean allocate) {
		this.debut = debut;
		this.fin = fin;
		this.treat = treat;
		this.num=num;
		this.allocate=allocate;
	}
	public BigInteger getDebut() {
		return debut;
	}
	public void setDebut(BigInteger debut) {
		this.debut = debut;
	}
	public BigInteger getFin() {
		return fin;
	}
	public void setFin(BigInteger fin) {
		this.fin = fin;
	}
	public Boolean getTreat() {
		return treat;
	}
	public void setTreat(Boolean treat) {
		this.treat = treat;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Boolean getAllocate() {
		return allocate;
	}
	public void setAllocate(Boolean allocate) {
		this.allocate = allocate;
	}
    
}
