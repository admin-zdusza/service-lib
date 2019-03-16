package pl.zdusza.validators;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.ZoneId;
import java.util.TimeZone;

public class TimezoneValidatorTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test()
    public final void testShouldTimezoneBeValid() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        new TimezoneValidator().validate();
    }

    @Test()
    public final void testShouldTimezoneBeInValid() {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Invalid timezone: sun.util.calendar.ZoneInfo[id=\"Europe/Warsaw\","
                + "offset=3600000,dstSavings=3600000,useDaylight=true,transitions=165,lastRule="
                + "java.util.SimpleTimeZone[id=Europe/Warsaw,offset=3600000,dstSavings=3600000,useDaylight=true,"
                + "startYear=0,startMode=2,startMonth=2,startDay=-1,startDayOfWeek=1,startTime=3600000,"
                + "startTimeMode=2,endMode=2,endMonth=9,endDay=-1,endDayOfWeek=1,endTime=3600000,endTimeMode=2]]");
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Warsaw")));
        new TimezoneValidator().validate();
    }

    @After
    public final void setTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }
}
