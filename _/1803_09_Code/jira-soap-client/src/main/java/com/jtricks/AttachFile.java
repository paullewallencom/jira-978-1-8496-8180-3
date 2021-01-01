package com.jtricks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import sun.misc.BASE64Encoder;

import com.atlassian.jira.rpc.soap.client.JiraSoapService;
import com.atlassian.jira.rpc.soap.client.RemoteAttachment;
import com.atlassian.jira.rpc.soap.client.RemoteAuthenticationException;
import com.atlassian.jira.rpc.soap.client.RemotePermissionException;
import com.atlassian.jira.rpc.soap.client.RemoteValidationException;

public class AttachFile {

	// Login details
	static final String LOGIN_NAME = "username";
	static final String LOGIN_PASSWORD = "password";

	// Constants for issue updation
	static final String ISSUE_KEY = "TST-22840";

	public static void main(String[] args) {
		System.out.println("Executing J-tricks' JIRA SOAP Client");

		String baseUrl = "http://jira.atlassian.com/rpc/soap/jirasoapservice-v2";

		try {
			// Create new Session
			SOAPSession soapSession = new SOAPSession(new URL(baseUrl));

			// Now login
			soapSession.connect(LOGIN_NAME, LOGIN_PASSWORD);

			// Get the JIRA SOAP Service and authentication token
			JiraSoapService jiraSoapService = soapSession.getJiraSoapService();
			String authToken = soapSession.getAuthenticationToken();

			// get CF Details
			attachFile(jiraSoapService, authToken);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void attachFile(JiraSoapService jiraSoapService, String authToken) throws RemotePermissionException,
			RemoteValidationException, RemoteAuthenticationException,
			com.atlassian.jira.rpc.soap.client.RemoteException, RemoteException {
		//Deprecated since 4.0
		boolean attachmentAdded = jiraSoapService.addAttachmentsToIssue(authToken, ISSUE_KEY, new String[] {
				"test1.txt", "test2.txt" }, getFileContents());

		if (attachmentAdded) {
			RemoteAttachment[] attachments = jiraSoapService.getAttachmentsFromIssue(authToken, ISSUE_KEY);
			for (RemoteAttachment remoteAttachment : attachments) {
				System.out.println("Attachment Name:" + remoteAttachment.getFilename() + ", Id:"
						+ remoteAttachment.getId());
				System.out.println("URL: /secure/attachment/" + remoteAttachment.getId() + "/"
						+ remoteAttachment.getFilename());
			}
		}

		// For bigger files, see https://jira.atlassian.com/browse/JRA-11693
		attachmentAdded = jiraSoapService.addBase64EncodedAttachmentsToIssue(authToken, ISSUE_KEY,
				new String[] { "test3.txt" }, getEncodedFileContents());
	}

	private static String[] getEncodedFileContents() {
		byte[] file = getFileContent("//home//root2//Desktop//test1.txt");

		String base64encodedFileData = new BASE64Encoder().encode(file);
		String[] encodedData = new String[] { base64encodedFileData };

		return encodedData;
	}

	private static byte[][] getFileContents() {
		byte[][] files = new byte[2][];

		files[0] = getFileContent("//home//root2//Desktop//test1.txt");
		files[1] = getFileContent("//home//root2//Desktop//test2.txt");

		return files;
	}

	public static byte[] getFileContent(String filePath) {
		byte fileContent[] = null;
		File file = new File(filePath);
		try {
			// create FileInputStream object
			FileInputStream fin = new FileInputStream(file);

			/*
			 * Create byte array large enough to hold the content of the file.
			 * Use File.length to determine size of the file in bytes.
			 */
			fileContent = new byte[(int) file.length()];

			/*
			 * To read content of the file in byte array, use int read(byte[]
			 * byteArray) method of java FileInputStream class.
			 */
			fin.read(fileContent);
			fin.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found" + e);
		} catch (IOException ioe) {
			System.out.println("Exception while reading the file " + ioe);
		}
		return fileContent;
	}
}
