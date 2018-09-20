package es.apryso.aprysobarcodeserver.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "session")
@Getter
@Setter
public class Session {
	
	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@OneToOne(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Device device;
	
	@OneToMany(mappedBy="session", cascade= CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
	private List<SessionEntry> entryList = new ArrayList<SessionEntry>();

}
