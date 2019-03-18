package buffere;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LineBufferedInputStream extends BufferedInputStream {
	public LineBufferedInputStream(InputStream in) {
		super(in);
	}
	public String readLine() throws IOException {
		StringBuilder sb = new StringBuilder();
		int b;
		while ((b = read()) >= 0) {
			if (b == '\n') break;
			if (b != '\r') sb.append((char) b);
		}
		if (b == -1 && sb.length() == 0) return null;
		return sb.toString();
	}

}
