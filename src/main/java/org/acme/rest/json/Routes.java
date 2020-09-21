/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.rest.json;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

/**
 * Camel route definitions.
 */
public class Routes extends RouteBuilder {
    private final Set<Fruit> fruits = Collections.synchronizedSet(new LinkedHashSet<>());
    private final Set<Legume> legumes = Collections.synchronizedSet(new LinkedHashSet<>());

    public Routes() {

        /* Let's add some initial fruits */
        this.fruits.add(new Fruit("Apple", "Winter fruit"));
        this.fruits.add(new Fruit("Pineapple", "Tropical fruit"));

        /* Let's add some initial legumes */
        this.legumes.add(new Legume("Carrot", "Root vegetable, usually orange"));
        this.legumes.add(new Legume("Zucchini", "Summer squash"));
    }

    @Override
    public void configure() throws Exception {
        from("platform-http:/fruits?httpMethodRestrict=GET,POST")
                .choice()
                .when(simple("${header.CamelHttpMethod} == 'GET'"))
                .setBody()
                .constant(fruits)
                .endChoice()
                .when(simple("${header.CamelHttpMethod} == 'POST'"))
                .unmarshal()
                .json(JsonLibrary.Jackson, Fruit.class)
                .process()
                .body(Fruit.class, fruits::add)
                .setBody()
                .constant(fruits)
                .endChoice()
                .end()
                .marshal().json();

        from("platform-http:/legumes?httpMethodRestrict=GET")
                .setBody().constant(legumes)
                .marshal().json();
        
        // from("platform-http:/as400?httpMethodRestrict=GET")
        //         .to("jt400://DEMO678:hola10@www.pub400.com/lib.lib/MSGINDQ.DTAQ?keyed=true");
        
        // from("platform-http:/as402?httpMethodRestrict=POST")
        //         .to("jt400://DEMO678:hola10@www.pub400.com/QSYS.LIB/LIBRARY.LIB/QUEUE.DTAQ")
        //         .log("test log");
        
        // from("platform-http:/as402a?httpMethodRestrict=POST")
        //         .to("jt400://DEMO678:hola10@www.pub400.com/DEMO6781/MYLIB.DTAQ")
        //         .log("test log");

        // from("platform-http:/as404?httpMethodRestrict=POST")
        //         .to("direct:hello").log("NOP");

        // from("direct:hello")
        //         .transform().constant("Hello World").log("asd");
        
        // from("direct:test")
        //         .transform().constant("TEST!!").log("TEST");
        // from("jt400://DEMO678:hola10@www.pub400.com/QSYS.LIB/BEATLES.LIB/PENNYLANE.DTAQ").to("mock:ringo");

        // from("direct:test1").to("jt400://DEMO678:hola10@www.pub400.com/QSYS.LIB/BEATLES.LIB/PENNYLANE.DTAQ");
        // from("jt400://DEMO678:hola10@www.pub400.com/QSYS.LIB/BEATLES.LIB/PENNYLANE.DTAQ").to("direct:hello");

        // from("direct:work")
        //         .to("jt400://DEMO678:hola10@www.pub400.com/QSYS.LIB/assets.LIB/compute.PGM?fieldsLength=10,10&ouputFieldsIdx=2,3")
        //         .to("direct:hello");
        
        // from("jt400://DEMO678:hola10@www.pub400.com/lib.lib/MSGOUTDQ.DTAQ?keyed=true&searchKey=MYKEY&searchType=GE")
        //         .to("direct:hello");


        // QRPGLESC
    }
}
