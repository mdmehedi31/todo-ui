package com.todo.ui.todoService;

import com.todo.ui.endpoint.TodoEndpoint;
import com.todom.library.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class todoService implements ITodoService{

    private static final Logger log = LoggerFactory.getLogger(todoService.class);
    @Autowired
    private TodoEndpoint todoEndpoint;
    @Override
    public TodoResponseDTO createOrUpdateTodo(TodoRequestDTO todoRequestDTO) {
        try{
            log.info("Its calling from todo service class and create todo method");
            TodoResponseDTO todoResponseDTO=null;
            if(todoRequestDTO.getId() !=null){
                todoResponseDTO =this.todoEndpoint.updateTodo(todoRequestDTO);
            }else{
                todoResponseDTO =this.todoEndpoint.createTodo(todoRequestDTO);
            }
            return todoResponseDTO;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<TodoResponseDTO> getAllTodos(Integer userId) {
        try {
            List<TodoResponseDTO> todoList = this.todoEndpoint.getAllTodos(userId);
            return todoList;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public TodoListRequestDTO createOrUpdateTodoListItem(TodoListRequestDTO todoListRequestDTO) {
        try{

            if(todoListRequestDTO.getId() !=null){
                return this.todoEndpoint.updateTodoList(todoListRequestDTO);
            }else{
                return this.todoEndpoint.createTodoList(todoListRequestDTO);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TodoListRequestDTO> getAllTodoListByTodoId(Integer todoBoardId) {
        try{
            return this.todoEndpoint.getAllTodoLists(todoBoardId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ApiResponse deleteTodo(Integer todoId) {
        try{
            ApiResponse deleteTodoResponse = this.todoEndpoint.deleteTodo(todoId);
            return deleteTodoResponse;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public ApiResponse deleteTodoList(Integer todoId) {
        try{
            ApiResponse deleteTodoResponse = this.todoEndpoint.deleteTodoList(todoId);
            return deleteTodoResponse;
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public ApiResponse checkIsValidToSetupGoogleCalendar(Integer userId) {
        try{
            ApiResponse response = this.todoEndpoint.checkIsUserIsValidToAddCalendar(userId);
            return response;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<UserCalendarDto> getAllUserCalendarsByUserId(Integer userId) {
        try{
            List<UserCalendarDto> userCalendarDtoList = this.todoEndpoint.getAllUserCalendars(userId);
            return userCalendarDtoList;
        }catch (Exception e) {
           // throw e;
            log.error("An exception thrown : ",e);
            return  new ArrayList<>();
        }
    }

    @Override
    public ApiResponse setupEventSchedule(Integer boardId, Integer boardItemId, Integer userId, GoogleCalendarEventCreateDto calendarEventCreateDto) {
        return this.todoEndpoint.setupEventSchedule(boardId, boardItemId, userId, calendarEventCreateDto);
    }

    @Override
    public List<CalendarEventDTO> getAllCalendarEventsByUserId(Integer userId, Instant startDate, Instant endDate) {
        MultiValueMap<String, String> forms= new LinkedMultiValueMap<>();
        if(userId !=null ){
            forms.add("userId",userId+"");
        }
        if(startDate !=null ){
            forms.add("eventStartDate",startDate+"");
        }
        if(endDate !=null ){
            forms.add("eventEndDate",endDate+"");
        }
        return this.todoEndpoint.getAllUserCalendarEvents(forms);
    }

    @Override
    public ApiResponse addAiGenTodoList(TodoDTO todoDTO, Integer createdBy) {
        return this.todoEndpoint.addAiGenTodoList(todoDTO, createdBy);
    }

    @Override
    public DashBoardDTO getAllDashBoardDataWithDateRange(LocalDate startDate, LocalDate endDate, Integer userId) {
        return this.todoEndpoint.getDashBoard(startDate,endDate,userId);
    }
}
