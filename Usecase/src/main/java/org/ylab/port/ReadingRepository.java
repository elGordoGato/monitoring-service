package org.ylab.port;

import org.ylab.entity.Meter;
import org.ylab.entity.Reading;
import org.ylab.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReadingRepository {
    /**
     * @param reading MeterReading to be saved
     * @return MeterReading that was saved
     */
    Reading save(Reading reading);

    /**
     * @param user User for whom readings to find
     * @param type Type of meter of which readings should be found
     * @return Optional of last MeterReading submitted by user of type
     */
    Optional<Reading> findLastByUserAndType(User user, Meter type);

    /**
     * @param user User for whom actual readings should be found
     * @return List of actual readings of user
     */
    List<Reading> findActualByUser(User user);

    /**
     * @return List of actual (last for each type of meter) readings submitted to db
     */
    List<Reading> findActualByAdmin();

    /**
     * @param currentUser User for whom readings to find
     * @param start       first date of month for which need to find readings
     * @param end         last date of month for which need to find readings
     * @return A list of all readings submitted by user within selected month
     */
    List<Reading> findAllByOwnerAndDateBetween(User currentUser, Instant start, Instant end);

    /**
     * @param start first date of month for which need to find readings
     * @param end   last date of month for which need to find readings
     * @return A list of all readings submitted within selected month
     */
    List<Reading> findAllByDateBetween(Instant start, Instant end);

    /**
     * @param currentUser User for whom readings to find
     * @return A list of all readings submitted by currentUser
     */
    List<Reading> findAllByOwner(User currentUser);


    /**
     * @return List of all submitted readings
     */
    List<Reading> findAll();
}