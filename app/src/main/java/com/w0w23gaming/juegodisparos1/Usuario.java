package com.w0w23gaming.juegodisparos1;

public class Usuario {

    String Edad, Email, Fecha_Registro, Imagen, Nick, Pais, Pass, Uid;
    int Naves;

    public Usuario() {
    }

    public Usuario(String edad, String email, String fecha_Registro, String imagen, String nick, String pais, String pass, String uid, int naves) {
        Edad = edad;
        Email = email;
        Fecha_Registro = fecha_Registro;
        Imagen = imagen;
        Nick = nick;
        Pais = pais;
        Pass = pass;
        Uid = uid;
        Naves = naves;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFecha_Registro() {
        return Fecha_Registro;
    }

    public void setFecha_Registro(String fecha_Registro) {
        Fecha_Registro = fecha_Registro;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getNick() {
        return Nick;
    }

    public void setNick(String nick) {
        Nick = nick;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public int getNaves() {
        return Naves;
    }

    public void setNaves(int naves) {
        Naves = naves;
    }
}
