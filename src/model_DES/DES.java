package model_DES;

import java.util.*;

public class DES {
	public static final String encode(String message,String key) throws Exception{

		if(message==null || message.length()==0) throw new Exception("Le message a chiffrer est vide.");
		if(!Binary.isBinaryRepresentation(message))throw new Exception("Le message a chiffrer n'est pas sur sa forme binaire.");		
		if(key==null) throw new Exception("La cle du chiffrement est vide.");
		if(key.length()<7) throw new Exception("La cle du chiffrement doit etre de longueur superieur ou egale a 7.");

		String result = "" ;		
		ArrayList<String> subMessages = DES.split(message);
		ArrayList<String> keys = DES.genereateKeySet(DESUtil.buildKey(key));

		for(int numMessage=0;numMessage<subMessages.size();numMessage++){
			String subMessage = subMessages.get(numMessage);
			String aux;
			int i= 0,splitPosition=DESConstant.THIRTYTWO_BIT;
			aux = DESUtil.applyPermutationTable(subMessage, DESConstant.IP);			
			String Li,Ri ;
			Li = aux.substring(0, splitPosition);
			Ri = aux.substring(splitPosition);

			for(i=1;i<=16;i++){			
				String Riplus1 = Binary.xor(Li, DES.f(Ri, keys.get(i-1)));//Riplus1 equivaut a R(i+1)
				Li = Ri ; // Li devient Li+1 avec pour valeur Ri
				Ri = Riplus1;
			}

			String chiffre = DESUtil.applyPermutationTable(Ri+Li, DESConstant.INVERSE_IP);
			result+=chiffre ;
		}			
		return result;
	}
	public static final String decode(String message,String key) throws Exception{
		System.out.println("key->"+key);

		if(message==null || message.length()==0) throw new Exception("Le message a dechiffrer est vide.");
		if(!Binary.isBinaryRepresentation(message))throw new Exception("Le message a dechiffrer n'est pas sur sa forme binaire.");	
		if(key==null) throw new Exception("La cle du dechiffrement est vide.");
		if(key.length()<7) throw new Exception("La cle du dechiffrement doit etre de longueur superieur ou egale a 7.");
		String result = "" ;		
		ArrayList<String> subMessages = DES.split(message);
		ArrayList<String> keys = DES.genereateKeySet(DESUtil.buildKey(key));

		for(int numMessage=0;numMessage<subMessages.size();numMessage++){
			String subMessage = subMessages.get(numMessage);
			String aux;
			int i= 16,splitPosition=DESConstant.THIRTYTWO_BIT;
			aux = DESUtil.applyPermutationTable(subMessage, DESConstant.IP);			
			String Li,Ri ;
			Ri = aux.substring(0, splitPosition);
			Li = aux.substring(splitPosition);

			for(i=15;i>=0;i--){
				String Rimoins1 = Li;
				Li =  Binary.xor(Ri,DES.f(Rimoins1, keys.get(i)));//Li devient Li-1
				Ri = Rimoins1; // Ri devient Rimoins1							
			}			

			String chiffre = DESUtil.applyPermutationTable(Li+Ri, DESConstant.INVERSE_IP);
			result+=chiffre ;
		}		
		System.out.println("resulta->"+result);
		return result;
	}
	public static final ArrayList<String> genereateKeySet(String key) throws Exception {

		if(!Binary.isBinaryRepresentation(key) || key.length()!=DESConstant.SIXTYFOUR_BIT)
			throw new Exception("La cle passee en parametre n'est pas une representtation binaire\n " +
					"ou ne tient pas sur 64 bits");

		ArrayList<String> list = new ArrayList<String> ();
		int splitPosition = 28 ; // de 0 a 27 --> ci et de 28 a 55 di
		int i=0;
		String Ci,Di,aux;

		aux = DESUtil.applyPermutationTable(key, DESConstant.PC_1);
		Ci = aux.substring(0, splitPosition);
		Di = aux.substring(splitPosition, aux.length());

		for(i=1;i<=16;i++){
			int pas = (i==1 || i==2 || i==9 || i==16)? 1:2;
			Ci = DESUtil.leftCircularRotation(Ci, pas);
			Di = DESUtil.leftCircularRotation(Di, pas);
			String Ki = DESUtil.applyPermutationTable(Ci+Di, DESConstant.PC_2);
			list.add(Ki);
		}

		if(list.size()!=16) throw new Exception("Erreur Interne:: La cardinalite de l'ensemble des cles partielles est different de 16");
		return list;
	}
	public static final String f(String param,String key) throws Exception{

		if(!Binary.isBinaryRepresentation(param)) throw new Exception("Le premier parametre de f n'est pas sur sa forme binaire");
		if(param.length()!=DESConstant.THIRTYTWO_BIT) throw new Exception("Le premier parametre de f ne tient pas sur 32 bits");

		if(!Binary.isBinaryRepresentation(key)) throw new Exception("La cle partielle utilisee dans f n'est pas sur sa forme binaire");
		if(key.length()!=DESConstant.FORTYEIGHT_BIT) throw new Exception("La cle partielle utilisee dans f ne tient pas sur 48 bits");

		String result = "" ;
		String paramExpansion = DESUtil.applyPermutationTable(param, DESConstant.EXPANSION);
		String aux = Binary.xor(paramExpansion, key);
		int j=0, incr = DESConstant.SIX_BIT;		
		for(int i=1;i<=8;i++){
			String Bi = aux.substring(j, j+incr); //Bloc i
			String Ci = DES.sboxes(i, Bi); //Chiffre du bloc i			
			result+=Ci;
			j+=incr;
		}
		result = DESUtil.applyPermutationTable(result, DESConstant.P);
		if(result.length()!=DESConstant.THIRTYTWO_BIT)
			throw new Exception("Erreur Interne :: Le resultat de l'application de f ne tient pas sur 32 bits.");
		return result ;
	}
	public static final String sboxes(int numSBoxe,String param) throws Exception{

		if(numSBoxe<1 || numSBoxe>8) throw new Exception("Le numero de la S-Boxe est incorrect");
		if(!Binary.isBinaryRepresentation(param)|| param.length()!=DESConstant.SIX_BIT )
			throw new Exception(param+" n'est pas une representation binaire tenant sur 6 bits");

		int row = 2*Integer.parseInt(""+param.charAt(0))+Integer.parseInt(""+param.charAt(5));
		int column = Binary.binaryStringToInt(param.substring(1,5)) ;
		byte value = -1 ;

		switch(numSBoxe){
		case 1 :  value = DESConstant.SBOXE_1[row][column]; break;
		case 2 :  value = DESConstant.SBOXE_2[row][column]; break;
		case 3 :  value = DESConstant.SBOXE_3[row][column]; break;
		case 4 :  value = DESConstant.SBOXE_4[row][column]; break;
		case 5 :  value = DESConstant.SBOXE_5[row][column]; break;
		case 6 :  value = DESConstant.SBOXE_6[row][column]; break;
		case 7 :  value = DESConstant.SBOXE_7[row][column]; break;
		case 8 :  value = DESConstant.SBOXE_8[row][column]; break;
		}
		if(value==-1) throw new Exception("Erreur lors du calcul de S_Boxe("+numSBoxe+","+param+")");

		String result = Binary.intToBinaryString(value) ;
		String substr = result.substring(0,4), val = result.substring(4);

		if(substr.compareTo("0000")!=0 || val==null || val.length()!=4)
			throw new Exception("Erreur Interne lors du calcul de S_Boxe"+numSBoxe+"("+param+").\n" +
					"Avec ligne = "+row+" et colonne = "+column);
		return val;		
	}
	public static final ArrayList<String> split(String message) throws Exception{
		if(!Binary.isBinaryRepresentation(message)) 
			throw new Exception("Le message a spliter n'est pas une chaine de caractere binaire");
		if((message.length()%64)!=0) 
			throw new Exception("Le message a spliter n'a pas pour taille un multiple de 64");

		ArrayList<String> list = new ArrayList<String>();
		int i=0, incr = DESConstant.SIXTYFOUR_BIT;
		while(i<message.length()){
			String substr = message.substring(i, i+incr);
			list.add(substr);
			i+=incr;
		}
		return list ;	
	}
}
