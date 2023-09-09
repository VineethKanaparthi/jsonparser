package org.dbnoobs.jsonparser;

import org.junit.jupiter.api.Test;

class JsonParserTest {

    @Test
    void parserTest() {
        JsonParser parser = new JsonParser();
        System.out.println(parser.parse("{\"foo\"   : [1, +2 , \n{\"bar\": 2}]}"));
        System.out.println(parser.parse("  {\n" +
                "    \"_id\": \"64fc15e816964298016ed4a4\",\n" +
                "    \"index\": 0,\n" +
                "    \"guid\": \"7f8f196f-24b6-4b62-a69a-26c911e450ff\",\n" +
                "    \"isActive\": false,\n" +
                "    \"balance\": \"$2,565.84\",\n" +
                "    \"picture\": \"http://placehold.it/32x32\",\n" +
                "    \"age\": 34,\n" +
                "    \"eyeColor\": \"green\",\n" +
                "    \"name\": \"Tabatha Rodgers\",\n" +
                "    \"gender\": \"female\",\n" +
                "    \"company\": \"ZOMBOID\",\n" +
                "    \"email\": \"tabatharodgers@zomboid.com\",\n" +
                "    \"phone\": \"+1 (818) 414-3938\",\n" +
                "    \"address\": \"735 Conover Street, Edgar, Texas, 768\",\n" +
                "    \"about\": \"Aliqua labore mollit sint non incididunt dolor elit cillum ex minim. Ullamco labore laboris commodo mollit commodo voluptate dolore. Tempor consequat consequat do qui est. Dolore commodo dolor occaecat Lorem et minim.\\r\\n\",\n" +
                "    \"registered\": \"2018-06-06T06:28:45 +07:00\",\n" +
                "    \"latitude\": -36.029072,\n" +
                "    \"longitude\": -79.561037,\n" +
                "    \"tags\": [\n" +
                "      \"ipsum\",\n" +
                "      \"ipsum\",\n" +
                "      \"deserunt\",\n" +
                "      \"et\",\n" +
                "      \"enim\",\n" +
                "      \"officia\",\n" +
                "      \"irure\"\n" +
                "    ],\n" +
                "    \"friends\": [\n" +
                "      {\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"Anthony Bowman\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"Buckley Valenzuela\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"name\": \"Deborah Meyers\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"greeting\": \"Hello, Tabatha Rodgers! You have 10 unread messages.\",\n" +
                "    \"favoriteFruit\": \"banana\"\n" +
                "  }"));
    }

}