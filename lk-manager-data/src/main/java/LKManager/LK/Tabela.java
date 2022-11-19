package LKManager.LK;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Tabela")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlSeeAlso({GraczPodsumowanie.class})
public class Tabela implements Serializable {

    private List<GraczPodsumowanie> graczePodsumowanie = new ArrayList<>();

    public Tabela() {




    }

    @XmlElement(name = "GraczPodsumowanie")
    public List<GraczPodsumowanie> getGraczePodsumowanie() {
        return graczePodsumowanie;
    }

    public void setGraczePodsumowanie(List<GraczPodsumowanie> graczePodsumowanie) {
        this.graczePodsumowanie = graczePodsumowanie;
    }
}
