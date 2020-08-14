package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Month;
import static org.junit.jupiter.api.Assertions.*;

public class DueDateCalculatorTest {
    private static final long MINUS_ONE_HOUR_TURNAROUND_TIME = -1L;
    private static final long ZERO_HOUR_TURNAROUND_TIME = 0L;
    private static final long ONE_HOUR_TURNAROUND_TIME = 1L;
    private static final long NINE_HOUR_TURNAROUND_TIME = 9L;
    private static final long SIXTEEN_HOUR_TURNAROUND_TIME = 16L;
    private static final long EIGHTY_HOUR_TURNAROUND_TIME = 80L;
    private static final LocalDateTime WEEKEND_DATE =
            LocalDateTime.of(2020, Month.AUGUST, 1, 10, 8, 55);
    private static final LocalDateTime AUG_3RD_MONDAY_BEFORE_NINE =
            LocalDateTime.of(2020, Month.AUGUST, 3, 8, 8, 11);
    private static final LocalDateTime AUG_3RD_MONDAY_AFTER_FIVE =
            LocalDateTime.of(2020, Month.AUGUST, 3, 22, 8, 22);
    private static final LocalDateTime AUG_3RD_MONDAY_NINE_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 3, 9, 15, 33);
    private static final LocalDateTime AUG_3RD_MONDAY_FOUR_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 3, 16, 15, 33);
    private static final LocalDateTime AUG_4TH_TUESDAY_NINE_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 4, 9, 15, 33);
    private static final LocalDateTime AUG_4TH_TUESDAY_TEN_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 4, 10, 15, 33);
    private static final LocalDateTime AUG_5TH_WEDNESDAY_NINE_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 5, 9, 15, 33);
    private static final LocalDateTime AUG_5TH_WEDNESDAY_FOUR_FIFTEEN =
            LocalDateTime.of(2020, 8, 5, 16, 15, 33);
    private static final LocalDateTime AUG_7TH_FRIDAY_FOUR_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 7, 16, 15, 33);
    private static final LocalDateTime AUG_10TH_MONDAY_NINE_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 10, 9, 15, 33);
    private static final LocalDateTime AUG_11TH_TUESDAY_NINE_FIFTEEN =
            LocalDateTime.of(2020, Month.AUGUST, 11, 9, 15, 33);
    private static final LocalDateTime JUL_31ST_FRIDAY_ONE_PM =
            LocalDateTime.of(2020, Month.JULY, 31, 13, 0, 33);
    private static final LocalDateTime AUG_14TH_FRIDAY_ONE_PM =
            LocalDateTime.of(2020, Month.AUGUST, 14, 13, 0, 33);
    private static final LocalDateTime XMAS =
            LocalDateTime.of(2020, 12, 25, 9, 15, 42);
    private static final LocalDateTime WORKING_SATURDAY =
            LocalDateTime.of(2020, 8, 29, 11, 15, 51);

    private DueDateCalculator dueDateCalculator;

    @BeforeEach
    void setUp() {
        dueDateCalculator = new DueDateCalculator();
    }

    @Test
    public void whenSubmitDateIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(null, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenSubmitDateIsOnWeekend_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(WEEKEND_DATE, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenSubmitTimeIsBeforeNine_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_BEFORE_NINE, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenSubmitTimeIsAfterFive_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_AFTER_FIVE, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenTurnaroundTimeIsNegative_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_NINE_FIFTEEN, MINUS_ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenTurnaroundTimeIsZero_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_NINE_FIFTEEN, ZERO_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddOneHourToWeekdayBeforeFour_ShouldRemainOnTheSameDay() {
        assertEquals(AUG_3RD_MONDAY_NINE_FIFTEEN.plusHours(ONE_HOUR_TURNAROUND_TIME),
                dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_NINE_FIFTEEN, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddOneHourToWeekdayAfterFour_ShouldBeTheNextWorkingDay() {
        assertEquals(AUG_4TH_TUESDAY_NINE_FIFTEEN,
                dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_FOUR_FIFTEEN, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddOneHourToFridayAfterFour_ShouldBeOnNextMonday() {
        assertEquals(AUG_10TH_MONDAY_NINE_FIFTEEN,
                dueDateCalculator.calculateDueDate(AUG_7TH_FRIDAY_FOUR_FIFTEEN, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddNineHourToWeekdayBeforeFour_ShouldBeOnNextWorkingDay() {
        assertEquals(AUG_4TH_TUESDAY_TEN_FIFTEEN,
                dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_NINE_FIFTEEN, NINE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddNineHourToWeekdayAfterFour_ShouldBeTwoWorkingDaysAfter() {
        assertEquals(AUG_5TH_WEDNESDAY_NINE_FIFTEEN,
                dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_FOUR_FIFTEEN, NINE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddNineHourToFridayAfterFour_ShouldBeOnNextTuesday() {
        assertEquals(AUG_11TH_TUESDAY_NINE_FIFTEEN,
                dueDateCalculator.calculateDueDate(AUG_7TH_FRIDAY_FOUR_FIFTEEN, NINE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddSixteenHourToWeekdayAfterFour_ShouldBeTwoWorkingDaysAfter() {
        assertEquals(AUG_5TH_WEDNESDAY_FOUR_FIFTEEN,
                dueDateCalculator.calculateDueDate(AUG_3RD_MONDAY_FOUR_FIFTEEN, SIXTEEN_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenAddEightyHourToFridayBeforeFour_ShouldBeTenWorkingDaysAfter() {
        assertEquals(AUG_14TH_FRIDAY_ONE_PM,
                dueDateCalculator.calculateDueDate(JUL_31ST_FRIDAY_ONE_PM, EIGHTY_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenDayIsAPublicHoliday_ShouldBeHandledAsWorkingDay() {
        assertEquals(XMAS.plusHours(ONE_HOUR_TURNAROUND_TIME),
                dueDateCalculator.calculateDueDate(XMAS, ONE_HOUR_TURNAROUND_TIME));
    }

    @Test
    public void whenDayIsAWorkingSaturday_ShouldBeHandledAsWeekend() {
        assertThrows(IllegalArgumentException.class,
                () -> dueDateCalculator.calculateDueDate(WORKING_SATURDAY, ONE_HOUR_TURNAROUND_TIME));
    }
}
