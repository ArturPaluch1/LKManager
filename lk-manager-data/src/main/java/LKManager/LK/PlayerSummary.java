package LKManager.LK;


import LKManager.model.RecordsAndDTO.UserMzDTO;
import LKManager.model.UserMZ.MZUserData;
import LKManager.services.Adapters.UserAdapter;
import lombok.Getter;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "GraczPodsumowanie")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({MZUserData.class})
@Getter
public class PlayerSummary {
    private UserMzDTO player;
    private Integer totalPoints =0;
    private Integer goalsScrored =0;
    private Integer goalsConceded =0;
    private  Integer difference =0;
    private   boolean wolnyLos=false;
    public PlayerSummary() {
    }
    @XmlElement(name = "UserData")
    public UserMzDTO getPlayer() {
        return player;
    }

    public void setPlayer(UserMzDTO player) {
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




    public PlayerSummary(MZUserData player) {
        this.player = UserAdapter.convertMZUserDataToUserMzDTO(player);
        this.totalPoints = 0;
        this.wolnyLos = false;
    }





}
