var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    res.render('login');
});

router.post('/', function(req, res, next) {
    let successful = false;
    let message = '';
    console.log("username: " + req.session.username);
    // TODO figure out how we are gonna save usernames and stuff
    if (req.body.username === 'user1' && req.body.password === 'test') {
        successful = true;
        req.session.username = req.body.username;
        // req.cookie('jason', 'the great!', { maxAge: 900000, httpOnly: true });
        res.redirect('/dashboard')
    }
    else {
        // delete the user as punishment
        delete req.session.username;
        message = 'Wrong username or password!'
        // TODO redirect back to log in page noting that username or password is incorrect

    }

    // Return success or failure
    res.json({
        successful: successful,
        message: message
    });

    //res.render('login');
});


module.exports = router;
