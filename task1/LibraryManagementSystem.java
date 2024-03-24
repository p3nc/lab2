package task1;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Book implements Serializable {
    private String title;
    private String author;
    private int year;
    private int shelfNumber;

    public Book(String title, String author, int year, int shelfNumber) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.shelfNumber = shelfNumber;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", shelfNumber=" + shelfNumber +
                '}';
    }
}

class Library implements Serializable {
    private ArrayList<Book> books;
    private ArrayList<Book> rentedBooks;

    public Library() {
        this.books = new ArrayList<>();
        this.rentedBooks = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void rentBook(String bookTitle, String renterName) {
        for (Book book : books) {
            if (book.getTitle().equals(bookTitle)) {
                rentedBooks.add(book);
                books.remove(book);
                System.out.println("Book '" + bookTitle + "' rented by " + renterName);
                return;
            }
        }
        System.out.println("Book '" + bookTitle + "' not found in the library.");
    }

    public void returnBook(String bookTitle) {
        for (Book book : rentedBooks) {
            if (book.getTitle().equals(bookTitle)) {
                books.add(book);
                rentedBooks.remove(book);
                System.out.println("Book '" + bookTitle + "' returned.");
                return;
            }
        }
        System.out.println("Book '" + bookTitle + "' not rented.");
    }

    public void displayAvailableBooks() {
        System.out.println("Available books:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void displayRentedBooks() {
        System.out.println("Rented books:");
        for (Book book : rentedBooks) {
            System.out.println(book);
        }
    }

    // Serialization methods
    public static void saveState(Library library, String fileName) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(library);
            System.out.println("System state saved to " + fileName);
        }
    }

    public static Library loadState(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Library library = (Library) inputStream.readObject();
            System.out.println("System state loaded from " + fileName);
            return library;
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library;

        try {
            library = Library.loadState("library_state.ser");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading system state: " + e.getMessage());
            library = new Library();
        }



        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nLibrary Management System Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. Rent Book");
            System.out.println("3. Return Book");
            System.out.println("4. Display Available Books");
            System.out.println("5. Display Rented Books");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    scanner.nextLine();
                    String title = scanner.nextLine();
                    System.out.print("Enter author name: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter publication year: ");
                    int year = scanner.nextInt();
                    System.out.print("Enter shelf number: ");
                    int shelfNumber = scanner.nextInt();
                    library.addBook(new Book(title, author, year, shelfNumber));
                    break;
                case 2:
                    System.out.print("Enter book title to rent: ");
                    scanner.nextLine();
                    String rentBookTitle = scanner.nextLine();
                    System.out.print("Enter renter's name: ");
                    String renterName = scanner.nextLine();
                    library.rentBook(rentBookTitle, renterName);
                    break;
                case 3:
                    System.out.print("Enter book title to return: ");
                    scanner.nextLine();
                    String returnBookTitle = scanner.nextLine();
                    library.returnBook(returnBookTitle);
                    break;
                case 4:
                    library.displayAvailableBooks();
                    break;
                case 5:
                    library.displayRentedBooks();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 0 and 5.");
            }
        } while (choice != 0);

        try {
            Library.saveState(library, "library_state.ser");
        } catch (IOException e) {
            System.err.println("Error saving system state: " + e.getMessage());
        }
    }
}
