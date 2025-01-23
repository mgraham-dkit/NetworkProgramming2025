package app;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Slf4j
public class Sample {
    public static void main(String[] args) {
        log.info("Application started");
        String pattern = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        Scanner input = new Scanner(System.in);
        System.out.println("Please enter your date of birth:");
        String birthday = input.nextLine();

        try {
            LocalDate dob = LocalDate.parse(birthday, formatter);
            System.out.println("Date of birth accepted.");
        }catch(DateTimeParseException e){
            log.error("Incorrect date format employed");
        }
    }
}
