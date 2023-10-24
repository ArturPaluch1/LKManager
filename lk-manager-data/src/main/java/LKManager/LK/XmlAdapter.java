package LKManager.LK;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public  class XmlAdapter {



    public static LocalDate unmarshall(XMLGregorianCalendar data) {

        return   LocalDate.of(data.getYear(),data.getMonth(),data.getDay());


    }

    public static XMLGregorianCalendar marshall(Date target) throws DatatypeConfigurationException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


        GregorianCalendar gregorianCalendar = new GregorianCalendar(target.getYear(),target.getMonth(),target.getDay());

        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar newDate =
                datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);


        return newDate;
    }
}
