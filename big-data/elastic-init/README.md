## Elastic indices initialization script.
This Node.JS script initializes Elastic indices.

It is also used in integration tests of webapp.

## Usage
``` bash
node initializeIndices.js elastic-host:9200 technologies 
```
Elastic default port is 9200.
`technologies` - it is a name of index to initialize.

