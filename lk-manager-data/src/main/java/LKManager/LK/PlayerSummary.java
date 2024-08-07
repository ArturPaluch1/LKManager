package LKManager.LK;

import LKManager.model.RecordsAndDTO.UserDataDTO;
import LKManager.model.UserMZ.UserData;
import LKManager.services.Adapters.UserAdapter;
import lombok.Getter;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "GraczPodsumowanie")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({UserData.class})
@Getter
public class PlayerSummary {
    private UserDataDTO player;
    private Integer totalPoints =0;
    private Integer goalsScrored =0;
    private Integer goalsConceded =0;
    private  Integer difference =0;
    private   boolean wolnyLos=false;
    public PlayerSummary() {
    }
    @XmlElement(name = "UserData")
    public UserDataDTO getPlayer() {
        return player;
    }

    public void setPlayer(UserDataDTO player) {
        this.player = player;
    }
    @XmlAttribute
    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void increaseTotalPoints(Integer add) {
        this.totalPoints +=add;
    }
    @XmlAttribute
    public Integer getGoalsScored() {
        return goalsScrored;
    }

    public void addGoalsScored(Byte goalsScored) {

        if(goalsScored!=null)
        this.goalsScrored += goalsScored;
    }
    @XmlAttribute
    public Integer getGoalsConceded() {
        return goalsConceded;
    }

    public void addGoalsConceded(Byte goalsConceded) {
        if(goalsConceded!=null)
        this.goalsConceded +=goalsConceded;
    }
    @XmlAttribute
    public Integer getDifference() {
        return countDifference();
    }

    private Integer countDifference() {
        return this.goalsScrored -this.goalsConceded;
    }

  /*  public void setRoznica(Integer roznica) {
        this.roznica = roznica;
    }*/




    public PlayerSummary(UserData player) {
        this.player = UserAdapter.convertUserDataToUserDataDTO(player);
        this.totalPoints = 0;
        this.wolnyLos = false;
    }





}
