package de.sybig.TFClassFASTA.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
		System.out.println("Adding data in " + protTreeDir.getName());
		File[] protDirs = protTreeDir.listFiles(File::isDirectory);
		for(File protDir : protDirs) {
			if(!protDir.getName().equals("0")) {
				searchPROTTree(protDir);
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
     * dbd -> DBD
     * whole -> DBD
     * modules -> Modules
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
    		addFileToDB(fastaFile,"DBD","Not_Aligned");
    	phymlFile = findDBDFile(dir, "dbd_phyml-output.fasta.txt");
    	if(phymlFile != null)
    		addFileToDB(phymlFile,"DBD","Phyml");    	
    	prankFile = findDBDFile(dir, "dbd_prank-output.fasta.txt");
    	if(prankFile != null)
    		addFileToDB(prankFile,"DBD","Prank"); 
    	if(dir.listFiles((file, name) -> name.contains("modules")).length > 0) {
        	fastaFile = findDBDFile(dir, "dbd-modules_phyml-input.fasta.txt");
        	if(fastaFile != null)
        		addFileToDB(fastaFile,"Modules","Not_Aligned"); 
        	phymlFile = findDBDFile(dir, "dbd-modules_phyml-output.fasta.txt");
        	if(phymlFile != null)
        		addFileToDB(phymlFile,"Modules","Phyml"); 
        	prankFile = findDBDFile(dir, "dbd-modules_prank-output.fasta.txt");   
        	if(prankFile != null)
        		addFileToDB(prankFile,"Modules","Prank"); 
    	}
    	if(dir.listFiles((file, name) -> name.contains("whole")).length > 0) {
        	fastaFile = findDBDFile(dir, "dbd-whole_phyml-input.fasta.txt");
        	if(fastaFile != null)
        		addFileToDB(fastaFile,"DBD","Not_Aligned"); 
        	phymlFile = findDBDFile(dir, "dbd-whole_phyml-output.fasta.txt");
        	if(phymlFile != null)
        		addFileToDB(phymlFile,"DBD","Phyml"); 
        	prankFile = findDBDFile(dir, "dbd-whole_prank-output.fasta.txt");   
        	if(prankFile != null)
        		addFileToDB(prankFile,"DBD","Prank"); 
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

    /**
     * Assuming that only fasta.txt and phyml.*.zip in PROT matter
     * fasta.txt -> normal fasta
     * input.seq in phyml.*.zip -> logoplot-fasta, input for Phylogeny.fr tree
     * @param dir
     */
    private static void searchPROTTree(File dir) {
    	System.out.println("Adding data in " + dir.getName());
    	File[] subDirs = dir.listFiles(File::isDirectory);
    	for(File subDir : subDirs) {
    		searchPROTTree(subDir);
    	}
    	File fastaFile =null;
    	try {
    		fastaFile = findPROTFile(dir, ".fasta.txt");
    		if(fastaFile != null)
    			addFileToDB(fastaFile,"Protein","Not_Aligned");
    	}
    	catch(IOException e) {
    		System.out.println("Error while searching for file *.fasta.txt in " + dir.getName());
    		System.out.println(e.getMessage());
    	}
    	if(fastaFile != null) {
    		String prefix = fastaFile.getName().substring(0, fastaFile.getName().length()-10);
    	    int pos = prefix.lastIndexOf(".");
    	    String tfclass = prefix.substring(0,pos);
    	    String version = prefix.substring(pos+1);
    	    String file = tfclass + "_" + version + "_fasta.input.seq";
    	    String zip = tfclass + ".phyml." + version + ".zip";
    	    addZipFileToDB(dir, file, zip, "Protein", "Phyml");
    	}	
    	   	
    }
    /**
     * Assuming that there are only mammalia and mammalia_slim possible in PROT
     * mammalia > mammalia_slim
     * Ordering by date, using newest
     * @param dir
     * @param suffix
     * @return
     */
    private static File findPROTFile(File dir, String suffix) throws IOException{
    	Supplier<Stream<Path>> fileStreamSupplier;
    	Path directory = dir.toPath();
    	fileStreamSupplier = () -> {
			try {
				return Files.find(directory, 1, (p, bfa) -> bfa.isRegularFile() && p.getFileName().toString().endsWith(suffix) && p.getFileName().toString().contains("mammalia") && !p.getFileName().toString().contains("metazoa") && !p.getFileName().toString().contains("vertebrata"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());;
				return null;
			}
    	};	
    	if(fileStreamSupplier.get().count() == 0) {
    		System.out.println("Could not find a *" + suffix + " for " + dir.getName());
    		return null;
    	}
    	else if(fileStreamSupplier.get().count() == 1) {
    		return fileStreamSupplier.get().findFirst().get().toFile();
    	}
    	else {
    		Map<Boolean, List<Path>> groups = fileStreamSupplier.get().collect(Collectors.partitioningBy(p -> (p.getFileName().toString().contains("mammalia") && !p.getFileName().toString().contains("slim"))));
    		List<Path> fileList;
    		if(groups.get(true).size() == 0) { // Using mammalia slim
    			fileList = groups.get(false);

    		}
    		else { //Using mammalia
    			fileList = groups.get(true);
    		}
			fileList.sort(new Comparator<Path>() {
				@Override
				public int compare(Path file0, Path file1) {
					try {
						BasicFileAttributes attr0 = Files.readAttributes(file0, BasicFileAttributes.class);
						BasicFileAttributes attr1 = Files.readAttributes(file1, BasicFileAttributes.class);
						return - attr0.creationTime().compareTo(attr1.creationTime());
					}
					catch(IOException e) {
						System.out.println("Error while comparing files");
						System.out.println(e.getMessage());
					}
					return 0;
				}			
			});    	
			return fileList.get(0).toFile();
    	}
    }
    private static void addZipFileToDB(File dir, String fileName, String zipName, String type, String align) {
		try{
			ZipFile zipFile = new ZipFile(new File(dir, zipName));
			ZipEntry entry = zipFile.getEntry(fileName);
			InputStream iStream = zipFile.getInputStream(entry);
			Path tempFile = Files.createTempFile("TFClassTempFile",".tmp");
			BufferedReader in = new BufferedReader(new InputStreamReader(iStream));
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile.toFile())));
			String line = null;
			while((line = in.readLine()) != null) {
				if(!line.isEmpty()) {
					out.println(line);
				}
			}
			in.close();
			out.close();
			System.out.println("Adding file " + fileName + " to DB");
			Matcher matcher = pat.matcher(fileName);
			if(!matcher.find()) {
				System.out.println("Could not identify tfclassId");
				zipFile.close();
				return;
			}
			String tfclassID = fileName.substring(0, matcher.start());
			Client client = ClientBuilder.newBuilder()
					.register(MultiPartFeature.class)
					.build();
			WebTarget target = client.target("http://localhost:8080/FASTA/upload");
			FileDataBodyPart filePart = new FileDataBodyPart("fasta", tempFile.toFile());
			FormDataMultiPart multiPart = new FormDataMultiPart();
			multiPart.field("type", type);
			multiPart.field("align", align);
			multiPart.field("tfclassid", tfclassID);
			multiPart.field("source", fileName);
			multiPart.bodyPart(filePart);
			
			Response response = target.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
			System.out.println(response.getStatus());
			client.close();
			Files.delete(tempFile);
			zipFile.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		} 
    }
    private static void addFileToDB(File file, String type, String align) {
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
			multiPart.field("tfclassid", tfclassID);
			multiPart.field("source", file.getName());
			multiPart.bodyPart(filePart);
			
			Response response = target.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
			System.out.println(response.getStatus());
			//System.out.println(response.readEntity(String.class));
			client.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}    	
    }
}
