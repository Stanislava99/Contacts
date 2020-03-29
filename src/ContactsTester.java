
import java.util.Scanner;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.lang.String;


public class ContactsTester {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int tests = in.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat dm = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {
            rvalue++;
            String operation = in.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = in.nextLine();
                    int m = in.nextInt();
                    Student[] students = new Student[m];
                    for (int i = 0; i < m; i++) {
                        rvalue++;
                        String firstName = in.next();
                        String lastName = in.next();
                        String city = in.next();
                        int age = in.nextInt();
                        long index = in.nextLong();

                        if (rindex == -1 || rvalue % 13 == 0)
                            rindex = index;

                        Student student = new Student(firstName, lastName, city, age, index);
                        students[i] = student;
                    }
                    faculty = new Faculty(name, students);
                    break;
                }
                case "ADD_EMAIL_CONTACT": {
                    long index = in.nextLong();
                    String date = in.next();
                    String email = in.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0)) {
                        rindex = index;
                    }
                    faculty.getStudent(index).addEmailContacts(date, email);
                    break;
                }
                case "ADD_PHONE_CONTACT": {
                    long index = in.nextLong();
                    String date = in.next();
                    String phoneNumber = in.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0)) {
                        rindex = index;
                    }

                    faculty.getStudent(index).addPhoneContact(date, phoneNumber);
                    break;
                }
                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: " + dm.format(faculty.getAverageNumberOfContacts()));
                    rvalue++;
                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": " + faculty.countStudentsFromCity(city));
                    break;
                }
                case "CHECK_DATES": {
                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact).getPhoneNumber()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0 && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }
                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts" + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }
        }
        in.close();
    }
}

abstract class Contact {
    private String date;
    private int year;
    private int month;
    private int day;

    Contact(String date) {
        this.date = date;
        this.year = Integer.parseInt(date.substring(0, 4));
        this.month = Integer.parseInt(date.substring(5, 7));
        this.day = Integer.parseInt(date.substring(8, 10));
    }

    public boolean isNewerThan(Contact c) {
        int a = this.date.compareTo(c.date);
        if (a > 0)
            return true;
        return false;
    }

    abstract public String getType();
}

class EmailContact extends Contact {
    private String email;

    EmailContact(String date, String email) {
        super(date);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return "Email";
    }
}

class PhoneContact extends Contact {
    private String phoneNumber;

    enum Operator {
        VIP, ONE, TMOBILE
    }

    PhoneContact(String date, String phoneNumber) {
        super(date);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getType() {
        return "Phone";
    }

    public Operator getOperator() {
        if (phoneNumber.charAt(2) == 0 || phoneNumber.charAt(2) == 1 || phoneNumber.charAt(2) == 2)
            return Operator.TMOBILE;
        else if (phoneNumber.charAt(2) == 5 || phoneNumber.charAt(2) == 6)
            return Operator.ONE;
        else
            return Operator.VIP;
    }
}

class Student {
    private Contact[] contacts;
    private String firstName;
    private String lastName;
    private String city;
    private int age;
    private long index;
    private int totalContactsNum;
    private int emailContactsNum;
    private int phoneContactsNum;

    public int getTotalContactsNum() {
        return totalContactsNum;
    }

    public Student(String firstName, String lastName, String city, int age, long index) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.age = age;
        this.index = index;
        contacts = null;
        totalContactsNum = 0;
        emailContactsNum = 0;
        phoneContactsNum = 0;
    }

    public void addEmailContacts(String date, String email) {
        Contact[] tmp = new Contact[totalContactsNum + 1];
        for (int i = 0; i < totalContactsNum; i++)
            tmp[i] = contacts[i];

        tmp[totalContactsNum++] = new EmailContact(date, email);
        contacts = tmp;
        emailContactsNum++;
    }

