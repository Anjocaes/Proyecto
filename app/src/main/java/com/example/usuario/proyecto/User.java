package com.example.usuario.proyecto;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class User extends SugarRecord{
    @Unique
    String user;
    String pass;
    int mod;
    int pro;

    public User(){
    }

    public User(String user, String pass, int mod, int pro){
        this.user = user;
        this.pass = pass;
        this.mod = mod;
        this.pro = pro;
    }
}
