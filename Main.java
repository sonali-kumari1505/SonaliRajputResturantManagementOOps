

import java.util.*;

// ------------------------- Custom Exception -------------------------
class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}

// ------------------------- Abstract MenuItem -------------------------
abstract class MenuItem {
    protected String name;
    protected double price;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public abstract double calculatePrice(int quantity);

    public void displayItem() {
        System.out.println(name + " - $" + price);
    }
}

// ------------------------- Food and Beverage -------------------------
class FoodItem extends MenuItem {
    public FoodItem(String name, double price) {
        super(name, price);
    }

    @Override
    public double calculatePrice(int quantity) {
        return price * quantity;
    }
}

class BeverageItem extends MenuItem {
    public BeverageItem(String name, double price) {
        super(name, price);
    }

    @Override
    public double calculatePrice(int quantity) {
        return price * quantity * 1.05; // 5% extra for beverage
    }
}

// ------------------------- Billing Interface -------------------------
interface Billable {
    void generateBill();
}

// ------------------------- Order -------------------------
class Order implements Billable {
    private MenuItem item;
    private int quantity;

    public Order(MenuItem item, int quantity) throws InvalidQuantityException {
        if (quantity <= 0)
            throw new InvalidQuantityException("Quantity must be greater than 0");
        this.item = item;
        this.quantity = quantity;
    }

    public double calculateTotal() {
        return item.calculatePrice(quantity);
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void generateBill() {
        System.out.println("Item: " + item.name + ", Quantity: " + quantity + ", Price: $" + calculateTotal());
    }
}

// ------------------------- Table -------------------------
class Table {
    private int tableNumber;
    private int capacity;
    private double costPerMeal;
    private boolean booked = false;

    public Table(int tableNumber, int capacity, double costPerMeal) {
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.costPerMeal = costPerMeal;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void bookTable() {
        booked = true;
    }

    public void displayTable() {
        String status = booked ? "Booked" : "Available";
        System.out.println("Table " + tableNumber + " | Capacity: " + capacity + " | Cost per meal: $" + costPerMeal + " | " + status);
    }
}

// ------------------------- Staff -------------------------
class Staff {
    private String name;
    private String role;
    private String task;

    public Staff(String name, String role) {
        this.name = name;
        this.role = role;
        this.task = "No task assigned";
    }

    public void assignTask(String task) {
        this.task = task;
    }

    public void displayStaff() {
        System.out.println("Name: " + name + " | Role: " + role + " | Task: " + task);
    }

    public String getRole() {
        return role;
    }

    public String getTask() {
        return task;
    }
}

// ------------------------- Kitchen -------------------------
abstract class Kitchen {
    public abstract void cookOrder(Order order);

    public void displayKitchenInfo() {
        System.out.println("Kitchen is ready to process orders...");
    }
}

// ------------------------- Manager -------------------------
class Manager {
    private String name;
    private List<Staff> staffList;

    public Manager(String name) {
        this.name = name;
        staffList = new ArrayList<>();
    }

    public void addStaff(Staff staff) {
        staffList.add(staff);
    }

    public void displayStaff() {
        System.out.println("Staff in the restaurant:");
        for (Staff s : staffList) {
            s.displayStaff();
        }
    }

    public void assignOrderToKitchen(Kitchen kitchen, Order order) {
        System.out.println("Manager assigns order to kitchen.");
        kitchen.cookOrder(order);
    }

    public void assignTasks() {
        for (Staff s : staffList) {
            if (s.getRole().equalsIgnoreCase("Cook")) s.assignTask("Prepare food orders");
            if (s.getRole().equalsIgnoreCase("Waiter")) s.assignTask("Serve tables to customers");
            if (s.getRole().equalsIgnoreCase("Sweeper")) s.assignTask("Clean tables and kitchen");
        }
    }

    public List<Staff> getStaffList() {
        return staffList;
    }
}

// ------------------------- Payment -------------------------
class Payment {
    public static void processPayment(String method, double amount) throws Exception {
        if (!method.equalsIgnoreCase("cash") && !method.equalsIgnoreCase("card") && !method.equalsIgnoreCase("upi")) {
            throw new Exception("Invalid payment method! Available: Cash, Card, UPI");
        }
        System.out.println("Payment of $" + amount + " successful via " + method);
    }
}

// ------------------------- Main -------------------------
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Initialize tables
        Table[] tables = new Table[3];
        tables[0] = new Table(1, 4, 50.0);
        tables[1] = new Table(2, 6, 80.0);
        tables[2] = new Table(3, 2, 30.0);

