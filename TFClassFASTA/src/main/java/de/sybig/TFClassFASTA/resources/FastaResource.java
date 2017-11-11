package de.sybig.TFClassFASTA.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.db.FastaDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.setup.Environment;

@Path("/FASTA")
@Produces(MediaType.APPLICATION_JSON)
public class FastaResource {

	private final FastaDAO fastaDAO;
	private final Environment environment;
	
	public FastaResource(FastaDAO fastaDAO, Environment environment) {
		this.fastaDAO = fastaDAO;
		this.environment = environment;
	}
	
	@GET
	@Path("/{UID}")
	@UnitOfWork
	public Fasta getFasta(@PathParam(value = "UID") LongParam UID) {
		return fastaDAO.getByUID(UID.get());
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("File/{UID}")
	@UnitOfWork
	public Response getFile(@PathParam(value = "UID") LongParam UID) {
		Fasta fasta = fastaDAO.getByUID(UID.get());
		String data = fasta.toFile();
		return Response.ok(data, MediaType.TEXT_PLAIN)
				.header("Content-Disposition", "attachment; filename=\"" + fasta.getUID() + "\"")
				.build();
	}
}
