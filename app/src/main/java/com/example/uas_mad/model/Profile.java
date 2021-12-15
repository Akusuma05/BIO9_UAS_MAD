package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class Profile implements Parcelable{
    private int id;
    private String email;
    private String username;
    private String name;
    private String school;
    private String role;
    private String city;
    private int birthyear;
    private String is_login;
    private String is_active;
    private String created_at;
    private String updated_at;

    protected Profile(Parcel in) {
        id = in.readInt();
        email = in.readString();
        username = in.readString();
        name = in.readString();
        school = in.readString();
        role = in.readString();
        city = in.readString();
        birthyear = in.readInt();
        is_login = in.readString();
        is_active = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public static Profile objectFromData(String str) {

        return new Gson().fromJson(str, Profile.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(int birthyear) {
        this.birthyear = birthyear;
    }

    public String getIs_login() {
        return is_login;
    }

    public void setIs_login(String is_login) {
        this.is_login = is_login;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(email);
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(school);
        parcel.writeString(role);
        parcel.writeString(city);
        parcel.writeInt(birthyear);
        parcel.writeString(is_login);
        parcel.writeString(is_active);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
    }

//    private List<Users> users;
//
//    protected Profile(Parcel in) {
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
//        @Override
//        public Profile createFromParcel(Parcel in) {
//            return new Profile(in);
//        }
//
//        @Override
//        public Profile[] newArray(int size) {
//            return new Profile[size];
//        }
//    };
//
//    public static Profile objectFromData(String str) {
//
//        return new Gson().fromJson(str, Profile.class);
//    }
//
//    public List<Users> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<Users> users) {
//        this.users = users;
//    }
//
//    public static class Users {
//        private int id;
//        private String name;
//        private String email;
//        private String password;
//        private String role;
//        private String created_at;
//        private String is_login;
//        private String is_active;
//        private String username;
//        private String school;
//        private int birthyear;
//        private String city;
//
//        public static Users objectFromData(String str) {
//
//            return new Gson().fromJson(str, Users.class);
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//
//        public void setPassword(String password) {
//            this.password = password;
//        }
//
//        public String getRole() {
//            return role;
//        }
//
//        public void setRole(String role) {
//            this.role = role;
//        }
//
//        public String getCreated_at() {
//            return created_at;
//        }
//
//        public void setCreated_at(String created_at) {
//            this.created_at = created_at;
//        }
//
//        public String getIs_login() {
//            return is_login;
//        }
//
//        public void setIs_login(String is_login) {
//            this.is_login = is_login;
//        }
//
//        public String getIs_active() {
//            return is_active;
//        }
//
//        public void setIs_active(String is_active) {
//            this.is_active = is_active;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getSchool() {
//            return school;
//        }
//
//        public void setSchool(String school) {
//            this.school = school;
//        }
//
//        public int getBirthyear() {
//            return birthyear;
//        }
//
//        public void setBirthyear(int birthyear) {
//            this.birthyear = birthyear;
//        }
//
//        public String getCity() {
//            return city;
//        }
//
//        public void setCity(String city) {
//            this.city = city;
//        }
//    }

}
