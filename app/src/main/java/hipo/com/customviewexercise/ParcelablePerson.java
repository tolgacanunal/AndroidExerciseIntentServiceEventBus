package hipo.com.customviewexercise;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tolga Can "tesleax" Ünal on 13/06/17
 */
public class ParcelablePerson implements Parcelable {
    String name;
    int yearsOfExperience;
    List<Skill> skillSet;
    float favoriteFloat;



    public static final Creator<ParcelablePerson> CREATOR = new Creator<ParcelablePerson>() {
        @Override
        public ParcelablePerson createFromParcel(Parcel in) {
            return new ParcelablePerson(in);
        }

        @Override
        public ParcelablePerson[] newArray(int size) {
            return new ParcelablePerson[size];
        }
    };

    private ParcelablePerson(Parcel in) {
        name = in.readString();
        yearsOfExperience = in.readInt();
        skillSet = new ArrayList<>();
        in.readTypedList(skillSet, Skill.CREATOR);
        favoriteFloat = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(yearsOfExperience);
        dest.writeTypedList(skillSet);
        dest.writeFloat(favoriteFloat);
    }


    static class Skill implements Parcelable {
        String name;
        boolean programmingRelated;

        Skill(Parcel in) {
            this.name = in.readString();
            this.programmingRelated = (in.readInt() == 1);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
            dest.writeInt(programmingRelated ? 1 : 0);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        static final Creator<Skill> CREATOR = new Creator<Skill>() {
            @Override
            public Skill createFromParcel(Parcel in) {
                return new Skill(in);
            }

            @Override
            public Skill[] newArray(int size) {
                return new Skill[size];
            }
        };
    }
}
