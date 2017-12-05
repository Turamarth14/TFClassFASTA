package de.sybig.TFClassFASTA.core;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Fasta.getByUID", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.UID = :UID"),
    @NamedQuery(name = "Fasta.getByTAXON", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.taxon = :TAXON "
    		+ "AND (:TYPE is null OR fst.type = :TYPE) "
            + "AND (:ALIGN is null OR fst.alignment = :ALIGN) "),
    @NamedQuery(name = "Fasta.getByTYPE", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.type = :TYPE "
            + "AND (:TAXON is null OR fst.taxon = :TAXON) "
            + "AND (:ALIGN is null OR fst.alignment = :ALIGN) "),
    @NamedQuery(name = "Fasta.getByALIGNMENT", query = "SELECT fst FROM "
            + "Fasta fst WHERE fst.alignment = :ALIGNMENT "
            + "AND (:TAXON is null OR fst.taxon = :TAXON) "
            + "AND (:TYPE is null OR fst.type = :TYPE) "),
    @NamedQuery(name = "Fasta.getByTFCLASS", query = "Select fst FROM "
    		+ "Fasta fst where fst.alignment = :ALIGNMENT "
    		+ "And fst.tfclassID = :TFCLASSID "
    		+ "AND (:TYPE is null OR fst.type = :TYPE) "
    		+ "AND (:DESC is null OR fst.desc = :DESC) ")
})
public class Fasta {
	
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)	
	private Long UID;
    
    private String taxon;
    private String type;
    @Enumerated(EnumType.STRING)
    private Alignment alignment;
    private String sequence;
    private String version;
    private String header;
    private String tfclassID;
    private String source;
    private String desc;
    
    public Fasta() {
    	
    }
    public Fasta(String header, String seq) {
    	this.header = header;
    	this.sequence = seq;
    	this.alignment = Alignment.Not_Aligned;
    	this.taxon = "Default";
    	this.type = "Default";
    	this.version = "Default";
    	this.tfclassID = "Default";
    	this.source = "Default";
    }
    public Fasta(String header, String seq, String align, String taxon, String type, String tfclassID, String desc, String source) {
    	this.header = header;
    	this.sequence = seq;
    	this.alignment = Alignment.getEnum(align);
    	this.taxon = taxon;
    	this.type = type;
    	this.version = "Default";
    	this.tfclassID = tfclassID;
    	this.desc = desc;
    	this.source = source;  	
    }
    public Long getUID() {
		return UID;
	}

	public void setUID(Long uID) {
		UID = uID;
	}

	public String getTaxon() {
		return taxon;
	}

	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getTfclassID() {
		return tfclassID;
	}

	public void setTfclassID(String tfclassID) {
		this.tfclassID = tfclassID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public enum Alignment{
    	Phyml, Prank, Not_Aligned;
    	@Override
    	public String toString(){
    	    switch (this) {
    	    case Phyml:
    		return "Phyml";
    	    case Prank:
    	    return "Prank";
    	    case Not_Aligned:
    	    return "not aligned";
    	    }
    	    return "";
    	}
    	public static Alignment getEnum(String value) {
    		if(value == null) {return null;}
    		try {
    			return Alignment.valueOf(value);
    		}
    		catch(IllegalArgumentException e) {
    			return Not_Aligned;
    		}
    	}
    }
	
	public String toString() {
		return this.header + "\n" + this.sequence;
	}
}
