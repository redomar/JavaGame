package com.redomar.game.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PrintToLog {

	private static PrintWriter printer;
	private File url;

	public PrintToLog(String url) {
		this.url = new File(url);
		initiate();
	}

	public void log(String data) {
		printer.println(data);
		printer.close();
	}

	private void initiate() {
		try {
			printer = new PrintWriter(new FileOutputStream(url, true));
		} catch (FileNotFoundException e) {
			//throw new FileNotFoundException();
			System.err.println(e);
		}
	}

	public File getUrl(){
		return this.url;
	}
}
