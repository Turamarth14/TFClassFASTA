package de.sybig.TFClassFASTA.db;

import java.util.List;
import org.hibernate.SessionFactory;
import de.sybig.TFClassFASTA.core.Fasta;
import io.dropwizard.hibernate.AbstractDAO;

public class FastaDAO extends AbstractDAO<Fasta>{
	
	public FastaDAO(SessionFactory factory) {
		super(factory);
	}
	
	public Fasta getByUID(Long uid){
		return get(uid);
	}
	public List<Fasta> getByTaxon(String taxon, String type, String align){
		return list(namedQuery("Fasta.getByTAXON").setParameter("TAXON", taxon).setParameter("TYPE", type).setParameter("ALIGN", Fasta.Alignment.getEnum(align)));
	}	
	public List<Fasta> getByType(String type, String taxon, String align){
		return list(namedQuery("Fasta.getByTYPE").setParameter("TYPE", type).setParameter("TAXON", taxon).setParameter("ALIGN", Fasta.Alignment.getEnum(align)));
	}
	public List<Fasta> getByAlign(String align, String taxon, String type){
		return list(namedQuery("Fasta.getByALIGNMENT").setParameter("ALIGN", Fasta.Alignment.getEnum(align)).setParameter("TAXON", taxon).setParameter("TYPE",type));
	}
}
