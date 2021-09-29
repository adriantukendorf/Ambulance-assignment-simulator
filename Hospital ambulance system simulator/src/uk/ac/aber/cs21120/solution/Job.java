package uk.ac.aber.cs21120.solution;
import uk.ac.aber.cs21120.hospital.IJob;
public class Job implements IJob, Comparable<IJob>{

    int id;
    int priority;
    int duration;
    int submit = 0;

    public Job(int id, int priority, int duration) {
        this.id = id;
        this.priority = priority;
        this.duration = duration;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void tick() { //Updating duration of a job
    duration = duration - 1;
    }

    @Override
    public boolean isDone() {
        if(duration<1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int getTimeSinceSubmit(int now) {
        if (now - submit==0){
            throw new RuntimeException();
        }else{
            return now - submit;
        }
    }

    @Override
    public void setSubmitTime(int time) {
    this.submit = time;
    }

    @Override//comparing
    public int compareTo(IJob iJob) {
        return this.getPriority() - iJob.getPriority();
    }
}
