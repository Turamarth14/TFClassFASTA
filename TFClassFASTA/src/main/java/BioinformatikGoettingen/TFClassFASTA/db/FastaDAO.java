package BioinformatikGoettingen.TFClassFASTA.db;

import java.util.List;
import org.hibernate.SessionFactory;
import BioinformatikGoettingen.TFClassFASTA.core.Fasta;
import io.dropwizard.hibernate.AbstractDAO;

public class FastaDAO extends AbstractDAO<Fasta>{
	
	public FastaDAO(SessionFactory factory) {
		super(factory);
	}
	
	public Fasta getByUID(Long uid){
		return get(uid);
		//return list(namedQuery("Fasta.getByUID").setParameter("UID", uid));
	}
}