    public void addPhoneContact(String date, String phoneNumber) {
        Contact[] tmp = new Contact[totalContactsNum + 1];
        for (int i = 0; i < totalContactsNum; i++)
            tmp[i] = contacts[i];

        tmp[totalContactsNum++] = new PhoneContact(date, phoneNumber);
        contacts = tmp;
        phoneContactsNum++;
    }

    public Contact[] getEmailContacts() {
        int j = 0;
        EmailContact[] emailContacts = new EmailContact[emailContactsNum];
        for (int i = 0; i < emailContactsNum; i++) {
            if (contacts[i].getType().equals("Email"))
                emailContacts[j++] = (EmailContact) contacts[i];
        }
        return emailContacts;
    }

    public Contact[] getPhoneContacts() {
        int j = 0;
        PhoneContact[] phoneContacts = new PhoneContact[phoneContactsNum];
        for (int i = 0; i < phoneContactsNum; i++) {
            if (contacts[i].getType().equals("Phone"))
                phoneContacts[j++] = (PhoneContact) contacts[i];
        }
        return phoneContacts;
    }

    public String getCity() {
        return city;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public long getIndex() {
        return index;
    }

    public Contact getLatestContact() {
        int size = contacts.length;
        if (size == 0) return null;
        Contact latest = contacts[0];
        for (int i = 0; i < size; i++)
            if (contacts[i].isNewerThan(latest)) {
                latest = contacts[i];
            }
        return latest;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("{");
        output.append("\"ime\":\"").append(firstName).append("\", \"prezime\":\"").append(lastName).append("\", \"vozrast\":").append(age).append(", \"grad\":\"").append(city).append("\", \"indeks\":").append(index).append(", \"telefonskiKontakti\":[");

        EmailContact[] emails = (EmailContact[]) getEmailContacts();
        PhoneContact[] phones = (PhoneContact[]) getPhoneContacts();

        for (int i = 0; i < phoneContactsNum; i++) {
            output.append("\"").append(phones[i].getPhoneNumber()).append("\"");
            if (i + 1 < phoneContactsNum)
                output.append(", ");
        }
        output.append("], \"emailKontakti\" :[");
        for (int i = 0; i < emailContactsNum; i++) {
            output.append("\"").append(emails[i].getEmail()).append("\"");
            if (i + 1 < emailContactsNum)
                output.append(", ");
        }
        output.append("]}");

        return output.toString();
    }
}

class Faculty {
    private String name;
    private Student[] students;

    public Faculty(String name, Student[] students) {
        this.name = name;
        this.students = students;
    }

    public int countStudentsFromCity(String cityName) {
        int count = 0;
        for (int i = 0; i < students.length; i++)
            if (students[i].getCity().equals(cityName)) {
                count++;
            }
        return count;
    }

    public Student getStudent(long index) {
        int returnStudent = -1;
        for (int i = 0; i < students.length; i++) {
            if (students[i].getIndex() == index)
                returnStudent = i;
        }
        return students[returnStudent];
    }

    public double getAverageNumberOfContacts() {
        int sum = 0;
        double average;
        for (int i = 0; i < students.length; i++) {
            sum += students[i].getTotalContactsNum();
        }
        average = (double)sum/students.length;
        return average;
    }

    public Student getStudentWithMostContacts() {
        int index = 0;
        int mostContacts = students[0].getTotalContactsNum();
        for (int i = 0; i < students.length; i++) {
            if (mostContacts < students[i].getTotalContactsNum()) {
                mostContacts = students[i].getTotalContactsNum();
                index = i;
            } else if (mostContacts == students[i].getTotalContactsNum()) {
                index = students[i].getIndex() > students[index].getIndex() ? i : index;
            }
        }
        return students[index];
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("{\"fakultet\":");
        string.append("\"").append(name).append("\"").append(", \"studenti\":[");
        for (int i = 0; i < students.length; i++) {
            string.append(students[i].toString());
            if (i + 1 < students.length)
                string.append(", ");
        }
        string.append("]}");
        return string.toString();
    }
}




