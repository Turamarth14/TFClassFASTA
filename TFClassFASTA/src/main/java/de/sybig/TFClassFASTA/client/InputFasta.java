package de.sybig.TFClassFASTA.client;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private static final Pattern pat = Pattern.compile("_|-|\\.[a-zA-Z]");
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
    
    /**
     * Assuming that only phyml-input, phyml-output and prank-output in DBD matter
     * phyml-input -> normal fasta
     * phyml-output -> logoplot-fasta, input for Phylogeny.fr tree
     * prank-output -> input for webPRANK tree 
     * Adding whole/modules with special desc value to DB
     * @param dir
     */
    private static void searchDBDTree(File dir) {
    	System.out.println("Adding data in " + dir.getName());
    	File[] subDirs = dir.listFiles(File::isDirectory);
    	for(File subDir : subDirs) {
    		searchDBDTree(subDir);
    	}
    	File phymlFile, prankFile, fastaFile;
    	fastaFile = findDBDFile(dir, "dbd_phyml-input.fasta.txt");
    	if(fastaFile != null)
    		addFileToDB(fastaFile,"DBD","Not_Aligned","normal");
    	phymlFile = findDBDFile(dir, "dbd_phyml-output.fasta.txt");
    	if(phymlFile != null)
    		addFileToDB(phymlFile,"DBD","Phyml","normal");    	
    	prankFile = findDBDFile(dir, "dbd_prank-output.fasta.txt");
    	if(prankFile != null)
    		addFileToDB(prankFile,"DBD","Prank","normal"); 
    	if(dir.listFiles((file, name) -> name.contains("modules")).length > 0) {
        	fastaFile = findDBDFile(dir, "dbd-modules_phyml-input.fasta.txt");
        	if(fastaFile != null)
        		addFileToDB(fastaFile,"DBD","Not_Aligned","modules"); 
        	phymlFile = findDBDFile(dir, "dbd-modules_phyml-output.fasta.txt");
        	if(phymlFile != null)
        		addFileToDB(phymlFile,"DBD","Phyml","modules"); 
        	prankFile = findDBDFile(dir, "dbd-modules_prank-output.fasta.txt");   
        	if(prankFile != null)
        		addFileToDB(prankFile,"DBD","Prank","modules"); 
    	}
    	if(dir.listFiles((file, name) -> name.contains("whole")).length > 0) {
        	fastaFile = findDBDFile(dir, "dbd-whole_phyml-input.fasta.txt");
        	if(fastaFile != null)
        		addFileToDB(fastaFile,"DBD","Not_Aligned","whole"); 
        	phymlFile = findDBDFile(dir, "dbd-whole_phyml-output.fasta.txt");
        	if(phymlFile != null)
        		addFileToDB(phymlFile,"DBD","Phyml","whole"); 
        	prankFile = findDBDFile(dir, "dbd-whole_prank-output.fasta.txt");   
        	if(prankFile != null)
        		addFileToDB(prankFile,"DBD","Prank","whole"); 
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
    
    private static void addFileToDB(File file, String type, String align, String desc) {
		try{
			System.out.println("Adding file " + file.getName() + " to DB");
			Matcher matcher = pat.matcher(file.getName());
			if(!matcher.find()) {
				System.out.println("Could not identify tfclassId");
				return;
			}
			String tfclassID = file.getName().substring(0, matcher.start());
			Client client = ClientBuilder.newBuilder()
					.register(MultiPartFeature.class)
					.build();
			WebTarget target = client.target("http://localhost:8080/FASTA/upload");
			FileDataBodyPart filePart = new FileDataBodyPart("fasta", file);
			FormDataMultiPart multiPart = new FormDataMultiPart();
			multiPart.field("type", type);
			multiPart.field("align", align);
			multiPart.field("desc", desc);
			multiPart.field("tfclassid", tfclassID);
			multiPart.bodyPart(filePart);
			
			Response response = target.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
			System.out.println(response.getStatus());
			System.out.println(response.readEntity(String.class));
			client.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}    	
    }
}
