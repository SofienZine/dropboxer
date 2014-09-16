package com.cloudfoundry.dropboxer;

import javax.swing.JFrame;

import com.cloudfoundry.dropboxer.api.DropBoxApi;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	DropBoxApi dropBoxer = new DropBoxApi();
    	dropBoxer.uploadFile("C:\\Users\\Ahmed\\Desktop\\Ahmed.txt");
    	
    }
}
