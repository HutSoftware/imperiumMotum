package org.hut.im;

import android.app.Application;
import java.io.*;

public class Applicacao extends Application {
	
	private InputStream entradaNXT;
	private OutputStream saidaNXT;
	
	
	public InputStream getEntradaNXT() {
		return entradaNXT;
	}
	public void setEntradaNXT(InputStream entradaNXT) {
		this.entradaNXT = entradaNXT;
	}
	public OutputStream getSaidaNXT() {
		return saidaNXT;
	}
	public void setSaidaNXT(OutputStream saidaNXT) {
		this.saidaNXT = saidaNXT;
	}


}
