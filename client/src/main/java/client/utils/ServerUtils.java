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
import commons.models.BoardIdResponseModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ServerUtils {
    @Inject
    private RestTemplate client;

    /**
     * Send a new Board to the server.
     *
     * @param board content of the board
     * @return the id of the board, or -1 and the error message if the creation fails
     */
    public BoardIdResponseModel addBoard(Board board) {
        try {
            HttpEntity<Board> req = new HttpEntity<>(board);
            BoardIdResponseModel response = client.postForObject(
                    "http://localhost:8080/board/create", req, BoardIdResponseModel.class);
            return response;
        } catch (Exception e) {
            return new BoardIdResponseModel(-1, "Oops, failed to connect to server...");
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
                    client.getForEntity("http://localhost:8080/board/find"+id, Board.class);
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
                    client.getForEntity("http://localhost:8080/board/findAll", Board[].class);
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
    public int addlist(commons.List list, int boardId) {
        HttpEntity<commons.List> req = new HttpEntity<commons.List>(list);
        int id = client.postForObject("http://localhost:8080/list/add/"+boardId, req, Integer.class);
        return id;
    }

    /**
     * Delete a list from the board and list repository.
     *
     * @param boardId id of the board
     * @param listId id of the list
     * @return true if it succeeds, false otherwise
     */
    public boolean deleteList(int boardId, int listId) {
        ResponseEntity<Boolean> response = client.getForEntity(
                "http://localhost:8080/list/delete/"+boardId+"/"+listId,
                Boolean.class
        );
        return response.getBody();
    }

    /**
     * Edits the list with the new values
     * @param name new name of the list
     * @param listId id of the list to be edited
     * @param background new background color of the list
     * @param font new font color of the list
     * @return true if it succeeds, false otherwise
     */
    public boolean editList(String name, int listId, String background, String font) {
        ResponseEntity<Boolean> response = client.getForEntity(
                "http://localhost:8080/list/edit/"+listId+"/"+name+"/"+background+"/"+font,
                Boolean.class
        );
        return response.getBody();
    }

    /**
     * Returns the list with a certain id
     * @param id id of the list
     * @return the list or null in case of an exception
     */
    public commons.List getList(int id) {
        try {
            ResponseEntity<commons.List> response =
                    client.getForEntity("http://localhost:8080/list/"+id, commons.List.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

}