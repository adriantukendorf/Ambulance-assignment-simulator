package uk.ac.aber.cs21120.solution;

import uk.ac.aber.cs21120.hospital.IJob;
import uk.ac.aber.cs21120.hospital.ISimulator;
import uk.ac.aber.cs21120.hospital.JobDisplay;
import uk.ac.aber.cs21120.hospital.RandomPriorityGenerator;

import java.util.*;

public class Simulator implements ISimulator {
    private int tickTime = 0; //simulator's time
    private int number_of_ambulances; //variable handling number of ambulances in the simulator
    private PriorityQueue<IJob> jobs_queue = new PriorityQueue<>(); //Priority Queue Data structure, handling queue of jobs to do
    private Set<IJob> currentJobs = new HashSet<>(); //Set Data Structure handling currently working jobs

    private Map<Integer, Integer> totalJobs = new HashMap(); //HashMap data structure handling total of Jobs from following priority
    private Map<Integer, Integer> totalTime = new HashMap(); //HashMap data structure handling total of times for following priorities

    public Simulator(int number_of_ambulances) {
        this.number_of_ambulances = number_of_ambulances;
    }

    @Override
    public void add(IJob j) { //Method which is adding jobs to the simulator
        j.setSubmitTime(tickTime);
        jobs_queue.add(j);
    }

    @Override
    public void tick() { //Update of the simulator
        if (number_of_ambulances > 0 && !jobs_queue.isEmpty()) { //If there are availability ambulances and job's queue is not empty
            int s = jobs_queue.size();
            for (int i = 0; i < s; i++) {
                if (isAnyAmbulanceFree()) { //If there are free ambulances then add job to the working jobs and remove from the queue, correcting number of ambulances
                    currentJobs.add(jobs_queue.poll());
                    number_of_ambulances--;
                }
            }
        }
        if (!currentJobs.isEmpty()) {
            ArrayList<IJob> arrayjob = new ArrayList(); //ArrayList to hold jobs to delete when done
            for (IJob runJob : currentJobs) {
                runJob.tick();
                if (runJob.isDone()) { //If job is completed then recording complete time and number of jobs from following priorities
                    totalJobs.put(runJob.getPriority(), totalJobs.getOrDefault(runJob.getPriority(), 0) + 1);
                    totalTime.put(runJob.getPriority(), totalJobs.getOrDefault(runJob.getPriority(), 0) + runJob.getTimeSinceSubmit(tickTime));
                    arrayjob.add(runJob);
                    number_of_ambulances++;
                }
            }
            for (IJob runJob : arrayjob) { //Removing jobs from working jobs, it was impossible to implement in the for loop because of data structure Set
                currentJobs.remove(runJob);
            }
        }
        tickTime = tickTime + 1;
    }

    @Override
    public int getTime() {
        return tickTime;
    }

    @Override
    public boolean allDone() { //Checking if jobs are done and queue empty
        if (jobs_queue.isEmpty() && currentJobs.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Set<Integer> getRunningJobs() { //Converting Set holding IJob to Set holdings integers of IDs
        Set<Integer> idRunning = new HashSet<>();

        for (IJob runJob : currentJobs) {
            idRunning.add(runJob.getID());
        }
        return idRunning;
    }

    boolean isAnyAmbulanceFree() { //Method which is checking if there are free ambulances ready to take a job
        if (number_of_ambulances == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public double getAverageJobCompletionTime(int priority) { //Method calculating average completion time for each priority
        return (double) totalTime.get(priority) / (double) totalJobs.get(priority);
    }

    public static void task3() { //Task3 - exact functionality as described in assignment brief
        Simulator s = new Simulator(4);
        RandomPriorityGenerator randPrior = new RandomPriorityGenerator();
        Random randDuration = new Random();
        JobDisplay jobDisplay = new JobDisplay();
        int jobIDgen = 0;
        for (int ticks = 0; ticks < 10000; ticks++) {
            if (randDuration.nextInt(3) == 0) {
                Job j = new Job(jobIDgen, randPrior.next(), randDuration.nextInt(10) + 10);
                s.add(j);
                jobIDgen = jobIDgen + 1;
            }
        }
        while (!s.allDone()) {
            s.tick();
            jobDisplay.add(s);
        }
        jobDisplay.show();
        System.out.println("Priority 0: " + s.getAverageJobCompletionTime(0));
        System.out.println("Priority 1: " + s.getAverageJobCompletionTime(1));
        System.out.println("Priority 2: " + s.getAverageJobCompletionTime(2));
        System.out.println("Priority 3: " + s.getAverageJobCompletionTime(3));
    }

    public static void task4() { //Task4 - exact functionality as described in assignment brief
        int caseNr = 0;
        for (int ambulance = 4; ambulance < 21; ambulance++) {
            Simulator s = new Simulator(ambulance);
            RandomPriorityGenerator randPrior = new RandomPriorityGenerator();
            Random randDuration = new Random();
            int jobIDgen = 0;
            for (int ticks = 0; ticks < 10000; ticks++) {
                if (randDuration.nextInt(3) == 0) {
                    Job j = new Job(jobIDgen, randPrior.next(), randDuration.nextInt(10) + 10);
                    s.add(j);
                    jobIDgen = jobIDgen + 1;
                }
            }
            while (!s.allDone()) {
                s.tick();
            }

            caseNr++;
            //This is printing average completion time for each priority and each case.
            System.out.println("Case number: " + caseNr + " * Number of ambulances: " + ambulance);
            System.out.println("Priority 0: " + s.getAverageJobCompletionTime(0));
            System.out.println("Priority 1: " + s.getAverageJobCompletionTime(1));
            System.out.println("Priority 2: " + s.getAverageJobCompletionTime(2));
            System.out.println("Priority 3: " + s.getAverageJobCompletionTime(3));
            System.out.println("\n");
        }
    }

}
