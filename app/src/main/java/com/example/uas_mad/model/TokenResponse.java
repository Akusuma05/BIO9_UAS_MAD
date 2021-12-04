package com.example.uas_mad.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

public class TokenResponse implements Parcelable {

    private String token_type;
    private int expires_in;
    private String access_token;
    private String refresh_token;

    protected TokenResponse(Parcel in) {
        token_type = in.readString();
        expires_in = in.readInt();
        access_token = in.readString();
        refresh_token = in.readString();
    }

    public static final Creator<TokenResponse> CREATOR = new Creator<TokenResponse>() {
        @Override
        public TokenResponse createFromParcel(Parcel in) {
            return new TokenResponse(in);
        }

        @Override
        public TokenResponse[] newArray(int size) {
            return new TokenResponse[size];
        }
    };

    public static TokenResponse objectFromData(String str) {

        return new Gson().fromJson(str, TokenResponse.class);
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getAuthorization(){
        return this.token_type + " " + this.access_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(token_type);
        parcel.writeInt(expires_in);
        parcel.writeString(access_token);
        parcel.writeString(refresh_token);
    }
}
