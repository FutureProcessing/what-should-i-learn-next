
function MappingEntry(line){
    var variables = line.split('\t');
    this.t1 = variables[0];
    this.t2 = variables[1];
    this.value = parseInt(variables[2]);
    this.key = this.t1 + "_" + this.t2;
}

module.exports = MappingEntry;