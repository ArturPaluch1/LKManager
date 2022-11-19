package LKManager.LK;

import LKManager.model.UserMZ.UserData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public   class GrajekTurniejowy implements Serializable
{
    UserData grajek;


    int punkty=0;
    List<String> listaPrzeciwnikow = new ArrayList<>();
    public GrajekTurniejowy(UserData grajek) {
        this.grajek = grajek;
    }

    public GrajekTurniejowy(UserData grajek,  int punkty, List<String> listaPrzeciwnikow) {
        this.grajek = grajek;

        this.punkty = punkty;
        this.listaPrzeciwnikow = listaPrzeciwnikow;
    }

    public UserData getGrajek() {
        return grajek;
    }

    public List<String> getListaPrzeciwnikow() {
        return listaPrzeciwnikow;
    }

    public void setListaPrzeciwnikow(List<String> listaPrzeciwnikow) {
        this.listaPrzeciwnikow = listaPrzeciwnikow;
    }

    public void setGrajek(UserData grajek) {
        this.grajek = grajek;
    }





    public int getPunkty() {
        return punkty;
    }

    public void setPunkty(int punkty) {
        this.punkty = punkty;
    }
}
