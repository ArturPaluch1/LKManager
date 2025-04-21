package LKManager.model;

import LKManager.model.MatchesMz.Match;
import LKManager.model.RecordsAndDTO.RoundDTO;
import LKManager.model.UserMZ.MZUserData;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Table;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "rundy", schema="lkm_dev")
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Match.class, MZUserData.class})

public class Round implements Serializable {


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
/*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "runda_id")
    private long id;
*/
@SequenceGenerator(
        name = "roundid_seq",
        sequenceName = "lkm_dev.roundid_seq",
        allocationSize = 1
)
@Id
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roundid_seq")

@Column(name = "runda_id")
private long id;


  //  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
@ManyToOne( cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
@JoinColumn(name = "terminarz_id")
    private Schedule schedule;


    @Column(name = "nr_rundy")
private int nr;

//@Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "round",
            cascade = { CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE},
        //    orphanRemoval = true,)
        //    orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)

      /*      @JoinTable
                    (
                            name="mecze",
                            joinColumns={ @JoinColumn(name="id", referencedColumnName="runda_id") },
                            inverseJoinColumns={ @JoinColumn(name="id", referencedColumnName="mecz_id") }
                    )
*/

private Set<Match> matches;




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
    protected LocalDate date;

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

    public Round(int nr, LocalDate date) {
        this.nr = nr;
        this.date = date;
        matches = new LinkedHashSet<>();
    }

    public Round() {
    }


public Round(RoundDTO roundDto)
{

}


    @XmlAttribute
    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    @XmlElement(name = "Match")

    public Set<Match> getMatches() {
        return matches;
    }

    public void setMatches(Set<Match> matches) {
        this.matches = matches;
    }

 // @XmlAttribute
 // @Transient
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


   public enum status{
    nierozegrana,
    rozegrana
    }
}
