package LKManager.LK;

import LKManager.model.MatchesMz.MatchTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
@XmlRootElement (name="terminarz")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Runda.class, MatchTeam.class})
public class Terminarz implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "terminarz_id")
    private long id;

@Column(name = "nazwa")
    private String nazwa;

    @OneToMany(mappedBy = "terminarz",
            cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
   private List<Runda> rundy = new ArrayList<>();




    public Terminarz(List<Runda> rundy) {
        this.rundy = rundy;
    }




  //  @XmlElementWrapper(name="terminarz")
    @XmlElement(name = "Runda")
    public List<Runda> getRundy() {
        return rundy;
    }


    @XmlAttribute(name = "nazwa")
    public String getNazwa() {
        return nazwa;
    }
    public void setRundy(List<Runda> rundy) {
        this.rundy = rundy;
    }
}
