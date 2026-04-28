package com.todo.ui.todoService;

import com.todom.library.dto.CalendarEventDTO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EventService implements IEventService {

    @Override
    public List<CalendarEventDTO> getAllEvents(Integer userId, Instant startDate, Instant endDate) {
        return List.of();
    }
}
