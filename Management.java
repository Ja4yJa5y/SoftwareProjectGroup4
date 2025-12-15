package library;

public class Management {
    protected String staffID;
    protected String staffName;

    public Management(String staffID, String staffName) {
        this.staffID = staffID;
        this.staffName = staffName;
    }

    public String getStaffID() {
        return staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    // updateBook(id:int,newPrice:double,newAvailability:boolean) : void
    public void updateBook(int id, double newPrice, boolean newAvailability) {
        LibraryItems.updateBook(id, newPrice, newAvailability);
    }
}
