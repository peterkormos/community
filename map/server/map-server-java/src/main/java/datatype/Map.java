package datatype;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;
    private String emailAddress;
    private String mapDescription;
    private String mapId; // Generált UUID

    // Konstruktorok, getterek és setterek
    public Map() {}

    public Map(String emailAddress, String mapDescription, String mapId) {
        this.emailAddress = emailAddress;
        this.mapDescription = mapDescription;
        this.mapId = mapId;
    }

    // Getterek és setterek
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmailAddress() { return emailAddress; }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public String getMapDescription() { return mapDescription; }
    public void setMapDescription(String mapDescription) { this.mapDescription = mapDescription; }
    public String getMapId() { return mapId; }
    public void setMapId(String mapId) { this.mapId = mapId; }

    @Override
    public String toString() {
        return "Map{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                ", mapDescription='" + mapDescription + '\'' +
                ", mapId='" + mapId + '\'' +
                '}';
    }
}