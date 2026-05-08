package com.todo.ui.todoService;

import com.todom.library.dto.*;

import java.time.Instant;
import java.util.List;

public interface ITodoService {

    public TodoResponseDTO createOrUpdateTodo(TodoRequestDTO todoRequestDTO);
    public List<TodoResponseDTO> getAllTodos(Integer userId);

    public TodoListRequestDTO createOrUpdateTodoListItem(TodoListRequestDTO todoListRequestDTO);
    public List<TodoListRequestDTO> getAllTodoListByTodoId(Integer todoId);
    public ApiResponse deleteTodo(Integer todoId);

    // Todo list
    public ApiResponse deleteTodoList(Integer todoId);

    // Google calendar
    public ApiResponse checkIsValidToSetupGoogleCalendar(Integer todoId);
    public List<UserCalendarDto> getAllUserCalendarsByUserId(Integer userId);
    public ApiResponse setupEventSchedule(Integer boardId, Integer boardItemId, Integer userId, GoogleCalendarEventCreateDto calendarEventCreateDto);
    public List<CalendarEventDTO> getAllCalendarEventsByUserId(Integer userId, Instant startDate, Instant endDate);
    public ApiResponse addAiGenTodoList(TodoDTO todoDTO, Integer createdBy);
}
