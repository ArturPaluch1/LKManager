package LKManager.LK;

import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;



import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
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

  //  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
@ManyToOne( cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
@JoinColumn(name = "terminarz_id")
    private Terminarz terminarz;


    @Column(name = "nr_rundy")
private int nr;


    @OneToMany(mappedBy = "runda",
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE},
        //    orphanRemoval = true,)

    fetch = FetchType.LAZY)

private List<Match> mecze;




/*
    @Column(name = "data")
    @Temporal(TemporalType.DATE)
    public Date getDateTimeItem() {
        return XmlAdapter.unmarshall( this.getData());
    }

    public void setDateTimeItem(Date target) throws DatatypeConfigurationException {
        setData(XmlAdapter.marshall( target));
    }

    */

@Column(name = "data")
    protected LocalDate data;

    @XmlAttribute
    public boolean isPlayed() {
        return played;
    }
    public void setPlayed(boolean played) {
        this.played = played;
    }
    @Transient
    @Column(name = "played")
private boolean played;

    public Runda(int nr, LocalDate data) {
        this.nr = nr;
        this.data = data;
        mecze= new ArrayList<>();
    }

    public Runda() {
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

 // @XmlAttribute
 // @Transient
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }


   public enum status{
    nierozegrana,
    rozegrana
    }
}
