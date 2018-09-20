package es.apryso.aprysobarcodeserver.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="session_entry")
@Getter
@Setter
public class SessionEntry {
	
	@Id
	@GeneratedValue
	@Column(nullable = false)
	private Long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sessionId")
	private Session session;
	
	
	private String content;
	
	private String format;
	
	private int numberOfItems;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	

}
