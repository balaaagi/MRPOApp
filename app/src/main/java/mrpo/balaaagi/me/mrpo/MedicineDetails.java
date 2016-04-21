package mrpo.balaaagi.me.mrpo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by balaaagi on 21/04/16.
 */
public class MedicineDetails implements Serializable,Parcelable {
    public String name;
    public int noOfDays;
    public boolean morning=false;
    public boolean noon=false;
    public boolean evening=false;
    public boolean beforeFood=false;
    public boolean afterFood=false;
    public String date;

    public MedicineDetails(){

    }

    public MedicineDetails(String s) {
        this.name=s;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public boolean isMorning() {
        return morning;
    }

    public boolean isNoon() {
        return noon;
    }

    public boolean isEvening() {
        return evening;
    }

    public boolean isBeforeFood() {
        return beforeFood;
    }

    public boolean isAfterFood() {
        return afterFood;
    }

    public String getDate() {
        return date;
    }

    public MedicineDetails(String name, String noOfDays, Object daysInfo, String beforeFood, String date){
        this.name=name;
        this.noOfDays=Integer.parseInt(noOfDays);
        this.date=date;
        if(daysInfo instanceof String){
            if(daysInfo.toString().equals("morning")){
                this.morning=true;
            }else if(daysInfo.toString().equals("noon")){
                this.noon=true;
            }else{
                this.evening=true;
            }
        }else if(daysInfo instanceof String[]){
            String[] daysDetails=(String[]) daysInfo;
            for(int i=0;i<daysDetails.length;i++){
                if (daysDetails[i].equals("morning")){
                    this.morning=true;
                }else if(daysDetails[i].equals("noon")){
                    this.noon=true;
                }else{
                    this.evening=true;
                }

            }

        }
        if(beforeFood.equals("before"))
            this.beforeFood=true;
        else
            this.afterFood=true;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.name);
            parcel.writeInt(this.noOfDays);
            parcel.writeString(String.valueOf(this.morning));
            parcel.writeString(String.valueOf(this.noon));
        parcel.writeString(String.valueOf(this.evening));
        parcel.writeString(String.valueOf(this.beforeFood));
        parcel.writeString(String.valueOf(this.afterFood));
    }

    private MedicineDetails(Parcel in){
        this.name=in.readString();
        this.noOfDays=in.readInt();
        this.morning=Boolean.parseBoolean(in.readString());
        this.noon=Boolean.parseBoolean(in.readString());
        this.evening=Boolean.parseBoolean(in.readString());
        this.beforeFood=Boolean.parseBoolean(in.readString());
        this.afterFood=Boolean.parseBoolean(in.readString());
    }

    public static final Parcelable.Creator<MedicineDetails> CREATOR = new Parcelable.Creator<MedicineDetails>() {

        @Override
        public MedicineDetails createFromParcel(Parcel source) {
            return new MedicineDetails(source);
        }

        @Override
        public MedicineDetails[] newArray(int size) {
            return new MedicineDetails[size];
        }
    };
}
