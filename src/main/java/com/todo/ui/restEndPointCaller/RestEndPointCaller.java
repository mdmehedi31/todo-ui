package com.todo.ui.restEndPointCaller;

import com.todom.library.dto.CalendarEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RestEndPointCaller {

    private final RestClient restClient;
    private static final Logger log = LoggerFactory.getLogger(RestEndPointCaller.class);
    public RestEndPointCaller(RestClient restClient) {
        this.restClient = restClient;
    }

    public <E,T> T postCallerWithRequestBody(String path, E requestType, Class<T> responseBody) {
        try {

            log.info("The rest client url is : {}", this.restClient.options().retrieve());
            return this.restClient.post().
                    uri(path).
                    body(requestType).
                    retrieve().
                    body(responseBody);

        } catch (HttpClientErrorException e) {
            log.error("An exception thrown HttpClientErrorException catch part : "+e.getMessage());
            throw e;
        } catch (HttpServerErrorException e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        }
    }

    public <E,T> T postCallerWithRequestBodyAndPathvariable(String path, E requestBody, Class<T> responseBody, Object... pathValue) {
        try {

            log.info("The rest client url is : {}", this.restClient.options().retrieve());
            return this.restClient.post().
                    uri(path, pathValue).
                    body(requestBody).
                    retrieve().
                    body(responseBody);

        } catch (HttpClientErrorException e) {
            log.error("An exception thrown HttpClientErrorException catch part : "+e.getMessage());
            throw e;
        } catch (HttpServerErrorException e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An exception thrown HttpServerErrorException catch part "+e.getMessage());
            throw e;
        }
    }

    public <T> T postCallerWithRequestParams(String path, MultiValueMap<String, String> requestParam, Class<T> responseBody) {
        try{
            return restClient.post()
                    .uri(ub->ub.path(path).
                            queryParams(requestParam).
                            build()).
                    retrieve()
                    .body(responseBody);
        } catch(HttpServerErrorException e){
            throw e;
        }
        catch(HttpClientErrorException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <E,T> T putCallerWithRequestBody(String path, E requestBody, Class<T> responseBody){
        try{
            return restClient.put().
                    uri(path).
                    body(requestBody).
                    retrieve().
                    body(responseBody);

        }catch(HttpClientErrorException e){
            throw e;
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch(Exception e){
            throw e;
        }
    }

    public <E, T> T putCallerWithRequestParam(String path, MultiValueMap<String, String> requestParam, Class<T> responseBody){
        try{
            return restClient.put().uri(ub-> ub.
                    path(path).
                    queryParams(requestParam).
                    build()).
                    retrieve().
                    body(responseBody);
        }catch(HttpClientErrorException e){
            throw e;
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw  e;
        }
    }

    public <T> T getCallerWithRequestParam(String path,MultiValueMap<String, String> requestParam, Class<T> responseBody){
        try{
            return restClient.get().
                    uri(ub->ub.path(path).
                            queryParams(requestParam).
                            build()).
                    retrieve().
                    body(responseBody);
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch(HttpClientErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <T> T getWithRequestParam(String path, ParameterizedTypeReference<T> responseBody,MultiValueMap<String, String> requestParam){
        try{
            return restClient.get().
                    uri(ub->ub.path(path).
                            queryParams(requestParam).
                            build()).
                    retrieve().
                    body(responseBody);
        }catch(HttpServerErrorException e){
            throw e;
        }
        catch(HttpClientErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <T> T putCallerWithPathVariable(String path,Class<T> responseBody, Object... pathValue){
        try{
            return restClient.put().
                    uri(path, pathValue).
                    retrieve().
                    body(responseBody);
        }catch(HttpClientErrorException e){
            throw e;
        }
        catch (HttpServerErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }

    public <T> T getCaller(String path, Class<T> responseBody){
        try{
            return restClient.get().uri(path).retrieve().body(responseBody);
        } catch(HttpClientErrorException e){
            throw e;
        }
        catch(HttpServerErrorException e){
            throw e;
        }
        catch (Exception e) {
            throw e;
        }
    }
    public <T> T getCallerWithPathVariable(String path, Class<T> responseBody, Object... pathValue){
        try{
            return restClient.get().
                    uri(path, pathValue).
                    retrieve().
                    body(responseBody);

        }catch(HttpClientErrorException e){
            throw e;
        }
        catch(HttpServerErrorException e){
            throw e;
        }
        catch(Exception e){
            throw e;
        }
    }

    public <T> T getCallerWithPathVariable(String path, ParameterizedTypeReference<T> responseBody, Object... pathValue){
        try{
            return restClient.get().
                    uri(path, pathValue).
                    retrieve().
                    body(responseBody);

        }catch(HttpClientErrorException e){
            throw e;
        }
        catch(HttpServerErrorException e){
            throw e;
        }
        catch(Exception e){
            throw e;
        }
    }

}
