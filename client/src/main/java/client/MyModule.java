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
package client;

import client.messageClients.MessageAdmin;
import client.messageClients.MessageSender;
import client.scenes.AddQuoteCtrl;
import client.scenes.MainCtrl;
import client.scenes.QuoteOverviewCtrl;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class MyModule extends AbstractModule {

    protected void configure() {
        install(new WSClientModule());
        bind(MainCtrl.class).in(Scopes.SINGLETON);
        bind(AddQuoteCtrl.class).in(Scopes.SINGLETON);
        bind(QuoteOverviewCtrl.class).in(Scopes.SINGLETON);
        bind(MessageAdmin.class).in(Scopes.SINGLETON);
        bind(MessageSender.class).in(Scopes.SINGLETON);
        RestTemplateBuilder rtb = new RestTemplateBuilder();
        RestTemplate rt = rtb.build();
        bind(RestTemplate.class).toInstance(rt);
    }
}