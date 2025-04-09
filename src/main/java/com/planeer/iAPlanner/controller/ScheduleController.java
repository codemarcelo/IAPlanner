package com.planeer.iAPlanner.controller;

import com.planeer.iAPlanner.model.dto.ScheduleDTO;
import com.planeer.iAPlanner.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;

    @PostMapping("/addSchedule")
    public ResponseEntity<String> addSchedule(@RequestBody ScheduleDTO schedule) {

        scheduleService.saveSchedule(schedule);

        return new ResponseEntity<>("Agendamento criado com sucesso!", HttpStatus.CREATED);
    }

    @GetMapping("/getSchedule/{id}")
    public ScheduleDTO getSchedule(@PathVariable("id") Long scheduleId) {
        return scheduleService.getSchedule(scheduleId.intValue());
    }

    @GetMapping("/getAllSchedules")
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        List<ScheduleDTO> schedules = scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }


}