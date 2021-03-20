var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function(req, res, next) {

    res.render('dashboard', {PartyName : req.session.username});
});

module.exports = router;
