package online.stringtek.simple.utilities;

import java.time.*;

public class DateUtil {

    public static LocalDateTime utcLocalDateTime() {
        Instant instant = Instant.now();
        return instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime utcLocalDateTime(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime utcLocalDateTime(ZonedDateTime zonedDateTime) {
        return utcLocalDateTime(zonedDateTime.toOffsetDateTime());
    }

    public static OffsetDateTime toOffsetDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toOffsetDateTime();
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime, ZoneId zoneId) {
        return toOffsetDateTime(localDateTime.atZone(zoneId));
    }

    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime,
        ZoneOffset zoneOffset) {
        return localDateTime.atOffset(zoneOffset);
    }

}
