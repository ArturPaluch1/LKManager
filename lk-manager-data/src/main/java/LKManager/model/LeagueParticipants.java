package LKManager.model;

import LKManager.model.account.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "leagueParticipants", schema="lkm_dev")
 @NoArgsConstructor @AllArgsConstructor
public class LeagueParticipants implements Serializable {

    @Id
   // @Column(name = "participantID",  nullable = false, insertable = true, updatable = false)
    private Long participantID=null;



    @MapsId  // Używa klucza głównego User jako klucza głównego Participant
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "participantID", referencedColumnName = "user_id", nullable = false)
    private User user; // Powiązanie z encją User

    public Long getParticipantID() {
        return participantID;
    }

    public User getUser() {
        return user;
    }

    public LeagueParticipants(User user) {
this.user=user;
    }
}
