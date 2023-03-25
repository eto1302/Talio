/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;


import com.google.inject.Inject;

import commons.*;
import commons.mocks.IServerUtils;
import commons.models.IdResponseModel;
import commons.models.ListEditModel;
import commons.models.SubtaskEditModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Set;


public class ServerUtils implements IServerUtils {
    @Inject
    private RestTemplate client;
    private static String url = "http://localhost:8080/";

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Send a new Board to the server.
     *
     * @param board content of the board
     * @return the id of the board, or -1 and the error message if the creation fails
     */
    public IdResponseModel addBoard(Board board) {
        try {
            HttpEntity<Board> req = new HttpEntity<>(board);
            IdResponseModel response = client.postForObject(
                    url+"board/create", req, IdResponseModel.class);
            return response;
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    /**
     * Get the board by id.
     * @param id the id of the board
     * @return the board or null if there is exception.
     */
    public Board getBoard(int id) {
        try {
            ResponseEntity<Board> response =
                    client.getForEntity(url+"board/find/"+id, Board.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get all the boards,
     * @return the boards or null if there is exception.
     */
    public Board[] getAllBoards() {
        try {
            ResponseEntity<Board[]> response =
                    client.getForEntity(url+"board/findAll", Board[].class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Add new list to the board.
     *
     * @param list list to be added
     * @param boardId id of the board
     * @return id of the list, or -1 if it fails
     */
    public IdResponseModel addList(commons.List list, int boardId) {
        try {
            HttpEntity<commons.List> req = new HttpEntity<commons.List>(list);
            IdResponseModel id = client.postForObject(
                    url+"list/add/"+boardId, req, IdResponseModel.class);
            return id;
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }

    }

    /**
     * Delete a list from the board and list repository.
     *
     * @param boardId id of the board
     * @param listId id of the list
     * @return true if it succeeds, false otherwise
     */
    public IdResponseModel deleteList(int boardId, int listId) {
        try {
            ResponseEntity<IdResponseModel> response = client.getForEntity(
                    url+"list/delete/"+boardId+"/"+listId,
                    IdResponseModel.class
            );
            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }

    }

    /**
     *
     * Edit the name, background color and font color of a list.
     *
     * @param boardId id of the board
     * @param listId id of the list
     * @param model  name and color information of the modified list
     * @return
     */
    public IdResponseModel editList(int boardId, int listId, ListEditModel model) {
        try {
            HttpEntity<ListEditModel> req = new HttpEntity<ListEditModel>(model);
            ResponseEntity<IdResponseModel> response = client.postForEntity(
                    url+"list/edit/"+boardId+"/"+listId, req,
                    IdResponseModel.class
            );

            if (listId != response.getBody().getId())
                return new IdResponseModel(-1, "list doesn't match");

            return response.getBody();

        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }

    }

    /**
     * Returns the list with a certain id
     * @param id id of the list
     * @return the list or null in case of an exception
     */
    public commons.List getList(int id) {
        try {
            ResponseEntity<commons.List> response = client.getForEntity(
                    url+"list/get/"+id, commons.List.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /***
     *
     *  Get the list set by board id.
     *
     * @param boardId id of the board
     * @return the set of list of the board
     */
    public Set<commons.List> getListByBoard(int boardId) {
        try {
            ResponseEntity<Set<commons.List>> response = client.exchange(
                    url+"list/getByBoard/" + boardId,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Set<List>>() {}
            );

            // return the list if the request succeeded
            if (response.getStatusCode().is2xxSuccessful()) {
                Set<commons.List> lists = response.getBody();
                return lists;
            }

            // board id doesn't exist
            if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new NoSuchElementException("There is no such board");
            }

            throw new RuntimeException("something went wrong...");

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Adds a subtask to the specified task
     * @param subtask the subtask to be added
     * @param taskID the id of the associated task
     * @return appropriate model, with the id of the newly added subtask if it succeeds or
     *      -1 if it fails
     */
    public IdResponseModel addSubtask(Subtask subtask, int taskID) {
        try {
            HttpEntity<Subtask> req = new HttpEntity<>(subtask);
            IdResponseModel response = client.postForObject(
                    url+"subtask/add/"+taskID, req, IdResponseModel.class);
            return response;
        } catch (Exception e) {
            return new IdResponseModel(-1, "Server connection failed");
        }
    }

    /**
     * Gets a subtask
     * @param id of the subtask
     * @return the subtask
     */
    public Subtask getSubtask(int id) {
        try {
            ResponseEntity<Subtask> response = client.getForEntity(
                    url+"subtask/get/"+id, Subtask.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes a subtask from the associated task
     * @param taskID the id of the associated task
     * @param subtaskID the id of the subtask
     * @return appropriate model, with the id of the deleted subtask if it succeeds or
     *          -1 if it fails
     */
    public IdResponseModel deleteSubtask(int taskID, int subtaskID) {
        try {
            ResponseEntity<IdResponseModel> response = client.getForEntity(
                    url+"subtask/delete/"+taskID+"/"+subtaskID,
                    IdResponseModel.class
            );
            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Server connection failed");
        }
    }

    /**
     * Edits the description and status of a subtask (checked/unchecked)
     * @param subtaskID the id of the subtask to be edited
     * @param model containing the description and status of the updated subtask,
     *              serving as the request body
     * @return appropriate model, with the id of the edited subtask if it succeeds or
     *                -1 if it fails
     */
    public IdResponseModel editSubtask(int subtaskID, SubtaskEditModel model) {
        try {
            HttpEntity<SubtaskEditModel> req = new HttpEntity<>(model);
            ResponseEntity<IdResponseModel> response = client.postForEntity(
                    url+"subtask/edit/"+subtaskID, req,
                    IdResponseModel.class);

            if (subtaskID != response.getBody().getId())
                return new IdResponseModel(-1, "Subtasks don't match");
            return response.getBody();

        } catch (Exception e) {
            return new IdResponseModel(-1, "Server connection failed");
        }
    }

    /**
     * Get all the subtasks associated with a task
     * @param taskID the id of the associated task
     * @return all the subtasks associated with a task or exceptions if
     *      there is no such task or if something else fails
     */
    public java.util.List<Subtask> getSubtasksByTask(int taskID){
        ResponseEntity<java.util.List<Subtask>> response = client.exchange(
                url+"subtask/getByTask/" + taskID,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){} );

        if (response.getStatusCode().is2xxSuccessful())
             return response.getBody();
        else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
            throw new NoSuchElementException("No such task id");

        throw new RuntimeException("Something went wrong");
    }
}