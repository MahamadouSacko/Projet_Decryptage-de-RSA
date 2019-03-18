package model_DES;
import java.nio.*;
import java.util.*;

public class DESUtil {

	public static String messageToDESBinaryString(String message) throws Exception{
		if(message==null || (message!=null && message.length()==0))
			throw new Exception("Le message a adpater est vide.");
		
		String param = message ;
		int sizeMessage = param.length()*8 ;
		if( (sizeMessage%DESConstant.SIXTYFOUR_BIT)!=0){
			int count = DESConstant.SIXTYFOUR_BIT - (sizeMessage%DESConstant.SIXTYFOUR_BIT) ;
			if( (count%8)!=0 ) throw new Exception("Erreur Interne :: Ne peut Spliter le message en bloc de 64 bits");
			int countSpace = (count/8);
			for(int i=0;i<countSpace;i++) param+=" ";
		}
		
		return Binary.intToBinaryString(DESUtil.AsciiEncoding(param));
	}
	public static final String applyPermutationTable(String param,byte[] table) throws IndexOutOfBoundsException{
		if(param!=null && table!=null){
			char[] result = new char[table.length];
			for(int i=0;i<result.length;i++) result[i]=param.charAt(table[i]-1);
			return (new String(result));
		}
		return null ;
	}
	public static final String AsciiDecoding(ArrayList<Integer> param) throws Exception{
		if(param==null) throw new Exception("La chaine a decoder n'est pas une chaine AScii"); 			
		CharBuffer buffer = CharBuffer.allocate(param.size());
		for(int i=0;i<param.size();i++){
			char val = (char)(param.get(i)).intValue();
			if(val<DESConstant.MIN_ASCII || val>DESConstant.MAX_ASCII) throw new Exception("La chaine a decoder n'est pas une chaine Ascii");
			buffer.put(val);
		}
		buffer.flip();
		return buffer.toString();
	}
	public static final ArrayList<Integer> AsciiEncoding(String param) throws Exception{
		if(param==null) throw new Exception("La chaine a encoder n'est pas une chaine AScii"); 			
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0;i<param.length();i++) {
			int val = param.charAt(i);
			if(val<DESConstant.MIN_ASCII || val>DESConstant.MAX_ASCII) throw new Exception("La chaine a encoder n'est pas une chaine Ascii");
			result.add(val);
		}
		return result;		
	}
	public static String binaryStringToReadableMessage(String message) throws Exception {
		
		if(!Binary.isBinaryRepresentation(message))
			throw new Exception("Le message a transformer n'est pas une representation binaire");
		
		int i=0,count=8;
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		while(i<message.length()){
			buffer.add(Binary.binaryStringToInt(message.substring(i, i+count)));
			i+=count;
		}
		return DESUtil.AsciiDecoding(buffer);
	}
	public static String buildKey(String param) throws Exception {
		String result = null;
		if(param!=null && param.length()>DESConstant.SIX_BIT){
			result = "";
			String str = Binary.intToBinaryString(DESUtil.AsciiEncoding(param.substring(0,7)));			
			if(str.length()!=DESConstant.FIFTYSIX_BIT) throw new Exception("La cle ne peut tenir sur 56 bits");		
			int i=0,incr = 7,count;			
			while(i<DESConstant.FIFTYSIX_BIT){
				String substr = str.substring(i,i+incr);
				count=0;
				if(substr!=null) for(int j=0;j<substr.length();j++) if(substr.charAt(j)=='0') count++;
				if((count%2)==0) substr+="1"; else substr+="0";
				result+=substr;
				i+=incr;
			}			
			if(result.length()!=DESConstant.SIXTYFOUR_BIT) throw new Exception("Erreur Interne :: La cle generee ne tient pas sur 64 bits");
		}
		return result;
	}
	public static final ByteBuffer byteTableToByteBuffer(ArrayList<Byte> param){
		if(param!=null && param.size()>0){
			ByteBuffer buffer = ByteBuffer.allocateDirect(param.size());
			for(int i=0; i<buffer.limit();i++) buffer.put(param.get(i));
			buffer.flip();
			return buffer;
		}
		return null;
	}
	public static final ByteBuffer byteTableToByteBuffer(byte[] param){
		if(param!=null && param.length>0){
			ByteBuffer buffer = ByteBuffer.allocateDirect(param.length);
			for(int i=0; i<buffer.limit();i++) buffer.put(param[i]);
			buffer.flip();
			return buffer;
		}
		return null;
	}
	public static final int bytesToInt(byte param[]) throws Exception{
		return 	(param[0] & 0xFF) << 24 | 
				(param[1] & 0xFF) << 16 | 	
				(param[2] & 0xFF) << 8 |
				(param[3] & 0xFF);
	}
	public static final  byte[] intToBytes(int param){
		byte res[] = new byte[4];
		res[0] = (byte)((param) >>> 24);  	
		res[1] = (byte)((param) >>> 16);  	
		res[2] = (byte)((param) >>> 8); 
		res[3] = (byte)((param));
		return res;
	}
	public static boolean isDESReadableMessage(String message){
		if(message!=null && message.length()>0){
			boolean isDESMessage = true ;
			for(int i=0;i<message.length();i++){				
				 if(message.charAt(i)<DESConstant.MIN_ASCII || message.charAt(i)>DESConstant.MAX_ASCII){
					 isDESMessage = false ;
					 break;
				 }
			}
			return isDESMessage;
		}
		return false;
	}
	public static final String leftCircularRotation(String param,int count) throws IndexOutOfBoundsException {
		if(param!=null && count>0){
			String part1 = param.substring(0,count);
			String part2 = param.substring(count);
			return new String(part2+part1);
		}
		return null ;
	}
}
