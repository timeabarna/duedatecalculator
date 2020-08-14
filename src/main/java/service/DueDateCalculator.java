package service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;

public class DueDateCalculator {
    private static final LocalTime NINE_O_CLOCK= LocalTime.of(9, 0, 0);
    private static final LocalTime FIVE_O_CLOCK= LocalTime.of(17, 0, 0);
    private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
    private static final long NON_WORK_HOURS= 16L;
    private static final long NON_WORKING_DAYS = 48L;

    public LocalDateTime calculateDueDate(LocalDateTime submissionDate, long turnaroundTime) {
        validateSubmissionDate(submissionDate);
        validateTurnaroundTime(turnaroundTime);
        return calculate(submissionDate, turnaroundTime);
    }

    private LocalDateTime calculate(LocalDateTime submissionDate, long turnaroundTime) {
        LocalDateTime dateWithTurnaroundAdded = submissionDate;

        int counter = 0;
        while ( counter < turnaroundTime) {
            dateWithTurnaroundAdded = dateWithTurnaroundAdded.plusHours(1L);
            if (isBetweenNineAndFive(dateWithTurnaroundAdded) && !isWeekend(dateWithTurnaroundAdded)) {
                counter++;
            }
        }
                return dateWithTurnaroundAdded;
        }

    private LocalDateTime shiftWorkday(LocalDateTime dateWithTurnaroundAdded) {
        if (!isBetweenNineAndFive(dateWithTurnaroundAdded)) {
            dateWithTurnaroundAdded = dateWithTurnaroundAdded.plusHours(NON_WORK_HOURS);
        }
        return dateWithTurnaroundAdded;
    }

    private LocalDateTime shiftWeekend(LocalDateTime dateWithTurnaroundAdded) {
        if (isWeekend(dateWithTurnaroundAdded)) {
            dateWithTurnaroundAdded = dateWithTurnaroundAdded.plusHours(NON_WORKING_DAYS);
        }
        return dateWithTurnaroundAdded;
    }

    private void validateSubmissionDate(LocalDateTime submissionDate) {
        isNullDateTime(submissionDate);
        validateIsWeekend(submissionDate);
        validateIsBetweenNineAndFive(submissionDate);
    }

    private void validateTurnaroundTime(long turnaroundTime) {
        if (turnaroundTime <= 0) {
            throw new IllegalArgumentException("Turnaround time must be greater than 0");
        }
    }

    private void isNullDateTime(LocalDateTime submissionDate) {
        if (null == submissionDate) {
            throw new IllegalArgumentException("Submission date can not be null");
        }
    }

    private boolean isWeekend(LocalDateTime dateTobeValidated) {
        return WEEKEND.contains(dateTobeValidated.getDayOfWeek());
    }

    private boolean isBetweenNineAndFive(LocalDateTime dateToBeValidated) {
        LocalTime timeFromSubmissionDate = dateToBeValidated.toLocalTime();
        return !timeFromSubmissionDate.isBefore(NINE_O_CLOCK) && !timeFromSubmissionDate.isAfter(FIVE_O_CLOCK);
    }

    private void validateIsWeekend(LocalDateTime dateTobeValidated) {
        if (isWeekend(dateTobeValidated)) {
            throw new IllegalArgumentException("Submission date must fall on the weekdays");
        }
    }

    private void validateIsBetweenNineAndFive(LocalDateTime dateToBeValidated) {
        if (!isBetweenNineAndFive(dateToBeValidated)) {
            throw new IllegalArgumentException("Submission hour must be between 9 AM and 5 PM");
        }
    }
}
