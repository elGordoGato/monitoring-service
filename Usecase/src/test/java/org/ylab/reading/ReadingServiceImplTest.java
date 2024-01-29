package org.ylab.reading;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.entity.Meter;
import org.ylab.entity.Reading;
import org.ylab.entity.User;
import org.ylab.exception.ConflictException;
import org.ylab.port.ReadingRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingServiceImplTest {

    @Mock
    private ReadingRepository readingRepository;

    @InjectMocks
    private ReadingServiceImpl readingService;

    private User user;
    private Meter meter;
    private Reading reading;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("secret");

        meter = new Meter();
        meter.setType("Electricity");

        reading = new Reading();
        reading.setMeter(meter);
        reading.setOwner(user);
        reading.setReading(100);
        reading.setCollectedDate(Instant.now());
    }

    @Test
    void testGetActualSuccess() {
        List<Reading> expected = Collections.singletonList(reading);
        when(readingRepository.findActualByUser(user)).thenReturn(expected);

        List<Reading> actual = readingService.getActual(user);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testCreateSuccess() {
        when(readingRepository.findLastByUserAndType(user, meter)).thenReturn(Optional.empty());
        when(readingRepository.save(any(Reading.class))).thenReturn(reading);

        Reading created = readingService.create(user, meter, 100);

        verify(readingRepository, times(1)).save(argThat(r ->
                r.getReading() == 100 &&
                        r.getOwner().equals(user) &&
                        r.getMeter().equals(meter)));
        assertThat(created).isNotNull();
        assertThat(created).isEqualTo(reading);
    }

    @Test
    void testCreateFailure() {
        when(readingRepository.findLastByUserAndType(user, meter)).thenReturn(Optional.of(reading));

        assertThrows(ConflictException.class, () -> readingService.create(user, meter, 100));
    }

    @Test
    void testGetForMonthSuccess() {
        LocalDate date = LocalDate.now();
        Instant start = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant end = date.plusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        List<Reading> expected = Collections.singletonList(reading);
        when(readingRepository.findAllByOwnerAndDateBetween(user, start, end)).thenReturn(expected);

        List<Reading> actual = readingService.getForMonth(user, date);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testGetAllByUserSuccess() {
        List<Reading> expected = Collections.singletonList(reading);
        when(readingRepository.findAllByOwner(user)).thenReturn(expected);

        List<Reading> actual = readingService.getAllByUser(user);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }
}