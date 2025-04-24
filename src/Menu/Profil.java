package Menu;

import javax.swing.*;
import java.util.ArrayList;

public class Profil {
    private String first_name;
    private String last_name;
    private String nickname;

    public Profil(String first_name, String last_name, String nickname) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.nickname = nickname;
    }
    public Profil(){

    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void toString(Profil profil){
        System.out.println("Nom: "+profil.getFirst_name());
        System.out.println("Prenom: "+profil.getLast_name());
        System.out.println("Pseudo: "+profil.getNickname());
    }
    public static Profil rechercheProfil(String nickname){
        Profil profil = new Profil();
        if(Data.data.size() > 0){
            for (Profil p : Data.data){
                if(p.getNickname().equals(nickname)){
                    profil = p;
                }
            }
        }
        return profil;
    }
}
