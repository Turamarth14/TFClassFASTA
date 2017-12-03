package de.sybig.TFClassFASTA.client;

import java.io.File;
import java.io.FilenameFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class InputFasta 
{
	private static File dbdTreeDir;
	private static File protTreeDir;
    public static void main( String[] args )
    {
		if(args.length != 2) {
			System.out.println("Usage : java InputFasta dbdTreeDir, protTreeDir");
			return;
		}
		dbdTreeDir = new File(args[0]);
		if(!dbdTreeDir.isDirectory()) {
			System.out.println("dbdTreeDir is not a directory");
			return;
		}
		protTreeDir = new File(args[1]);
		if(!protTreeDir.isDirectory()) {
			System.out.println("protTreeDir is not a directory");
			return;
		}
		System.out.println("Adding data in " + dbdTreeDir.getName());
		File[] dbdDirs = dbdTreeDir.listFiles(File::isDirectory);
		for(File dbdDir : dbdDirs) {
			if(!dbdDir.getName().equals("0")) {
				searchDBDTree(dbdDir);
			}
		}
		/*
		try{
			Client client = ClientBuilder.newBuilder()
					.register(MultiPartFeature.class)
					.build();
			WebTarget target = client.target("http://localhost:8080/FASTA/upload");
			FileDataBodyPart filePart = new FileDataBodyPart("fasta", new File(args[0]));
			FormDataMultiPart multiPart = new FormDataMultiPart();
			multiPart.field("type", "TYP1");
			multiPart.field("taxon", "human");
			multiPart.bodyPart(filePart);
			System.out.println(multiPart.getMediaType());
			System.out.println(multiPart.getField("taxon").getValue());
			System.out.println(multiPart.getField("fasta").getEntity());
			
			multiPart.getBodyParts().forEach(part -> System.out.println(part.getMediaType() + "\t" + part.getEntity() + "\t" + part.getContentDisposition()));
			Response response = target.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
			System.out.println(response.getStatus());
			System.out.println(response.readEntity(String.class));
			client.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}*/
    }
    
    private static void searchDBDTree(File dir) {
    	System.out.println("Adding data in " + dir.getName());
    	File[] subDirs = dir.listFiles(File::isDirectory);
    	for(File subDir : subDirs) {
    		searchDBDTree(subDir);
    	}
    	File phymlFile, prankFile, logoFile;
    	File[] fileList = dir.listFiles((file, name) -> name.endsWith("_phyml-input.fasta.txt"));
    	logoFile = findDBDFile(dir, "dbd_phyml-input.fasta.txt");
    	if(logoFile != null)
    		addFileToDB(logoFile,"DBD");
    	phymlFile = findDBDFile(dir, "dbd_phyml-output.fasta.txt");
    	if(phymlFile != null)
    		addFileToDB(phymlFile,"DBD");    	
    	prankFile = findDBDFile(dir, "dbd_prank-output.fasta.txt");
    	if(prankFile != null)
    		addFileToDB(prankFile,"DBD"); 
    	if(dir.listFiles((file, name) -> name.contains("modules")).length > 0) {
        	logoFile = findDBDFile(dir, "dbd-modules_phyml-input.fasta.txt");
        	if(logoFile != null)
        		addFileToDB(logoFile,"DBD"); 
        	phymlFile = findDBDFile(dir, "dbd-modules_phyml-output.fasta.txt");
        	if(phymlFile != null)
        		addFileToDB(phymlFile,"DBD"); 
        	prankFile = findDBDFile(dir, "dbd-modules_prank-output.fasta.txt");   
        	if(prankFile != null)
        		addFileToDB(prankFile,"DBD"); 
    	}
    	if(dir.listFiles((file, name) -> name.contains("whole")).length > 0) {
        	logoFile = findDBDFile(dir, "dbd-whole_phyml-input.fasta.txt");
        	if(logoFile != null)
        		addFileToDB(logoFile,"DBD"); 
        	phymlFile = findDBDFile(dir, "dbd-whole_phyml-output.fasta.txt");
        	if(phymlFile != null)
        		addFileToDB(phymlFile,"DBD"); 
        	prankFile = findDBDFile(dir, "dbd-whole_prank-output.fasta.txt");   
        	if(prankFile != null)
        		addFileToDB(prankFile,"DBD"); 
    	}    	
    }
    /**
     * Assuming that there are only mammalia and mammalia_slim possible in DBD
     * mammalia > mammalia_slim
     * @param dir
     * @param suffix
     * @return
     */
    private static File findDBDFile(File dir, String suffix) {
    	File fileList[];
    	fileList = dir.listFiles((file, name) -> name.endsWith(suffix));
    	if(fileList.length == 0) {
    		System.out.println("Could not find a *" + suffix + " for " + dir.getName());
    		return null;
    	}
    	else if(fileList.length == 1) {
    		return fileList[0];
    	}
    	else if(fileList.length == 2){
    		if(fileList[0].getName().contains("mammalia") && !fileList[0].getName().contains("slim")) {
    				return fileList[0];
    		}
    		else {
    			return fileList[1];
    		}
    	}
    	else {
    		System.out.println("Multiple files for *" + suffix + " for " + dir.getName());
    		return null;
    	}
    }
    
    private static void addFileToDB(File file, String type) {
		try{
			System.out.println("Adding file " + file.getName() + " to DB");
			Client client = ClientBuilder.newBuilder()
					.register(MultiPartFeature.class)
					.build();
			WebTarget target = client.target("http://localhost:8080/FASTA/upload");
			FileDataBodyPart filePart = new FileDataBodyPart("fasta", file);
			FormDataMultiPart multiPart = new FormDataMultiPart();
			multiPart.field("type", type);
			multiPart.bodyPart(filePart);
			
			Response response = target.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
			System.out.println(response.getStatus());
			System.out.println(response.readEntity(String.class));
			client.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}    	
    }
}
