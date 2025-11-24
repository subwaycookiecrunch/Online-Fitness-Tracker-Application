package model;

public class Admin extends User {
    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, "ADMIN");
    }

    @Override
    public void showDashboard() {
        System.out.println("Opening Admin Dashboard...");
        // Logic to open Admin Dashboard UI will be handled in Controller/UI layer
    }
}
