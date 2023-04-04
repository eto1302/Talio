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
import commons.models.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
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
     * Get the color by id.
     * @param id the id of the color
     * @return the color or null if there is exception.
     */
    public Color getColor(int id) {
        try {
            ResponseEntity<Color> response =
                    client.getForEntity(url+"color/find/"+id, Color.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public IdResponseModel deleteBoard(int id) {
        try {
            ResponseEntity<IdResponseModel> response =
                    client.exchange(url+"board/delete/"+id, HttpMethod.DELETE,
                            null, IdResponseModel.class);
            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    @Override
    public IdResponseModel editBoard(int boardId, BoardEditModel edit) {
        try {
            HttpEntity<BoardEditModel> req = new HttpEntity<>(edit);
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url + "board/edit/" + boardId, HttpMethod.PUT, req, IdResponseModel.class);

            if (boardId != response.getBody().getId()) {
                return new IdResponseModel(-1, "Board doesn't match");
            }

            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    @Override
    public IdResponseModel deleteColor(int boardId, int colorId) {
        try {
            ResponseEntity<IdResponseModel> response =
                    client.exchange(url+"color/delete/"+boardId + "/" + colorId,
                            HttpMethod.DELETE,
                            null, IdResponseModel.class);
            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    @Override
    public IdResponseModel addColor(Color color) {
        try {
            HttpEntity<commons.Color> req = new HttpEntity<Color>(color);
            IdResponseModel id = client.postForObject(
                    url+"color/add", req, IdResponseModel.class);
            return id;
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    @Override
    public IdResponseModel setColorToBoard(Color color, int boardId) {
        try {
            HttpEntity<commons.Color> req = new HttpEntity<Color>(color);
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"color/add/"+color.getId()+"/"+boardId,
                    HttpMethod.PUT, req, IdResponseModel.class);
            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    @Override
    public IdResponseModel editColor(int colorId, ColorEditModel model) {
        try {
            HttpEntity<ColorEditModel> req = new HttpEntity<ColorEditModel>(model);
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"color/edit/"+colorId, HttpMethod.PUT,
                    req, IdResponseModel.class);
            if (colorId != response.getBody().getId())
                return new IdResponseModel(-1, "list doesn't match");

            return response.getBody();

        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }
    }

    /**
     * Returns the board with the corresponding invite key
     * @param inviteKey the invite-key og the board
     * @return the board or null if there is no board with such an invite-key.
     */
    public Board getBoardByInviteKey(String inviteKey){
        try{
            ResponseEntity<Board> response =
                    client.getForEntity(url+"board/getByInvite/"+inviteKey, Board.class);
            return response.getBody();
        }
        catch(Exception e){
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

    public Board[] getBoardsUpdated() {
        try {
            ResponseEntity<Board[]> response =
                    client.getForEntity(url+"board/findAllUpdated", Board[].class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }


    public boolean verifyAdmin(String password) {
        try {
            ResponseEntity<Boolean> response = client.getForEntity(
                    url+"board/verify/"+password, Boolean.class);
            return response.getBody();
        } catch (Exception e) {
            return false;
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
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"list/delete/"+boardId+"/"+listId, HttpMethod.DELETE,
                            null, IdResponseModel.class);

            return response.getBody();
        } catch (Exception e) {
            return new IdResponseModel(-1, "Oops, failed to connect to server...");
        }

    }

    /**
     *
     * Edit the name of a list.
     *
     * @param boardId id of the board
     * @param listId id of the list
     * @param model  name information of the modified list
     * @return The id of the list.
     */
    public IdResponseModel editList(int boardId, int listId, ListEditModel model) {
        try {
            HttpEntity<ListEditModel> req = new HttpEntity<ListEditModel>(model);
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"list/edit/"+boardId+"/"+listId, HttpMethod.PUT,
                    req, IdResponseModel.class);
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
     * Adds a task to a list.
     * @param task The task to be added.
     * @param listID The id of the list to be added to.
     * @return ID of the task, or -1 if it fails.
     */
    public IdResponseModel addTask(commons.Task task, int listID){
        try{
            HttpEntity<commons.Task> req = new HttpEntity<>(task);
            IdResponseModel id = client.postForObject(
                    url + "task/add/" + listID, req, IdResponseModel.class);
            return id;
        }
        catch(Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }

    /**
     * Removes a task from a list.
     * @param taskID The id of the task to be removed.
     * @param listID The id of the list to be removed from.
     * @return ID of the task, or -1 if it fails.
     */
    public IdResponseModel removeTask(int taskID, int listID){
        try{
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"task/remove/"+taskID+"/"+listID, HttpMethod.DELETE, null,
                    IdResponseModel.class);
            return response.getBody();
        }
        catch (Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }

    /**
     * Edits a task according to a model.
     * @param taskID The id of the task to be edited.
     * @param model The model associated with the changes to be made.
     * @return ID of the tas, or -1 if it fails.
     */
    public IdResponseModel editTask(int taskID, commons.models.TaskEditModel model){
        try {
            HttpEntity<TaskEditModel> req = new HttpEntity<>(model);
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url + "task/edit/" + taskID, HttpMethod.PUT,
                    req, IdResponseModel.class);
            return response.getBody();
        }
        catch (Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }

    /**
     * Returns a task by its ID.
     * @param id The id of the task.
     * @return The task associated with the id, or null if it fails.
     */
    public commons.Task getTask(int id) {
        try {
            ResponseEntity<commons.Task> response = client.getForEntity(
                    url + "task/get/" + id, commons.Task.class
            );
            return response.getBody();
        } catch (Exception e) {
            return null;
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
     * Gets all the tasks associated with a list.
     * @param listID The id of the list the tasks are associated with.
     * @return A list containing all the tasks associated with the list.
     */
    public java.util.List<commons.Task> getTaskByList(int listID) {
        try {
            ResponseEntity<java.util.List<commons.Task>> response = client.exchange(
                    url+"task/getByList/" + listID, HttpMethod.GET, null,
                    new ParameterizedTypeReference<java.util.List<commons.Task>>() {}
            );

            // return the task if the request succeeded
            if (response.getStatusCode().is2xxSuccessful()) {
                java.util.List<commons.Task> tasks = response.getBody();
                return tasks;
            }

            // list id doesn't exist
            if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new NoSuchElementException("There is no such list");
            }

            throw new RuntimeException("something went wrong...");

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Gets the tasks from a list sorted by the index they are at in the list
     * @param listID the id of the associated list
     * @return the sorted list of tasks
     */
    public java.util.List<Task> getTasksOrdered(int listID){
        ResponseEntity<java.util.List<Task>> response = client.exchange(
                url+"task/getSorted/" + listID,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){} );

        if (response.getStatusCode().is2xxSuccessful())
            return response.getBody();
        else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
            throw new NoSuchElementException("No such list id");

        throw new RuntimeException("Something went wrong");
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
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"subtask/delete/"+taskID+"/"+subtaskID, HttpMethod.DELETE,
                    null, IdResponseModel.class);
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
            ResponseEntity<IdResponseModel> response = client.exchange(
                    url+"subtask/edit/"+subtaskID, HttpMethod.PUT,
                    req, IdResponseModel.class);

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

    @Override
    public IdResponseModel addTagToTask(Tag tag, int taskID){
        try{
            HttpEntity<Tag> req = new HttpEntity<>(tag);
            IdResponseModel id = client.postForObject(
                    url + "tag/addToTask/" + taskID, req, IdResponseModel.class);
            return id;
        }
        catch(Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }

    @Override
    public IdResponseModel addTagToBoard(Tag tag, int boardID){
        try{
            HttpEntity<Tag> req = new HttpEntity<>(tag);
            IdResponseModel id = client.postForObject(
                    url + "tag/addToBoard/" + boardID, req, IdResponseModel.class);
            return id;
        }
        catch(Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }

    public IdResponseModel editTag(int tagID, TagEditModel model){
        try{
            HttpEntity<TagEditModel> req = new HttpEntity<>(model);

            ResponseEntity<IdResponseModel> response = client.exchange(
                    url + "tag/edit/" + tagID, HttpMethod.PUT,
                    req, IdResponseModel.class);
            return response.getBody();
        }
        catch (Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }

    public IdResponseModel removeTag(int tagID){
        try{
            ResponseEntity<IdResponseModel> response = client.getForEntity(
                    url + "/tag/removeTag/" + tagID, IdResponseModel.class);
            return response.getBody();
        }
        catch(Exception e){
            return new IdResponseModel(-1, "Oops, failed to connect to the server...");
        }
    }


    /**
     * Returns a tag by its ID.
     * @param id The id of the tag.
     * @return The tag associated with the id, or null if it fails.
     */
    public Tag getTag(int id){
        try{
            ResponseEntity<Tag> response = client.getForEntity(
                    url + "/tag/get/" + id, Tag.class);
            return response.getBody();
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * Gets all the tags associated with a task.
     * @param taskID The id of the task the tags are associated with.
     * @return A list containing all the tags associated with the task.
     */
    public java.util.List<Tag> getTagByTask(int taskID) {
        try {
            ResponseEntity<java.util.List<commons.Tag>> response = client.exchange(
                url+"tag/getByTask/" + taskID, HttpMethod.GET, null,
                new ParameterizedTypeReference<java.util.List<commons.Tag>>() {}
            );

            // return the tag if the request succeeded
            if (response.getStatusCode().is2xxSuccessful()) {
                java.util.List<commons.Tag> tags = response.getBody();
                return tags;
            }

            // task id doesn't exist
            if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new NoSuchElementException("There is no such list");
            }

            throw new RuntimeException("something went wrong...");

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Gets all the tags associated with a board.
     * @param boardID The id of the board the tags are associated with.
     * @return A list containing all the tags associated with the board.
     */
    public java.util.List<Tag> getTagByBoard(int boardID) {
        try {
            ResponseEntity<java.util.List<commons.Tag>> response = client.exchange(

                url+"tag/getByBoard/" + boardID, HttpMethod.GET, null,
                new ParameterizedTypeReference<java.util.List<commons.Tag>>() {}
            );

            // return the tag if the request succeeded
            if (response.getStatusCode().is2xxSuccessful()) {
                java.util.List<commons.Tag> tags = response.getBody();
                return tags;
            }

            // board id doesn't exist
            if(response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new NoSuchElementException("There is no such list");
            }

            throw new RuntimeException("something went wrong...");

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Gets the subtasks from a task ordered by their index
     * @param taskID the id of the associated task
     * @return the list of ordered subtasks
     */
    public java.util.List<Subtask> getSubtasksOrdered(int taskID){
        ResponseEntity<java.util.List<Subtask>> response = client.exchange(
                url+"subtask/getOrdered/" + taskID,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>(){} );

        if (response.getStatusCode().is2xxSuccessful())
            return response.getBody();
        else if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST))
            throw new NoSuchElementException("No such task id");

        throw new RuntimeException("Something went wrong");
    }

    public IdResponseModel removeTagFromTask(int tagId, int taskId){
        try{
            String fullurl = url+"/tag/removeFromTask/" + tagId + "/" + taskId;
            Map<String, String> params = new HashMap<String, String>();
            params.put("tagId", String.valueOf(tagId));
            params.put("taskId", String.valueOf(taskId));

            ResponseEntity<IdResponseModel> resp = client.exchange(
                    fullurl,
                    HttpMethod.DELETE,
                    null,
                    IdResponseModel.class,
                    params
            );
            return resp.getBody();
        }
        catch(Exception e){
            return new IdResponseModel(-1, "failed to connect to server");
        }
    }
}