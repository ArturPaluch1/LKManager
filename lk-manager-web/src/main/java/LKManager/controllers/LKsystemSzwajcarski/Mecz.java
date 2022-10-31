package LKManager.controllers.LKsystemSzwajcarski;

import LKManager.controllers.Options;

import java.io.Serializable;

public class Mecz implements Serializable {
    Options.GrajekTurniejowy grajekTurniejowy1;
    Options.GrajekTurniejowy grajekTurniejowy2;
    //data
    String wynik1 = "";
    String wynik2 = "";
    Integer runda = 0;

    public Mecz(Options.GrajekTurniejowy grajekTurniejowy1, Options.GrajekTurniejowy grajekTurniejowy2, Integer runda) {
        this.grajekTurniejowy1 = grajekTurniejowy1;
        this.grajekTurniejowy2 = grajekTurniejowy2;
        this.runda = runda;
    }

    public Options.GrajekTurniejowy getGrajekTurniejowy1() {
        return grajekTurniejowy1;
    }

    public void setGrajekTurniejowy1(Options.GrajekTurniejowy grajekTurniejowy1) {
        this.grajekTurniejowy1 = grajekTurniejowy1;
    }

    public Options.GrajekTurniejowy getGrajekTurniejowy2() {
        return grajekTurniejowy2;
    }

    public void setGrajekTurniejowy2(Options.GrajekTurniejowy grajekTurniejowy2) {
        this.grajekTurniejowy2 = grajekTurniejowy2;
    }

    public String getWynik1() {
        return wynik1;
    }

    public void setWynik1(String wynik1) {
        this.wynik1 = wynik1;
    }

    public String getWynik2() {
        return wynik2;
    }

    public void setWynik2(String wynik2) {
        this.wynik2 = wynik2;
    }

    public Integer getRunda() {
        return runda;
    }

    public void setRunda(Integer runda) {
        this.runda = runda;
    }
/*    public Mecz(GrajekTurniejowy grajekTurniejowy1, int runda) {
        this.grajekTurniejowy1 = grajekTurniejowy1;
        this.runda = runda;
    }*/
}
