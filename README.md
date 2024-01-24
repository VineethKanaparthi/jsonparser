# JSON Parser in Java

## Overview

This project is a Java-based JSON parser, aiming to provide a comprehensive understanding of the intricacies of data interchange. The parser is built from scratch, covering tokenization, object parsing, and array parsing. 

## Table of Contents

1. [Lexer](#lexer)
    - [Whitespaces, Symbols, Strings, Numerics, and Identifiers](#whitespaces-symbols-strings-numerics-and-identifiers)
2. [JsonParser](#jsonparser)
    - [Object and Array Parsing](#object-and-array-parsing)

## Lexer

### Whitespaces, Symbols, Strings, Numerics, and Identifiers

The Lexer module is the foundation of the parser, responsible for tokenizing the input JSON string. It intelligently identifies and extracts whitespaces, symbols, strings, numerics, and identifiers. 

### Code Snippet

```java
    List<JsonToken> tokens = new JsonLexer().lex("{\"foo\"   : [1, +2 , \n{\"bar\": 2}]}");
```

output:

```
[
   JsonToken{token={, tokenType=CONSTANT, location=Location{line=1, col=0}},
   JsonToken{token=foo, tokenType=STRING, location=Location{line=1, col=1}},
   JsonToken{token=:, tokenType=CONSTANT, location=Location{line=1, col=9}},
   JsonToken{token=[, tokenType=CONSTANT, location=Location{line=1, col=11}},
   JsonToken{token=1, tokenType=INT, location=Location{line=1, col=12}},
   JsonToken{token=,, tokenType=CONSTANT, location=Location{line=1, col=13}},
   JsonToken{token=2, tokenType=INT, location=Location{line=1, col=15}},
   JsonToken{token=,, tokenType=CONSTANT, location=Location{line=1, col=18}},
   JsonToken{token={, tokenType=CONSTANT, location=Location{line=2, col=0}},
   JsonToken{token=bar, tokenType=STRING, location=Location{line=2, col=1}},
   JsonToken{token=:, tokenType=CONSTANT, location=Location{line=2, col=6}},
   JsonToken{token=2, tokenType=INT, location=Location{line=2, col=8}}, 
   JsonToken{token=},tokenType=CONSTANT, location=Location{line=2, col=9}}, 
   JsonToken{token=], tokenType=CONSTANT, location=Location{line=2, col=10}}, 
   JsonToken{token=}, tokenType=CONSTANT, location=Location{line=2, col=11}}
 ]
```

## JsonParser

### Object and Array Parsing

The JsonParser module processes the tokenized JSON and navigates through objects and arrays. It handles nested structures and different data types, ensuring a robust parsing mechanism.

### Code Snippet

```java
    JsonParser parser = new JsonParser()
    parser.parse("{\"foo\"   : [1, +2 , \n{\"bar\": 2}]}")
```

output:

```
{
   address=735 Conover Street, Edgar, Texas, 768,
   gender=female, latitude=-36.029072,
   greeting=Hello, Tabatha Rodgers! You have 10 unread messages.,
   about=Aliqua labore mollit sint non incididunt dolor elit cillum ex minim. Ullamco labore laboris commodo mollit commodo voluptate dolore. Tempor consequat consequat do qui est. Dolore commodo dolor occaecat Lorem et minim.\r\n, 
   index=0, 
   registered=2018-06-06T06:28:45 +07:00, 
   isActive=false, 
   picture=http://placehold.it/32x32, 
   friends=[{name=Anthony Bowman, id=0}, {name=Buckley Valenzuela, id=1}, {name=Deborah Meyers, id=2}], 
   tags=[ipsum, ipsum, deserunt, et, enim, officia, irure], 
   favoriteFruit=banana, 
   balance=$2,565.84, 
   eyeColor=green, 
   phone=+1 (818) 414-3938, 
   name=Tabatha Rodgers, 
   guid=7f8f196f-24b6-4b62-a69a-26c911e450ff, 
   company=ZOMBOID, 
   _id=64fc15e816964298016ed4a4, 
   age=34, 
   email=tabatharodgers@zomboid.com, 
   longitude=-79.561037
 }
```

## Thanks and Credits : [eatonphil](https://github.com/eatonphil)