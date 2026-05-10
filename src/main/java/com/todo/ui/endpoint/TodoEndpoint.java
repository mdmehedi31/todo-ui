package com.todo.ui.endpoint;

import com.todo.ui.restEndPointCaller.RestEndPointCaller;
import com.todom.library.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;

@Service
public class TodoEndpoint {

    private static final Logger log = LoggerFactory.getLogger(TodoEndpoint.class);

    @Autowired
    private RestEndPointCaller restEndPointCaller;

    public TodoResponseDTO createTodo(TodoRequestDTO todoRequestDTO) {
        try {
            log.info("When create todo method call from todo Endpoint class");
            TodoResponseDTO todoResponseDTO = this.restEndPointCaller.postCallerWithRequestBody(TodoEndpointPath.CREATE_TODO, todoRequestDTO, TodoResponseDTO.class);
            return todoResponseDTO;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<TodoResponseDTO> getAllTodos(Integer userId) {
        try {
            log.info("When trying to load all todos list");
            List<TodoResponseDTO> todoList = this.restEndPointCaller.getCallerWithPathVariable(TodoEndpointPath.GET_TODO_LIST, new ParameterizedTypeReference<List<TodoResponseDTO>>() {
            }, userId);
            return todoList;
        } catch (Exception e) {
            throw e;
        }
    }

    public TodoResponseDTO updateTodo(TodoRequestDTO todoRequestDTO) {
        TodoResponseDTO todoResponseDTO = this.restEndPointCaller.putCallerWithRequestBody(TodoEndpointPath.UPDATE_TODO, todoRequestDTO, TodoResponseDTO.class);
        return todoResponseDTO;
    }

    public ApiResponse deleteTodo(Integer todoId) {
        ApiResponse apiResponse = this.restEndPointCaller.putCallerWithPathVariable(TodoEndpointPath.DELETE_TODO, ApiResponse.class, todoId);
        return apiResponse;
    }

    // Todo List Endpoint
    public TodoListRequestDTO createTodoList(TodoListRequestDTO todoListRequestDTO) {
        try{
            log.info("When trying to create todo list");
            TodoListRequestDTO createResponse = this.restEndPointCaller.postCallerWithRequestBody(TodoEndpointPath.CREATE_TODO_LIST_ITEM, todoListRequestDTO, TodoListRequestDTO.class);
            return createResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<TodoListRequestDTO> getAllTodoLists(Integer todoBoardId) {
      try{
          List<TodoListRequestDTO> todoListResponseDTOList =this.restEndPointCaller.getCallerWithPathVariable(TodoEndpointPath.GET_TODO_LIST_ITEM, new ParameterizedTypeReference<List<TodoListRequestDTO>>() {}, todoBoardId);
          return todoListResponseDTOList;
      } catch (Exception e) {
          throw e;
      }
     }

    public TodoListRequestDTO updateTodoList(TodoListRequestDTO todoListRequestDTO) {
        try{
            TodoListRequestDTO todoListResponseDTO= this.restEndPointCaller.putCallerWithRequestBody(TodoEndpointPath.UPDATE_TODO_LIST_ITEM, todoListRequestDTO, TodoListRequestDTO.class);
        return todoListResponseDTO;
        }catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse deleteTodoList(Integer todoListId) {
        try{
            ApiResponse apiResponse= this.restEndPointCaller.putCallerWithPathVariable(TodoEndpointPath.DELETE_TODO_LIST_ITEM, ApiResponse.class, todoListId);
            return apiResponse;
        } catch (Exception e) {
            throw e;
        }
    }

    public ApiResponse checkIsUserIsValidToAddCalendar(Integer userId){
        ApiResponse response = this.restEndPointCaller.getCallerWithPathVariable(TodoEndpointPath.CHECK_USER_IS_VALID_TO_ADD_CALENDAR_PATH, ApiResponse.class, userId);
        return response;
    }

    public List<UserCalendarDto> getAllUserCalendars(Integer userId) {
        List<UserCalendarDto> userCalendarDtoList= this.restEndPointCaller.getCallerWithPathVariable(TodoEndpointPath.USER_GOOGLE_CALENDAR_LIST, new ParameterizedTypeReference<List<UserCalendarDto>>() {}, userId);
        return userCalendarDtoList;
    }

    public ApiResponse setupEventSchedule(Integer boardId, Integer boardItemId, Integer userId,GoogleCalendarEventCreateDto calendarEventCreateDto) {
        ApiResponse response= this.restEndPointCaller.postCallerWithRequestBodyAndPathvariable(TodoEndpointPath.SETUP_SCHEDULE_EVENT,calendarEventCreateDto, ApiResponse.class, boardId, boardItemId, userId);
        return response;
    }

    public List<CalendarEventDTO> getAllUserCalendarEvents(MultiValueMap<String,String> forms) {

        List<CalendarEventDTO> eventDTOList= this.restEndPointCaller.getWithRequestParam(TodoEndpointPath.GET_USER_EVENTS, new ParameterizedTypeReference<List<CalendarEventDTO>>() {},forms);


        return eventDTOList;
    }

    public ApiResponse addAiGenTodoList(TodoDTO todoDTO, Integer createdById) {
        ApiResponse response = this.restEndPointCaller.postCallerWithRequestBodyAndPathvariable(TodoEndpointPath.AI_GEN_TODO_LIST, todoDTO, ApiResponse.class, createdById);
        return response;
    }

    public DashBoardDTO getDashBoard(LocalDate startDate, LocalDate endDate, Integer userId) {
        DashBoardDTO dashBoardData= this.restEndPointCaller.getCallerWithPathVariable(TodoEndpointPath.GET_DASHBOARD_DATA,
                new ParameterizedTypeReference<DashBoardDTO>() {}, startDate,endDate,userId);
        return dashBoardData;
    }


}
