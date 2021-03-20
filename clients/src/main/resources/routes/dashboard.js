var express = require('express');
var router = express.Router();
const http = require('http');

/* GET users listing. */
router.get('/', function(req, res, next) {
    req.session.username = "user1"

    var data;
    http.get("http://localhost:10050/transaction/list", (resp) => {
        resp.on("data", (information) => {
            data += information
        });
        resp.on("end", () => {
            console.log(data);
        });
    });


    res.render('dashboard', {PartyName : req.session.username, transactions: data});
});

module.exports = router;
