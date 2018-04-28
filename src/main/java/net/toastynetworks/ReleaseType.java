package net.toastynetworks;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public enum ReleaseType {
    @SerializedName("release")
    RELEASE,
    @SerializedName("beta")
    BETA,
    @SerializedName("alpha")
    ALPHA;

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ENGLISH);
    }

    public static ReleaseType fromName(String name) {
        for(ReleaseType type : values()) {
            if(type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
}