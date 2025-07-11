package day4;
import java.io.*;
import java.util.*;

public class studentReportManager {
    private static final String FILE_NAME = "StudentReports.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("===== Student Report Manager =====");

        while (true) {
            System.out.println("\n1. Add Student Report");
            System.out.println("2. View All Reports");
            System.out.println("3. Search by Roll Number");
            System.out.println("4. Delete Student Record");
            System.out.println("5. Update Student Marks");
            System.out.println("6. Clear All Records");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addStudentReport(scanner);
                case 2 -> viewReports();
                case 3 -> searchByRoll(scanner);
                case 4 -> deleteRecord(scanner);
                case 5 -> updateMarks(scanner);
                case 6 -> clearAllRecords(scanner);
                case 7 -> {
                    System.out.println("Exiting... ");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private static void addStudentReport(Scanner scanner) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            System.out.print("Enter Student Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Roll Number: ");
            String roll = scanner.nextLine();

            System.out.print("Enter Subject: ");
            String subject = scanner.nextLine();

            System.out.print("Enter Marks: ");
            int marks = scanner.nextInt();
            scanner.nextLine();

            String record = name + "," + roll + "," + subject + "," + marks;
            writer.write(record);
            writer.newLine();

            System.out.println(" Report saved successfully!");
        } catch (IOException e) {
            System.out.println(" Error writing to file: " + e.getMessage());
        }
    }

    private static void viewReports() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nAll Student Reports:");
            System.out.println("----------------------------------------------------");
            System.out.printf("%-15s %-10s %-10s %-5s\n", "Name", "Roll", "Subject", "Marks");
            System.out.println("----------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    System.out.printf("%-15s %-10s %-10s %-5s\n", data[0], data[1], data[2], data[3]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void searchByRoll(Scanner scanner) {
        System.out.print("Enter roll number to search: ");
        String searchRoll = scanner.nextLine();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && data[1].equalsIgnoreCase(searchRoll)) {
                    System.out.println(" Student Found:");
                    System.out.printf("Name: %s, Roll: %s, Subject: %s, Marks: %s\n",
                            data[0], data[1], data[2], data[3]);
                    found = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        if (!found) {
            System.out.println(" No record found with that roll number.");
        }
    }

    private static void deleteRecord(Scanner scanner) {
        System.out.print("Enter roll number to delete: ");
        String rollToDelete = scanner.nextLine();
        boolean deleted = false;

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && data[1].equalsIgnoreCase(rollToDelete)) {
                    deleted = true;
                    continue; // Skip writing this line
                }
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println(" Error processing file: " + e.getMessage());
            return;
        }

        if (deleted) {
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                System.out.println("Record deleted successfully.");
            }
        } else {
            tempFile.delete();
            System.out.println("No record found to delete.");
        }
    }

    private static void updateMarks(Scanner scanner) {
        System.out.print("Enter roll number to update marks: ");
        String rollToUpdate = scanner.nextLine();
        boolean updated = false;

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && data[1].equalsIgnoreCase(rollToUpdate)) {
                    System.out.print("Enter new marks: ");
                    int newMarks = scanner.nextInt();
                    scanner.nextLine();

                    String updatedRecord = data[0] + "," + data[1] + "," + data[2] + "," + newMarks;
                    writer.write(updatedRecord);
                    writer.newLine();
                    updated = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
            return;
        }

        if (updated) {
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                System.out.println(" Marks updated successfully.");
            }
        } else {
            tempFile.delete();
            System.out.println("No record found to update.");
        }
    }

    private static void clearAllRecords(Scanner scanner) {
        System.out.print("Are you sure you want to delete all records? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                // Overwrites the file with nothing
                System.out.println("All records cleared.");
            } catch (IOException e) {
                System.out.println("Error clearing file: " + e.getMessage());
            }
        } else {
            System.out.println("Cancelled.");
        }
    }
}