        // Initialize menu
        MenuItem[] menu = new MenuItem[5];
        menu[0] = new FoodItem("Pizza", 12.0);
        menu[1] = new FoodItem("Burger", 8.0);
        menu[2] = new FoodItem("Pasta", 10.0);
        menu[3] = new BeverageItem("Coke", 3.0);
        menu[4] = new BeverageItem("Coffee", 4.0);

        // Initialize manager & staff
        Manager manager = new Manager("Mr. John");
        manager.addStaff(new Staff("Alice", "Cook"));
        manager.addStaff(new Staff("Bob", "Waiter"));
        manager.addStaff(new Staff("Charlie", "Sweeper"));
        manager.assignTasks();

        // Kitchen
        Kitchen kitchen = new Kitchen() {
            @Override
            public void cookOrder(Order order) {
                System.out.println("Kitchen is cooking " + order.getQuantity() + " x " + order.getItem().name);
            }
        };

        // Role Selection
        System.out.println("Welcome to Our Restaurant");
        System.out.println("Select Role: 1-Customer, 2-Manager, 3-Staff");
        int roleChoice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (roleChoice) {
            case 1: // Customer
                System.out.println("\nAvailable Tables:");
                for (Table t : tables) t.displayTable();

                System.out.print("Select table number to book: ");
                int tableNum = sc.nextInt(); sc.nextLine();
                Table bookedTable = null;
                for (Table t : tables) {
                    if (t.getTableNumber() == tableNum) bookedTable = t;
                }
                if (bookedTable == null || bookedTable.isBooked()) {
                    System.out.println("Invalid or already booked table.");
                    return;
                }
                bookedTable.bookTable();
                System.out.println("Table " + bookedTable.getTableNumber() + " booked successfully!");

                System.out.println("Plan Event: 1-Birthday Party, 2-Anniversary, 3-No Event");
                int eventChoice = sc.nextInt(); sc.nextLine();
                String event = (eventChoice==1)?"Birthday Party":(eventChoice==2)?"Anniversary":"No Event";
                System.out.println("Event Planned: " + event);

                System.out.println("\nMenu:");
                for (int i=0;i<menu.length;i++) {
                    System.out.print((i+1)+". ");
                    menu[i].displayItem();
                }

                Order[] orders = new Order[10];
                int orderCount=0;
                char more;
                do {
                    try {
                        System.out.print("Enter item number: ");
                        int itemNumMenu = sc.nextInt(); sc.nextLine();
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt(); sc.nextLine();
                        orders[orderCount++] = new Order(menu[itemNumMenu-1], qty);
                    } catch(Exception e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.print("Order more items? (y/n): ");
                    more = sc.nextLine().charAt(0);
                } while(more=='y'||more=='Y');

                // Kitchen
                for(int i=0;i<orderCount;i++)
                    manager.assignOrderToKitchen(kitchen, orders[i]);

                // Bill
                System.out.println("\nGenerating Bill...");
                double grandTotal=0;
                for(int i=0;i<orderCount;i++) {
                    orders[i].generateBill();
                    grandTotal += orders[i].calculateTotal();
                }
                double tax = grandTotal*0.05;
                System.out.println("Subtotal: $" + grandTotal);
                System.out.println("Tax: $" + tax);
                System.out.println("Total: $" + (grandTotal+tax));

                // Payment
                try {
                    System.out.print("Enter payment method (Cash/Card/UPI): ");
                    String method = sc.nextLine();
                    Payment.processPayment(method, grandTotal+tax);
                } catch(Exception e) { System.out.println(e.getMessage()); }

                System.out.println("Enjoy your " + event + " at Table "+ bookedTable.getTableNumber() + "!");
                System.out.println("You can go to rear view of restaurant and click photos!");

                break;

            case 2: // Manager
                System.out.println("\nManager Dashboard:");
                manager.displayStaff();
                break;

            case 3: // Staff
                System.out.println("\nEnter your name: ");
                String staffName = sc.nextLine();
                boolean found=false;
                for(Staff s : manager.getStaffList()) {
                    if(s.getRole().equalsIgnoreCase("Cook") && s.getRole().equalsIgnoreCase("Cook") || s.getRole().equalsIgnoreCase("Waiter") || s.getRole().equalsIgnoreCase("Sweeper")) {
                        if(s.getRole().equalsIgnoreCase("Cook") && s.getRole().equalsIgnoreCase("Cook")) {
                            System.out.println("You are assigned task: Prepare food orders");
                        } else {
                            System.out.println("Your assigned task: " + s.getTask());
                        }
                        found=true;
                    }
                }
                if(!found) System.out.println("Staff not found!");
                break;

            default:
                System.out.println("Invalid role selection.");
        }

        sc.close();
    }
}
