package phuongnq.asm3.entity;

public class DoctorCreationRequest {
    private User user;
    private Doctor doctor;

    public DoctorCreationRequest(User user, Doctor doctor) {
        this.user = user;
        this.doctor = doctor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
