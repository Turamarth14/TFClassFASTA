package de.sybig.TFClassFASTA.db;

import java.util.List;
import org.hibernate.SessionFactory;
import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.core.MetaFile;
import io.dropwizard.hibernate.AbstractDAO;

public class FastaDAO extends AbstractDAO<Fasta>{
	
	public FastaDAO(SessionFactory factory) {
		super(factory);
	}
	
	public Fasta getByUID(Long uid){
		return get(uid);
	}

	public List<Fasta> getByFile(MetaFile file, String taxon){
		return list(namedQuery("Fasta.getByFile").setParameter("FILE", file).setParameter("TAXON", taxon));
	}
	
	public Fasta create(Fasta fst) {
		return persist(fst);
	}
}
