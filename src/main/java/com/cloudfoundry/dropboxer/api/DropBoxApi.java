package com.cloudfoundry.dropboxer.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

public class DropBoxApi {

	private final String APP_KEY = "166p33giu4anwig";
	private final String APP_SECRET = "k3gfgcle5elwi8o";
	private final String APP_ACCESS_TOKEN = "ODCaAn9DyEcAAAAAAAAABZwITtWaDofjxN4hS2IuyWHLpSPg8vdL5DVBXX_6ydae";
	
	private DbxClient mDbxClient;
	
	public DropBoxApi() {
		super();

		// Connect the application with the DropBox
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());
		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
		
		//Getting client access
		mDbxClient = new DbxClient(config, APP_ACCESS_TOKEN);
		try {
			System.out.println("Linked account: " + mDbxClient.getAccountInfo().displayName);
		} catch (DbxException e) {
			e.printStackTrace();
		}		
	}
	
	
	
	public void uploadFile(String filePath){
		
		File inputFile = new File(filePath);
		FileInputStream inputStream = null;
		try {
				inputStream = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		}
		try {
			    DbxEntry.File uploadedFile = mDbxClient.uploadFile("/magnum-opus.txt",
		        DbxWriteMode.add(), inputFile.length(), inputStream);
			    System.out.println("Uploaded: " + uploadedFile.toString());
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
