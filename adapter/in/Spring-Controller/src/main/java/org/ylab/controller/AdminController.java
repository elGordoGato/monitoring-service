package org.ylab.controller;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ylab.annotations.Loggable;
import org.ylab.dto.MeterDto;
import org.ylab.dto.ReadingDto;
import org.ylab.entity.Meter;
import org.ylab.entity.Reading;
import org.ylab.entity.UserEntity;
import org.ylab.mapper.MeterMapper;
import org.ylab.mapper.ReadingMapper;
import org.ylab.meter.MeterService;
import org.ylab.reading.ReadingService;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Controller class to handle requests from admin
 */
@Loggable
@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ReadingService readingService;
    private final MeterService meterService;
    private final ReadingMapper readingMapper;
    private final MeterMapper meterMapper;

    public AdminController(@Qualifier("adminReadingService") ReadingService readingService,
                           MeterService meterService,
                           ReadingMapper readingMapper, MeterMapper meterMapper) {
        this.readingService = readingService;
        this.meterService = meterService;
        this.readingMapper = readingMapper;
        this.meterMapper = meterMapper;
    }

    /**
     * @param principal Logged admin
     * @return List of actual readings
     */
    @GetMapping("/readings")
    public List<ReadingDto> getActual(@AuthenticationPrincipal UserEntity principal) {
        List<Reading> foundReadings = readingService.getActual(principal);
        return readingMapper.toReadingDtoList(foundReadings);
    }

    /**
     * @param principal Logged admin
     * @param date YearMonth with format "YYYY-MM" for month to get readings
     * @return List of readings submitted within month of selected date
     */
    @GetMapping(value = "/readings", params = "date")
    public List<ReadingDto> getByMonth(@AuthenticationPrincipal UserEntity principal,
                                       @RequestParam("date") @PastOrPresent YearMonth date) {
        LocalDate requestDate = date.atDay(1);
        List<Reading> foundReadings = readingService.getForMonth(principal, requestDate);
        return readingMapper.toReadingDtoList(foundReadings);
    }

    /**
     * @param principal Logged admin
     * @return List of all readings submitted before
     */
    @GetMapping("/readings/history")
    public List<ReadingDto> getHistory(@AuthenticationPrincipal UserEntity principal) {
        List<Reading> foundReadings = readingService.getAllByUser(principal);
        return readingMapper.toReadingDtoList(foundReadings);
    }

    /**
     * @param inputMeterDto MeterDto json to be created
     * @return Dto of created Meter
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/meter", produces = MediaType.APPLICATION_JSON_VALUE)
    public MeterDto createMeter(@RequestBody @Valid MeterDto inputMeterDto) {
        Meter meterToCreate = meterMapper.toMeter(inputMeterDto);
        Meter createdMeter = meterService.create(meterToCreate);
        return meterMapper.toMeterDto(createdMeter);
    }
}
