package com.iutbmteprow.shootingarchery.dbman;

import java.util.Date;

public class PartieGlobal {
    private int id;
    private Date datePartie;
    private String TypePartie;

    public PartieGlobal(int id,Date datePartie, String TypePartie)
    {
        super();
        this.id = id;
        this.datePartie = datePartie;
        this.TypePartie = TypePartie;
    }


    public int getId() {
        return id;
    }


    public Date getDatePartie() {
        return datePartie;
    }
    public void setDatePartie(Date datePartie) {
        this.datePartie = datePartie;
    }

    public String getTypePartie() {
        return TypePartie;
    }


}
