### Coding Style
<a href="https://oracle.com/technetwork/java/codeconventions-150003.pdf">Oracle</a>

### Location of the Configuration file
src/main/resources/level_1.json

### Description of the Configuration file
The config file has the hero as its own JSONObject.
The immovable, movable and enemy entities are all stored in
three separate JSON arrays. Each entity that belongs to an
array requires attributes specific to that array. This facilitates
the creation process and allows you to add a new entity anywhere in
its corresponding JSON array.

## Acknowledgements
<a href="https://opengameart.org/content/top-down-2d-metal-box">Block png</a><br>
