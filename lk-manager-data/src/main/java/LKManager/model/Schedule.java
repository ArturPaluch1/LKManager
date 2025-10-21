package LKManager.model;

import LKManager.model.MatchesMz.MatchTeam;
import LKManager.model.RecordsAndDTO.ScheduleSettingsDTO;
import LKManager.model.RecordsAndDTO.ScheduleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.collection.internal.PersistentBag;

import javax.persistence.Table;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "terminarze", schema="dbo")
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement(name = "terminarz")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Round.class, MatchTeam.class})
public class Schedule implements Serializable  {




 /*   @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "terminarz_id")
    private long id;*/
   @Id
   @SequenceGenerator(
           name = "scheduleId_seq",
           sequenceName = "dbo.scheduleid_seq",
           allocationSize = 1
   )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduleId_seq")

    @Column(name = "terminarz_id")
    private long id;
    @Column(name = "nazwa")
    private String name;

    @Column(name = "rodzajTerminarza")
    private ScheduleType scheduleType;

@Column(name = "statusTerminarza", columnDefinition = "TINYINT")
@Enumerated(EnumType.ORDINAL)
private ScheduleStatus scheduleStatus;

    @Column(name = "dataStartu")
private LocalDate startDate;
    @Column(name = "dataKonca")
    private LocalDate endDate;

    //  @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    //@Fetch(FetchMode.JOIN)

    @OneToMany(mappedBy = "schedule",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE},
         //   orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)

    private List<Round> rounds = new ArrayList<>();

/*    public Schedule(List<Round> rounds, String name,ScheduleType scheduleType ) {
        this.rounds=rounds;
        this.name=name;
        this.scheduleType=scheduleType;
    }*/
/*    public Schedule( String name,ScheduleType scheduleType ) {

        this.name=name;
        this.scheduleType=scheduleType;
    }*/

    public Schedule(List<Round> rounds, String scheduleName, ScheduleType scheduleType, LocalDate startDate, ScheduleStatus status) {
        this.rounds=rounds;
        this.startDate=startDate;
        this.scheduleStatus=status;
        this.scheduleType=scheduleType;
        this.name=scheduleName;

    }
    public Schedule(String scheduleName, ScheduleType scheduleType, LocalDate startDate, ScheduleStatus status) {

        this.startDate=startDate;
        this.scheduleStatus=status;
        this.scheduleType=scheduleType;
        this.name=scheduleName;

    }

    public Schedule(List<Round> rounds, String scheduleName,LocalDate startDate,LocalDate endDate, ScheduleType scheduleType,  ScheduleStatus status ) {
        this.rounds=rounds;
        this.startDate=startDate;
        this.scheduleStatus=status;
        this.scheduleType=scheduleType;
        this.name=scheduleName;
        this.endDate=endDate;
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


    public int compareTo(ScheduleSettingsDTO o) {
        return this.name.compareTo(o.getName());
    }
}
