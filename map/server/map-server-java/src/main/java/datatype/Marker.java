package datatype;

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
    private Long mapId;
    private double lat;
    private double lng;
    private String content;
    private String emailAddress;

    // Konstruktorok, getterek és setterek
    public Marker() {}

    public Marker(Long mapId, double lat, double lng, String content, String emailAddress) {
        this.mapId = mapId;
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

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
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

    @Override
    public String toString() {
        return "Marker{" +
                "id=" + id +
                ", mapId=" + mapId +
                ", lat=" + lat +
                ", lng=" + lng +
                ", content='" + content + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}