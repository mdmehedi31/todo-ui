package com.todo.ui.todoService;

import com.todom.library.dto.CalendarEventDTO;

import java.time.Instant;
import java.util.List;

public interface IEventService {

    public List<CalendarEventDTO> getAllEvents(Integer userId, Instant startDate, Instant endDate);
}
