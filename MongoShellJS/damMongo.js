// Connexio a la base de dades
var selectDB = function(port, dbName) {

    if(!port) {
        port = 27017;
    }

    if(!dbName) {
        dbName = "dam";
    }

    db = connect("localhost:" + port + "/" + dbName);

    return db;
}

// C - Create - One name, 0..n hobbies
var insert = function(name, hobbies) {

    /*
    var foo = {
        'name': name,
        'hoobies': {
            'hobby': 'tenis',
            'hooby': 'karate'
        }
    }
    */

    print('saving data ...');

    if(!hobbies) { // no hobbies
        db.usuaris.insert({'name': name});
    }
    else { // yes hobbies

        if(Array.isArray(hobbies)) { // hobbies is array?
            db.usuaris.insert({
                'name': name,
                'hobbies': {
                    'hobby': hobbies
                }
            });
        }
        else { // just one hooby
            var aux = {
                'name': name,
                'hobbies': {
                    'hobby': hobbies
                }
            }
            db.usuaris.insert(aux);
        }
    }

    print('saved');
    
}

// C - Create - push in hobby array another item
var insertHobby = function(name, hobby) {
    db.usuaris.update({"name":name},{$push:{"hobbies.hobby":hobby}});
    print(name + " updated.");
}

// R - Read - All
var selectAll = function() {
    info = db.usuaris.find();

    while(info.hasNext()) {
        printjson(info.next());
    }
}

// R - Read - Read by name
var selectOneByName = function(name) {
    info = db.usuaris.findOne({"name": name})
    printjson(info);
}

// R - Read - Proyection by name
var showNames = function() {
    // $orderby: 1 ascending, -1 descending
    info = db.usuaris.find({},{$orderby:{"name":1}}).sort({"name":1});
    while(info.hasNext()) {
        printjson(info.next());
    }
}

// R - Read - Proyection by hobbies
var showHobbies = function() {
    info = db.usuaris.find({},{"hobbies.hobby":1, "_id":0});
    while(info.hasNext()) {
        printjson(info.next());
    }
}


// U - Update - Name
var updateName = function(oldName, newName) {
    db.usuaris.update({"name": oldName}, {$set: {"name": newName}});
    info = db.usuaris.findOne({"name": newName});
    printjson(info);
}

// R - Remove - Remove all de document witch this name
var remove = function(name) {
    db.usuaris.remove({"name": name});
    print(name + " deleted");
}