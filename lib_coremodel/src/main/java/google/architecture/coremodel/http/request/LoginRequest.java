package google.architecture.coremodel.http.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhanglianglin
 * @version 1.0
 * @desc
 * @since 2018/09/04
 */

public class LoginRequest implements Parcelable {

    public static final Parcelable.Creator<LoginRequest> CREATOR = new Parcelable.Creator<LoginRequest>() {
        @Override
        public LoginRequest createFromParcel(Parcel source) {
            return new LoginRequest(source);
        }

        @Override
        public LoginRequest[] newArray(int size) {
            return new LoginRequest[size];
        }
    };
    private String userName;

    public LoginRequest(String userName) {
        this.userName = userName;
    }

    public LoginRequest() {
    }

    protected LoginRequest(Parcel in) {
        this.userName = in.readString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
    }
}
