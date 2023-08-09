package LKManager.LK;

import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "rundy")
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Match.class, UserData.class})
public class Runda implements Serializable {


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "runda_id")
    private long id;
@ManyToOne( cascade = CascadeType.ALL)
@JoinColumn(name = "terminarz_id")
    private Terminarz terminarz;


    @Column(name = "nr_rundy")
private int nr;

    @OneToMany(mappedBy = "runda",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
    fetch = FetchType.EAGER)
private List<Match> mecze;

    @Column(name = "data")
    @Transient
    private XMLGregorianCalendar data;

    @Transient
private status status;

    public Runda(int nr, XMLGregorianCalendar data) {
        this.nr = nr;
        this.data = data;
        mecze= new ArrayList<>();
    }

    public Runda() {
    }
    @XmlAttribute
    public Runda.status getStatus() {
        return status;
    }

    public void setStatus(Runda.status status) {
        this.status = status;
    }

    @XmlAttribute
    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    @XmlElement(name = "Match")
    public List<Match> getMecze() {
        return mecze;
    }

    public void setMecze(List<Match> mecze) {
        this.mecze = mecze;
    }

    @XmlAttribute
    public XMLGregorianCalendar getData() {
        return data;
    }

    public void setData(XMLGregorianCalendar data) {
        this.data = data;
    }


   public enum status{
    nierozegrana,
    rozegrana
    }
}
