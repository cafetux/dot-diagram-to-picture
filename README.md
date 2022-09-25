# A very basic DOT renderer to PNG for java

DOT: graph description language
https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29

Custom basic DOT diagram parser.

Image renderer and graph organisation (to easy lisible graph) made with diagramming from mindfusion:
https://www.mindfusion.eu/java-diagram.html

Exemple of generated picture:
![diagramm generation exemple](result.png "Sample diagramm")

Usage: 

```java
    Path path = resourceDirectory.resolve("some-dependency-diagram.gv");
    FlowChart result = parser.parse(path);
    renderer.render(result,"target/result");
```
