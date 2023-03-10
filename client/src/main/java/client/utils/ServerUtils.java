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
     * Add new card to the board.
     *
     * @param card card to be added
     * @param boardId id of the board
     * @return id of the card, or -1 if it fails
     */
    public int addCard(Card card, int boardId) {
        HttpEntity<Card> req = new HttpEntity<Card>(card);
        int id = client.postForObject("http://localhost:8080/card/add"+boardId, req, Integer.class);
        return id;
    }

    /**
     * Delete a card from the board and card repository.
     *
     * @param boardId id of the board
     * @param cardId id of the card
     * @return true if it succeeds, false otherwise
     */
    public boolean delete(int boardId, int cardId) {
        ResponseEntity<Boolean> response = client.getForEntity(
                "http://localhost:8080/card/delete/"+boardId+"/"+cardId,
                Boolean.class
        );
        return response.getBody();
    }

    /**
     * Rename a card.
     *
     * @param name new name of the card
     * @param cardId id of the card
     * @return true if it succeeds, false otherwise
     */
    public boolean renameCard(String name, int cardId) {
        ResponseEntity<Boolean> response = client.getForEntity(
                "http://localhost:8080/card/rename/"+cardId+"/"+name,
                Boolean.class
        );
        return response.getBody();
    }
}