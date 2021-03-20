var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    res.render('login');
});


router.get('/assetList', function(req, res, next) {

    res.render('assetList', { data: 'Express' });
});

module.exports = router;
