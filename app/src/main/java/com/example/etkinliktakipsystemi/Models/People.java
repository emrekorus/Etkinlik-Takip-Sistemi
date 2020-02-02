package com.example.etkinliktakipsystemi.Models;

public class People {
    String qr_id;
    String name_surname;
    String phone_number;


    public People() {
    }

    public People(String qr_id, String name_surname, String phone_number) {
        this.qr_id = qr_id;
        this.name_surname = name_surname;
        this.phone_number = phone_number;
    }

    public String getQr_id() {
        return qr_id;
    }

    public void setQr_id(String qr_id) {
        this.qr_id = qr_id;
    }

    public String getName_surname() {
        return name_surname;
    }

    public void setName_surname(String name_surname) {
        this.name_surname = name_surname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "People{" +
                "qr_id='" + qr_id + '\'' +
                ", name_surname='" + name_surname + '\'' +
                ", phone_number='" + phone_number + '\'' +
                '}';
    }
}
