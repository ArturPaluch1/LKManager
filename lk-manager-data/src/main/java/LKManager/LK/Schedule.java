package LKManager.LK;

import LKManager.model.MatchesMz.MatchTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.collection.internal.PersistentBag;

import javax.persistence.*;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terminarze")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement(name = "terminarz")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Round.class, MatchTeam.class})
public class Schedule implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "terminarz_id")
    private long id;

    @Column(name = "nazwa")
    private String name;

    //  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    //@Fetch(FetchMode.JOIN)

    @OneToMany(mappedBy = "schedule",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE},
            fetch = FetchType.LAZY)
    private List<Round> rounds = new ArrayList<>();


    public Schedule(List<Round> rounds) {
        this.rounds = rounds;
    }


    //  @XmlElementWrapper(name="terminarz")
    @XmlElement(name = "Runda")
    public List<Round> getRounds() {
        return rounds;
    }

   public boolean checkMatchesInitialization()
    {
        return ((PersistentBag) this.getRounds().get(0).getMatches()).wasInitialized();
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    @XmlAttribute(name = "nazwa")
    public String getName() {
        return name;
    }
}
