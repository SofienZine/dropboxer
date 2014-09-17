package com.cloudfoundry.dropboxer.api;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
	public static DropBoxApi dropBoxApi;
	
	public static DropBoxApi getInstance(){
		if(dropBoxApi ==  null){
			dropBoxApi = new DropBoxApi();
		}
		return dropBoxApi;		
	}
	
	public DropBoxApi() {
		super();

		// Connect the application with the DropBox
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
				Locale.getDefault().toString());
				
		//Getting client access
		mDbxClient = new DbxClient(config, APP_ACCESS_TOKEN);
		try {
			System.out.println("Linked account: " + mDbxClient.getAccountInfo().displayName);
		} catch (DbxException e) {
			e.printStackTrace();
		}		
	}
		
	public void uploadFile(File file){		
		File inputFile = new File(file.getAbsolutePath());
		FileInputStream inputStream = null;
		try {
				inputStream = new FileInputStream(inputFile);		
			    DbxEntry.File uploadedFile = mDbxClient.uploadFile(String.format("/%s",file.getName()),
			    							  						DbxWriteMode.add(), 
			    							  						inputFile.length(), 
			    							  						inputStream);
			    System.out.println("Uploaded: " + uploadedFile.toString());
		} catch (DbxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
				inputStream.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<DbxEntry> getFilesFromFolder(String folderPath) throws DbxException{
		DbxEntry.WithChildren listing = mDbxClient.getMetadataWithChildren(folderPath);
		System.out.println("Files in the root path:");		
		return listing.children.isEmpty() ? null : (ArrayList<DbxEntry>)listing.children;
	}
}
