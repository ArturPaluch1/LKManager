package LKManager.LK;

import LKManager.model.MatchesMz.Match;
import LKManager.model.UserMZ.UserData;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({Match.class, UserData.class})
public class Runda implements Serializable {
private int nr;
private List<Match> mecze;
    private XMLGregorianCalendar data;
private status status;

    public Runda(int nr, XMLGregorianCalendar data) {
        this.nr = nr;
        this.data = data;
        mecze= new ArrayList<>();
    }

    public Runda() {
    }
    @XmlAttribute
    public Runda.status getStatus() {
        return status;
    }

    public void setStatus(Runda.status status) {
        this.status = status;
    }

    @XmlAttribute
    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    @XmlElement(name = "Match")
    public List<Match> getMecze() {
        return mecze;
    }

    public void setMecze(List<Match> mecze) {
        this.mecze = mecze;
    }

    @XmlAttribute
    public XMLGregorianCalendar getData() {
        return data;
    }

    public void setData(XMLGregorianCalendar data) {
        this.data = data;
    }


   public enum status{
    nierozegrana,
    rozegrana
    }
}
