package com.example.pr_sqlite;

public class User {
    public String Name;
    public String Phone;
    public String DateOfBirth;

    public User(String name, String phone, String dateOfBirth){
        Name = name;
        Phone = phone;
        DateOfBirth = dateOfBirth;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }

    @Override
    public String toString() {
        return "Имя: " + Name + "\n" +
                "Номер телефона: " + Phone + "\n" +
                "Дата рождения: " + DateOfBirth + "\n\n";
    }
}
