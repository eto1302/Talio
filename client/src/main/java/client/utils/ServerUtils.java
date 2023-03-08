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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import commons.Board;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

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
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
    }

    public Quote addQuote(Quote quote) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/quotes") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
    }

    /**
     * Send a new Board to the server.
     *
     * @param boardModel content of the board
     * @return the id of the board, or -1L if the creation fails
     */
    public int addBoard(Board boardModel) {
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path("board/create") //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(boardModel, APPLICATION_JSON), Integer.class);
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
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(SERVER).path("board/find/"+id) //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .post(Entity.entity(id, APPLICATION_JSON), Board.class);
        } catch (Exception e) {
            return null;
        }
    }

}