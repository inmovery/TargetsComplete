package hse.t.targetscomplete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Target {
    String Name;
    String Specific;
    Date Time_bound;
    ArrayList MicroTarget;
    Boolean CompleteOk;
    Double Progress;
    static int CountMTComplited = 0;
    Target() {
        this.Name = "Undefined";
        this.Specific = "Undefined";
        this.Time_bound = null;
        this.MicroTarget = new ArrayList(0);
        this.CompleteOk = false;
    }

    Target(String Name,String Specific,Date Time) {
        this.Name = Name;
        this.Specific = Specific;
        this.Time_bound = Time;
        this.CompleteOk = false;
        this.Progress = 0.0;
    }
    void ChangeProgress(int CountMicroTargetsComplited) {
        CountMTComplited +=CountMicroTargetsComplited;
        this.Progress = 100/(double)CountMTComplited;
    }
    void CompleteTarget() {
        this.CompleteOk=true;
    }
    void ChangeTimeOfTagret(Date NewDate) {
        this.Time_bound = NewDate;
    }
    void AddMicroTarget(String NameOfMTagret) {
        this.MicroTarget.add(NameOfMTagret);
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpecific() {
        return Specific;
    }

    public void setSpecific(String specific) {
        Specific = specific;
    }

    public Date getTime_bound() {
        return Time_bound;
    }

    public void setTime_bound(Date time_bound) {
        Time_bound = time_bound;
    }

    public ArrayList getMicroTarget() {
        return MicroTarget;
    }

    public void setMicroTarget(ArrayList microTarget) {
        MicroTarget = microTarget;
    }

    public Boolean getCompleteOk() {
        return CompleteOk;
    }

    public void setCompleteOk(Boolean completeOk) {
        CompleteOk = completeOk;
    }

    public Double getProgress() {
        return Progress;
    }

    public void setProgress(Double progress) {
        Progress = progress;
    }

    public static int getCountMTComplited() {
        return CountMTComplited;
    }

    public static void setCountMTComplited(int countMTComplited) {
        CountMTComplited = countMTComplited;
    }
}
