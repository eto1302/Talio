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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import commons.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ServerUtils {
    @Inject
    RestTemplate client;
    public void getQuotesTheHardWay() throws IOException {
        var url = new URL("http://localhost:8080/api/quotes");
        var is = url.openConnection().getInputStream();
        var br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public List<Quote> getQuotes() {
        ResponseEntity<Quote[]> quotes =
                client.getForEntity("http://localhost:8080/api/quotes", Quote[].class);
        List<Quote> res = Arrays.asList(quotes.getBody());
        return res;
    }

    public Quote addQuote(Quote quote) {
        HttpEntity<Quote> req = new HttpEntity<>(quote);
        Quote resp = client.postForObject("http://localhost:8080/api/quotes", req, Quote.class);
        return resp;
    }

    /**
     * Send a new Board to the server.
     *
     * @param board content of the board
     * @return the id of the board, or -1 if the creation fails
     */
    public int addBoard(Board board) {
        try {
            HttpEntity<Board> req = new HttpEntity<>(board);
            int id = client.postForObject("http://localhost:8080/board/create", req, Integer.class);
            return id;
        } catch (Exception e) {
            return -1;
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
     * Add new list to the board.
     *
     * @param list list to be added
     * @param boardId id of the board
     * @return id of the list, or -1 if it fails
     */
    public int addlist(commons.List list, int boardId) {
        HttpEntity<commons.List> req = new HttpEntity<commons.List>(list);
        int id = client.postForObject("http://localhost:8080/list/add"+boardId, req, Integer.class);
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
     * Rename a list.
     *
     * @param name new name of the list
     * @param listId id of the list
     * @return true if it succeeds, false otherwise
     */
    public boolean renameList(String name, int listId) {
        ResponseEntity<Boolean> response = client.getForEntity(
                "http://localhost:8080/list/rename/"+listId+"/"+name,
                Boolean.class
        );
        return response.getBody();
    }
}