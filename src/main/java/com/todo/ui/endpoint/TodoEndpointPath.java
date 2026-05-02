package com.todo.ui.endpoint;

public class TodoEndpointPath {
    private static String ROOT_PATH = "todo/";
    private static String GOOGLE_CALENDAR_ADD_ROOT_PATH =  "calendar/";
    // TODO Endpoint path
    public static final String CREATE_TODO = ROOT_PATH + "create";
    public static final String GET_TODO_LIST = ROOT_PATH + "get/{userId}";
    public static final String UPDATE_TODO = ROOT_PATH + "update";
    public static final String DELETE_TODO = ROOT_PATH + "delete/{todoId}";
    public static final String AI_GEN_TODO_LIST = ROOT_PATH + "/add-ai-generated-todo/{createdById}";
    // Todo list endpoint path
    public static final String CREATE_TODO_LIST_ITEM = ROOT_PATH + "create-todolist";
    public static final String GET_TODO_LIST_ITEM = ROOT_PATH + "get-todolist/{todoId}";
    public static final String UPDATE_TODO_LIST_ITEM = ROOT_PATH + "update-todolist";
    public static final String DELETE_TODO_LIST_ITEM = ROOT_PATH + "delete-todolist/{todolistId}";
    public static final String CHECK_USER_IS_VALID_TO_ADD_CALENDAR_PATH= GOOGLE_CALENDAR_ADD_ROOT_PATH + "check-is-valid-to-set-calendar-date/{userId}";
    public static final String USER_GOOGLE_CALENDAR_LIST= GOOGLE_CALENDAR_ADD_ROOT_PATH + "user-calendar-list/{userId}";
    public static final String SETUP_SCHEDULE_EVENT = GOOGLE_CALENDAR_ADD_ROOT_PATH +"event-create/{boardId}/{boardItemId}/{userId}";
    public static final String GET_USER_EVENTS= GOOGLE_CALENDAR_ADD_ROOT_PATH + "/get-all-user-events";
}
