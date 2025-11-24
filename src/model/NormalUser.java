package model;

public class NormalUser extends User {
    public NormalUser(int id, String name, String email, String password) {
        super(id, name, email, password, "USER");
    }

    @Override
    public void showDashboard() {
        System.out.println("Opening User Dashboard...");
        // Logic to open User Dashboard UI will be handled in Controller/UI layer
    }
}
