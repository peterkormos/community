package datatype;

import java.util.Objects;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long id;
    String mapId;
    private double lat;
    private double lng;
    private String content;
    private String emailAddress;

    // Konstruktorok, getterek és setterek
    public Marker() {}

    public Marker(double lat, double lng, String content, String emailAddress) {
        this.lat = lat;
        this.lng = lng;
        this.content = content;
        this.emailAddress = emailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(emailAddress, lat, lng, mapId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Marker other = (Marker) obj;
		return Objects.equals(emailAddress, other.emailAddress)
				&& Double.doubleToLongBits(lat) == Double.doubleToLongBits(other.lat)
				&& Double.doubleToLongBits(lng) == Double.doubleToLongBits(other.lng)
				&& Objects.equals(mapId, other.mapId);
	}

	@Override
	public String toString() {
		return "Marker [id=" + id + ", mapId=" + mapId + ", lat=" + lat + ", lng=" + lng + ", content=" + content
				+ ", emailAddress=" + emailAddress + "]";
	}

   
    
}