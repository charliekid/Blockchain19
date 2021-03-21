var express = require('express');
const http = require('http');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    var data;
    // http.get("http://localhost:10050/transaction/list", (resp) => {
    //     resp.on("data", (information) => {
    //         data += information
    //     });
    //     resp.on("end", () => {
    //         console.log(data);
    //     });
    // });
    res.render('index', { title: 'Express' });
});

module.exports = router;
