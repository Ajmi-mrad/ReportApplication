package Constat.ServerRmi.Chat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    String msg;
    String pseudo;
    Date time;

    public Message() {
        this.msg = "";
        this.pseudo = "";
        this.time = new Date();
    }

    public Message(String msg, String pseudo) {
        this.msg = msg;
        this.pseudo = pseudo;
        this.time = new Date();
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", time=" + time +
                '}';
    }
}

