package de.sybig.TFClassFASTA.client;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class InputFasta {


	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage : java InputFasta fastaFilename");
			return;
		}
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
		}
	}
}
