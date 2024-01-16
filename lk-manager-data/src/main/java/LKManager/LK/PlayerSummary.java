package LKManager.LK;

import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "GraczPodsumowanie")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({UserData.class})
public class PlayerSummary {
    private UserData gracz;
    private Integer sumaPunktow=0;
    private Integer goleStrzelone=0;
    private Integer goleStracone=0;
    private final Integer roznica=0;

    public PlayerSummary() {
    }
    @XmlElement(name = "UserData")
    public UserData getGracz() {
        return gracz;
    }

    public void setGracz(UserData gracz) {
        this.gracz = gracz;
    }
    @XmlAttribute
    public Integer getSumaPunktow() {
        return sumaPunktow;
    }

    public void zwiekszSumePunktow(Integer dodaj) {
        this.sumaPunktow +=dodaj;
    }
    @XmlAttribute
    public Integer getGoleStrzelone() {
        return goleStrzelone;
    }

    public void addGoleStrzelone(Integer goleStrzelone) {
        this.goleStrzelone += goleStrzelone;
    }
    @XmlAttribute
    public Integer getGoleStracone() {
        return goleStracone;
    }

    public void addGoleStracone(Integer goleStracone) {
        this.goleStracone +=goleStracone;
    }
    @XmlAttribute
    public Integer getRoznica() {
        return countDifference();
    }

    private Integer countDifference() {
        return this.goleStrzelone-this.goleStracone;
    }

  /*  public void setRoznica(Integer roznica) {
        this.roznica = roznica;
    }*/
}
