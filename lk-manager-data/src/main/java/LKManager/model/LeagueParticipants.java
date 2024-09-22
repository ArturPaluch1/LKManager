package LKManager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "leagueParticipants", schema="lkm_dev")
@Getter @Setter @NoArgsConstructor
public class LeagueParticipants implements Serializable {

    @Id
    @Column(name = "id",  nullable = false, insertable = true, updatable = false)
    private Long id=null;

    public LeagueParticipants(Long id) {
        this.id = id;
    }
}
