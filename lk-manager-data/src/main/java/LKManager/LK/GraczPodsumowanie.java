package LKManager.LK;

import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "GraczPodsumowanie")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({UserData.class})
public class GraczPodsumowanie  {
    private UserData gracz;
    private String sumaPunktow="";
    private String goleStrzelone="";
    private String goleStracone="";
    private Integer roznica=0;

    public GraczPodsumowanie() {
    }
    @XmlElement(name = "UserData")
    public UserData getGracz() {
        return gracz;
    }

    public void setGracz(UserData gracz) {
        this.gracz = gracz;
    }
    @XmlAttribute
    public String getSumaPunktow() {
        return sumaPunktow;
    }

    public void setSumaPunktow(String sumaPunktow) {
        this.sumaPunktow = sumaPunktow;
    }
    @XmlAttribute
    public String getGoleStrzelone() {
        return goleStrzelone;
    }

    public void setGoleStrzelone(String goleStrzelone) {
        this.goleStrzelone = goleStrzelone;
    }
    @XmlAttribute
    public String getGoleStracone() {
        return goleStracone;
    }

    public void setGoleStracone(String goleStracone) {
        this.goleStracone = goleStracone;
    }
    @XmlAttribute
    public Integer getRoznica() {
        return roznica;
    }

    public void setRoznica(Integer roznica) {
        this.roznica = roznica;
    }
}
