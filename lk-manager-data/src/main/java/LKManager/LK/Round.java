package LKManager.LK;

import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "rundy", schema="lkm_dev")
@Getter
@Setter
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Match.class, UserData.class})

public class Round implements Serializable {


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
    private Schedule schedule;


    @Column(name = "nr_rundy")
private int nr;

//@Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "round",
            cascade = { CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE},
        //    orphanRemoval = true,)
            fetch = FetchType.LAZY)
      /*      @JoinTable
                    (
                            name="mecze",
                            joinColumns={ @JoinColumn(name="id", referencedColumnName="runda_id") },
                            inverseJoinColumns={ @JoinColumn(name="id", referencedColumnName="mecz_id") }
                    )
*/

private List<Match> matches;




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
        matches = new ArrayList<>();
    }

    public Round() {
    }





    @XmlAttribute
    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    @XmlElement(name = "Match")

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
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
